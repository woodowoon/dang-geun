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

main .container {
	width:400px;
	margin: 80px auto 50px;
	padding: 10px;
	min-height: 200px; 
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
			<form name="loginForm" method="post">
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