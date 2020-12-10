package com.gerry.pang.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.gerry.pang.common.properties.SecurityProperties;
import com.gerry.pang.validate.filter.ValidateImageCodeFilter;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private SecurityProperties securityProperties;

	// 自定义成功处理器
	@Autowired
	private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

	// 自定义失败处理器
	@Autowired
	private AuthenticationFailureHandler myAuthenticationFailureHandler;
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		// 在服务启动时创建表 persistent_logins
		// tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 图形验证码校验filter
		ValidateImageCodeFilter validateImageCodeFilter = new ValidateImageCodeFilter();
		validateImageCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
		validateImageCodeFilter.setSecurityProperties(securityProperties);
		validateImageCodeFilter.afterPropertiesSet();
		
		http.addFilterBefore(validateImageCodeFilter, UsernamePasswordAuthenticationFilter.class) // 在用户名密码登录验证之前添加验证码校验filter
			.formLogin() // formLogin() http表单形式登录, httpBasic() 弹框形式登录
				.loginPage("/authentication/require") // 自定义登录页面
				.loginProcessingUrl("/authentication/form") // 登录处理url
				.successHandler(myAuthenticationSuccessHandler) // 配置自定义成功处理器
				.failureHandler(myAuthenticationFailureHandler) // 配置自定义失败处理器
				.and()
			.rememberMe() // 记住我配置
				.tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeExpriedSecond())
				.userDetailsService(userDetailsService)
				.and()
			.authorizeRequests()
				.antMatchers("/authentication/require", 
						securityProperties.getBrowser().getLoginPage(),
						"/code/*").permitAll() // 允许授权跳过多个请求
				.anyRequest().authenticated() // 任何请求都需要身份认证
				.and()
			.csrf()
				.disable(); // 关闭csrf跨站请求保护
	}

}
