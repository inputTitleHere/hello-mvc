package com.kh.mvc.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MemberLogoutServlet
 */
@WebServlet("/member/logout")
public class MemberLogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 업무로직 -> 세션 객체를 가져와서 무효화 처리
		HttpSession session = request.getSession(false); // 세션 객체 존재하지 않으면 null 반환 (기본값 true로 생략가능함)
		
		if(session!=null) {
			session.invalidate();
		}
		// 2. 리다이렉트 처리로 로그아웃 
		response.sendRedirect(request.getContextPath()+"/");
	}


}
