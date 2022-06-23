package com.kh.mvc.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;


@WebServlet("/member/checkIdDuplicate")
public class checkIdDuplicateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
	/**
	 * select * from member where member_id=?
	 * - member 객체 변환 == 해당 ID 사용 불가.
	 * - null : 사용가능한 id
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 인코딩 처리
		request.setCharacterEncoding("utf-8");
		
		// 2. 사용자 입력값 처리
		String memberId = request.getParameter("memberId");
		System.out.println("memberId = "+memberId);
		
		// 3. 업무로직
		Member member = memberService.findById(memberId);
		boolean available = member==null;
		System.out.println("available = "+available);
		
		// 4. 응답처리
		request.setAttribute("available", available);
		request.getRequestDispatcher("/WEB-INF/views/member/checkIdDuplicate.jsp").forward(request, response);
		
		
	}

	

}
