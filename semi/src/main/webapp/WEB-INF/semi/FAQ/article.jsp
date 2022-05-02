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
	margin: 30px 0;
	color: #FF8A3D;
}

.container .title span {
	margin-bottom: 15px;
	font-size: 20px;
}

.container table {
	width: 100%;
	border-collapse: collapse;
	border-top: 2px solid #FF8A3D;
	min-height: 500px;	
}

.container table .border_btn {
	border-bottom: 1px solid #FF8A3D;
	height: 50px;
}

.container table .border_rgt {
	border-right: 1px solid #FF8A3D;
}

.container table .border_last {
	border-bottom: 2px solid #FF8A3D;
}
.container table .category {
	font-weight: 800;
	width: 13%;
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
	border-bottom: 1px solid #FF8A3D;
}

.container table .reg_date {
	text-align: right;
	height: 50px;
}

.container table .preNext{
	height: 50px;
}
.container .button {
	display: flex;
	justify-content: space-between;
	margin: 5px 8px;
}


</style>
<script type="text/javascript">
function deleteFAQ(){
	if(confirm('게시글을 삭제하시겠습니까?')){
		let query = "page=${page}&fNum=${dto.fNum}";
		let url = "${pageContext.request.contextPath}/FAQ/delete.do?"+query;
		location.href = url;
	}
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
	<div class="container">
		<div class="title">
			<h3><i class="fa-solid fa-clipboard-question"></i> 질문 내용</h3>
		</div>
		<table>
			<tr class="border_btn">
				<td class="border_rgt t_header">제목</td>
				<td class="category" >
					<c:choose>
						<c:when test="${dto.category == 1}">
							[로그인관련]
						</c:when>
						<c:when test="${dto.category == 2}">
							[보안관련]
						</c:when>
						<c:when test="${dto.category == 3}">
							[거래관련]
						</c:when>
					</c:choose>
				</td>
				<td>${dto.subject}</td>
			</tr>
			
			<tr>
				<td rowspan="2" class="border_rgt t_header">내용</td>
				<td colspan="2" style="text-align: center;">
					<c:forEach var="vo" items="${listPhoto}">
						<img src = "${pageContext.request.contextPath}/uploads/FAQ/${vo.savePhotoname}" width="80%;">					</c:forEach>
				</td>
			</tr>
			<tr >
				<td colspan="2"  class="content">${dto.content}</td>
			</tr>
			<tr class="border_btn preNext">
				<td class="border_rgt t_header">이전글
				</td>
				<td colspan="2">
					<a  href="${pageContext.request.contextPath}/FAQ/article.do?${query}&fNum=${preReadFAQ.fNum}">${preReadFAQ.subject}</a>				
				</td>
			</tr>
			<tr class="border_btn preNext" style="border-bottom: 2px solid #FF8A3D;">
				<td class="border_rgt t_header">다음글
				</td>
				<td colspan="2">
					<a href="${pageContext.request.contextPath}/FAQ/article.do?${query}&fNum=${nextReadFAQ.fNum}">${nextReadFAQ.subject}</a>
				</td>
			</tr> 
		</table>
		<div class="button">
			<div class="btn_left">
			<c:choose>
				<c:when test="${sessionScope.member.userId=='admin'}">
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/FAQ/update.do?fNum=${dto.fNum}&page=${page}';">수정</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn" disabled="disabled">수정</button>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${sessionScope.member.userId=='admin'}">
					<button type="button" class="btn" onclick="deleteFAQ()">삭제</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn" disabled="disabled">삭제</button>
				</c:otherwise>
			</c:choose>
					
			</div>
			<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/FAQ/list.do?${query}';">리스트</button>
		</div>
	</div>
</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>

</body>
</html>