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
<script type="text/javascript">

function memberOk(){
	const f = document.memberForm;
}
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
		
		<form name = "memberForm" method="post">
			<table class="table table-border table-form" >
				<tr>
					<td>아이디</td>
					<td>
						<input type="text" name="userId" id="userId" maxlength="10" class="form-control">
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
					<td>이름</td>
					<td>
						<input type="text" name="uName" class="form-control" maxlength="10" >
					</td>
				</tr>
	       		<tr>
					<td>닉네임</td>
					<td>
						<input type="text" name="uNick" class="form-control" maxlength="10" >
					</td>
				</tr>
				<tr>
					<td>전화번호</td>
					<td>
						<input type="text" name="uTel" class="form-control" maxlength="11" pattern="\d*">
						<p class="restrict">전화번호는 -을 제외한 숫자만 입력해주세요.</p>
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
	
		
			<table class="table " >
				<tr>
					<td align="center">
						<button type="button" class="btn" onclick="memberOk();">가입하기</button>
						<button type="reset" class="btn">다시입력</button>
						<button type="button" class="btn" onclick="">가입취소</button>
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