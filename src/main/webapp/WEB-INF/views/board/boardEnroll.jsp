<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>    
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/board.css" />

<script>
/**
* boardEnrollFrm 유효성 검사
*/
function boardValidate(){
	//제목을 작성하지 않은 경우 폼제출할 수 없음.
					   
	//내용을 작성하지 않은 경우 폼제출할 수 없음.

	return true;
}
</script>
<section id="board-container">
<h2>게시판 작성</h2>
<form
	name="boardEnrollFrm"
	action="<%=request.getContextPath() %>/board/boardEnroll" 
	method="post">
	<table id="tbl-board-view">
	<tr>
		<th>제 목</th>
		<td><input type="text" name="title" required></td>
	</tr>
	<tr>
		<th>작성자</th>
		<td>
			<input type="text" name="writer" value="<%=loginMember.getMemberId() %>" readonly/>
		</td>
	</tr>
	<tr>
		<th>첨부파일</th>
		<td>			
			<input type="file" name="upFile1">
			<input type="file" name="upFile2">
		</td>
	</tr>
	<tr>
		<th>내 용</th>
		<td><textarea rows="5" cols="40" name="content"></textarea></td>
	</tr>
	<tr>
		<th colspan="2">
			<input type="submit" value="등록하기">
		</th>
	</tr>
</table>
</form>
</section>
<script>
// boardEnrollFrm 유효성 검사.
document.boardEnrollFrm.onsubmit=(e)=>{
	// 제목을 작성하지 않는 경우 폼 제출 불가.
	if(!/^(.|\n)+$/.test(frm.content.title))){
		alert("제목을 작성하세요");
		return false;
	}
	// 내용을 작성하지 않으면 폼 제출 불가.
	if(!/^(.|\n)+$/.test(frm.content.value))){
		alert("내용을 작성하세요");
		return false;
	}
	return true;
}

</script>


<%@ include file="/WEB-INF/views/common/footer.jsp" %>
