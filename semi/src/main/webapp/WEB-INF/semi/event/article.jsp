<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>당근마켓 - 이벤트 게시글</title>
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
	border-collapse: collapse;
	border-top: 2px solid #FF8A3D;
	min-height: 800px;	
}

.container table .border_btn {
	border-bottom: 1px solid #FF8A3D;
	height: 50px;
}

.container table .border_rgt {
	border-right: 1px solid #FF8A3D;
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

.container .button {
	display: flex;
	justify-content: space-between;
	margin: 5px 8px;
}


</style>
<script type="text/javascript">
function deleteEvent(){
	if(confirm('게시글을 삭제하시겠습니까?')){
		let query = "page=${page}&eNum=${dto.eNum}";
		let url = "${pageContext.request.contextPath}/event/delete.do?"+query;
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
			<h3><i class="fa-solid fa-carrot"></i> 이벤트 상세내용</h3>
		</div>
		<table>
			<tr class="border_btn">
				<td class="border_rgt t_header">제목</td>
				<td>${dto.subject}</td>
			</tr>
			
			<tr>
				<td rowspan="2" class="border_rgt t_header">내용</td>
				<td colspan="2" style="text-align: center;">
					<c:forEach var="vo" items="${listPhoto}">
						<img src = "${pageContext.request.contextPath}/uploads/event/${vo.savePhotoname}" width="80%;">
					</c:forEach>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;" class="content">${dto.content}</td>
			</tr>
			<tr class="border_btn">
				<td class="border_rgt t_header">이벤트기간</td>
				<td>${dto.startDate} ~ ${dto.endDate}</td>
			</tr>
		</table>
		<div class="button">
			<div class="btn_left">
			<c:choose>
				<c:when test="${sessionScope.member.uRole==1}">
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/event/update.do?eNum=${dto.eNum}&page=${page}';">수정</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn" disabled="disabled" style="display:none;">수정</button>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${sessionScope.member.uRole==1}">
					<button type="button" class="btn" onclick="deleteEvent()">삭제</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn" disabled="disabled" style="display:none;">삭제</button>
				</c:otherwise>
			</c:choose>
					
			</div>
			<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/event/list.do?${query}';">리스트</button>
		</div>
	</div>
</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>

</body>
</html>