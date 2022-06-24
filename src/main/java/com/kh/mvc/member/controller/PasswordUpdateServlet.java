package com.kh.mvc.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mvc.common.HelloMvcUtils;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class PasswordUpdateServlet
 */
@WebServlet("/member/passwordUpdate")
public class PasswordUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/member/passwordUpdate.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("loginMember");
		System.out.println("[@PasswordUpdateServlet] : "+member.getMemberId());
		
		
		String oldPassword = HelloMvcUtils.getEncryptedPassword(request.getParameter("oldPassword"), member.getMemberId());
		String currentPassword = member.getPassword();
		
		if(!currentPassword.equals(oldPassword)){
			session.setAttribute("msg", "비밀번호가 틀렸습니다.");
			System.out.println("입력 : " +oldPassword);
			System.out.println("저장된값 : "+member.getPassword());
			response.sendRedirect(request.getContextPath()+"/member/passwordUpdate");
		}else {
			String newPassword = HelloMvcUtils.getEncryptedPassword(request.getParameter("newPassword"), member.getMemberId());
			memberService.updatePassword(member, newPassword);		
			session.setAttribute("msg", "비밀번호 변경 성공");
			// 세션 갱신 
			member.setPassword(newPassword);
			session.setAttribute("loginMember", member);
			
			response.sendRedirect(request.getContextPath()+"/member/memberView");
		}
		
		
		
		
	}

}
