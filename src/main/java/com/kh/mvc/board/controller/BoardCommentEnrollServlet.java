package com.kh.mvc.board.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.board.model.dto.BoardComment;
import com.kh.mvc.board.model.dto.CommentLevel;
import com.kh.mvc.board.model.service.BoardService;

/**
 * Servlet implementation class BoardCommentEnrollServlet
 */
@WebServlet("/board/boardCommentEnroll")
public class BoardCommentEnrollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
//			1. 사용자 입력값 처리
			CommentLevel commentLevel = CommentLevel.valueOf(Integer.parseInt(request.getParameter("commentLevel"))); 
			
			String writer = request.getParameter("writer");
			String content  =request.getParameter("content");
			int boardNo = Integer.parseInt(request.getParameter("boardNo"));
			int commentRef = Integer.parseInt(request.getParameter("commentRef"));
			BoardComment boardComment=new BoardComment(0,commentLevel, writer, content, boardNo, commentRef,null);
			
			System.out.println("boardComment = "+boardComment);
			
//			2. 업무로직 
			int result = boardService.insertBoardComment(boardComment);
			
//			3. redirect 응답.
			response.sendRedirect(request.getContextPath()+"/board/boardView?no="+boardNo);
			
			
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
