package com.gerry.pang.filter;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ResponseHeaderFilter extends ZuulFilter {

	@Override
	public String filterType() {
		/*
		 * 定义filter类型，包括四种类型， 1、per前置过滤器 2、routing路由转发过滤器 3、post后置过滤器 4、error其他阶段发生错误
		 */
		// 后置过滤器
		return FilterConstants.POST_TYPE;
	}

	@Override
	public int filterOrder() {
		// 定义filter的顺序
		return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
	}

	@Override
	public boolean shouldFilter() {
		// 是否应该过滤
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		log.info("===== execute ResponseHeaderFilter ===== ");
		// 过滤器真正执行的逻辑内容
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletResponse response = requestContext.getResponse();
		response.setHeader("X-foo", UUID.randomUUID().toString().replace("-", ""));
		
		return null;
	}

}
