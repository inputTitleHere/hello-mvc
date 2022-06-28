package com.kh.mvc.admin.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.dto.MemberRole;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class AdminMemberRoleUpdate
 */
@WebServlet("/admin/memberRoleUpdate")
public class AdminMemberRoleUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//1 사용자 입력값 처리
			String memberId = request.getParameter("memberId");
			MemberRole memberRole = MemberRole.valueOf(request.getParameter("memberRole"));
			Member member = new Member();
			member.setMemberId(memberId);
			member.setMemberRole(memberRole);
			System.out.println("@ AdminMemberRoleUpdate : member = "+member);
			//2 업무로직
			//updateMemberRole = update member set member_role = ? where member_id = ?
			int result = memberService.updateMemberRole(member);
			
			//3 redirect 처리 바로함 (DML이므로)
			HttpSession s = request.getSession();
			s.setAttribute("msg", "회원 권한을 성공적으로 수정했습니다.");
			
			String referer=request.getHeader("Referer");
			response.sendRedirect(referer); // 머물던 페이지
			
			
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}

}
