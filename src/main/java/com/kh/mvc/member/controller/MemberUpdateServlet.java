package com.kh.mvc.member.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mvc.member.model.dto.Gender;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class MemberUpdateServlet
 */
@WebServlet("/member/memberView")
public class MemberUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Run memberUpdate doPost");
		try {
			// 1 인코딩 처리
			// insertMember = insert into member values(?,?,?,default,?,?,?,?,?,default,default)
//			request.setCharacterEncoding("utf-8"); // -> filter 에서 처리해줌.
			
			// 2 사용자 입력값 처리
			String memberId = request.getParameter("memberId");
//			String password = request.getParameter("password");
			String memberName = request.getParameter("memberName");
			String _gender = request.getParameter("gender");
			String _birthday = request.getParameter("birthday");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String[] hobbies = request.getParameterValues("hobby");
			String hobby = hobbies!=null?String.join(", ", hobbies):null;
			Date birthday=(_birthday!=null&&!"".equals(_birthday))?Date.valueOf(_birthday):null;
			int point = Integer.parseInt(request.getParameter("point"));
			Gender gender = _gender!=null?Gender.valueOf(_gender):null;
			
			
			Member member = new Member(memberId, null, memberName, null,gender,birthday,email, phone, hobby, point,null);
			System.out.println("POINT : "+member.getPoint());
			
			System.out.println("@memberUpdateservlet"+member);
			// 3 업무로직 : DB insert
			
			int result=memberService.updateMember(member); // 실질 업데이트 실행코드
			// 쿠키 및 정보 업데이트
			HttpSession session = request.getSession();
			session.setAttribute("loginMember", member);
			
			// save id 처리를 해줌. -> cookie를 이용해서 처리한다.
			Cookie cookie = new Cookie("saveId", memberId);
			cookie.setPath(request.getContextPath());
			
			
			
			System.out.println("@memberUpdateServlet : "+result);
			
			// 4 응답 처리 : redirect
			session.setAttribute("msg", "회원정보 수정이 정상처리되었습니다.");
			response.sendRedirect(request.getContextPath()+"/member/memberView");
		} catch (Exception e) {
			e.printStackTrace(); // 로깅 책임이 있음
			throw e; // WAS(Tomcat)에게 예외 던지기
		}
	}
	
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		request.getRequestDispatcher("/WEB-INF/views/member/memberView.jsp").forward(request, response);
		
	}

	
}
