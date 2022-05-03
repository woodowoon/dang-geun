<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>당근-회원가입</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<style type="text/css">
.table-form .tel input[type=text] {
	width: 100px;
}

.msg-box {
	text-align: center; color: blue;
}

.form .img-viewer {
	cursor: pointer;
	border: 1px solid #ccc;
	width: 60px;
	height: 60px;
	border-radius: 45px;
	background-image: url("${pageContext.request.contextPath}/resource/images/add_photo.png");
	position: relative;
	z-index: 9999;
	background-repeat : no-repeat;
	background-size : cover;
}
</style>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript">

function memberOk(){
	const f = document.memberForm;
	let str;
	
	str = f.userId.value;
	if(! /^[a-z][a-z0-9_]{4,9}$/i.test(str)){
		alert("아이디를 다시 입력하세요");
		f.userId.focus();
		return;
	}
	
	// 아이디 중복검사
	if(f.userIdValid.value === "false"){
		alert("아이디 중복확인을 하지 않았습니다.");
		f.userId.focus();
		return;
	}
	
	
	str = f.uPwd.value;
	if(! /^[a-z][a-z0-9]{4,9}$/i.test(str)){
		alert("비밀번호를 다시 입력하세요");
		f.uPwd.focus();
		return;
	}
	
	if(str !== f.uPwd2.value){
		alert("비밀번호가 일치하지 않습니다.");
		f.uPwd.focus();
		return;
	}
	
	
	str = f.uName.value;
	if(! /^[가-힣]{2,5}$/.test(str)){
		alert("이름을 다시 입력하세요");
		f.uName.focus();
		return;
	}
	
	str = f.uNick.value;
	if(!str){
		alert("닉네임을 입력하세요");
		f.uNick.focus();
		return;
	}
	
	str = f.uTel1.value;
	if(!str){
		alert("번호를 선택하세요");
		f.uTel1.focus();
		return;
	}
	
	str = f.uTel2.value;
	if(! /^\d{3,4}$/.test(str)){
		alert("번호를 입력하세요");
		f.uTel2.focus();
		return;
	}
	
	str = f.uTel3.value;
	if(! /^\d{3,4}$/.test(str)){
		alert("번호를 입력하세요");
		f.uTel3.focus();
		return;
	}
	
	str = f.rCode.value;
	if(!str){
		alert("지역을 선택하세요");
		f.rCode.focus();
		return;
	}
	
	
	f.action = "${pageContext.request.contextPath}/member/join_ok.do";
	f.submit();
	
}


function userIdCheck() {
	let userId = $("#userId").val();
	
	if(! /^[a-z][a-z0-9_]{4,9}$/i.test(userId)){
		let s = "아이디는 첫글자는 영문자로 시작하며 5~10자 이내로 가능합니다.";
		$("#userId").focus();
		$("#userId").parent().next(".restrict").html(s);
		return;
	}
	
	let url = "${pageContext.request.contextPath}/member/userIdCheck.do";
	let query = "userId=" + userId;
	
	$.ajax({
		type: "post",
		url: url,
		data: query,
		dataType: "json",
		success:function(data){
			let passed = data.passed;
			
			if(passed === "true"){
				let s = "<span style='color:blue;font-weight:600;'>" + userId + "</span> 아이디는 사용가능합니다.";
				$("#userId").parent().next(".restrict").html(s);
				$("#userIdValid").val("true");
			} else {
				let s = "<span style='color:red;font-weight:600;'>" + userId + "</span> 아이디는 사용할 수 없습니다.";
				$("#userId").parent().next(".restrict").html(s);
				$("#userIdValid").val("false");
				$("#userId").focus();
			}
		},
		error:function(e){
			console.log(e.responseText);
		}
	});
	
}


$(function(){
	$(".form .img-viewer").click(function(){
		$("form[name=memberForm] input[name=selectFile]").trigger("click");
	});
	
	$("form[name=memberForm] input[name=selectFile]").change(function(){
		let img;
		
		let file = this.files[0];
		if(!file){
			$(".form .img-viewer").empty();
			img = "${pageContext.request.contextPath}/resource/images/add_photo.png";
			$(".form .img-viewer").css("background-image","url("+img+")");
			return false;
		}
		
		let reader = new FileReader();
		reader.onload = function(e){
			$(".form .img-viewer").empty();
			$(".form .img-viewer").css("background-image","url("+e.target.result+")");
		};
		
		reader.readAsDataURL(file);
		
	});
});


</script>

<jsp:include page="/WEB-INF/semi/layout/staticHeader.jsp"/>
</head>

<body>

<header>
    <jsp:include page="/WEB-INF/semi/layout/header.jsp"></jsp:include>
</header>

<main>
	<div class="body-container">
		<div class= "body-title">
			<h3 class="title"><span>|</span> 회원가입</h3>
		</div>
		
		<form name = "memberForm" method="post" enctype="multipart/form-data">
			<table class="table table-border table-form" >
				<tr>
					<td>프로필<br>사진</td>
					<td>
						<div class="form">
			            	<div class="img-viewer"></div>
			            	<input type="file" name="selectFile" accept="image/*" style="display: none;">
						</div>
		            </td>
				</tr>
				<tr>
					<td>아이디</td>
					<td>
						<p>
							<input type="text" name="userId" id="userId" maxlength="10" class="form-control" style="width: 70%;">
							<button type="button" class="btn" onclick="userIdCheck();">중복확인</button>
						</p>
						<p class="restrict">아이디는 5자~10자 이내로 입력해주세요</p>
					</td>											
				</tr>
				<tr>
					<td>패스워드</td>
					<td>
						<input type="password" name="uPwd" class="form-control" maxlength="10">
						<p class="restrict">패스워드는 5~10자 이내이며 영문자와 숫자를 포함하여야 합니다.</p>
					</td>
				</tr>
				<tr>
					<td>패스워드 확인</td>
					<td>
						<input type="password" name="uPwd2" class="form-control" maxlength="10">
						<p class="restrict">패스워드를 한 번 더 입력해주세요.</p>
					</td>
				</tr>
				<tr>
					<td>관리자</td>
					<td>
						<input type="checkbox" name="uRole" id="uRole" value="1">
					</td>
				</tr>     	
	       		<tr>
					<td>이름</td>
					<td>
						<input type="text" name="uName" class="form-control" maxlength="10" style="width: 70%">
					</td>
				</tr>
	       		<tr>
					<td>닉네임</td>
					<td>
						<input type="text" name="uNick" class="form-control" maxlength="10" style="width: 70%">
					</td>
				</tr>
				<tr>
					<td>전화번호</td>
					<td>
						<div class="tel">
							<select name="uTel1" class="form-select">
								<option value="">::선택::</option>
								<option value="02">02</option>
								<option value="010">010</option>
								<option value="021">021</option>
								<option value="031">031</option>
								<option value="041">041</option>
								<option value="051">051</option>
								<option value="061">061</option>
								<option value="070">070</option>
							</select>
							<input type="text" name="uTel2" class="form-control" maxlength="4" pattern="\d*">-
							<input type="text" name="uTel3" class="form-control" maxlength="4" pattern="\d*">
							<p class="restrict">전화번호는 숫자만 입력해주세요.</p>
						</div>
					</td>
				</tr>
				<tr>
					<td>지역</td>
					<td>
						<select name="rCode" class="form-select">
							<option value="">선택</option>
							<option value="1">서울</option>
							<option value="2">인천</option>
							<option value="3">경기</option>
							<option value="4">대전</option>
							<option value="5">대구</option>
							<option value="6">부산</option>
							<option value="7">울산</option>
							<option value="8">광주</option>
							<option value="9">세종</option>
							<option value="10">제주</option>
						</select>
					</td>
				</tr>
				
			</table>
			
			<table class="table">
				<tr>
					<td align="center">
						<input type="hidden" name="userIdValid" id="userIdValid" value="false">
						<button type="button" class="btn" onclick="memberOk();">가입하기</button>
						<button type="reset" class="btn">다시입력</button>
						<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/';">가입취소</button>
					</td>
				</tr>
				
				<tr>
					<td align="center">
						<span class="msg-box">${message}</span>
					</td>
				</tr>
			</table>
	
		</form>
	</div>
</main>



<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>

</body>
</html>