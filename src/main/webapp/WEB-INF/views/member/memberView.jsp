<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<%
Member member = (Member)session.getAttribute("loginMember");
SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
String dateString;
if(member.getBirthday()!=null){
dateString = sdf.format(member.getBirthday());	
}
else{
	dateString="1900-01-01";
}
System.out.println(dateString);
%>

<section id=enroll-container>
	<h2>회원 정보</h2>
	<form name="memberUpdateFrm" action="<%=request.getContextPath()%>/member/memberView" method="POST">
		<table>
			<tr>
				<th>아이디<sup>*</sup></th>
				<td>
					<input type="text" name="memberId" id="memberId" value="<%=member.getMemberId() %>" readonly>
				</td>
			</tr>
			<tr>
				<th>패스워드<sup>*</sup></th>
				<td>
					<input type="password" name="password" id="password" value="<%=member.getPassword() %>" required>
				</td>
			</tr>
			<tr>
				<th>패스워드확인<sup>*</sup></th>
				<td>	
					<input type="password" id="passwordCheck" value="<%=member.getPassword() %>" required><br>
				</td>
			</tr> 
			<tr>
				<th>이름<sup>*</sup></th>
				<td>	
				<input type="text"  name="memberName" id="memberName" value="<%=member.getMemberName() %>"  required><br>
				</td>
			</tr>
			<tr>
				<th>생년월일</th>
				<td>	
				
				<input type="date" name="birthday" id="birthday" value="<%=dateString%>"><br>
				</td>
			</tr> 
			<tr>
				<th>이메일</th>
				<td>	
					<input type="email" placeholder="abc@xyz.com" name="email" id="email" value="<%= member.getEmail()%>"><br>
				</td>
			</tr>
			<tr>
				<th>휴대폰<sup>*</sup></th>
				<td>	
					<input type="tel" placeholder="(-없이)01012345678" name="phone" id="phone" maxlength="11" value="<%=member.getPhone()%>" required><br>
				</td>
			</tr>
			<tr>
				<th>포인트</th>
				<td>	
					<input type="text" placeholder="" name="point" id="point" value="<%=member.getPoint()%>" readonly><br>
				</td>
			</tr>
			<tr>
				<th>성별 </th>
				<td>
			       <input type="radio" name="gender" id="gender0" value="M" <%="M".equals(member.getGender().toString())? "checked":"" %>>
						 <label for="gender0">남</label>
						 <input type="radio" name="gender" id="gender1" value="F" <%="F".equals(member.getGender().toString())? "checked":"" %>>
						 <label for="gender1">여</label>
				</td>
			</tr>
			<tr>
				<th>취미 </th>
				<td>
				<% String hobbies = member.getHobby()!=null? member.getHobby():" "; %>
					<input type="checkbox" name="hobby" id="hobby0" value="운동" <%=hobbies.contains("운동")?"checked":"" %>><label for="hobby0">운동</label>
					<input type="checkbox" name="hobby" id="hobby1" value="등산" <%=hobbies.contains("등산")?"checked":"" %>><label for="hobby1">등산</label>
					<input type="checkbox" name="hobby" id="hobby2" value="독서" <%=hobbies.contains("독서")?"checked":"" %>><label for="hobby2">독서</label><br />
					<input type="checkbox" name="hobby" id="hobby3" value="게임" <%=hobbies.contains("게임")?"checked":"" %>><label for="hobby3">게임</label>
					<input type="checkbox" name="hobby" id="hobby4" value="여행" <%=hobbies.contains("여행")?"checked":"" %>><label for="hobby4">여행</label><br />

				</td>
			</tr>
		</table>
        <input type="submit" value="정보수정"/>
        <input type="button" onclick="deleteMember();" value="탈퇴"/>
	</form>
</section>
<form action="" name="memberDelFrm"></form>
<script>

/*
 * POST형식 /member/memberDelete 서블릿 실행
 *	memberDelFrm 제출을 하도록
 *	과제 : 
 */

const deleteMember=()=>{
	
};

</script>



<%@ include file="/WEB-INF/views/common/footer.jsp" %>
