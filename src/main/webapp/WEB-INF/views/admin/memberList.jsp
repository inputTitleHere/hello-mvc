<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<!-- 관리자용 admin.css link -->

<%
List<Member> members = (List<Member>) request.getAttribute("list");

String type = request.getParameter("searchType");
if(type==null){
	type="member_id";
}
String kw = request.getParameter("searchKeyword");

if(kw==null){
	kw="";
}

//System.out.println(members);
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
DecimalFormat df = new DecimalFormat("#,###");
%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/admin.css" />
<style>
div#search-container{
	width:100%;
	margin : 0 0 10px 0;
	padding: 3px;
	background-color:rgba(0,188,212,.3);
}
div#search-memberId{
	display:<%= "member_id".equals(type)? "inline-block":"none" %>;
}
div#search-memberName{
	display:<%= "member_name".equals(type)? "inline-block":"none" %>;
}
div#search-gender{
	display:<%= "gender".equals(type)? "inline-block":"none" %>;
}

</style>
<script>
window.onload=(e)=>{
	document.querySelector("select#searchType").onchange=(e)=>{
		document.querySelectorAll(".search-type").forEach((div,index)=>{
			div.style.display="none";
		});
		let id;
		switch(e.target.value){
		case "member_id": id= "memberId"; break;
		case "member_name": id= "memberName"; break;
		case "gender": id= "gender"; break;
		}
		document.querySelector(`#search-\${id}`).style.display="inline-block";
	};
};
</script>	
<section id="memberList-container">
	<h2>회원관리</h2>
	<div id="search-container">
		<label for="searchType">검색타입 :</label> <select id="searchType">
			<option value="member_id" <%="member_id".equals(type)?"selected":"" %>>아이디</option>
			<option value="member_name" <%="member_name".equals(type)?"selected":"" %>>회원명</option>
			<option value="gender" <%="gender".equals(type)?"selected":"" %>>성별</option>
		</select>
		<div id="search-memberId" class="search-type">
			<form action="<%=request.getContextPath()%>/admin/memberFinder">
				<input type="hidden" name="searchType" value="member_id" /> <input
					type="text" name="searchKeyword" size="25"
					placeholder="검색할 아이디를 입력하세요." value="<%="member_id".equals(type)?kw:"" %>" />
				<button type="submit">검색</button>
			</form>
		</div>
		<div id="search-memberName" class="search-type">
			<form action="<%=request.getContextPath()%>/admin/memberFinder">
				<input type="hidden" name="searchType" value="member_name" /> <input
					type="text" name="searchKeyword" size="25"
					placeholder="검색할 이름을 입력하세요." value="<%="member_name".equals(type)?kw:"" %>"/>
				<button type="submit">검색</button>
			</form>
		</div>
		<div id="search-gender" class="search-type">
			<form action="<%=request.getContextPath()%>/admin/memberFinder">
				<input type="hidden" name="searchType" value="gender" /> <input
					type="radio" name="searchKeyword" value="M" <%="gender".equals(type)&&"M".equals(kw)?"checked":"" %>> 남 <input
					type="radio" name="searchKeyword" value="F" <%="gender".equals(type)&&"F".equals(kw)?"checked":"" %>> 여
				<button type="submit">검색</button>
			</form>
		</div>
	</div>


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
			<%
			if (members != null) {
				for (Member member : members) {
			%>
			<tr>
				<td><%=member.getMemberId()%></td>
				<td><%=member.getMemberName()%></td>
				<td><%=member.getMemberRole().name()%></td>
				<td><%=member.getGender() != null ? member.getGender().name() : "NULL"%></td>
				<td><%=member.getBirthday() != null ? sdf.format(member.getBirthday()) : "NULL"%></td>
				<td><%=member.getEmail()%></td>
				<td><%=member.getPhone()%></td>
				<td><%=df.format(member.getPoint())%></td>
				<td><%=member.getHobby()%></td>
				<td><%=sdf.format(member.getEnrollDate())%></td>
			</tr>
			<%
			}
			}
			%>
		</tbody>
	</table>

	<div id="pagebar">
		<%=request.getAttribute("pagebar")%>
	</div>

</section>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
