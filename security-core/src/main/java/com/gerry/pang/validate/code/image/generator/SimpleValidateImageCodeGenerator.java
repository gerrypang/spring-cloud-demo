package com.gerry.pang.validate.code.image.generator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

import com.gerry.pang.common.properties.ImageCodeProperties;
import com.gerry.pang.common.properties.SecurityProperties;
import com.gerry.pang.validate.code.base.ValidateCode;
import com.gerry.pang.validate.code.base.ValidateCodeGenerator;
import com.gerry.pang.validate.code.image.ImageCode;

/**
 * 默认图形验证码生成器
 */
public class SimpleValidateImageCodeGenerator implements ValidateCodeGenerator {

	private SecurityProperties securityProperties;

	@Override
	public ValidateCode generator(HttpServletRequest request) {
		ImageCodeProperties imageCodeProperties = securityProperties.getValidateCode().getImageCode();
		int width =  ServletRequestUtils.getIntParameter(request, "width", imageCodeProperties.getWidth());
		int height = ServletRequestUtils.getIntParameter(request, "height", imageCodeProperties.getHeight());
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String sRand = "";
		int length = imageCodeProperties.getLength();
		for (int i = 0; i < length; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 16);
		}
		g.dispose();

		return new ImageCode(image, sRand, imageCodeProperties.getExpireIn());
	}
	
	/**
	 * 生成随机背景条纹
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

}
