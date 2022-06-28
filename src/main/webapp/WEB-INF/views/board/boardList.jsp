<%@page import="com.kh.mvc.board.model.dto.BoardExt"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.kh.mvc.board.model.dto.Board"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/board.css" />

<%
List<Board> boardList = (List<Board>)request.getAttribute("boardList");
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
%>


<section id="board-container">
	<h2>게시판 </h2>
	<% if(loginMember!=null){ %> <%-- loginMember는 Header에 저장됨(사용가능) --%>
	<input type="button" value="글쓰기" id="btn-add" onclick="location.href='<%=request.getContextPath()%>/board/boardEnroll';"/>
	<%} %>
	<table id="tbl-board">
		<tr>
			<th>번호</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>첨부파일</th><%--첨부파일이 있는 경우 /images/file.png 표시 width:16px --%>
			<th>조회수</th>
		</tr>
		<%
		if(boardList!=null){
			for(Board _b : boardList){
				BoardExt b = (BoardExt) _b;
		%>
		<tr>
			<td><%=b.getNo() %></td>
			<td><%=b.getTitle() %></td>
			<td><%=b.getWriter() %></td>
			<td><%=sdf.format(b.getRegDate()) %></td>
			<% if(b.getAttachmentCount()>0){ %>
			<td><img src="<%=request.getContextPath()%>/images/file.png" alt="" style="width:16px;"/></td>
			<%}else{ %>
			<td>&nbsp;</td>
			<%} %>
			<td><%=b.getReadCount() %></td>
		</tr>
		<%
			}
		}
		%>
		
	</table>

	<div id='pagebar'>
		<%=request.getAttribute("pagebar") %>
	</div>
</section>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
