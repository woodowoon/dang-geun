<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Header Dummy</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>
<div class="header-top">
	<div class="header-left">
		<a href="${pageContext.request.contextPath}/main.do">
			<img class="fixed-logo" alt="당근마켓" src="https://d1unjqcospf8gs.cloudfront.net/assets/home/base/header/logo-basic-24b18257ac4ef693c02233bf21e9cb7ecbf43ebd8d5b40c24d99e14094a44c81.svg" width="120" height="34">
		</a>
	</div>
	
	<div class="menu">
		<ul class="nav">
			<li>
				<a href="${pageContext.request.contextPath}/sell/list.do">판매</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/community/list.do">커뮤니티</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/share/list.do">무료나눔</a>
			</li>
			<li class="notice">
				<a href="${pageContext.request.contextPath}/notice/list.do">공지사항</a>
				<ul>
					<li>
						<a href="${pageContext.request.contextPath}/FAQ/list.do">자주묻는 질문</a>
					</li>
					<li>
						<a href="#">이벤트</a>
					</li>
				</ul>
			</li>
		</ul>
	</div>
	
	<div class="header-right">
		<c:if test="${empty sessionScope.member}">
			<a href="${pageContext.request.contextPath}/member/login.do">로그인</a>
			&nbsp;|&nbsp;
			<a href="${pageContext.request.contextPath}/member/join.do">회원가입</a>
		</c:if>
		<c:if test="${not empty sessionScope.member}">
			<span>${sessionScope.member.uNick}</span>님
			<a href="${pageContext.request.contextPath}/member/logout.do">로그아웃</a>
			&nbsp;|&nbsp;
			<a href="#">마이페이지</a>

		</c:if>
	</div>	
</div>
</body>
</html>