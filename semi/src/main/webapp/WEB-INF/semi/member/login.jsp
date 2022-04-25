<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<script type="text/javascript">
function bgLabel(obj, id) {
	if( ! obj.value ) {
		document.getElementById(id).style.display="";
	} else {
		document.getElementById(id).style.display="none";
	}
}

function inputsFocus( id ) {
	// 객체를 보이지 않게 숨긴다.
	document.getElementById(id).style.display="none";
}

function sendLogin() {
    const f = document.loginForm;

	let str = f.userId.value;
    if(!str) {
        alert("아이디를 입력하세요. ");
        f.userId.focus();
        return;
    }

    str = f.uPwd.value;
    if(!str) {
        alert("패스워드를 입력하세요. ");
        f.uPwd.focus();
        return;
    }

    f.action = "${pageContext.request.contextPath}/member/login_ok.do";
    f.submit();
}

</script>

<style type="text/css">
* {
	padding: 0;
	margin: 0;
	box-sizing: border-box;
}

main {
	width:100%;
	text-align:left;
}
main:before, main:after{
	content: "";
	display: block;
	clear: both;	
}

main .container {
	margin: 80px auto 50px;
	width: 380px;
	padding: 10px;
	min-height: 200px; 
}

a { color: #222; text-decoration: none; cursor: pointer; }
a:active, a:hover { color: #f28011; text-decoration: underline; }

.title-body {
	padding: 10px 0; text-align: center;
}

.title-body .login-title {
	font-weight: bold; font-size: 27px; cursor: pointer;
	color : #FF8A3D;
}

.form-body {
	text-align: center;
}

.form-body .lbl {
	position: absolute; margin-left: 15px; margin-top: 15px; color: #999;
}

.form-body .inputTF {
	width: 100%;
	height: 45px;
	padding: 5px;
	padding-left: 15px;
	border:1px solid #666;
}

.msg-box {
	text-align: center; color: blue;
}

.form-control {
	border: 1px solid #999; border-radius: 4px; background-color: #fff;
	padding: 5px 5px; 
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
	vertical-align: baseline;
}
.form-control[readonly] { background-color:#f8f9fa; }

textarea.form-control { height: 170px; resize : none; }

.form-select {
	border: 1px solid #999; border-radius: 4px; background-color: #fff;
	padding: 4px 5px; 
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
	vertical-align: baseline;
}
.form-select[readonly] { background-color:#f8f9fa; }

/* table */
.table { width: 100%; border-spacing: 0; border-collapse: collapse; }
.table th, .table td { padding-top: 10px; padding-bottom: 10px; }
tr.hover:hover { cursor: pointer; background: #f5fffa; }

.center{ text-align: center; }

.btnConfirm {
	background-color:#FF8A3D;
	border:none;
	color:#fff;
	width: 100%;
	padding: 15px 0;
	font-weight: 700; 
	font-size: 15px;
	cursor: pointer;
	vertical-align: baseline;
}


</style>



<title>sami</title>
<jsp:include page="/WEB-INF/semi/layout/staticHeader.jsp"/>
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/semi/layout/header.jsp"></jsp:include>
</header>

<main>
	<div class="container">
		<div class="title-body">
			<span class="login-title">회원 로그인</span>
		</div>
	
		<div class="form-body">
			<form name="loginForm" method="post" action="">
				<table class="table">
					<tr align="center"> 
						<td> 
							<label for="userId" id="lblUserId" class="lbl">아이디</label>
							<input type="text" name="userId" id="userId" class="inputTF" maxlength="15"
								tabindex="1"
								onfocus="inputsFocus('lblUserId');"
								onblur="bgLabel(this, 'lblUserId');">
						</td>
					</tr>
					<tr align="center"> 
					    <td>
							<label for="userPwd" id="lblUserPwd" class="lbl">패스워드</label>
							<input type="password" name="uPwd" id="uPwd" class="inputTF" maxlength="20" 
								tabindex="2"
								onfocus="inputsFocus('lblUserPwd');"
								onblur="bgLabel(this, 'lblUserPwd');">
					    </td>
					</tr>
					<tr align="center"> 
					    <td>
							<button type="button" onclick="sendLogin();" class="btnConfirm">로그인</button>
					    </td>
					</tr>
					<tr align="center">
					    <td>
							<a href="${pageContext.request.contextPath}">아이디 찾기</a>&nbsp;|&nbsp; 
							<a href="${pageContext.request.contextPath}">비밀번호 찾기</a>&nbsp;|&nbsp;
							<a href="${pageContext.request.contextPath}">회원가입</a>
					    </td>
					</tr>
				</table>
				
				<table class="table">
					<tr>
						<td class="msg-box">${message}</td>
					</tr>
				</table>
			</form>           
		</div>
	</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>

</body>
</html>