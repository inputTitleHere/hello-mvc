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

import javax.swing.plaf.SliderUI;

import com.kh.mvc.board.model.dto.Attachment;
import com.kh.mvc.board.model.dto.Board;
import com.kh.mvc.board.model.dto.BoardComment;
import com.kh.mvc.board.model.dto.BoardExt;
import com.kh.mvc.board.model.dto.CommentLevel;
import com.kh.mvc.board.model.exception.BoardException;
import com.kh.mvc.member.model.service.MemberService;

import static com.kh.mvc.common.JdbcTemplate.*;

public class BoardDao {
// property 변수 선언
	private Properties prop = new Properties();

	public BoardDao() {
		String filename = BoardDao.class.getResource("/sql/board/board-query.properties").getPath();
		System.out.println(filename);
		try {
			prop.load(new FileReader(filename));
		} catch (IOException e) {
			throw new BoardException("Board prop load fail", e);
		}
	}

	private BoardExt handleBoardResultSet(ResultSet rset, boolean comments) throws SQLException {
		// 반환형은 BoardExt이지만 자식타입이므로 Board으로 받을 수 있다.
		int no = rset.getInt("no");
		String title = rset.getString("title");
		String writer = rset.getString("writer");
		String content = rset.getString("content"); // 없어도 되나?
		int readCount = rset.getInt("read_count");
		Timestamp regDate = rset.getTimestamp("reg_date");
		if(comments) {
			int commentCount = rset.getInt("comment_count");
			return new BoardExt(no, title, writer, content, readCount, regDate, commentCount);			
		}else {
			return new BoardExt(no, title, writer, content, readCount, regDate, 0);
		}
	}

	private Attachment handleAttachmentResultSet(ResultSet rset) throws SQLException {
		Attachment attach = new Attachment();
		attach.setNo(rset.getInt("no"));
		attach.setBoardNo(rset.getInt("board_no"));
		attach.setOriginalFilename(rset.getString("original_filename"));
		attach.setRenamedFilename(rset.getString("renamed_filename"));
		attach.setRegDate(rset.getTimestamp("reg_date"));

		return attach;
	}

	private BoardComment handleBoardCommentResultSet(ResultSet rset) throws SQLException {
		BoardComment boardComment = new BoardComment();
		boardComment.setNo(rset.getInt("no"));
		boardComment.setCommentLevel(CommentLevel.valueOf(rset.getInt("comment_level")));
		boardComment.setWriter(rset.getString("writer"));
		boardComment.setContent(rset.getString("content"));
		boardComment.setBoardNo(rset.getInt("board_no"));
		boardComment.setCommentRef(rset.getInt("comment_ref")); // null인경우 0을 반환함.
		boardComment.setRegDate(rset.getTimestamp("reg_date"));

		return boardComment;
	}

	public List<Board> loadBoard(Connection conn, Map<String, Object> param) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Board> queryResult = new ArrayList<Board>();
		String sql = prop.getProperty("loadBoard");
//		select * from (select row_number() over(order by reg_date desc) rnum, (select count(*) from board_comment bc where bc.board_no = b.no) comment_count, b.*, (select count(*)from attachment where board_no=b.no) attach_count from board b ) b where rnum between ? and ?;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (int) param.get("start"));
			pstmt.setInt(2, (int) param.get("end"));
			rset = pstmt.executeQuery();
			// 결과 리스트에 로드
			while (rset.next()) {
				BoardExt be = handleBoardResultSet(rset,true); // comments가 있으면 true를 받는다. 
				be.setAttachmentCount(rset.getInt("attach_count"));
				queryResult.add(be);
			}

		} catch (SQLException e) {
			throw new BoardException("게시판 로드 실패", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return queryResult;
	}

	public int getTotalContentCount(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int totalCount = 0;
		String sql = prop.getProperty("getTotalContent");

		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				totalCount = rset.getInt(1);
			}
		} catch (SQLException e) {
			throw new BoardException("전체 게시판수 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return totalCount;
	}

	public int insertBoard(Connection conn, Board board) {
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertBoard");
//		enrollBoard = insert into board values(seq_board_no.nextval,?[title],?[writer],?[content],default,default)
		int result = 0;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getWriter());
			pstmt.setString(3, board.getContent());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new BoardException("게시판 등록 오류", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	public int getLastBoardNo(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int boardNo = 0;
		String sql = prop.getProperty("getLastBoardNo");

		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				boardNo = rset.getInt(1);
			}
		} catch (SQLException e) {
			throw new BoardException("생성된 게시글 번호 조회 오류!", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return boardNo;
	}

	public int insertAttachment(Connection conn, Attachment attach) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("insertAttachment");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, attach.getBoardNo());
			pstmt.setString(2, attach.getOriginalFilename());
			pstmt.setString(3, attach.getRenamedFilename());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new BoardException("첨부파일 등록오류 !", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	public Board findByNo(Connection conn, int no) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Board board = null;
		String sql = prop.getProperty("findByNo");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				board = handleBoardResultSet(rset,false); // 내부적으로 boardExt 객체를 만들어준다.
			}

		} catch (SQLException e) {
			throw new BoardException("게시글 단건 조회 오류", e);
		}

		return board;
	}

	public List<Attachment> findAttachmentByBoardNo(Connection conn, int no) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Attachment> attachments = new ArrayList<>();
		String sql = prop.getProperty("findAttachmentByBoardNo");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				attachments.add(handleAttachmentResultSet(rset));
			}
		} catch (SQLException e) {
			throw new BoardException("게시글별 첨부파일 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return attachments;
	}

	public int updateReadCount(Connection conn, int no) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateReadCount");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new BoardException("조회수 증가 오류!", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	public Attachment findAttachmentByNo(Connection conn, int no) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		Attachment attach = null;
		String sql = prop.getProperty("findAttachmentByNo");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);

			rset = pstmt.executeQuery();
			while (rset.next()) {
				attach = handleAttachmentResultSet(rset);
			}
		} catch (SQLException e) {
			throw new BoardException("특정 첨부파일 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}

		return attach;
	}

	public int deleteByNo(Connection conn, int no) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("deleteBoard");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);

			result = pstmt.executeUpdate(); // attachment 테이블쪽은 테이블 자체의 constraint으로 삭제될 것이다.
		} catch (SQLException e) {
			throw new BoardException("테이블 삭제중 오류 발생", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	public int deleteAttachmentByNo(Connection conn, int no) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("deleteAttachment");
		// deleteAttachment = delete from attachment where no =?
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new BoardException("첨부파일 삭제 오류", e);
		} finally {
			close(pstmt);
		}
		return result;

	}

	public int updateBoard(Connection conn, BoardExt board) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateBoard");
//		updateBoard = update board set title=?, content=? where no = ?
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getContent());
			pstmt.setInt(3, board.getNo());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new BoardException("게시판 업데이트 오류", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	public int insertBoardComment(Connection conn, BoardComment boardComment) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("insertBoardComment");
//		insertBoardComment = insert into board_comment values(seq_board_comment_no.nextval, ?, ?, ?, ?, ?, default);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardComment.getCommentLevel().getValue());
			pstmt.setString(2, boardComment.getWriter());
			pstmt.setString(3, boardComment.getContent());
			pstmt.setInt(4, boardComment.getBoardNo());
			pstmt.setObject(5, boardComment.getCommentRef() == 0 ? null : boardComment.getCommentRef());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new BoardException("댓글/답글 등록 오류", e);
		} finally {
			close(pstmt);
		}
		return result;

	}

	public List<BoardComment> findBoardCommentByBoardNo(Connection conn, int boardNo) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<BoardComment> commentList = new ArrayList<>();
		String sql = prop.getProperty("findBoardCommentByBoardNo");
//		findBoardCommentByBoardNo = select * from board_comment where board_no= ? start with comment_level = 1 connect by prior no=comment_ref order siblings by no asc
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				commentList.add(handleBoardCommentResultSet(rset));
			}
		} catch (SQLException e) {
			throw new BoardException("게시글별 댓글 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return commentList;
	}

	public int deleteCommentByNo(Connection conn, int no) {
		PreparedStatement pstmt = null;
		int result =0;
		String sql = prop.getProperty("deleteCommentByNo");
//		deleteCommentByNo = delete from board_comment where no=?
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			result = pstmt.executeUpdate();
			
		}catch(SQLException e) {
			throw new BoardException("댓글 삭제 오류",e);
		}finally {
			close(pstmt);
		}
		return result;
		
	}

}
