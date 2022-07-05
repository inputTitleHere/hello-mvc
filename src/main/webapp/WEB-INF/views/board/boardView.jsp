<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.kh.mvc.board.model.dto.CommentLevel"%>
<%@page import="com.kh.mvc.board.model.dto.BoardComment"%>
<%@page import="java.util.List"%>
<%@page import="com.kh.mvc.board.model.dto.Attachment"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.kh.mvc.board.model.dto.BoardExt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/board.css" />

<%
BoardExt boardExt = (BoardExt)request.getAttribute("board");
List<Attachment> attachments = boardExt.getAttachments();
List<BoardComment> commentList = (List<BoardComment>)request.getAttribute("commentList");
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
				<%-- 첨부파일이 있을경우만, 이미지와 함께 original파일명 표시 --%> <img alt="첨부파일"
				src="<%=request.getContextPath() %>/images/file.png" width=16px>
				<a
				href="<%=request.getContextPath()%>/board/fileDownload?no=<%=attach.getNo()%>"><%=attach.getOriginalFilename() %></a>
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
			<th colspan="2"><input type="button" value="수정하기"
				onclick="updateBoard()"> <input type="button" value="삭제하기"
				onclick="deleteBoard()"></th>
		</tr>
		<%
		}
		%>
	</table>

	<hr style="margin-top: 30px;" />

	<div class="comment-container">
		<div class="comment-editor">
			<form action="<%=request.getContextPath()%>/board/boardCommentEnroll"
				method="post" name="boardCommentFrm">
				<input type="hidden" name="boardNo" value="<%=boardExt.getNo() %>" />
				<input type="hidden" name="writer"
					value="<%=loginMember!=null ? loginMember.getMemberId():"" %>" />
				<input type="hidden" name="commentLevel" value="1" /> <input
					type="hidden" name="commentRef" value="0" />
				<%-- int는 null이 없으므로 0으로 설정. --%>
				<textarea name="content" cols="60" rows="3"></textarea>
				<button type="submit" id="btn-comment-enroll1">등록</button>
			</form>
		</div>
		<!--table#tbl-comment-->
		<table id="tbl-comment">
			<%
        	if(commentList!=null&&!commentList.isEmpty()){
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        		for(BoardComment bc : commentList){
        			boolean canDelete = loginMember!=null && (loginMember.getMemberId().equals(bc.getWriter())||loginMember.getMemberRole()==MemberRole.A);
        %>
			<tr
				class="<%= bc.getCommentLevel()==CommentLevel.COMMENT?"level1":"level2"%>">
				<td><sub class="comment-writer"><%=bc.getWriter() %></sub> <sub
					class="comment-date"><%=sdf.format(bc.getRegDate()) %></sub>
					<div>
						<%=bc.getContent() %>
					</div></td>
				<td>
					<% if(bc.getCommentLevel()==CommentLevel.COMMENT){ %>
					<button class="btn-reply" value="<%= bc.getNo()%>">답글</button> 
					<%} %>
					<%if(canDelete){ %>
					<button class="btn-delete" value="<%= bc.getNo()%>">삭제</button>
					<%} %>
				</td>
			</tr>
			<%
       			}
        	}
       	%>
		</table>
	</div>

</section>
<form action="<%=request.getContextPath() %>/board/boardCommentDelete" name="boardCommentDelFrm" method="post">
	<input type="hidden" name="no"/>
</form>

<script> <%-- 모두가 사용하는 스크립트 --%>

document.querySelectorAll(".btn-delete").forEach((btn)=>{
	btn.addEventListener('click',(e)=>{
		if(confirm('댓글을 삭제하겠습니까?')){
			const {value}=e.target;
			const frm = document.boardCommentDelFrm;
			frm.no.value=value;
			frm.submit();
		}
	});
});

document.querySelectorAll(".btn-reply").forEach((btn)=>{
	btn.addEventListener('click',(e)=>{
		<% if(loginMember==null){ %> 
			loginAlert();
			return;
		<% } %>
		const {value} =e.target;
		console.log(value);
		
		const tr = `<tr>
		<td colspan="2" style="text-align:left;">
		<form
      action="<%=request.getContextPath()%>/board/boardCommentEnroll" 
      method="post" 
      name="boardCommentFrm"
      >
          <input type="hidden" name="boardNo" value="<%=boardExt.getNo() %>" />
          <input type="hidden" name="writer" value="<%=loginMember!=null ? loginMember.getMemberId():"" %>" />
          <input type="hidden" name="commentLevel" value="2" />
          <input type="hidden" name="commentRef" value="\${value}" />  <%-- 참조할 댓글 번호 --%>  
          <textarea name="content" cols="60" rows="3"></textarea>
          <button type="submit" id="btn-comment-enroll1">등록</button>
      </form>
     </td>
    </tr>`;
		
    const target = e.target.parentElement.parentElement; // tr
    target.insertAdjacentHTML('afterend',tr);
            
	}, {once:true});
});

document.boardCommentFrm.content.addEventListener('focus',(e)=>{
	if(<%=loginMember == null%>){
		loginAlert();
	}
});

document.addEventListener('submit',(e)=>{
	if(e.target.matches("form[name=boardCommentFrm]")){
		if(<%=loginMember == null%>){
			loginAlert();
			e.preventDefault();
			return;
		}
		
		if(!/^(.|\n)+$/.test(e.target.content.value)){
			alert("내용을 작성하세요");
			e.preventDefault();
			return;
		}
	}
});

const loginAlert =()=>{
	alert("로그인후 이용하실수 있습니다.");
	document.querySelector("#memberId").focus();
};
</script>

<%
if (canEdit) {
%>
<form action="<%=request.getContextPath()%>/board/boardDelete"
	name="boardDelFrm" method="post">
	<input type="hidden" name="no" value="<%=boardExt.getNo()%>" />
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
<%
}
%>

<%@ include file="/WEB-INF/views/common/footer.jsp"%>
