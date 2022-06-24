<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>

<%
// page 지시어 isErrorPage="true"으로 지정
// 발생한 예외객체에 선언없이 접근가능하다.

// String msg = exception.getMessage(); 에러코드로 넘어온 경우 exception이 없다.
int statusCode = response.getStatus();

%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
body{text-align:center;}
h1{font-size:500px; margin:0;}
.err-msg{color:red;}
</style>
</head>
<body>

	<h1>텅</h1>
	<p class="err-msg">찾으시려는 페이지가 없습니다.</p>
	<hr />
	<a href="<%=request.getContextPath()%>">홈으로</a>
	<br />
	<a href="javascript:history.back();">뒤로가기</a>
</body>
</html>