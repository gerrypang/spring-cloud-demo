package com.gerry.pang.common.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateCodeProperties {
	
	private ImageCodeProperties imageCode = new ImageCodeProperties();

	private SmsCodeProperties smsCode = new SmsCodeProperties();
}
