package com.kh.mvc.board.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mvc.board.model.dto.Board;
import com.kh.mvc.board.model.service.BoardSerivce;

/**
 * Servlet implementation class BoardEnrollServlet
 */
@WebServlet("/board/boardEnroll")
public class BoardEnrollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardSerivce boardService = new BoardSerivce();
	/**
	 * GET 게시글 등록폼 요청
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/board/boardEnroll.jsp").forward(request, response);		
	}

	/**
	 * POST : DB insert 요청
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String writer = request.getParameter("writer");
		String content = request.getParameter("content");
		Board b = new Board(0,title,writer,content,0,null); 
		
		int result = boardService.inputBoard(b);
		
		// 응답처리
		HttpSession session=request.getSession();
		session.setAttribute("msg", "게시글을 등록했습니다.");
		response.sendRedirect(request.getContextPath()+"/board/boardList"); // 게시판 첫페이지로.
		
		
	}

}
