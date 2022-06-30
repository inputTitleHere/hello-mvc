package com.kh.mvc.board.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.board.model.dto.Board;
import com.kh.mvc.board.model.service.BoardService;
import com.kh.mvc.common.HelloMvcUtils;

/**
 * Servlet implementation class BoardViewServlet
 */
@WebServlet("/board/boardView")
public class BoardViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1 사용자 입력값 처리
			int no = Integer.parseInt(request.getParameter("no"));
			// a. 읽음 여부 판단.
			Cookie[] cookies = request.getCookies(); // javax.servlet의 cookie이다.
			String boardCookieVal="";
			boolean hasRead=false;
			
			if(cookies!=null) {
				for(Cookie c : cookies) {
					String name = c.getName();
					String value = c.getValue();
					if("boardCookie".equals(name)) {
						boardCookieVal=value;
						if(value.contains("["+no+"]")) { // 임의로 꺽쇠로 감쌓았다.
							hasRead=true;
						}
						break;
					}
				}
			}
			// 쿠키 처리
			if(!hasRead) {
				Cookie cookie = new Cookie("boardCookie",boardCookieVal+"["+no+"]"); // 기존값에 추가.
				cookie.setPath(request.getContextPath()+"/board/boardView"); // 클라이언트에서 서버로 보낼때 언제 보내냐.
				cookie.setMaxAge(365*24*60*60); //1년 (거의 영속적)
				response.addCookie(cookie);
				System.out.println("@BoardViewServlet : boardCookie가 신규 발급됨 : "+cookie.getValue());
			}
			
			
			// 2 업무로직
			// 게시글 조회 및 조회수 증가처리
			Board board = hasRead ? boardService.findByNo(no):boardService.findByNo(no,hasRead); // Board으로 받지만 attachment를 함유하는 BoardExt형식이다.
			
			// XSS 공격 대비 : Cross-Site Scripting 공격 (신뢰할 수 없는 사용자 입력값을 그대로 처리하는 것) -> 2008년 옥션 개인정보 사태때부터 있음.
			board.setTitle(HelloMvcUtils.escapeXml(board.getTitle()));
			board.setContent(HelloMvcUtils.escapeXml(board.getContent()));
			
			
			
			// 개행문자 처리(content 영역)
			board.setContent(HelloMvcUtils.convertLineFeedToBr(board.getContent()));
			
			// 3 view 처리
			request.setAttribute("board", board);
			request.getRequestDispatcher("/WEB-INF/views/board/boardView.jsp").forward(request, response);
			
			
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		
	}

}
