package com.gerry.pang.validate.code.image;

import java.awt.image.BufferedImage;

import com.gerry.pang.validate.code.base.ValidateCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageCode extends ValidateCode {
	
	/** 验证码图像*/
	private BufferedImage image;

	/**
	 * 初始化构造
	 * 
	 * @param image
	 * @param code
	 * @param expireIn 单位秒
	 */
	public ImageCode(BufferedImage image, String code, int expireIn) {
		super(code, expireIn);
		this.image = image;
	}


}
