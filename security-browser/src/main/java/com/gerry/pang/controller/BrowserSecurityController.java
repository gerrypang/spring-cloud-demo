package com.gerry.pang.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gerry.pang.common.properties.SecurityProperties;
import com.gerry.pang.support.SimpleResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BrowserSecurityController {

	@Autowired
	private SecurityProperties securityProperties;
	
	private RequestCache requestCache = new HttpSessionRequestCache();
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@GetMapping("/authentication/require")
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SavedRequest saveRequest = requestCache.getRequest(request, response);
		if (saveRequest != null) {
			String targetUrl = saveRequest.getRedirectUrl();
			log.info("引发跳转的请求是:{}", targetUrl);
			if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
				String url = securityProperties.getBrowser().getLoginPage();
				redirectStrategy.sendRedirect(request, response, url);
			}
		}
		
		return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
	}
}
