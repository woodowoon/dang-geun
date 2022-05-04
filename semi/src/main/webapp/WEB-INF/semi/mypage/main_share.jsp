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
		
		<c:forEach var ="vo" items="${sharelist}">
			<tr>
				<td>	
					<img src="${pageContext.request.contextPath}/uploads/share/${vo.photoName}" style="width:100px; height:100px;">
				</td>
				<td>${vo.subject}</td>
				<td>${vo.reg_date}</td>
				<td>
					<c:choose>
						<c:when test="${vo.status == 0}">
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/share/article.do?code=${vo.code}';">나눔중</button>
						</c:when>
						<c:when test="${vo.status == 1}">
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/';" style="margin-bottom: 10px;">나눔승인</button>
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/';" style="background: #777;">나눔거절</button>
							<input type="hidden" name="code" value="${vo.code}">
						</c:when>
					</c:choose>
				</td>
			</tr>
		</c:forEach>	
	</table>
	<div class="page-box">
			${dataCount == 0 ? "현재 판매중인 상품이 없습니다." : sharePaging}
	</div>
	
	<div class="title">
		<h3><i class="fa-solid fa-carrot"></i> 나눔완료 상품 </h3>
	</div>
	<table class= "mypage mySold">
		<tr>
			<th style="width: 20%">제품사진</th>
			<th style="">제품명</th>
			<th style="width: 15%">등록일</th>
			<th style="width: 15%">나눔일</th>
		</tr>
		<c:forEach var ="vo" items="${shareoutList}">
			<tr>
				<td>	
					<img src="${pageContext.request.contextPath}/uploads/share/${vo.photoName}" style="width:100px; height:100px;">
				</td>
				<td>${vo.subject}</td>
				<td>${vo.reg_date}</td>
				<td>
					${vo.share_date}
				</td>
			</tr>
	</c:forEach>	
	</table>
	<div class="page-box">
		${dataCount == 0 ? "판매 완료된 상품이 없습니다." : shareoutPaging}
	</div>
</body>
</html>