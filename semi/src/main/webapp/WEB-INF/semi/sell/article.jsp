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
.title {
	margin-bottom: 15px;
	color: #FF8A3D;
}

.board {
	margin: 30px auto;
}

.board .title span {
	margin-bottom: 15px;
	font-size: 20px;
}

.board table {
	width: 100%;
	border-collapse: collapse;
}

.itemInfo {
	margin: 50px auto;
	border-top: 2px solid #FF8A3D;
}

.itemInfo tr td {
	padding: 7px;
}

.itemInfo tr td:first-child {
	width: 10%;
	color: #fff;
	border-bottom: 1px solid #fff;
	font-weight: 600;
	background: #FF8A3D;
	text-align: center;
	vertical-align: middle;
}

.itemInfo tr:first-child td:nth-child(odd) {
	width: 10%;
	color: #fff;
	font-weight: 600;
	background: #FF8A3D;
	text-align: center;
	vertical-align: middle;
}

.itemInfo tr td:not(:first-child) {
	border-bottom: 1px solid #FF8A3D;
}



.itemInfo tr:nth-child(odd) {
	height: 50px;
}

.detail {
	min-height: 200px;
}

.board .button {
	display: flex;
	justify-content: space-between;
	margin: 5px 8px;
}

li {
	list-style-type: none;
}

#slideShow {
	width: 700px;
	height: 300px;
	position: relative;
	margin: 50px auto;
	overflow: hidden;
}

.slides {
	position: absolute;
	left: 0;
	top: 0;
	width: 2500px;
	transition: left 0.5s ease-out;
}

.slides li:first-child {
	margin-left: 100px;
}

.slides li:not(:last-child) {
	float: left;
	margin-right: 100px;
}

.slides li {
	float: left;
}

.slides img {
	width: 500px;
	height: 300px;
	border-radius: 15px;
}

.controller span {
	position: absolute;
	color: black;
	background-color: transparent;
	text-align: center;
	border-radius: 50%;
	padding: 10px 20px;
	top: 50%;
	font-size: 1.3em;
	cursor: pointer;
}

.controller span:hover {
	background-color: rgba(128, 128, 128, 0.11);
}

.prev {
	left: 10px;
}

.prev:hover {
	transform: translateX(-10px);
}

.next {
	right: 10px;
}

.next:hover {
	transform: translateX(10px);
}

.info {
	border-top: 1px solid #ddd;
}

</style>
<jsp:include page="/WEB-INF/semi/layout/staticHeader.jsp"/>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
$(function() {
	const slides = document.querySelector('.slides');
	const slideImg = document.querySelectorAll('.slides li');
	let currentIdx = 0;
	const slideCount = slideImg.length;
	const prev = document.querySelector('.prev');
	const next = document.querySelector('.next');
	const slideWidth = 500;
	const slideMargin = 100;
	
	slides.style.width = (slideWidth + slideMargin) * slideCount + 'px';
	
	function moveSlide(num) {
		slides.style.left = -num * 600 + 'px';
		currentIdx = num;
	}
	
	prev.addEventListener('click', function() {
		if(currentIdx !== 0) moveSlide(currentIdx - 1);
	});
	
	next.addEventListener('click', function() {
		if(currentIdx !== slideCount - 1) {
			moveSlide(currentIdx + 1);
		}
	});
});
</script>
</head>
<body>


<header>
    <jsp:include page="/WEB-INF/semi/layout/header.jsp"></jsp:include>
</header>

<main>
<div class="body-container">
	<div class="board">
		<div class="title">
			<h3><i class="fa-solid fa-carrot"></i> ${dto.subject}</h3>
		</div>
		<hr style="border:2px solid #FF8A3D">
		<div id="slideShow">
			<ul class="slides">
				<c:forEach var="vo" items="${listPhoto}">
					<li><img src="${pageContext.request.contextPath}/uploads/notice/${vo.photoName}" alt=""></li>
				</c:forEach>
			</ul>
			 <p class="controller">
				 <span class="prev"><i class="fa-solid fa-angle-left"></i></span>
				 <span class="next"><i class="fa-solid fa-angle-right"></i></span>
			 </p>
		</div>
		<table class="itemInfo">
			<tr>
				<td>판매자 명</td>
				<td>${dto.uNick}</td>
				<td>지역</td>
				<td>${dto.rName}</td>
			</tr>
			<tr>
				<td>제목</td>
				<td colspan="3">${dto.subject}</td>
			</tr>
			<tr>
				<td>가격</td>
				<td colspan="3"><fmt:formatNumber type="number" maxFractionDigits="3" value="${dto.price}" /> 원</td>
			</tr>
			<tr>	
				<td>상세 내용</td>
				<td colspan="3">
					<div class="detail">${dto.content}</div>
				</td>	
			</tr>
		</table>
		
		<div class="button">
			<div class="btn_left">
				<c:if test="${sessionScope.member.userId == dto.userId}">
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/sell/update.do?num=${dto.code}&${query}';">수정</button>
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/sell/delete.do?num=${dto.code}'">삭제</button>
				</c:if>
			</div>
			<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/sell/list.do?${query}';">리스트</button>
		</div>
		<hr style="border:2px solid #FF8A3D">
		<div class="reply">
		 댓글
		</div>
	</div>
</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>

</body>
</html>