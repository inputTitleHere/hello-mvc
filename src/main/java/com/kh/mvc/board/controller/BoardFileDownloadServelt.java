package com.kh.mvc.board.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.board.model.dto.Attachment;
import com.kh.mvc.board.model.service.BoardService;

/**
 * Servlet implementation class BoardFileDownloadServelt
 */
@WebServlet("/board/fileDownload")
public class BoardFileDownloadServelt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// 1. 사용자 입력값 처리
			int no = Integer.parseInt(request.getParameter("no"));
			System.out.println("@BoardFileDownloadServlet : no = " + no);

			// 2. 업무로직
			// a. attach 조회 -> 실제 첨부파일의 서버컴퓨터상의 저장된 파일명을 찾아야 하기때문.
			Attachment attach = boardService.findattachmentByNo(no);
			System.out.println("@BoardFileDownloadServlet : attach = " + attach);

			// 3. 응답 : 파일 입출력 처리
			// 입력 - saveDirectory + renamedFilename으로 찾아야 한다.
			// 응답 헤더 작성 contentType application/octet-stream
			response.setContentType("application/octect-stream"); // 이진데이터 사용시의 스트림.
			String filename = URLEncoder.encode(attach.getOriginalFilename(),"utf-8"); // 사용자에게 파일 반환시 파일 원본의 이름을 반환.
				// header 부분이 encoding이 안되므로 한글이 깨진다.(기본 인코딩은 ISO-8859-1)이다. -> URLEncoder으로 utf-8인코딩 해준다. 
			response.setHeader("Content-Disposition", "attachment;filename="+filename); // 응답해더에서 첨부파일임을 명시
			
			// 출력 - http응답 메세지 출력스트림에 쓴다. -> response.getOutputStream(); (바이트기반 스트림)
			// response에는 두개 스트림이 존재(바이트기반, 문자열기반).
			String saveDirectory = getServletContext().getRealPath("/upload/board");
			File downFile = new File(saveDirectory, attach.getRenamedFilename()); // java.io.File이다.
			try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(downFile));
					BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());) {
				byte[] buffer = new byte[8192]; // 8kb (1byte씩 읽을 수 있음 크면 대충 더 효율적)
				int len = 0; // 읽어온 byte 수
				while((len=bis.read(buffer))!=-1) { // 읽어온 데이터가 없으면 -1을 반환.
					bos.write(buffer,0,len); // buffer에서 0번지부터 len까지 출력(len은 exclusive하게 읽어짐)
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
