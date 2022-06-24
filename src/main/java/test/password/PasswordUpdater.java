package test.password;

import static com.kh.mvc.common.JdbcTemplate.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kh.mvc.common.HelloMvcUtils;
import com.kh.mvc.member.model.dto.Member;

public class PasswordUpdater {

	public static void main(String[] args) {
		new PasswordUpdater().start();
	}

	private void start() {
		// 1. 회원 ID조회. 및 신규비번 설정
		Connection conn = getConnection();
		String sql = "select * from member";
		List<Member> list = new ArrayList<Member>();
		System.out.println("IS RUNNING");
		try(
			PreparedStatement pstmt=conn.prepareStatement(sql);
			ResultSet rset = pstmt.executeQuery();
			){
			while(rset.next()) {
				String memberId=rset.getString("member_id");
				String password=HelloMvcUtils.getEncryptedPassword("1234", memberId);
				
				Member member = new Member();
				member.setMemberId(memberId);
				member.setPassword(password);
				
				list.add(member);
			}
			for(Member member : list) {
				System.out.printf("id : %s, password : %s\n", member.getMemberId(), member.getPassword());
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		// 2. 일괄 업데이트
		sql="update member set password = ? where member_id = ?";
		try(PreparedStatement pstmt=conn.prepareStatement(sql);){
			for(Member member : list) {
				pstmt.setString(1, member.getPassword());
				pstmt.setString(2, member.getMemberId());
				pstmt.executeUpdate();
				System.out.println("%s 비밀번호 업데이트 성공");
			}
			commit(conn);
		}catch (SQLException e) {
			rollback(conn);
			e.printStackTrace();
		}
		close(conn);
		System.out.println("[비밀번호 일괄갱신이 끝났습니다.]");
		
	}
	
	
}
