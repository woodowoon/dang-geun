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
	height: 500px;
	border-collapse: collapse;
	border-top: 2px solid #FF8A3D;
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
					<li><img src="${pageContext.request.contextPath}/uploads/notice/${vo.photoName}" alt="" style="width:500px; height: 300px;"></li>
				</c:forEach>
			</ul>
			 <p class="controller">
				 <span class="prev"><i class="fa-solid fa-angle-left"></i></span>
				 <span class="next"><i class="fa-solid fa-angle-right"></i></span>
			 </p>
		</div>
		<table>	
			<tr class="sellerInfo">
				<td></td>
				<td>${dto.uNick}</td>
				<td>${dto.price}</td>
				<td>${dto.rName}</td>
				<td></td>
			</tr>
			<tr class="contentInfo">
				<td></td>
				<td colspan="3">${dto.content}</td>
				<td></td>
			</tr>
		</table>
		<div class="button">
			<div class="btn_left">
				<button type="button" class="btn" onclick="${pageContext.request.contextPath}/sell/update.do?${query}">수정</button>
				<button type="button" class="btn" onclick="${pageContext.request.contextPath}/sell/delete.do?${query}">삭제</button>
			</div>
			<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/sell/list.do?${query}'">리스트</button>
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