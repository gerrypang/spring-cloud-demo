package com.gerry.pang.validate.code.base;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateCode {
	/** 验证码 */
	private String code;
	
	/** 过期时间 */
	private LocalDateTime expireTime;

	/**
	 * 初始化构造
	 * 
	 * @param code
	 * @param expireIn 单位秒
	 */
	public ValidateCode(String code, int expireIn) {
		super();
		this.code = code;
		this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
	}

	public boolean isExpried() {
		return LocalDateTime.now().isAfter(expireTime);
	}

}
