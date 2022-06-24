package com.kh.mvc.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class AdminMemberListServlet
 */
@WebServlet("/admin/memberList")
public class AdminMemberListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberService memberService = new MemberService();
		try {
			// 1. 사용자입력값
			
			// 2. 업무로직
			// select * from member order by enroll_date desc
			List<Member>list=memberService.findAll();
//			System.out.println("list = "+list);
			
			// 3. view를 통한 응답처리
			request.setAttribute("list", list);
			List<Member> members = (List<Member>)request.getAttribute("list");
			request.getRequestDispatcher("/WEB-INF/views/admin/memberList.jsp").forward(request, response);
			
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}// END doGet

}
