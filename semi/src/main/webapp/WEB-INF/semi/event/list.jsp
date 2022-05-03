<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>이벤트 - 리스트</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<style type="text/css">
.boxTF {
	border: 1px solid #999;
	padding: 5px 5px;
	width: 50%;
	background-color: #fff;
	border-radius: 4px;
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
	vertical-align: baseline;
}

.table {
	width: 100%;
	border-spacing: 0;
	border-collapse: collapse;
}

.table th, .table td {
	padding: 10px 0;
}

.table-border tr {
	border-bottom: 1px solid #FF8A3D;
}
 
.table-border tr:first-child {
	border-top: 2px solid #fff;
}

.board {
	margin: 30px auto;
	width: 100%;
}

.show-currentPage {
	font-size: 16px;
	text-align: left;
	font-weight: 600;
	color: #FF8A3D;
}

.table-list tr:first-child {
	background: #FF8A3D;
	color: #fff;
}

.table-list th, .table-list td {
	text-align: center;
}
.table-list .num {
	width: 5%;
}

.table-list .subject{
	width: 15%;
}
.table-list .name {
	width: 5%;
}

.table-list .date {
	width: 10%;
}

</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery-ui.min.js"></script>

<jsp:include page="/WEB-INF/semi/layout/staticHeader.jsp"/>
</head>

<header>
    <jsp:include page="/WEB-INF/semi/layout/header.jsp"></jsp:include>
</header>


<body>

<main>
	<div class="body-container">
		<div class="board">
			<div class="title">
				<h3><i class="fa-solid fa-carrot"></i> 이벤트</h3>
			</div>
			
			<table class="table">
				<tr>
					<td class="show-currentPage">${dataCount}개 (${page}/${total_page})PAGE</td>
					<td align="right">
						<c:if test="${sessionScope.member.uRole == 1}">
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/event/write.do';" >글등록</button>
						</c:if>
					</td>
				</tr>
			</table>
			
		
					<table class="table table-border table-list">
						<tr>
							<th class="num">번호</th>
							<th class="subject" >제목</th>
							<th class="name">작성자</th>
							<th class="date">이벤트 기간</th>
						</tr>
					<c:forEach var="dto" items="${list}">
						<tr align="center">
							<td>${dto.listNum}</td>
							<td><a href="${articleUrl}&eNum=${dto.eNum}">${dto.subject}</a></td>
							<td>${dto.uNick}</td>
							<td>${dto.startDate} ~ ${dto.endDate}</td>
						</tr>
					</c:forEach>
					</table>
			
			<div class="page-box">
				${dataCount == 0 ? "등록된 자주 묻는 질문이 없습니다." : paging}
			</div>
		</div>
	</div>
</main>
	
<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>
</body>
</html>