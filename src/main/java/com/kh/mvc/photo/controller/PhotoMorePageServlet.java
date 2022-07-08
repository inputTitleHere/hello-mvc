package com.kh.mvc.photo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.kh.mvc.photo.dto.Photo;
import com.kh.mvc.photo.model.service.PhotoService;

/**
 * Servlet implementation class PhotoMorePageServlet
 */
@WebServlet("/photo/morePage")
public class PhotoMorePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PhotoService photoService = new PhotoService();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1 사용자입력
			int cPage = Integer.parseInt(request.getParameter("cPage"));
			int numPerPage=5;
			int start=(cPage-1)*numPerPage+1;
			int end = cPage*numPerPage;
			Map<String, Object> param=new HashMap<String, Object>();
			param.put("start", start);
			param.put("end", end);
			// 2 업무로직
			List<Photo> list = photoService.selectPhotoMore(param);
			System.out.println("@PhotoMorePageServlet : list = "+list);
			
			response.setContentType("application/json; charset=utf-8");
			new Gson().toJson(list, response.getWriter());
			
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		
		
	}

}
