package com.kh.mvc.admin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.common.HelloMvcUtils;
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
			int cPage=1; 
			int numPerPage=10;
			try {
				cPage = Integer.parseInt(request.getParameter("cPage"));
			} catch (NumberFormatException e) {
				//아무것도 안함 : 기본값 1로 유지됨.
			}
			// 2. 업무로직
			// a. content 영역 - paging query
			int start = (cPage-1)*numPerPage+1;
			int end = cPage*numPerPage;
			Map<String, Object>param=new HashMap<>();
			param.put("start", start);
			param.put("end", end);
			
			System.out.printf("cPage = %s, numPerPage = %s, start = %s, end = %s\n",cPage, numPerPage, start,end);
			
			// select * from member order by enroll_date desc
			List<Member>list=memberService.findAll(param);
			
			// b. pagebar 영역
			// select count(*) from member
			int totalContent = memberService.getTotalContent(); // 총 개시글 개수 구하기
			System.out.println("totalContent = "+totalContent);
			String url = request.getRequestURI(); // URL이 아니라 URI이다.
			String pagebar = HelloMvcUtils.getPagebar(cPage, numPerPage, totalContent, url);
			System.out.println("pageBar = "+pagebar);
			request.setAttribute("pagebar", pagebar);
			
			// 3. view를 통한 응답처리
			request.setAttribute("list", list);
			request.getRequestDispatcher("/WEB-INF/views/admin/memberList.jsp").forward(request, response);
			
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}// END doGet

}
