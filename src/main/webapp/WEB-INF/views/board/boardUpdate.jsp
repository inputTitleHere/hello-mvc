<%@page import="java.util.Iterator"%>
<%@page import="com.kh.mvc.board.model.dto.Attachment"%>
<%@page import="java.util.List"%>
<%@page import="com.kh.mvc.board.model.dto.BoardExt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>    
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/board.css" />

<%
BoardExt board = (BoardExt)request.getAttribute("board");
List<Attachment> attachments = board.getAttachments();
%>

<section id="board-container">
<h2>게시판 수정</h2>
<form name="boardUpdateFrm" action="<%=request.getContextPath() %>/board/boardUpdate" method="post" enctype="multipart/form-data">
	<input type="hidden" value="<%=board.getNo() %>" name="no" readonly />
	<table id="tbl-board-view">
	<tr>
		<th>제 목</th>
		<td><input type="text" name="title" value="<%=board.getTitle() %>" required></td>
	</tr>
	<tr>
		<th>작성자</th>
		<td>
			<input type="text" name="writer" value="<%=loginMember.getMemberId() %>" readonly/>
		</td>
	</tr>
	<tr>
		<th>첨부파일</th>
		<td >
		<%
		if(attachments!=null && !attachments.isEmpty()){
			for(int i=0;i<attachments.size();i++){
				Attachment attach = attachments.get(i);
		%>
			<img src="<%=request.getContextPath() %>/images/file.png" alt="" width="16px" />
			<%=attach.getOriginalFilename()%>
			<input type="checkbox" name="delFile" id="delFile<%=i %>" value="<%=attach.getNo() %>" />
			<label for="delFile">삭제</label>
			<br />
		<%
		}}
		%>
			<input type="file" name="upFile1">
			<input type="file" name="upFile2">
		</td>
	</tr>
	<tr>
		<th>내 용</th>
		<td><textarea rows="5" cols="40" name="content"><%=board.getContent() %></textarea></td>
	</tr>
	<tr>
		<th colspan="2">
			<input type="submit" value="수정하기"/>
			<input type="button" value="취소" onclick="history.go(-1);"/>
		</th>
	</tr>
</table>
</form>
</section>
<script>
document.boardUpdateFrm.onsubmit = (e) => {
	const frm = e.target;
	//제목을 작성하지 않은 경우 폼제출할 수 없음.
	const titleVal = frm.title.value.trim(); // 좌우공백
	if(!/^.+$/.test(titleVal)){
		alert("제목을 작성해주세요.");
		frm.title.select();
		return false;
	}		
					   
	//내용을 작성하지 않은 경우 폼제출할 수 없음.
	const contentVal = frm.content.value.trim();
	if(!/^(.|\n)+$/.test(contentVal)){
		alert("내용을 작성해주세요.");
		frm.content.select();
		return false;
	}
}
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
