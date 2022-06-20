package com.kh.mvc.member.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mvc.member.model.dto.Gender;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class MemberEnrollServlet
 */
@WebServlet("/member/enroll")
public class MemberEnrollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/member/memberEnroll.jsp").forward(request, response);
		
	}
	
	
	// DB insert 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1 인코딩 처리
			// insertMember = insert into member values(?,?,?,default,?,?,?,?,?,default,default)
			request.setCharacterEncoding("utf-8");
			
			// 2 사용자 입력값 처리
			String memberId = request.getParameter("memberId");
			String password = request.getParameter("password");
			String memberName = request.getParameter("memberName");
			String _gender = request.getParameter("gender");
			String _birthday = request.getParameter("birthday");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String[] hobbies = request.getParameterValues("hobby");
			String hobby = hobbies!=null?String.join(", ", hobbies):null;
			Date birthday=(_birthday!=null&&!"".equals(_birthday))?Date.valueOf(_birthday):null;

			Gender gender = _gender!=null?Gender.valueOf(_gender):null;
			
			Member member = new Member(memberId, password, memberName, null,gender,birthday,email, phone, hobby, 0,null);
			System.out.println("@memberenrollservlet"+member);
			// 3 업무로직 : DB insert
			
			int result=memberService.inputMember(member);
			
			
			System.out.println("@MemberEnrollServlet : "+result);
			
			// 4 응답 처리 : redirect
			HttpSession session = request.getSession();
			session.setAttribute("msg", "회원가입이 정상처리되었습니다.");
			response.sendRedirect(request.getContextPath()+"/");
		} catch (Exception e) {
			e.printStackTrace(); // 로깅 책임이 있음
			throw e; // WAS(Tomcat)에게 예외 던지기
		}
		
	}

}
