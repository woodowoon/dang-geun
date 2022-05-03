<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!--리스트 : 제품 사진 / 제품 명 / 판매 가격 / 등록일 / 조회수  -->
<title>Insert title here</title>
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

.selectField {
	border: 1px solid #999;
	width:13%;
	padding: 4px 5px;
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
	text-align: right;
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

.table-list .photo {
	width: 30%;
}

.table-list .name {
	width: 20%;
}

.table-list .price {
	width: 15%;
}

.table-list .date {
	width: 13%;
}

.table-list .hit {
	width: 7%;
}

</style>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript">


function searchList() {
	const f = document.serachForm;
	f.submit();
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
		<div class="board">
			<div class="title">
				<h3><i class="fa-solid fa-carrot"></i>당근 판매</h3>
			</div>
			
			<table class="table">
				<tr>
					<td align="left">
						<form name="serachForm" action="${pageContext.request.contextPath}/sell/list.do" method="post">
							<select name="rCode" class="selectField">
								<option value="0">::전체::</option>
								<c:forEach var="dto" items="${regionList}">
									<option value="${dto.rCode}" 
										    ${rCode == dto.rCode ? "selected='selected'" : ""}>
										${dto.rName}
									</option>
								</c:forEach>		
							</select>
							<input type="text" name="keyword" value="${keyword}" class="boxTF">
							<button type="button" class="btn" onclick="searchList();">검색</button>
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/sell/list.do';">새로고침</button>
						</form>
					</td>
					<td align="right">
					<c:choose>
						<c:when test="${empty sessionScope.member}">
							<button type="button" class="btn sell-btn" onclick="location.href='${pageContext.request.contextPath}/member/login.do';">판매등록</button>
						</c:when>	
						<c:otherwise>
							<button type="button" class="btn sell-btn" onclick="location.href='${pageContext.request.contextPath}/sell/write.do';">판매등록</button>
						</c:otherwise>
					</c:choose>
					</td>
				</tr>
			</table>
			
			<table class="table">
				<tr>
					<td class="show-currentPage">( ${current_page}/${total_page}페이지 )</td>
				</tr>
			</table>
			
			<table class="table table-border table-list">
				<tr>
					<th class="num">번호</th>
					<th class="photo">제품 사진</th>
					<th class="name">제품 명</th>
					<th class="price">판매 가격</th>
					<th class="date">등록일</th>
					<th class="hit">조회수</th>
				</tr>
			<c:forEach var="dto" items="${list}">
				<tr style="cursor:pointer;" onclick="location.href='${articleUrl}&num=${dto.code}'">
					<td>${dto.listNum}</td>
					<td>
						 <img src="${pageContext.request.contextPath}/uploads/notice/${dto.photoName}" style="width:100px; height:100px;">
					</td>
					<td>${dto.subject}</td>	
					<td><fmt:formatNumber type="number" maxFractionDigits="3" value="${dto.price}" /> 원</td>
					
					<td>${fn:substring(dto.reg_date, 0, 11)}</td>
					<td>${dto.hitCount}</td>
				</tr>
			</c:forEach>
			</table>
			
			<div class="page-box">
				${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
			</div>
		</div>
	</div>
</main>
	
<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>
</body>
</html>