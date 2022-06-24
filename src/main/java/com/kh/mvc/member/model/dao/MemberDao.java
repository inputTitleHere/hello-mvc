package com.kh.mvc.member.model.dao;

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
				member = handleMemberResultSet(rset);
			}
			
		} catch (SQLException e) {
			throw new MemberException("회원 조회 실패!",e);
		}finally {
			close(rset);
			close(pstmt);
		}
		return member;
	}

	private Member handleMemberResultSet(ResultSet rset) throws SQLException {
		Member member;
		String memberId = rset.getString("member_id");
		String password = rset.getString("password");
		String memberName = rset.getString("member_name");
		MemberRole memberRole = MemberRole.valueOf(rset.getString("member_role"));
		
		String _gender = rset.getString("gender");
		Gender gender = _gender!=null?Gender.valueOf(_gender):null; // enum에 null전달 방지
		Date birthday = rset.getDate("birthday");
		String email = rset.getString("email");
		String phone = rset.getString("phone");
		String hobby = rset.getString("hobby");
		int point = rset.getInt("point");
		Timestamp enrollDate = rset.getTimestamp("enroll_date");
		member=new Member(memberId, password, memberName, memberRole, gender, birthday, email, phone, hobby, point, enrollDate);
		return member;
	}

	/*
	 * DML인 경우
	 * 1. PrepareStatement 객체 생성 - (sql전달&값 대입)
	 * 2. 쿼리 실행 executeUpdate - int 반환
	 * 3. PreparedStatement 객체 메모리 해제.
	 */
	public int updateMember(Connection conn, Member member) {
		PreparedStatement pstmt=null;
		int result=0;
		String sql=prop.getProperty("updateMember");
		// password 없앰
		// updateMember = update member set member_name=?, gender=?, birthday=?,email=?,phone=?,hobby=? where member_id=?
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberName());
			pstmt.setString(2, member.getGender()!=null?member.getGender().name():null);
			pstmt.setDate(3, member.getBirthday());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getPhone());
			pstmt.setString(6, member.getHobby());
			pstmt.setString(7, member.getMemberId());
			
			result=pstmt.executeUpdate();
			
		}catch(SQLException e) {
			throw new MemberException("정보수정 오류",e); // service에 예외를 던진다. -- 비즈니스를 설명가능한 구체적 커스텀 예외로 전환해서 던진다.
		}finally {
			close(pstmt);
		}
		return result;
	}

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

	public int updatePassword(Connection conn, Member member, String newPassword) {
		//---------------------------------- DML이다. ---------------------------------- // 
		PreparedStatement pstmt=null;
		int result=0;
		String sql=prop.getProperty("updatePassword");
		// updatePassword = update member set password=? where member_id=?
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,newPassword);
			pstmt.setString(2,member.getMemberId());
			
			result=pstmt.executeUpdate();
			
		}catch(SQLException e) {
			throw new MemberException("비밀번호수정 오류",e); // service에 예외를 던진다. -- 비즈니스를 설명가능한 구체적 커스텀 예외로 전환해서 던진다.
		}finally {
			close(pstmt);
		}
		return result;
		
	}

	public List<Member> findAll(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Member>list = new ArrayList<Member>();
		String sql = prop.getProperty("findAll");
		
		try {
			pstmt=conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				Member member = handleMemberResultSet(rset);
				list.add(member);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(rset);
			close(pstmt);
		}
		return list;
	}

	public int deleteMember(Connection conn, String memberId) {
		
		PreparedStatement pstmt=null;
		int result=0;
		String sql=prop.getProperty("deleteMember");
		//deleteMember = delete from member where member_id =?

		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,memberId);
			
			result=pstmt.executeUpdate();
			
		}catch(SQLException e) {
			throw new MemberException("비밀번호수정 오류",e); // service에 예외를 던진다. -- 비즈니스를 설명가능한 구체적 커스텀 예외로 전환해서 던진다.
		}finally {
			close(pstmt);
		}
		return result;
	}
}
