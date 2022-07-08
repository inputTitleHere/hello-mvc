package com.kh.mvc.photo.model.dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.kh.mvc.board.model.dao.BoardDao;
import com.kh.mvc.photo.dto.Photo;
import com.kh.mvc.photo.model.exception.PhotoException;

import static com.kh.mvc.common.JdbcTemplate.*;


public class PhotoDao {
	
	private Properties prop = new Properties();
	
	
	public PhotoDao() {
		
		String fileName = PhotoDao.class.getResource("/sql/photo/photo-query.properties").getPath();
		try {
			prop.load(new FileReader(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}


	public int getTotalContent(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int totalContent =0;
		String sql = prop.getProperty("getTotalContent");
		
		
		try {
			pstmt=conn.prepareStatement(sql);
			rset=pstmt.executeQuery();
			while(rset.next()) {
				totalContent=rset.getInt(1);
			}
		}catch(SQLException e) {
			throw new PhotoException("총 게시글 수 조회 오류!",e);
		}finally {
			close(rset);
			close(pstmt);
		}
		
		return totalContent;
	}


	public List<Photo> selectPhotoMore(Connection conn, Map<String, Object> param) {
		PreparedStatement pstmt=null;
		ResultSet rset = null;
		List<Photo>list=new ArrayList<Photo>();
		String sql = prop.getProperty("selectPhotoMore");
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, (int)param.get("start"));
			pstmt.setInt(2, (int)param.get("end"));
			
			rset = pstmt.executeQuery();
			while(rset.next()) {
				int no = rset.getInt("no");
				String writer = rset.getString("writer");
				String content = rset.getString("content");
				String originalFilename = rset.getString("original_filename");
				String renamedFilename = rset.getString("renamed_filename");
				int readCount = rset.getInt("read_count");
				Date regDate = rset.getDate("reg_date");
				
				Photo photo = new Photo(no, writer, content, originalFilename, renamedFilename, readCount, regDate);
				list.add(photo);
				
			}
			
		}catch(SQLException e) {
			throw new PhotoException("더보기 페이지 조회 오류",e);
		}finally {
			close(rset);
			close(pstmt);
		}
		return list;
	}
	
}
