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
* {
	padding: 0;
	margin: 0;
	box-sizing: border-box;
}

botton {
	border-style: none;
}

main {
	width: 100%;
}


.container {
	width: 100%;
	margin: 40px auto;
}

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
	border-bottom: 1px solid #eee;
}

.container table .border_rgt {
	border-right: 1px solid #eee;
}

.container table .border_last {
	border-bottom: 2px solid #eee;
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

.btn {
	color: #fff;
	border: 2px solid #FF8A3D;
	background: #FF8A3D;
	padding: 4px 10px;
	border-radius: 4px;
	font-weight: bold;
	cursor: pointer;
	font-size: 14px;
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
	vertical-align: baseline;	
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
	<div class="container">
		<div class="title">
			<h3><span>|</span> 글보기</h3>
		</div>
		<table>
			<tr class="border_btn">
				<td class="border_rgt t_header">제목</td>
				<td colspan="3">한달 사용한 모니터 팝니다.</td>
			</tr>
			<tr class="border_btn">
				<td class="border_rgt t_header">닉네임</td>
				<td width="35%" class="border_rgt">홍길동</td>
				<td class="border_rgt t_header">지역</td>
				<td width="35%">서울</td>
			</tr>
			<tr class="border_btn">
				<td class="border_rgt t_header">가격</td>
				<td class="border_rgt">50000원</td>
				<td class="border_rgt t_header">조회수</td>
				<td>5</td>
			</tr>
			<tr>
				<td rowspan="2" class="border_rgt t_header">내용</td>
				<td colspan="3" class="content"><img alt="첨부이미지" src="monitor.jpg"></td>
			</tr>
			<tr class="border_last">
				<td colspan="3" class="content">모니터 팝니다. 오만원</td>
			</tr>
			<tr>
				<td colspan="4" class="reg_date">2022.04.22</td>
			</tr>
		</table>
		<div class="button">
			<div class="btn_left">
				<button type="button" class="btn">수정</button>
				<button type="button" class="btn">삭제</button>
			</div>
			<button type="button" class="btn">리스트</button>
		</div>
	</div>
</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>

</body>
</html>