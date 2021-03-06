<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!--리스트 : 제품 사진 / 제품 명 / 판매 가격 / 등록일 / 조회수  -->
<title>semi-나눔</title>
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

.table-list .date {
	width: 13%;
}

.table-list .hit {
	width: 7%;
}

.page-box {
	clear: both;
	padding: 20px 0;
	text-align: center;
}
.paginate {
	text-align: center;
	white-space: nowrap;
	font-size: 14px;	
}
.paginate a {
	border: 1px solid #ccc;
	color: #000;
	font-weight: 600;
	text-decoration: none;
	padding: 3px 7px;
	margin-left: 3px;
	vertical-align: middle;
}
.paginate a:hover, .paginate a:active {
	color: #6771ff;
}
.paginate span {
	border: 1px solid #e28d8d;
	color: #cb3536;
	font-weight: 600;
	padding: 3px 7px;
	margin-left: 3px;
	vertical-align: middle;
}
.paginate :first-child {
	margin-left: 0;
}

</style>
<script type="text/javascript">
function searchList() {
	const f = document.searchForm;
	f.submit();
}

</script>

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
				<h3><i class="fa-solid fa-carrot"></i>당근 나눔</h3>
			</div>
			<form name="searchForm" action="${pageContext.request.contextPath}/share/list.do" method="post">
				<table class="table">
					<tr>
						<td align="left">
							<select name="rCode" class="selectField">
								<option value="0">::전체::</option>
								<c:forEach var="vo" items="${regionList}">
									<option value="${vo.rCode}" 
										    ${rCode == vo.rCode ? "selected='selected'" : ""}>
										${vo.rCode_name}
									</option>
								</c:forEach>
							</select>
							<input type="text" name="keyword" value="${keyword}" class="boxTF">
							<button type="button" class="btn" onclick="searchList();">검색</button>
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/share/list.do';">새로고침</button>
						</td>
						<td align="right">
							<button type="button" class="btn sell-btn" onclick="location.href='${pageContext.request.contextPath}/share/write.do';">나눔등록</button>
						</td>
					</tr>
				</table>
			</form>
			<table class="table">
				<tr>
					<td class="show-currentPage">(${current_page}/${total_page})페이지</td>
				</tr>
			</table>
			
			<table class="table table-border table-list">
				<tr>
					<th class="num">번호</th>
					<th class="photo">제목</th>
					<th class="name">작성자</th>
					<th class="date">등록일</th>
					<th class="hit">조회수</th>
				</tr>
			<c:forEach var="dto" items="${list}">
				<tr>
					<td>${dto.listNum}</td>
					<td>
						<a href="${articleUrl}&code=${dto.code}">[${dto.rCode_name}]&nbsp;${dto.subject}</a>
					</td>
					<td>${dto.uNick}</td>
					<td>${dto.reg_date}</td>
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