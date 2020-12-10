package com.gerry.pang.validate.code.base;

import javax.servlet.http.HttpServletRequest;

public interface ValidateCodeGenerator {
	
	/**
	 * 生成校验码
	 * 
	 * @param request
	 * @return
	 */
	public ValidateCode generator(HttpServletRequest request);
}
