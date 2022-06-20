package com.kh.mvc.member.model.dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import com.kh.mvc.member.model.dto.Gender;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.dto.MemberRole;
import com.kh.mvc.member.model.exception.MemberException;

import static com.kh.mvc.common.JdbcTemplate.close;

public class MemberDao {
	
	private Properties prop = new Properties();
	
	public MemberDao() {
		String filename = MemberDao.class.getResource("/sql/member/member-query.properties").getPath();
		System.out.println("filename @ MemberDao = "+filename);
		
		try {
			prop.load(new FileReader(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/*
	 * DQL 요청일 떄 - dao 처리 프로세스
	 * 1. PrepareStatement 객체 생성 - (sql전달&값 대입)
	 * 2. 쿼리 실행 executeQuery - ResultSet 반환
	 * 3. ResultSet 처리 -> DTO 객체로 변환.
	 * 4. ResultSet, PreparedStatement 객체 메모리 해제.
	 */
	public Member findById(Connection conn, String memberId) {
		PreparedStatement pstmt=null;
		ResultSet rset=null;
		Member member =null;
		String sql = prop.getProperty("findById");
				
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			rset=pstmt.executeQuery();
			while(rset.next()) {
				memberId = rset.getString("member_id");
				String password = rset.getString("password");
				String memberName = rset.getString("member_name");
				MemberRole memberRole = MemberRole.valueOf(rset.getString("member_role"));
				Gender gender = Gender.valueOf(rset.getString("gender"));
				Date birthday = rset.getDate("birthday");
				String email = rset.getString("email");
				String phone = rset.getString("phone");
				String hobby = rset.getString("hobby");
				int point = rset.getInt("point");
				Timestamp enrollDate = rset.getTimestamp("enroll_date");
				member=new Member(memberId, password, memberName, memberRole, gender, birthday, email, phone, hobby, point, enrollDate);
			}
			
		} catch (SQLException e) {
			throw new MemberException("회원 조회 실패!",e);
		}finally {
			close(rset);
			close(pstmt);
		}
		return member;
	}

	/*
	 * DML인 경우
	 * 1. PrepareStatement 객체 생성 - (sql전달&값 대입)
	 * 2. 쿼리 실행 executeUpdate - int 반환
	 * 3. PreparedStatement 객체 메모리 해제.
	 */
	public int insertMember(Connection conn, Member member) {
		PreparedStatement pstmt=null;
		int result=0;
		String sql=prop.getProperty("insertMember");
		
		// insertMember = insert into member values(?,?,?,default,?,?,?,?,?,default,default)
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getGender()!=null?member.getGender().name():null);
			pstmt.setDate(5, member.getBirthday());
			pstmt.setString(6, member.getEmail());
			pstmt.setString(7, member.getPhone());
			pstmt.setString(8, member.getHobby());
			
			result=pstmt.executeUpdate();
			
		}catch(SQLException e) {
			throw new MemberException("회원가입 오류",e); // service에 예외를 던진다. -- 비즈니스를 설명가능한 구체적 커스텀 예외로 전환해서 던진다.
		}finally {
			close(pstmt);
		}
		return result;
	}
}
