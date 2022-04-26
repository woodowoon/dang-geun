<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
				<h3><i class="fa-solid fa-carrot"></i>당근 판매</h3>
			</div>
			
			<table class="table">
				<tr>
					<td align="left">
						<select name="condition" class="selectField">
							<option value="all">::지역::</option>
						</select>
						<input type="text" name="keyword" value="" class="boxTF">
						<button type="button" class="btn">검색</button>
						<button type="button" class="btn">새로고침</button>
					</td>
					<td align="right">
						<button type="button" class="btn sell-btn" onclick="location.href='${pageContext.request.contextPath}/bbs/write.do';">판매등록</button>
					</td>
				</tr>
			</table>
			
			<table class="table">
				<tr>
					<td class="show-currentPage">(1/3)페이지</td>
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
			<c:forEach var="a" begin="1" end="10">
				<tr>
					<td>${a}</td>
					<td>
						<a href="${pageContext.request.contextPath}/bbs/article.do">사진더미</a>
					</td>
					<td>제품 더미</td>
					<td>1,000,000</td>
					<td>2022-04-22</td>
					<td>10</td>
				</tr>
			</c:forEach>
			</table>
			
			<div class="page-box">
				페이지 박스
			</div>
		</div>
	</div>
</main>
	
<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>
</body>
</html>