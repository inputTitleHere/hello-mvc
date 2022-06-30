package com.kh.mvc.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mvc.board.model.dto.Attachment;
import com.kh.mvc.board.model.dto.BoardExt;
import com.kh.mvc.board.model.service.BoardService;

/**
 * Servlet implementation class BoardDeleteServlet
 */
@WebServlet("/board/boardDelete")
public class BoardDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService(); 
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//TODO : 게시글 삭제시 첨부파일도 삭제하기.  -> DB쪽은 on delete cascade적용되어서 삭제됨. 근데 local에서 파일이 남음 -> 이것도 지워야함.
		
		/*
		 * 1. 저장된 첨부파일 삭제. 
		 * 		Java에서 파일 지우기 -> (java.io.File#delete) 메소드를 사용하자.
		 * 2. board 삭제 : (on delete cascade에 의해 attachment 연쇄 삭제)
		 */
		int no = Integer.parseInt(request.getParameter("no"));
		// 저장 첨부파일 삭제
		// 저장된 파일의 이름들 획득 
		List<Attachment> attachments = ((BoardExt)boardService.findByNo(no)).getAttachments();
		
		// 파일이 저장되는 위치 찾기. 
		ServletContext application = getServletContext();
		String localFileDirectory = application.getRealPath("/upload/board");
		
		// 파일을 시스템에서 삭제
		for(Attachment attach : attachments) {
			File file = new File(localFileDirectory+"\\"+attach.getRenamedFilename());
			//File file = new File(localFileDirectory, attach.getRenamedFilename()); // 파일 찾는 것을 이렇게 해도 된다.
//			System.out.println(file.getPath());
			boolean fileDeleteResult = file.delete();
			System.out.println("@BoardDeleteServlet : fileDeleteResult OF "+attach.getRenamedFilename()+" IS "+fileDeleteResult);
		}
		
		// DB에서 게시글 제거
		int result = boardService.deleteByNo(no);
		
		// redirect
		HttpSession session=request.getSession();
		session.setAttribute("msg", "게시글을 삭제했습니다.");
		response.sendRedirect(request.getContextPath()+"/board/boardList");
	}

}
