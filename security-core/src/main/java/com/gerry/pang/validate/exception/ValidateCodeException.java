package com.gerry.pang.validate.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证错误异常
 */
public class ValidateCodeException extends AuthenticationException {
	
	private static final long serialVersionUID = 1L;
	
	public ValidateCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidateCodeException(String message) {
		super(message);
	}

}
