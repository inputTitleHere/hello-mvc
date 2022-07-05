package com.kh.mvc.board.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.board.model.service.BoardService;

/**
 * Servlet implementation class BoardCommentDelete
 */
@WebServlet("/board/boardCommentDelete")
public class BoardCommentDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int no = Integer.parseInt(request.getParameter("no"));
		System.out.println("@boardCommentDelete : no = "+no);
		
		int result = boardService.deleteCommentByNo(no);
		
		String referer= request.getHeader("Referer");
		response.sendRedirect(referer);
	}
}
