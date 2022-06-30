package com.kh.mvc.board.model.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import com.kh.mvc.board.model.dao.BoardDao;
import com.kh.mvc.board.model.dto.Attachment;
import com.kh.mvc.board.model.dto.Board;
import com.kh.mvc.board.model.dto.BoardExt;

import static com.kh.mvc.common.JdbcTemplate.*;

public class BoardService {

	private BoardDao boardDao = new BoardDao();

	public List<Board> loadBoard(Map<String, Object> param) {
		Connection conn = getConnection();
		List<Board> loadResult = boardDao.loadBoard(conn, param);
		close(conn);
		return loadResult;
	}

	public int getTotalContentCount() {
		Connection conn = getConnection();
		int totalContentCount = boardDao.getTotalContentCount(conn);
		close(conn);
		return totalContentCount;
	}

	public int inputBoard(Board board) {
		Connection conn = getConnection();
		int result;
		try {

			// board table에 insert
			result = boardDao.insertBoard(conn, board);
			List<Attachment> attachments = ((BoardExt) board).getAttachments();

			// 방금 등록된 board.no 컬럼값 조회.
			//
//			getLastBoardNo = select seq_board_no.currval from dual
			int boardNo = boardDao.getLastBoardNo(conn);

			if (attachments != null && !attachments.isEmpty()) {
				for (Attachment attach : attachments) {
					attach.setBoardNo(boardNo);
//					insertAttachment = insert into attachment values(seq_attachment_no.nextval, ?게시판 번호, ?(Ori), ?(rename), default)
					result = boardDao.insertAttachment(conn, attach);
				}
			}

			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

	public Board findByNo(int no) {
		return findByNo(no,true);
	}

	public Board findByNo(int no, boolean hasRead) {
		Connection conn = getConnection();
		Board board = null;
		try {
			if(!hasRead) { // 읽었음이 false인 경우에만 증가. 
				int result = boardDao.updateReadCount(conn, no);
			}
				
			commit(conn);

			// board테이블에서 조회
			board = boardDao.findByNo(conn, no);

			// attachment 테이블에서 조회 List<attachment>
			List<Attachment> attachments = boardDao.findAttachmentByBoardNo(conn, no);
//			System.out.println("@BoardSerivce : attachments = "+attachments);

			((BoardExt) board).setAttachments(attachments);
			System.out.println("@boardService : board = " + board);
			
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return board;
	}

	public Attachment findattachmentByNo(int no) {
		Connection conn = getConnection();
		Attachment attach = boardDao.findAttachmentByNo(conn, no);
		close(conn);
		return attach;
	}
	
	public List<Attachment> findAttachmentByBoardNo(int no){
		Connection conn = getConnection();
		List<Attachment> attachments = boardDao.findAttachmentByBoardNo(conn, no);
		close(conn);
		return attachments;
	}

	/**
	 * 
	 * 
	 * 
	 */
	public int deleteByNo(int no) {
		Connection conn = getConnection();
		int result = 0;
		try { 
			result = boardDao.deleteByNo(conn, no);
			
			commit(conn);
		}catch(Exception e) {
			rollback(conn);
			throw e;
		}finally {
			close(conn);
		}
		
		return result;
	}

	public int removeAttachmentByNo(int no) {
		Connection conn = getConnection();
		int result =0;
		
		try {
			result = boardDao.deleteAttachmentByNo(conn, no);
			commit(conn);
		}catch(Exception e) {
			rollback(conn);
			throw e;
		}finally {
			close(conn);
		}
		return result;
	}

	public int updateBoard(BoardExt board) {
		Connection conn = getConnection();
		int result;
		try {

			// board table에 insert
			result = boardDao.updateBoard(conn, board);
			List<Attachment> attachments = board.getAttachments();

			// 방금 등록된 board.no 컬럼값 조회.
			//
//			getLastBoardNo = select seq_board_no.currval from dual

			if (attachments != null && !attachments.isEmpty()) {
				for (Attachment attach : attachments) {
					attach.setBoardNo(board.getNo());
//					insertAttachment = insert into attachment values(seq_attachment_no.nextval, ?게시판 번호, ?(Ori), ?(rename), default)
					result = boardDao.insertAttachment(conn, attach);
				}
			}
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

}
