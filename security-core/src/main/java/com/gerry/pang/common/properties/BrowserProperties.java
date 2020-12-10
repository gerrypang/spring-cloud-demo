package com.gerry.pang.common.properties;

import com.gerry.pang.common.enums.LoginTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrowserProperties {
	
	/**
	 * 登录页面
	 */
	private String loginPage = "/signIn.html";
	
	/**
	 * 登录形式
	 */
	private LoginTypeEnum loginType = LoginTypeEnum.JSON;
	
	/**
	 * 记住我失效时间，单位秒
	 */
	private int rememberMeExpriedSecond = 3600 * 12;
}
