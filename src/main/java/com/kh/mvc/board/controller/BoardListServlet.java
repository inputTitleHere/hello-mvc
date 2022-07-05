package com.kh.mvc.board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.PrinterMakeAndModel;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.board.model.dto.Board;
import com.kh.mvc.board.model.service.BoardService;
import com.kh.mvc.common.HelloMvcUtils;
import com.kh.mvc.member.model.service.MemberService;


/**
 * 사용자 입력값 처리 cPage
 * 서비스단 요청 - 반환타입
 * 예외처리
 * 포워딩
 */
@WebServlet("/board/boardList")
public class BoardListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1 사용자 입력값
		try {
			int cPage=1;
			int numPerPage = 5;
			try {
				cPage = Integer.parseInt(request.getParameter("cPage"));
			}catch (NumberFormatException e) {}
			// 2. 업무로직
			// a. content area
			int start=(cPage-1)*numPerPage+1;
			int end = cPage*numPerPage;
			Map<String, Object>param=new HashMap<String, Object>();
			param.put("start", start);
			param.put("end", end);
			// load Board page from DB
			List<Board>boardList = boardService.loadBoard(param);
			
			// b. pagebar generation
			int totalContent = boardService.getTotalContentCount();
			String url = request.getRequestURI();
			String pagebar = HelloMvcUtils.getPagebar(cPage, numPerPage, totalContent, url);
			request.setAttribute("pagebar", pagebar);
			
			// 3. view 처리
			request.setAttribute("boardList", boardList);
			request.getRequestDispatcher("/WEB-INF/views/board/boardList.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	} // END doGET

}
