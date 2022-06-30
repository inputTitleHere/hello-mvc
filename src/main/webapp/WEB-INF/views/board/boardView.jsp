<%@page import="java.util.List"%>
<%@page import="com.kh.mvc.board.model.dto.Attachment"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.kh.mvc.board.model.dto.BoardExt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/board.css" />

<%
BoardExt boardExt = (BoardExt)request.getAttribute("board");
List<Attachment> attachments = boardExt.getAttachments();
%>
<section id="board-container">
	<h2>게시판</h2>
	<table id="tbl-board-view">
		<tr>
			<th>글번호</th>
			<td><%=boardExt.getNo() %></td>
		</tr>
		<tr>
			<th>제 목</th>
			<td><%=boardExt.getTitle() %></td>
		</tr>
		<tr>
			<th>작성자</th>
			<td><%=boardExt.getWriter() %></td>
		</tr>
		<tr>
			<th>조회수</th>
			<td><%=boardExt.getReadCount() %></td>
		</tr>
		<tr>
				<%
				if(attachments!=null && attachments.size()>0){
					Iterator<Attachment> it = attachments.iterator();
					while(it.hasNext()){
						Attachment attach = it.next();
				%>
			<th>첨부파일</th>
			<td>
				<%-- 첨부파일이 있을경우만, 이미지와 함께 original파일명 표시 --%>
				<img alt="첨부파일" src="<%=request.getContextPath() %>/images/file.png" width=16px>
				<a href="<%=request.getContextPath()%>/board/fileDownload?no=<%=attach.getNo()%>"><%=attach.getOriginalFilename() %></a>
			</td>
		</tr>
			<%
				}
			}
			%>
		<tr>
			<th>내 용</th>
			<td><%=boardExt.getContent() %></td>
		</tr>
		<% 
			boolean canEdit=loginMember!=null && (loginMember.getMemberId().equals(boardExt.getWriter()) || loginMember.getMemberRole()==MemberRole.A);
			if(canEdit){ 
		%>
		<tr>
			<%-- 작성자와 관리자만 마지막행 수정/삭제버튼이 보일수 있게 할 것 --%>
			<th colspan="2">
				<input type="button" value="수정하기" onclick="updateBoard()">
				<input type="button" value="삭제하기" onclick="deleteBoard()">
			</th>
		</tr>
		<%
		}
		%>
	</table>
</section>

<% if(canEdit){ %>
<form action="<%=request.getContextPath() %>/board/boardDelete" name="boardDelFrm" method="post">
	<input type="hidden" name="no" value="<%=boardExt.getNo()%>"/>
</form>

<script>
const updateBoard=()=>{
	location.href="<%=request.getContextPath()%>/board/boardUpdate?no=<%=boardExt.getNo()%>";
};

const deleteBoard=()=>{
	if(confirm("게시글을 삭제하시겠습니까?")){
		document.boardDelFrm.submit();
	}
};

</script>
<% } %>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
