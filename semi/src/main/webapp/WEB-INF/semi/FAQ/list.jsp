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
	width:15%;
	padding: 4px 3px;
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

.table-list .category{
	width: 15%;
}
.table-list .writer {
	width: 10%;
}


.table-list .date {
	width: 10%;
}
.table-list td:nth-child(4n+3){
	text-align: left;
	padding-left: 15px;
} 
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery-ui.min.js"></script>

<script type="text/javascript">
$(function(){
	let article = (".click");
	$(".click dt").click(function(){
		let myArticle = $(this).parents().next("tr");
		if($(myAcrticle).hasClass('hide')){
			$(article).removeClass('show').addClass('hide');
			$(myArticle).removeClass('hide').addClass("show");
		} else {
			$(myArticle).addClass('hide').removeClass('show');
		}
	});
});

function search(){
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
				<h3><i class="fa-solid fa-clipboard-question"></i> 자주 묻는 질문</h3>
			</div>
			
			<form name = "searchForm" method = "post" action="${pageContext.request.contextPath}/FAQ/list.do" >
				<table class="table">
					<tr>
						<td align="left">
							<select name = "condition" class="selectField">
								<option value = "0" ${condition == 0 ? "selected='selected'":"" }>카테고리</option>
								<option value = "1" ${condition == 1 ? "selected='selected'":"" }>로그인 관련</option>
								<option value = "2" ${condition == 2 ? "selected='selected'":"" }>보안 관련</option>
								<option value = "3" ${condition == 3 ? "selected='selected'":"" }>거래 관련</option>
							</select>
							<input type="text" name="keyword" value="${keyword}" class="boxTF">
							<button type="button" class="btn" onclick="search();">검색</button>
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/FAQ/list.do';">새로고침</button>
						</td>
						<td align="right">
							<c:if test="${sessionScope.member.uRole == 1}">
								<button type="button" class="btn sell-btn" onclick="location.href='${pageContext.request.contextPath}/FAQ/write.do';" >질문등록</button>
							</c:if>
						</td>
					</tr>
				</table>
			</form>
			
			
			
			<table class="table">
				<tr>
					<td class="show-currentPage">${dataCount}개의 글(${page}/${total_page})페이지</td>
				</tr>
			</table>
			
		
					<table class="table table-border table-list">
						<tr>
							<th class="num">번호</th>
							<th class="category" >카테고리</th>
							<th class="name" >질문 내용</th>
							<th class="writer">작성자</th>
							<th class="date">등록일</th>
						</tr>
					<c:forEach var="dto" items="${list}">
						<tr style=" cursor: pointer;" onclick="location.href='${articleUrl}&fNum=${dto.fNum}'" >
							<td>${dto.listNum}</td>
							<th>
								<c:choose>
									<c:when test="${dto.category == 1}">
										[로그인관련]
									</c:when>
									<c:when test="${dto.category == 2}">
										[보안관련]
									</c:when>
									<c:when test="${dto.category == 3}">
										[거래관련]
									</c:when>
									
								</c:choose>
							</th>
							<td >
								${dto.subject}	
							</td>
							<td>${dto.uNick}</td>
							<td>${dto.reg_date}</td>
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