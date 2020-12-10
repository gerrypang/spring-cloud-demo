package com.gerry.pang.validate.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gerry.pang.common.properties.SecurityProperties;
import com.gerry.pang.validate.code.base.ValidateCode;
import com.gerry.pang.validate.controller.ValidateCodeController;
import com.gerry.pang.validate.exception.ValidateCodeException;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义图片验证吗Filter
 */
@Slf4j
public class ValidateImageCodeFilter extends OncePerRequestFilter implements InitializingBean {
	
	private SecurityProperties securityProperties;
	
	private Set<String> urlSet = new HashSet<>(16);

	private AuthenticationFailureHandler authenticationFailureHandler; 
	
	// spring session 工具类
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	private PathMatcher pathMatcher = new AntPathMatcher();
	
	@Override
	public void afterPropertiesSet() throws ServletException {
		String[] urlArray = StringUtils.split(securityProperties.getValidateCode().getImageCode().getUrl(), ",");
		if (urlArray != null && urlArray.length > 0) {
			for (int i = 0; i < urlArray.length; i++) {
				urlSet.add(urlArray[i]);
			}
		}
		urlSet.add("/authentication/form");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
//		if (StringUtils.equals(request.getRequestURI(), "/authentication/form") &&
//				StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
		boolean action = false;
		for (String uri : urlSet) {
			if (pathMatcher.match(uri, request.getRequestURI())) {
				action = true;
			}
		}
		
		if (action) {
			try {
				this.validate(new ServletWebRequest(request));
			} catch (ValidateCodeException e) {
				authenticationFailureHandler.onAuthenticationFailure(request, response, e);
				log.error("验证码验证失败");
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private void validate(ServletWebRequest request) throws ServletRequestBindingException {
		String sessionKey = ValidateCodeController.SESSION_IMAGE_KEY;
		String codeName = "imageCode";
		ValidateCode codeInSession = (ValidateCode) sessionStrategy.getAttribute(request, sessionKey);
		String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), codeName);

		if (StringUtils.isBlank(codeInRequest)) {
			throw new ValidateCodeException("验证码的值不能为空");
		}
		if (codeInSession == null) {
			throw new ValidateCodeException("验证码不存在");
		}
		if (codeInSession.isExpried()) {
			sessionStrategy.removeAttribute(request, sessionKey);
			throw new ValidateCodeException("验证码已过期");
		}
		if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), codeInRequest)) {
			throw new ValidateCodeException("验证码不匹配");
		}
		
		sessionStrategy.removeAttribute(request, sessionKey);
	}
	
	public AuthenticationFailureHandler getAuthenticationFailureHandler() {
		return authenticationFailureHandler;
	}

	public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}
	
	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}
}
