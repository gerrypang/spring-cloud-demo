package com.gerry.pang.validate.code.sms.sender;

/**
 * 短信验证码发送接口
 */
public interface SmsCodeSender {

	/**
	 * 短信发送接口
	 * 
	 * @param mobile 手机
	 * @param code 验证码
	 */
	public void sender(String mobile, String code);
}
