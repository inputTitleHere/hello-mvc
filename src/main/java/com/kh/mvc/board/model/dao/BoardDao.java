package com.kh.mvc.board.model.dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.kh.mvc.board.model.dto.Board;
import com.kh.mvc.board.model.dto.BoardExt;
import com.kh.mvc.board.model.exception.BoardException;

import static com.kh.mvc.common.JdbcTemplate.*;

public class BoardDao {
// property 변수 선언
	private Properties prop = new Properties();

	
	

	public BoardDao() {
		String filename = BoardDao.class.getResource("/sql/board/board-query.properties").getPath();
		System.out.println(filename);
		try {
			prop.load(new FileReader(filename));
		}catch(IOException e) {
			throw new BoardException("Board prop load fail",e);
		}
	}


	private BoardExt handleBoardResultSet(ResultSet rset) throws SQLException{
		// 반환형은 BoardExt이지만 자식타입이므로 Board으로 받을 수 있다.
		int no = rset.getInt("no");
		String title = rset.getString("title");
		String writer = rset.getString("writer");
		String content = rset.getString("content"); // 없어도 되나?
		int readCount = rset.getInt("read_count");
		Timestamp regDate = rset.getTimestamp("reg_date");
		
		return new BoardExt(no, title, writer, content, readCount, regDate,0);
	}
	
	
	public List<Board> loadBoard(Connection conn, Map<String, Object> param) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Board> queryResult= new ArrayList<Board>();
		String sql=prop.getProperty("loadBoard");
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, (int)param.get("start"));
			pstmt.setInt(2, (int)param.get("end"));
			rset = pstmt.executeQuery();
			// 결과 리스트에 로드
			while(rset.next()) {
				BoardExt be = handleBoardResultSet(rset);
				be.setAttachmentCount(rset.getInt("attach_count"));
				queryResult.add(be);			
			}
			
		}catch(SQLException e) {
			throw new BoardException("게시판 로드 실패",e);
		}finally {
			close(rset);
			close(pstmt);
		}
		return queryResult;
	}


	public int getTotalContentCount(Connection conn) {
		PreparedStatement pstmt=null;
		ResultSet rset = null;
		int totalCount=0;
		String sql = prop.getProperty("getTotalContent");
		
		try {
			pstmt=conn.prepareStatement(sql);
			rset=pstmt.executeQuery();
			if(rset.next()) {
				totalCount=rset.getInt(1);
			}
		}catch(SQLException e) {
			throw new BoardException("전체 게시판수 조회 오류",e);
		}finally {
			close(rset);
			close(pstmt);
		}
		return totalCount;
	}


	public int enrollBoard(Connection conn, Board board) {
		PreparedStatement pstmt=null;
		String sql = prop.getProperty("enrollBoard");
//		enrollBoard = insert into board values(seq_board_no.nextval,?[title],?[writer],?[content],default,default)
		int result=0;
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getWriter());
			pstmt.setString(3, board.getContent());
			
			result=pstmt.executeUpdate();
		}catch(SQLException e) {
			throw new BoardException("게시판 등록 오류",e);
		}finally {
			close(pstmt);
		}
		return result;
	}

	
	
}
