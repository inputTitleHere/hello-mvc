package com.kh.mvc.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mvc.board.model.dto.Attachment;
import com.kh.mvc.board.model.dto.Board;
import com.kh.mvc.board.model.dto.BoardExt;
import com.kh.mvc.board.model.service.BoardService;
import com.kh.mvc.common.HelloMvcFileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * Servlet implementation class BoardEnrollServlet
 */
@WebServlet("/board/boardEnroll")
public class BoardEnrollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();
	/**
	 * GET 게시글 등록폼 요청
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/board/boardEnroll.jsp").forward(request, response);		
	}

	/**
	 * POST : DB insert 요청
	 * 첨부파일이 포함된 게시글 등록
	 * 1. 서버컴퓨터에 파일 저장
	 * 	- MultipartRequest객체 생성.
	 * 		- HttpServletRequest
	 * 		- saveDirectory
	 * 		- maxPostSize
	 * 		- encoding
	 * 		- FileRenamePolicy객체 - DefaultFileRenamePolicy(기본)
	 * 	* 주의사항 : 기존 request객체가 아닌 MultipartRequest객체에서 모든 사용자 입력값을 가져와야한다.
	 * 
	 * 2. 저장된 파일정보를 attachment테이블의 record으로 작성
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		try {
			// 0 첨부파일 처리
			ServletContext application = getServletContext(); // 해당 페이지의 서블릿 초기 정보 설정 정보를 담고 있는 config 내장 객체를 리턴한다
			
			String saveDirectory=application.getRealPath("/upload/board");
			int maxPostSize = 1024 * 1024 * 10; // 10MB (1kb * 1024 = 1MB)
			String encoding = "utf-8";
			FileRenamePolicy policy = new HelloMvcFileRenamePolicy();
			MultipartRequest multiReq = new MultipartRequest(request, saveDirectory, maxPostSize, encoding, policy); // 파일 생성?
			// 저장된 파일 체크
//			String originalFilename = multiReq.getOriginalFileName("upFile1");
//			String renamedFilename = multiReq.getFilesystemName("upFile1");
			
//			System.out.println("@BoardEnrollServlet : Original file name" + originalFilename);
//			System.out.println("@BoardEnrollServlet : renamed file name" + renamedFilename);
			
			
			// 1. 사용자 입력값 처리
			String title = multiReq.getParameter("title");
			String writer = multiReq.getParameter("writer");
			String content = multiReq.getParameter("content");
			BoardExt board = new BoardExt(0,title,writer,content,0,null); 
			
			Enumeration<String> filenames = multiReq.getFileNames();
			while(filenames.hasMoreElements()) {
				String filename=filenames.nextElement();
				File upFile=multiReq.getFile(filename);
				if(upFile!=null) {
					Attachment attach = new Attachment();
					attach.setOriginalFilename(multiReq.getOriginalFileName(filename));
					attach.setRenamedFilename(multiReq.getFilesystemName(filename));
					board.addAttachment(attach);
				}
			}
			// 2 업무로직
			int result = boardService.inputBoard(board); //이제 여기에 board하고 attachment모두를 담아야 한다.
			
			// 3 응답처리
			HttpSession session=request.getSession();
			session.setAttribute("msg", "게시글을 등록했습니다.");
			response.sendRedirect(request.getContextPath()+"/board/boardList"); // 게시판 첫페이지로.
		} catch (Exception e) {
			throw e;
		}
		
		
	}

}
