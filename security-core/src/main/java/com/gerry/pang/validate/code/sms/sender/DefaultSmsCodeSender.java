package com.gerry.pang.validate.code.sms.sender;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultSmsCodeSender implements SmsCodeSender {

	@Override
	public void sender(String mobile, String code) {
		log.info("===> 向手机：{}，发送短信验证码：{}", mobile, code);
	}

}
