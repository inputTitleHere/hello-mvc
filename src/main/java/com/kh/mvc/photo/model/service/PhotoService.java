package com.kh.mvc.photo.model.service;

import com.kh.mvc.photo.dto.Photo;
import com.kh.mvc.photo.model.dao.PhotoDao;
import static com.kh.mvc.common.JdbcTemplate.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;



public class PhotoService {

	private PhotoDao photoDao = new PhotoDao();

	public int getTotalContent() {
		Connection conn = getConnection();
		int totalContent = photoDao.getTotalContent(conn);
		close(conn);
		return totalContent;
	}

	public List<Photo> selectPhotoMore(Map<String, Object> param) {
		Connection conn = getConnection();
		List<Photo> list = photoDao.selectPhotoMore(conn, param);
		
		close(conn);
		return list;
	}
	
	
	
	
}
