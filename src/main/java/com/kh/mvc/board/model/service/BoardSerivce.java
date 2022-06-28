package com.kh.mvc.board.model.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.kh.mvc.board.model.dao.BoardDao;
import com.kh.mvc.board.model.dto.Board;
import static com.kh.mvc.common.JdbcTemplate.*;

public class BoardSerivce {

	private BoardDao boardDao = new BoardDao();

	public List<Board> loadBoard(Map<String, Object> param) {
		Connection conn = getConnection();
		List<Board>loadResult=boardDao.loadBoard(conn, param);
		close(conn);
		return loadResult;
	}

	public int getTotalContentCount() {
		Connection conn = getConnection();
		int totalContentCount=boardDao.getTotalContentCount(conn);
		close(conn);
		return totalContentCount;
	}

	public int inputBoard(Board board) {
		Connection conn = getConnection();
		int result;
		try {
			result = boardDao.enrollBoard(conn, board);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		}finally {
			close(conn);
		}
		return result;
	}
	
	
	
}
