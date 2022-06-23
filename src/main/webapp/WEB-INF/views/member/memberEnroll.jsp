<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<section id=enroll-container>
	<h2>회원 가입 정보 입력</h2>
	<form name="memberEnrollFrm" action="" method="POST"><!--  action이 없으면 현재 page url을 가져다 action으로 사용한다 -->
		<table>
			<tr>
				<th>아이디<sup>*</sup></th>
				<td>
					<input type="text" placeholder="4글자이상" name="memberId" id="_memberId" required value="asdf">
					<input type="button" value="중복검사" onclick="checkIdDuplicate();">
					<input type="hidden" id="idValid" value="0">
					<%-- 중복검사전 = 0, 중복검사 후(유효한 id) = 1 --%>
				</td>
			</tr>
			<tr>
				<th>패스워드<sup>*</sup></th>
				<td>
					<input type="password" name="password" id="_password" required value="1234"><br>
				</td>
			</tr>
			<tr>
				<th>패스워드확인<sup>*</sup></th>
				<td>	
					<input type="password" id="passwordCheck" required  value="1234"><br>
				</td>
			</tr>  
			<tr>
				<th>이름<sup>*</sup></th>
				<td>	
				<input type="text"  name="memberName" id="memberName" required value="김얍"><br>
				</td>
			</tr>
			<tr>
				<th>생년월일</th>
				<td>	
				<input type="date" name="birthday" id="birthday" ><br />
				</td>
			</tr> 
			<tr>
				<th>이메일</th>
				<td>	
					<input type="email" placeholder="abc@xyz.com" name="email" id="email" value="eeemail@mail.com"><br>
				</td>
			</tr>
			<tr>
				<th>휴대폰<sup>*</sup></th>
				<td>	
					<input type="tel" placeholder="(-없이)01012345678" name="phone" id="phone" maxlength="11" required value="01011225566"><br>
				</td>
			</tr>
			<tr>
				<th>성별 </th>
				<td>
					<input type="radio" name="gender" id="gender0" value="M" checked>
					<label for="gender0">남</label>
					<input type="radio" name="gender" id="gender1" value="F">
					<label for="gender1">여</label>
				</td>
			</tr>
			<tr>
				<th>취미 </th>
				<td>
					<input type="checkbox" name="hobby" id="hobby0" value="운동"><label for="hobby0">운동</label>
					<input type="checkbox" name="hobby" id="hobby1" value="등산"><label for="hobby1">등산</label>
					<input type="checkbox" name="hobby" id="hobby2" value="독서" checked><label for="hobby2">독서</label><br />
					<input type="checkbox" name="hobby" id="hobby3" value="게임" checked><label for="hobby3">게임</label>
					<input type="checkbox" name="hobby" id="hobby4" value="여행"><label for="hobby4">여행</label><br />
				</td>
			</tr>
		</table>
		<input type="submit" value="가입" >
		<input type="reset" value="취소">
	</form>
</section>


<form action="<%=request.getContextPath()%>/member/checkIdDuplicate" name="checkIdDuplicateFrm">
	<input type="hidden" name="memberId" />
</form>

<script>

// 사용자 입력 id 중복여부 검사.
// 폼을 팝업에서 제출.
const checkIdDuplicate=()=>{
	const memberId=document.querySelector('#_memberId');
	if(!/^[a-zA-Z0-9]{4,}$/.test(memberId.value)){
		alert("유효한 아이디를 입력해주세요");
		memberId.select();
		return;
	}
	
	// popup 제어
	const title ="checkIdDuplicatePopup";
	const spec="width=300px, height=300px";
	const popup = open("", title, spec);  // popup 객체에 window 객체 저장.
	
	// form제어
	const frm = document.checkIdDuplicateFrm;
	frm.target=title; // 폼을 제출대상이 현재 윈도우가 아닌 팝업으로 지정. a target="_self"(현재페이지) // a target="_blank"(새 탭))
	frm.memberId.value=memberId.value;
	// 앞의 frm은 input:hidden.value이고 뒤의 memberId.value는 memberEnrollFrm의 것이다. 
	frm.submit();
};



// 비밀번호 일치여부 검사

document.querySelector("#passwordCheck").onblur=(e)=>{
	const password = document.querySelector("#_password");
	const passwordCheck = e.target;
	if(password.value !== passwordCheck.value){
		alert("비밀번호가 일치하지 않습니다.");
		password.select();
	}
};

document.querySelector("#_memberId").onchange=(e)=>{
	document.querySelector("#idValid").value=0;
};


// 폼 내용 무결검사.
document.memberEnrollFrm.onsubmit=(e)=>{
	const memberId = document.querySelector("#_memberId");
	if(!/^[a-zA-Z0-9]{4,}$/.test(memberId.value)){
		alert("아이디는 영문 숫자로 구성되어 최소 4자리 이상입니다.");
		memberId.select();
		return false;
	}
	
	const idValid=document.querySelector("#idValid");
	if(idValid.value!=="1"){
		alert("아이디 중복검사를 해주세요");
		return false;
	}
	
	
	const password = document.querySelector("#_password");
	if(!/^[a-zA-Z0-9`~!@#$%^&*()]{4,}$/.test(password.value)){
		alert("비밀번호는 영문/숫자로/특수문자~[!@#$%^&*()`] 구성되어 최소 4자리 이상입니다.");
		password.select();
		return false;
	}
	
	const passwordCheck=document.querySelector("#passwordCheck");
	if(password.value !== passwordCheck.value){
		alert("비밀번호가 일치하지 않습니다.");
		password.select();
		return false;
	}
	
	const memberName=document.querySelector("#memberName");
	if(!/^[가-힣]{2,}$/.test(memberName.value)){
		alert("한글 2글자 이상 입력해주세요.");
		memberName.select();
		return false;
	}
	const phone=document.querySelector("#phone");
	if(!/^010[0-9]{8}$/.test(phone.value)){
		alert("유효한 전화번호를 입력하세요.");
		phone.select();
		return false;
	}
};



</script>


<%@ include file="/WEB-INF/views/common/footer.jsp" %>
