<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<style type="text/css">

.container .title {
	margin-bottom: 15px;
	color: #FF8A3D;
}

.container .title span {
	margin-bottom: 15px;
	font-size: 20px;
}

.container table {
	width: 100%;
	height: 500px;
	border-collapse: collapse;
	border-top: 2px solid #FF8A3D;
}

.container table tr:first-child {
	border-top: 2px solid #FF8A3D;
}

.container table .border_btn {
	border-bottom: 1px solid #FF8A3D;
}

.container table .border_rgt {
	border-right: 1px solid #FF8A3D;
}

.container table .border_last {
	border-bottom: 2px solid #FF8A3D;
}

.container table td {
	padding: 5px 8px;
}

.container table .t_header {
	width: 15%;
	color:#fff;
	text-align: center;
	font-weight: bold;
	background: #FF8A3D;
}

.container table .content {
	padding: 8px 12px;
}

.container table .reg_date {
	text-align: right;
}


.container .button {
	display: flex;
	justify-content: space-between;
	margin: 5px 8px;
}


.container img {
	height: 250px;
}

.container table {
	margin-bottom: 13px;
}


</style>
<script type="text/javascript">
<c:if test="${sessionScope.member.userId == dto.userId || sessionScope.member.userId == 'admin'}">
	function deleteOk() {
		if(confirm("삭제하시겠습니까?")){
			let query = "num=${dto.num}&${query}";
			let url = "${pageContext.request.contextPath}/community/delete.do?"+query;
			location.href= url;
		}
	}
</c:if>

</script>
<jsp:include page="/WEB-INF/semi/layout/staticHeader.jsp"/>
</head>
<body>


<header>
    <jsp:include page="/WEB-INF/semi/layout/header.jsp"></jsp:include>
</header>

<main>
<div class="body-container">
	<div class="container">
		<div class="title">
			<h3><span>|</span> 글보기</h3>
		</div>
		<table>
			<tr class="border_btn">
				<td class="border_rgt t_header">제목</td>
				<td colspan="3">${dto.subject}</td>
			</tr>
			<tr class="border_btn">
				<td class="border_rgt t_header">닉네임</td>
				<td width="35%" class="border_rgt">${dto.uNick}</td>
				<td class="border_rgt t_header">지역</td>
				<td width="35%">${dto.rName}</td>
			</tr>
			<tr class="border_btn">
				<td class="border_rgt t_header">작성일</td>
				<td class="border_rgt">${dto.reg_date}</td>
				<td class="border_rgt t_header">조회수</td>
				<td>${dto.hitCount}</td>
			</tr>
			<tr>
				<td rowspan="2" class="border_rgt t_header">내용</td>
				<td colspan="3" class="content">
					<c:if test="${not empty dto.imageFilename}">
						<c:forEach var="vo" items="${listPhoto}">
							<img alt="첨부이미지" src="${pageContext.request.contextPath}/uploads/cmmu/${vo.imageFilename}">
						</c:forEach>
					</c:if>
				</td>
			</tr>
			<tr class="border_last">
				<td colspan="3" class="content">${dto.content}</td>
			</tr>
			<tr class="border_btn">
				<td class="border_rgt t_header">이전글</td>
				<td colspan="3">
					<c:if test="${not empty preReadCmmu}">
						<a href="${pageContext.request.contextPath}/community/article.do?num=${preReadCmmu.num}&${query}">${preReadCmmu.subject}</a>
					</c:if>
				</td>
			</tr>
			<tr class="border_last">
				<td class="border_rgt t_header">다음글</td>
				<td colspan="3">
					<c:if test="${not empty nextReadCmmu}">
						<a href="${pageContext.request.contextPath}/community/article.do?num=${nextReadCmmu.num}&${query}">${nextReadCmmu.subject}</a>
					</c:if>
				</td>
			</tr>
		</table>
		<div class="button">
			<div class="btn_left">
				<c:choose>
					<c:when test="${sessionScope.member.userId == dto.userId}">
						<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/community/update.do?num=${dto.num}&page=${page}';">수정</button>
					</c:when>
					<c:otherwise>
						<button type="button" class="btn" disabled="disabled">수정</button>
					</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${sessionScope.member.userId == dto.userId || sessionScope.member.userId == 'admin'}">
						<button type="button" class="btn" onclick="deleteOk();">삭제</button>
					</c:when>
					<c:otherwise>
						<button type="button" class="btn" disabled="disabled">삭제</button>
					</c:otherwise>
				</c:choose>
				
			</div>
			<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/community/list.do?${query}';">리스트</button>
		</div>
	</div>
</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>

</body>
</html>