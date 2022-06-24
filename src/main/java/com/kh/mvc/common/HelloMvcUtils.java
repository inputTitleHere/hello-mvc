package com.kh.mvc.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

public class HelloMvcUtils {

	// 암호화 처리는 두단계를 지닌다. 1. 암호화 처리 2. 인코딩 처리
	public static String getEncryptedPassword(String rawPassword, String salt) {
		String encryptedPwd = null;
		try {
			// 1. 암호화
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] input=rawPassword.getBytes("utf-8");
			byte[] saltBytes = salt.getBytes("utf-8");
			
			md.update(saltBytes); // salt 전달. salt를 먼저 전달한다.
			byte[] encryptedBytes=md.digest(input); // 이후 비밀번호를 전달한다.
			
			//System.out.println(new String(encryptedBytes));
			
			// 2. 인코딩 처리
			Encoder encoder = Base64.getEncoder();
			encryptedPwd=encoder.encodeToString(encryptedBytes);
			
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		return encryptedPwd;
	}
	
}
