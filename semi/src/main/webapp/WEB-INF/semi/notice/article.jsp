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

.container table .border_btn {
	border-bottom: 1px solid #FF8A3D;
	height: 25px;
}

.container table .border_rgt {
	border-right: 1px solid #FF8A3D;
}

.container table .border_last {
	border-bottom: 2px solid #FF8A3D;
	text-align: center;
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


</style>

<script type="text/javascript">
<c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
function deleteNotice() {
	if(confirm('게시글을 삭제 하시겠습니까? ')) {
		let query = "nNum=${dto.nNum}&page=${page}";
		let url = "${pageContext.request.contextPath}/notice/delete.do?" + query;
		location.href = url;
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
			<h3><i class="fas fa-chalkboard-teacher"></i> 공지사항 보기</h3>
		</div>
		
		<table>
			<tr class="border_btn">
				<td class="border_rgt t_header">제목</td>
				<td colspan="3">${dto.subject}</td>
			</tr>
			<tr class="border_btn">
				<td class="border_rgt t_header">닉네임</td>
				<td width="30%" class="border_rgt">${dto.uNick}</td>
				<td class="border_rgt t_header">조회 | 등록일</td>
				<td>${dto.hitCount} | ${dto.reg_date}</td>
			</tr>
			
			<tr class="border_btn">
				<td class="border_rgt t_header">내용</td>
				<td colspan="3" class="content" valign="top" height="200">${dto.content}</td>
			</tr>
	
         <c:forEach var="vo" items="${listFile}">
			<tr class="border_last">
				<td height="30">첨부파일 : <td>
				<td style="text-align: left">
					<a href="${pageContext.request.contextPath}/notice/download.do?fNum=${vo.fNum}">${vo.originalFileName}</a>
				</td>
			</tr>
        </c:forEach>
		</table>
	</div>	
			
			<table class="table">
			<tr>
				<td width="50%">
				    <c:choose>
				        <c:when test="${sessionScope.member.userId=='admin'}">
				             <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/notice/update.do?nNum=${dto.nNum}&page=${page}';">수정</button>
				        </c:when>
				        <c:otherwise>
				             <button type="button" class="btn" disabled="disabled">수정</button>  
				        </c:otherwise>
				    </c:choose>
				    
				    <c:choose>
				        <c:when test="${sessionScope.member.userId=='admin'}">
				             <button type="button" class="btn" onclick="deleteNotice();">삭제</button>
				        </c:when>
				        <c:otherwise>
				             <button type="button" class="btn" disabled="disabled">삭제</button>
				        </c:otherwise>
				    </c:choose>
					
				</td>
				<td align="right">
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/notice/list.do?${query}';">리스트</button>
				</td>
			</tr>
		</table>
		
		<table>
		<tr>
					<td colspan="2" height="30">
						이전글 :
						<c:if test="${not empty preReadNotice}">
						    <a href="${pageContext.request.contextPath}/notice/article.do?${query}&nNum=${preReadNotice.nNum}">${preReadNotice.subject}</a>
						</c:if>
					</td>
				</tr>
				<tr>
					<td colspan="2" height="30">
						다음글 :
						<c:if test="${not empty nextReadNotice}">
						    <a href="${pageContext.request.contextPath}/notice/article.do?${query}&nNum=${nextReadNotice.nNum}">${nextReadNotice.subject}</a>
						</c:if>
					</td>
				</tr>	
		
		</table>

</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>

</body>
</html>