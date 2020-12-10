package com.gerry.pang.validate.code.sms.generator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gerry.pang.common.properties.SecurityProperties;
import com.gerry.pang.common.properties.SmsCodeProperties;
import com.gerry.pang.validate.code.base.ValidateCode;
import com.gerry.pang.validate.code.base.ValidateCodeGenerator;

/**
 * 短信验证码生成器
 */
@Component("simpleValidateSmsCodeGenerator")
public class SimpleValidateSmsCodeGenerator implements ValidateCodeGenerator {

	@Autowired
	private SecurityProperties securityProperties;
	
	@Override
	public ValidateCode generator(HttpServletRequest request) {
		SmsCodeProperties smsCodeProperties = securityProperties.getValidateCode().getSmsCode();
		String code = RandomStringUtils.randomNumeric(smsCodeProperties.getLength());
		return new ValidateCode(code, smsCodeProperties.getExpireIn());
	}

	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

}
