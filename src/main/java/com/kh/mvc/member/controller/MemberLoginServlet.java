package com.kh.mvc.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class MemberLoginServlet
 */
@WebServlet("/member/login")
public class MemberLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			//1 인코딩 처리
			request.setCharacterEncoding("utf-8");
			
			//2 사용자 입력 처리
			String memberId=request.getParameter("memberId");
			String password = request.getParameter("password");
			String saveId = request.getParameter("saveId");
			System.out.println("member id = "+memberId);
			System.out.println("password = "+password);
			System.out.println("saveId = "+saveId); // "on" || null
			
			//3 비즈니스 로직(처리로직) -- 로그인 여부 판단
			// memberId를 기반으로 DB에 가서 조회해온다.
			
			
			Member member = memberService.findById(memberId);
			System.out.println("member@MemberLoginServlet = "+member);
					
			HttpSession session = request.getSession();
			System.out.println(session.getId());
			
			
			// 
			if(member!=null && password.equals(member.getPassword())) {
				// 로그인 성공
				session.setAttribute("loginMember", member);
				
				// save id 처리를 해줌. -> cookie를 이용해서 처리한다.
				Cookie cookie = new Cookie("saveId", memberId);
				cookie.setPath(request.getContextPath());
//			saveID 사용하는 경우
				if(saveId!=null) {
					cookie.setMaxAge(7*24*60*60);
				}else {
					cookie.setMaxAge(0); // 즉시 삭제
				}
				response.addCookie(cookie); // 응답 메세지에 set cookie 항목으로 전송
				
			}else {
				// 로그인 실패 - ID not exist OR Password wrong
				session.setAttribute("msg",	"아이디 또는 비밀번호가 일치하지 않습니다.");
			}
			
			//4 응답메시지 
//		RequestDispatcher reqDispatcher = request.getRequestDispatcher("/index.jsp");
//		reqDispatcher.forward(request, response);
			
			response.sendRedirect(request.getContextPath()+"/"); // /mvc와 동일한 효과 => /mvc/으로 끝내기 마무리를 하자.
		} catch (Exception e) {
			e.printStackTrace(); // 로깅
			throw e; // tomcat에게 예외 투척
		}
		
	}

}
