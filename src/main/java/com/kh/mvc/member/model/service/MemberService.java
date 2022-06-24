package com.kh.mvc.member.model.service;

import com.kh.mvc.member.model.dao.MemberDao;
import com.kh.mvc.member.model.dto.Member;
import java.sql.Connection;
import java.util.List;

import static com.kh.mvc.common.JdbcTemplate.*;


public class MemberService {

	private MemberDao memberDao = new MemberDao();
	
	
	/*
	 * DQL / DML
	 * DQL일 때 요청 => 1. Connection 객체 생성. 2. DAO 요청 & Connection 전달. 3. Connection 반환(처리후)=close처리 
	 * 
	 * 
	 */
	public Member findById(String memberId) {
		Connection conn = getConnection();
		Member member = memberDao.findById(conn, memberId);
		close(conn);
		return member;
	}

// 		DML 처리 - service
	/*
	 * 1. connection 객체 생성
	 * 2. DAO 요청 & Connection 전달
	 * 3. 트랜잭션 처리(정상시 commit, 예외시 rollback) -- 여기만 다르다.
	 * 4. connection 반환
	 */
	public int inputMember(Member member) {
		Connection conn = getConnection();
		int result=0;
		
		try {
			result=memberDao.insertMember(conn, member);
			commit(conn); // 성공시 커밋
		}catch(Exception e) {
			rollback(conn); // 실패시 롤백
			throw e; // controller에 예외를 던진다.(그래야 controller가 파악한다)
		}finally {
			close(conn);
		}
		
		return result;
	}

	public int updateMember(Member member) {
		Connection conn = getConnection();
		int result=0;
		
		try {
			result=memberDao.updateMember(conn, member);
			commit(conn); // 성공시 커밋
		}catch(Exception e) {
			rollback(conn); // 실패시 롤백
			throw e; // controller에 예외를 던진다.(그래야 controller가 파악한다)
		}finally {
			close(conn);
		}
		
		return result;
	}
	
	public int updatePassword(Member member, String newPassword) {
		Connection conn = getConnection();
		int result=0;
		
		try {
			result=memberDao.updatePassword(conn, member, newPassword);
			commit(conn); // 성공시 커밋
		}catch(Exception e) {
			rollback(conn); // 실패시 롤백
			throw e; // controller에 예외를 던진다.(그래야 controller가 파악한다)
		}finally {
			close(conn);
		}
		
		return result;
	}

	public List<Member> findAll() {
		Connection conn = getConnection();
		List<Member>list = memberDao.findAll(conn);
		close(conn);
		return list;
	}

	public int deleteMember(String memberId) {
		Connection conn = getConnection();
		
		int result=0;
		
		try {
			result=memberDao.deleteMember(conn, memberId);
			commit(conn); // 성공시 커밋
		}catch(Exception e) {
			rollback(conn); // 실패시 롤백
			throw e; // controller에 예외를 던진다.(그래야 controller가 파악한다)
		}finally {
			close(conn);
		}
		
		return result;
		
		
	}
	
	
	
}
