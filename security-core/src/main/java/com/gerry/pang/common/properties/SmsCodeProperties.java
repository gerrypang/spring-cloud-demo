package com.gerry.pang.common.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信验证码参数配置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsCodeProperties {
	
	/** 验证码长度 */
	private int length = 4;
	
	/** 过期时间，单位秒 */
	private int expireIn = 300;
	
	/** 拦截url，逗号分隔 */
	private String url;
}
