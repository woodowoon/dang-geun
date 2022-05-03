<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/semi/layout/staticHeader.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery-ui.min.js"></script>

<title>Insert title here</title>
</head>
<body>
	<div class="title">
		<h3><i class="fa-solid fa-carrot"></i> 나눔중인 상품 </h3>
	</div>
	<table class= "mypage myshare">
		<tr>
			<th style="width: 20%">제품사진</th>
			<th>제품명</th>
			<th style="width: 15%">등록일</th>
			<th style="width: 15%">나눔완료</th>
		</tr>			
	</table>
	<div class="page-box">
			${dataCount == 0 ? "현재 판매중인 상품이 없습니다." : paging}
	</div>
	
	<div class="title">
		<h3><i class="fa-solid fa-carrot"></i> 나눔완료 상품 </h3>
	</div>
	<table class= "mypage mySold">
		<tr>
			<th style="width: 20%">제품사진</th>
			<th style="">제품명</th>
			<th style="width: 15%">등록일</th>
			<th style="width: 15%">판매일</th>
		</tr>
		<tr>
			<td>판매완료</td>
		<tr>
	</table>
	<div class="page-box">
			${dataCount == 0 ? "판매 완료된 상품이 없습니다." : paging}
	</div>
</body>
</html>