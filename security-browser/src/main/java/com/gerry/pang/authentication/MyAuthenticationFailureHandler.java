package com.gerry.pang.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerry.pang.common.enums.LoginTypeEnum;
import com.gerry.pang.common.properties.SecurityProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义登录失败处理器
 * 方法一：实现 AuthenticationFailureHandler 接口
 * 方法二：继承 SimpleUrlAuthenticationFailureHandler
 */
@Slf4j
@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if (LoginTypeEnum.JSON.equals(securityProperties.getBrowser().getLoginType())) {
			// 配置状态码为500
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setContentType("application/json;charset=UTF-8");
			// 将authentication转为json字符串输出
			response.getWriter().write(objectMapper.writeValueAsString(exception));
		} else {
			super.onAuthenticationFailure(request, response, exception);
		}
		log.info("===> 登录验证失败");
	}

}
