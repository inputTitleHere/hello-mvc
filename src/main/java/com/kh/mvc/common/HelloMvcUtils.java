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

	/**
	 * totalPage = 전체 페이지 수
	 * pagebarSize = 한 페이지에 표시할 페이지 번호 개수
	 * pagebarStart ~ pagebarEnd
	 * pageNo = 증감변수
	 * 
	 * 1. 이전영역
	 * 2. pageNo영역
	 * 3. 다음영역
	 */
	public static String getPagebar(int cPage, int numPerPage, int totalContent, String url) {
		StringBuilder pagebar = new StringBuilder();
		url += (url.indexOf("?")<0) ? "?cPage=" : "&cPage="; //(Get request uri)에다가 쿼리 구문 추가.
		int totalPage = (int)Math.ceil((double)totalContent/numPerPage);
		int pagebarSize=5;
		int pagebarStart = ((cPage-1)/pagebarSize*pagebarSize)+1;
		int pagebarEnd = pagebarStart+pagebarSize-1;
		int pageNo = pagebarStart;
		
		// 이전영역 : 최초 페이지 넘버가 1(맨앞)이면 "다음"을 안붙임.
		if(pageNo==1) {
			//아무것도 안함
		}else {
			pagebar.append("<a href='"+url+(pageNo-1)+"'>이전</a>\n"); 
		}
		// pageNo 영역
		while(pageNo<=pagebarEnd&&pageNo<=totalPage) {
			if(pageNo==cPage) {
				pagebar.append("<span class='cPage'>"+pageNo+"</span>\n");
			}else { // 현재페이지가 아닌 경우
				pagebar.append("<a href='"+url+pageNo+"'>"+pageNo+"</a>\n");
			}
			pageNo++;
		}
		// 다음영역
		if(pageNo>totalPage) {
			//아무것도 안함.
		}else {
			pagebar.append("<a href='"+url+pageNo+"'>다음</a>\n");
		}
		return pagebar.toString();
	}
	
}




















