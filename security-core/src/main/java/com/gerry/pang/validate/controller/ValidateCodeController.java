package com.gerry.pang.validate.controller;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.gerry.pang.validate.code.base.ValidateCodeGenerator;
import com.gerry.pang.validate.code.image.ImageCode;
import com.gerry.pang.validate.code.sms.SmsCode;
import com.gerry.pang.validate.code.sms.sender.SmsCodeSender;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ValidateCodeController {
	
	@Autowired	
	private ValidateCodeGenerator simpleValidateImageCodeGenerator;
	
	@Autowired	
	private ValidateCodeGenerator simpleValidateSmsCodeGenerator;

	@Autowired	
	private SmsCodeSender smsCodeSender;
	
	public static final String SESSION_IMAGE_KEY = "session_key_image_code";
	
	public static final String SESSION_SMS_KEY = "session_key_sms_code";
	
	// spring session 工具类
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	/**
	 * 生成图片验证码接口
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/code/image")
	public void createImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 生成图形验证码
		ImageCode imageCode = (ImageCode) simpleValidateImageCodeGenerator.generator(request);
		// 将验证码存储到session
		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_IMAGE_KEY, imageCode);
		// 将生成的图片写到接口响应
		ImageIO.write(imageCode.getImage(), "jpeg", response.getOutputStream());
		log.info("图片验证码生成完成，imageCode：{}", imageCode);
	}

	/**
	 * 生成短信验证码接口
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletRequestBindingException 
	 */
	@GetMapping("/code/sms")
	public void createSmsCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 生成短信验证码 
		SmsCode smsCode = (SmsCode) simpleValidateSmsCodeGenerator.generator(request);
		// 将随验证码储到session
		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_SMS_KEY, smsCode);
		// 请求重必须要包含moblie参数
		final String mobile = ServletRequestUtils.getRequiredStringParameter(request, "mobile");
		// 发送短信验证码
		smsCodeSender.sender(mobile, smsCode.getCode());
		log.info("短信验证码生成发送完成, mobile：{}，smsCode:{}", mobile, smsCode);
	}
}
