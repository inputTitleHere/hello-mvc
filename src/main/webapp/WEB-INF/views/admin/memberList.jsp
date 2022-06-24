<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<!-- 관리자용 admin.css link -->

<%
List<Member> members = (List<Member>)request.getAttribute("list");

if(members==null){
	System.out.println();
}

System.out.println(members);
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
DecimalFormat df = new DecimalFormat("#,###");
%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/admin.css" />
<section id="memberList-container">
	<h2>회원관리</h2>
	
	<table id="tbl-member">
		<thead>
			<tr>
				<th>아이디</th>
				<th>이름</th>
				<th>회원권한</th>
				<th>성별</th>
				<th>생년월일</th>
				<th>이메일</th>
				<th>전화번호</th>
				<th>포인트</th>
				<th>취미</th>
				<th>가입일</th>
			</tr>
		</thead>
		<tbody>
		<%-- 여기에 내용 추가 --%>
		<%for(Member member : members){ %>
		<tr>
			<td><%=member.getMemberId() %></td>		
			<td><%=member.getMemberName() %></td>		
			<td><%=member.getMemberRole().name() %></td>
			<td><%=member.getGender().name() %></td>
			<td><%=member.getBirthday()!=null?sdf.format(member.getBirthday()):"NULL"%></td>
			<td><%=member.getEmail() %></td>
			<td><%=member.getPhone() %></td>
			<td><%=df.format(member.getPoint())%></td>
			<td><%=member.getHobby() %></td>
			<td><%=sdf.format(member.getEnrollDate()) %></td>
		</tr>
		<%} %>
		</tbody>
	</table>
</section>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
