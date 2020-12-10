package com.gerry.pang.validate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gerry.pang.common.properties.SecurityProperties;
import com.gerry.pang.validate.code.base.ValidateCodeGenerator;
import com.gerry.pang.validate.code.image.generator.SimpleValidateImageCodeGenerator;
import com.gerry.pang.validate.code.sms.sender.DefaultSmsCodeSender;
import com.gerry.pang.validate.code.sms.sender.SmsCodeSender;

/**
 * 验证码配置类
 */
@Configuration
public class ValidateCodeBeanConfig {

	@Autowired
	private SecurityProperties securityProperties;

	@Bean
	@ConditionalOnMissingBean(name = "simpleValidateImageCodeGenerator")
	public ValidateCodeGenerator simpleValidateImageCodeGenerator() {
		SimpleValidateImageCodeGenerator validateImageCodeGenerator = new SimpleValidateImageCodeGenerator();
		validateImageCodeGenerator.setSecurityProperties(securityProperties);
		return validateImageCodeGenerator;
	}

	@Bean
	@ConditionalOnMissingBean(SmsCodeSender.class)
	public SmsCodeSender smsCodeSender() {
		SmsCodeSender smsCodeSender = new DefaultSmsCodeSender();
		return smsCodeSender;
	}
}
