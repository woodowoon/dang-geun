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

.leftBtn .rightBtn {
	width: 3%;
}

.board .button {
	display: flex;
	justify-content: space-between;
	margin: 5px 8px;
}


</style>
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
				<h3><i class="fa-solid fa-carrot"></i> ${dto.subject}</h3>
			</div>
		<table>
			<tr class="img_tr">
				<td class="leftBtn" style="width:3%;"><i class="fa-solid fa-angle-left"></i></td>
				<td colspan="3"><img src=""></td>
				<td class="rightBtn"style="width:3%;"><i class="fa-solid fa-angle-right"></i></td>
				
			</tr>
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
				<button type="button" class="btn">수정</button>
				<button type="button" class="btn">삭제</button>
			</div>
			<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/sell/list.do?${query}'">리스트</button>
		</div>
		
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