package com.kh.mvc.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.board.model.dto.Attachment;
import com.kh.mvc.board.model.dto.Board;
import com.kh.mvc.board.model.dto.BoardExt;
import com.kh.mvc.board.model.service.BoardService;
import com.kh.mvc.common.HelloMvcFileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * Servlet implementation class BoardUpdateServlet
 */
@WebServlet("/board/boardUpdate")
public class BoardUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();

	/**
	 * GET -> 수정 Form을 요청함.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// view에서 ?no= 전달받음
		BoardExt board = (BoardExt) boardService.findByNo(Integer.parseInt(request.getParameter("no")));
		request.setAttribute("board", board);
		

		request.getRequestDispatcher("/WEB-INF/views/board/boardUpdate.jsp").forward(request, response);
	}

	/**
	 * 실제 DB에 요청
	 * - 파일 업로드 포함 가능.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletContext application = getServletContext(); // 해당 페이지의 서블릿 초기 정보 설정 정보를 담고 있는 config 내장 객체를 리턴한다
		String saveDirectory=application.getRealPath("/upload/board");
		int maxPostSize = 1024 * 1024 * 10; // 10MB (1kb * 1024 = 1MB)
		String encoding = "utf-8";
		FileRenamePolicy policy = new HelloMvcFileRenamePolicy();
		MultipartRequest multiReq = new MultipartRequest(request, saveDirectory, maxPostSize, encoding, policy); // 파일 생성 -> 전달받은 파일은 모두 저장.
		
		// 기본적인 board객체.
		int no = Integer.parseInt(multiReq.getParameter("no"));
		String title = multiReq.getParameter("title");
		String content = multiReq.getParameter("content");
		String writer = multiReq.getParameter("writer");
		BoardExt board = new BoardExt(no,title,writer,content,0,null,0); 
		
		// boardNo를 가져와서 attachment DB에 쿼리, 이 boardNo 소속의 attachments를 가져옴. (없으면 빈 ArrayList이다.)
		List<Attachment> attachments = boardService.findAttachmentByBoardNo(no);
		
		// 강사님 삭제파일 코드
		String[] delFiles=multiReq.getParameterValues("delFile");
		if(delFiles!=null) {
			for(String temp : delFiles) {
				int attachNo=Integer.parseInt(temp);
				// 첨부파일 삭제
				Attachment attach = boardService.findattachmentByNo(attachNo);
				File delFile = new File(saveDirectory, attach.getRenamedFilename());
				delFile.delete();
				
				int result =boardService.removeAttachmentByNo(attachNo);
			}
		}
		// 강사님 삭제파일 코드 끝
		
		
		
//		만약 기존파일을 삭제하고 신규파일을 넣는 경우 기존 파일 삭제.
		
		Set<String> originalFilenames = new HashSet<>();
		Enumeration<String> filenames = multiReq.getFileNames();
		while(filenames.hasMoreElements()) {
			String filename=filenames.nextElement();
			File upFile=multiReq.getFile(filename);
			if(upFile!=null) {
				originalFilenames.add(multiReq.getOriginalFileName(filename));
			
				Attachment attach = new Attachment();
				attach.setOriginalFilename(multiReq.getOriginalFileName(filename));
				attach.setRenamedFilename(multiReq.getFilesystemName(filename));
				board.addAttachment(attach);
			
			}
		}
		/*
		for(Attachment a : attachments) {
			if(!originalFilenames.contains(a.getOriginalFilename())) {
				int result = boardService.removeAttachmentByNo(a.getNo());
				File delFile = new File(saveDirectory, a.getRenamedFilename());
				delFile.delete();
			}
		}
		*/
		// 업무로직?
		int result = boardService.updateBoard(board);

		// 리다이렉트
		response.sendRedirect(request.getContextPath()+"/board/boardView?no="+board.getNo());
	}

}
