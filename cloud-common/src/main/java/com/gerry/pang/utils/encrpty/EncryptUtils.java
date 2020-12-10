package com.gerry.pang.utils.encrpty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 加/解密工具类
 * 
 * @author pangguowei
 */
public class EncryptUtils {
	
    /** 算法 */
    private static final String CIPHER_MODE = "AES/ECB/PKCS5Padding";
	
	/**
	 *  设置加密密码处理长度。 不足此长度补0；
	 */
	private static final int PWD_SIZE = 16;
	
	private EncryptUtils() {
		// do nothing
	}
	
	/**
	 * 密码处理方法 如果加解密出问题， 请先查看本方法，排除密码长度不足补"0",导致密码不一致
	 * 
	 * @param password 待处理的密码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] pwdHandler(String password) throws UnsupportedEncodingException {
		byte[] data = null;
		if (password == null) {
			password = "";
		}
		StringBuffer sb = new StringBuffer(PWD_SIZE);
		sb.append(password);
		while (sb.length() < PWD_SIZE) {
			sb.append("0");
		}
		if (sb.length() > PWD_SIZE) {
			sb.setLength(PWD_SIZE);
		}
		data = sb.toString().getBytes("UTF-8");
		return data;
	}
	
	/**
	 * 3des 加密
	 * 
	 * @param plainText 明文
	 * @param key 密钥
	 * @param charset 编码
	 * @return
	 */
	public static byte[] des3Encrypt(String plainText, String key, String charset) {
		try {
			Charset.forName(charset);
			final SecretKey secretKey = new SecretKeySpec(key.getBytes(charset), "DESede");
			final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return cipher.doFinal(plainText.getBytes(charset));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * 3des 加密
	 * 
	 * @param plainText 明文
	 * @param key 密钥
	 * @param charset 编码
	 * @param icv 向量
	 * @return
	 */
	public static byte[] des3Encrypt(String plainText, String key, String charset, byte[] icv) {
		try {
			Charset.forName(charset);
			final SecretKey secretKey = new SecretKeySpec(key.getBytes(charset), "DESede");
			final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			final IvParameterSpec iv = new IvParameterSpec(icv);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			return cipher.doFinal(plainText.getBytes(charset));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 3des 解密
	 * 
	 * @param cipherText 密文
	 * @param key 密钥
	 * @param charset 编码
	 * @return
	 */
	public static String des3Decrypt(byte[] cipherText, String key, String charset) {
		try {
			Charset.forName(charset);
			final SecretKey secretKey = new SecretKeySpec(key.getBytes(charset), "DESede");
			final Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			c1.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] bytes = c1.doFinal(cipherText);
			return new String(bytes, charset);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * 3des 解密
	 * 
	 * @param cipherText 密文
	 * @param key 密钥
	 * @param charset 编码
	 * @param icv 向量
	 * @return
	 */
	public static String des3Decrypt(byte[] cipherText, String key, String charset, byte[] icv) {
		try {
			Charset.forName(charset);
			final SecretKey secretKey = new SecretKeySpec(key.getBytes(charset), "DESede");
			final Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			final IvParameterSpec iv = new IvParameterSpec(icv);
			c1.init(Cipher.DECRYPT_MODE, secretKey, iv);
			byte[] bytes = c1.doFinal(cipherText);
			return new String(bytes, charset);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	

	public static String RSAEncryptByPublicKey(String data, String publicKey, String charset) {
		byte[] encryptData;
		try {
			Charset.forName(charset);
			encryptData = RSAEncrypt(data.getBytes(charset), publicKey, 1);
			return base64Encode(encryptData);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String RSAEncryptByPrivateKey(String data, String privateKey, String charset) {
		byte[] decryptData;
		try {
			Charset.forName(charset);
			decryptData = RSADecrypt(data.getBytes(charset), privateKey, 1);
			return base64Encode(decryptData);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String RSADecryptByPublicKey(String data, String publicKey, String charset) {
		byte[] dataByte;
		try {
			Charset.forName(charset);
			dataByte = base64Decode(data);
			byte[] decryptData = RSAEncrypt(dataByte, publicKey, 1);
			return new String(decryptData, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String RSADecryptByPrivateKey(String data, String privateKey, String charset) {
		byte[] dataByte;
		try {
			Charset.forName(charset);
			dataByte = base64Decode(data);
			byte[] decryptData = RSADecrypt(dataByte, privateKey, 2);
			return new String(decryptData, charset);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] RSAEncrypt(byte[] data, String key, int mode) {
		byte[] cipherText = null;
		try {
			RSAPublicKey publicKey = getRSAPublicKey(key);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(mode, publicKey);
			int maxEncryptBlockSize = publicKey.getModulus().bitLength() / 8 - 11;
			cipherText = encryptOrDecrypt(data, cipher, maxEncryptBlockSize);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return cipherText;
	}

	public static byte[] RSADecrypt(byte[] data, String key, int mode) {
		byte[] decryptedData = null;
		try {
			RSAPrivateKey privateKey = getRSAPrivateKey(key);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(mode, privateKey);
			int maxDecryptBlockSize = privateKey.getModulus().bitLength() / 8;
			decryptedData = encryptOrDecrypt(data, cipher, maxDecryptBlockSize);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return decryptedData;
	}

	public static RSAPublicKey getRSAPublicKey(String key) {
		byte[] keyBytes = base64Decode(key);
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
		RSAPublicKey publicKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = (RSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return publicKey;
	}

	public static RSAPrivateKey getRSAPrivateKey(String key) {
		byte[] keyBytes = base64Decode(key);
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		RSAPrivateKey privateKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			privateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return privateKey;
	}

	private static byte[] encryptOrDecrypt(byte[] data, Cipher cipher, int maxSize) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] result = null;
		try {
			int dataLength = data.length;
			for (int i = 0; i < data.length; i += maxSize) {
				int encryptLength = (dataLength - i < maxSize) ? dataLength - i : maxSize;
				byte[] doFinal = cipher.doFinal(data, i, encryptLength);
				bout.write(doFinal);
			}
			result = bout.toByteArray();
		} catch (Exception e) {
		} finally {
			if (bout != null)
				try {
					bout.close();
				} catch (IOException e) {
				}
		}
		return result;
	}

	public static String base64Encode(String input, String charset) {
		try {
			Charset.forName(charset);
			return base64Encode(input.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String base64Encode(byte[] input) {
		return Base64.encodeBase64String(input);
	}

	public static byte[] base64Decode(String input) {
		try {
			return Base64.decodeBase64(input);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String base64Decode(String input, String charset) {
		try {
			Charset.forName(charset);
			return new String(Base64.decodeBase64(input), charset);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String urlEncode(String input, String charset) {
		try {
			Charset.forName(charset);
			return URLEncoder.encode(input, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String urlDecode(String input, String charset) {
		try {
			Charset.forName(charset);
			return URLDecoder.decode(input, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String sha256Digest(String input, String charset) {
		MessageDigest md;
		try {
			Charset.forName(charset);
			md = MessageDigest.getInstance("SHA-256");
			byte[] bytes = md.digest(input.getBytes(charset));

			StringBuilder sb = new StringBuilder(64);
			for (int i = 0; i < bytes.length; ++i) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1)
					sb.append('0');
				sb.append(hex);
			}

			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * DSC 加密
	 * 
	 * @param dataSource String
	 * @param password   String
	 * @return byte[]
	 */
	public static byte[] desEncrypt(String dataSource, String password) {
		if (dataSource == null) {
			return null;
		}
		try {
			byte[] byteArray = dataSource.getBytes();
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(byteArray);
		} catch (Throwable e) {
			throw new RuntimeException("DSC加密失败", e);
		}
	}
	
	/**
	 * DSC 加密
	 * 
	 * @param dataSource
	 * @param password
	 * @return
	 */
	public static String desEncrypt(String dataSource, String password, String charset) {
		try {
			Charset.forName(charset);
			return base64Encode(desEncrypt(dataSource, password));
		} catch (Throwable e) {
			throw new RuntimeException("DSC加密失败", e);
		}
	}

	/**
	 * DSC 解密
	 * 
	 * @param dataSource String
	 * @param password String
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] desDecrypt(byte[] dataSource, String password) {
		if (dataSource == null) {
			return null;
		}
		try {
			// DES算法要求有一个可信任的随机数源
			SecureRandom random = new SecureRandom();
			// 创建一个DESKeySpec对象
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			// 创建一个密匙工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// 将DESKeySpec对象转换成SecretKey对象
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey, random);
			// 真正开始解密操作
			return cipher.doFinal(dataSource);
		} catch (Exception e) {
			throw new RuntimeException("DSC解密失败", e);
		}
	}
	
	/**
	 * DSC 解密
	 * 
	 * @param dataSource
	 * @param password
	 * @return
	 */
	public static String desDecrypt(String dataSource, String password) {
		try {
			return new String(desDecrypt(base64Decode(dataSource), password));
		} catch (Throwable e) {
			throw new RuntimeException("DSC解密失败", e);
		}
	}
	
	/**
	 * AES加密
	 * 
	 * @param content
	 * @param encryptKey
	 * @return
	 * @throws Exception
	 */
	 public static String aesEncryptToStr(String content, String encryptKey) throws Exception {
		try {
			byte[] encryptByte =aesEncrypt(content.getBytes(), encryptKey);
			return base64Encode(encryptByte);
		} catch (Exception e) {
			throw new RuntimeException("AES加密失败", e);
		}
	 }
	 
    /**
     * AES加密
     * 
     * @param clearTextBytes 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncrypt(byte[] clearTextBytes, String encryptKey) throws Exception {
		try {
			byte[] pwdBytes = pwdHandler(encryptKey);
			// 1 获取加密密钥
            SecretKeySpec keySpec = new SecretKeySpec(pwdBytes, "AES");
            // 2 获取Cipher实例
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            // 查看数据块位数 默认为16（byte） * 8 =128 bit
            // System.out.println("数据块位数(byte)：" + cipher.getBlockSize());
            // 3 初始化Cipher实例。设置执行模式以及加密密钥
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            // 4 执行
            byte[] cipherTextBytes = cipher.doFinal(clearTextBytes);
            // 5 返回密文字符集
            return cipherTextBytes;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
    }
    
    /**
     * AES解密
     * 
     * @param encrypt
     * @param decryptKey
     * @return
     * @throws Exception
     */
    public static String aesDecryptToStr(String encrypt, String decryptKey) throws Exception {
		try {
			return new String(aesDecrypt(base64Decode(encrypt), decryptKey));
		} catch (Exception e) {
			throw new RuntimeException("AES解密失败", e);
		}
    }
    
    /**
     * AES解密
     * 
     * @param cipherTextBytes 待解密的byte[]
     * @param decryptKey 解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static byte[] aesDecrypt(byte[] cipherTextBytes, String decryptKey) throws Exception {
    	try {
			byte[] pwdBytes = pwdHandler(decryptKey);
			// 1 获取解密密钥
            SecretKeySpec keySpec = new SecretKeySpec(pwdBytes, "AES");
            // 2 获取Cipher实例
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            // 查看数据块位数 默认为16（byte） * 8 =128 bit
            // System.out.println("数据块位数(byte)：" + cipher.getBlockSize());
            // 3 初始化Cipher实例。设置执行模式以及加密密钥
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            // 4 执行
            byte[] clearTextBytes = cipher.doFinal(cipherTextBytes);
            // 5 返回明文字符集
            return clearTextBytes;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
    }
    
    /**
	 * 将byte数组转换为表示16进制值的字符串，
	 * 如：byte[]{8,18}转换为：0813，
	 * 和public static byte[] hexStr2ByteArr(String strIn)
	 * 互为可逆的转换过程
	 * @param bytes 需要转换的byte数组
	 * @param toUpperCase 是否转大写
	 * @return 转换后的字符串
	 * @throws Exception 本方法不处理任何异常，所有异常全部抛出
	 */
	public static String byteArr2HexStr(byte[] bytes, boolean toUpperCase) throws Exception {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        String tmp = "";
        for (int n = 0; n < bytes.length; n++) {
            // 整数转成十六进制表示
            tmp = (Integer.toHexString(bytes[n] & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        if (toUpperCase) {
        	return sb.toString().toUpperCase(); // 转成大写
        }
        return sb.toString();
	}
 
 
	/**
	 * 将表示16进制值的字符串转换为byte数组，
	 * 和public static String byteArr2HexStr(byte[] arrB)
	 * 互为可逆的转换过程
	 * @param str 需要转换的字符串
	 * @return 转换后的byte数组
	 * @throws Exception 本方法不处理任何异常，所有异常全部抛出
	 */
	public static byte[] hexStr2ByteArr(String str) throws Exception {
		if (str == null || str.length() < 2) {
			return new byte[0];
		}
		str = str.toLowerCase();
		int l = str.length() / 2;
		byte[] result = new byte[l];
		for (int i = 0; i < l; ++i) {
			String tmp = str.substring(2 * i, 2 * i + 2);
			result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
		}
		return result;
	}
	
	 /**
     * paramMap为请求参数的集合
     * 
     * security 提供的加密参数
     * 
     * @param paramMap
     * @param security
     * @return
     */
    public static String getSign(Map<String, String> paramMap, String security) {
        List<String> paramList = new ArrayList<>();
        for (Entry<String, String> entry : paramMap.entrySet()) {
            String keyName = entry.getKey();
            if ("sign".equals(keyName) || "Sign".equals(keyName)) {
                continue;
            }
            String value = entry.getValue();
            paramList.add(keyName + "=" + value);
        }
        Collections.sort(paramList);// 参数按照A--Z排序
        StringBuilder paramBuilder = new StringBuilder(256);
        for (String param : paramList) {
            paramBuilder.append(param).append("&");
        }
        String params = paramBuilder.toString();
		if (params.endsWith("&")) {
			params = params.substring(0, params.length() - 1);
		}
		// System.out.println(params);
        // 最后加上security小写
        params += security.toLowerCase();
        // System.out.println(params);
        String sign = null;
        try {
        	// md5加密以后
            sign = md5(params);
        } catch (Exception e) {
        	 throw new RuntimeException("请求参数签名生成错误", e);
        }
        return sign;
    }

    /**
     * Md5生成方式
     * 
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String md5(final String key) {
        StringBuilder sb = new StringBuilder(128);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes("UTF-8"));
            byte[] data = md.digest();
            int index;
            for (byte b : data) {
                index = b;
                if (index < 0) {
                    index += 256;
                }
                if (index < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(index));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Md5生成失败", e);
        } catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Md5生成失败", e);
		}
        return sb.toString().toUpperCase();
    }
    
    /**
	 * 将object对象转换为map
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 * @author pangguowei
	 */
    public static Map<String, String> Obj2Map(Object obj) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.get(obj) != null) {
				map.put(field.getName(), field.get(obj).toString());
			}
		}
		return map;
	}
    
    /**
     * hmac 算法
     * 
     * @param algorithms
     * @param key
     * @param valueToDigest
     * @return
     * @author pangguowei 
     * @since 2020年5月12日
     */
    public static byte[] hmac(HmacAlgorithms algorithms, final byte[] key, final byte[] valueToDigest) {
    	byte[] sign = null;
    	if (HmacAlgorithms.HMAC_SHA_1.equals(algorithms)) {
    		sign = HmacUtils.hmacSha1(key, valueToDigest);
    		return sign;
    	}
    	if (HmacAlgorithms.HMAC_MD5.equals(algorithms)) {
    		sign = HmacUtils.hmacMd5(key, valueToDigest);
    		return sign;
    	}
    	if (HmacAlgorithms.HMAC_SHA_256.equals(algorithms)) {
    		sign = HmacUtils.hmacSha256(key, valueToDigest);
    		return sign;
    	}
    	if (HmacAlgorithms.HMAC_SHA_384.equals(algorithms)) {
    		sign = HmacUtils.hmacSha384(key, valueToDigest);
    		return sign;
    	}
    	if (HmacAlgorithms.HMAC_SHA_512.equals(algorithms)) {
    		sign = HmacUtils.hmacSha512(key, valueToDigest);
    		return sign;
    	}
		return sign;
    }
}
