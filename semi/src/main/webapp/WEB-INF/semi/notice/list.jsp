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
<script type="text/javascript">
function searchOk() {
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
				<h3><i class="fa-solid fa-carrot"></i> 공지사항 </h3>
				
			<table class="table">
		      <tr>
				<td align="center">
					<form name="searchForm" action="${pageContext.request.contextPath}/notice/list.do" method="post">
						<select name="condition" class="form-select">
							<option value="all" ${condition=="all" ?"selected='selected'":""}>제목+내용</option>
							<option value="reg_date" ${condition=="reg_date" ?"selected='selected'":""}>등록일</option>
							<option value="subject" ${condition=="subject" ?"selected='selected'":""}>제목</option>
							<option value="content" ${condition=="content" ?"selected='selected'":""}>내용</option>
						</select>				
						<input type="text" name="keyword" value="${keyword}" class="form-control">
						<button type="button" class="btn" onclick="searchOk();">검색</button>
					</form>
				</td>
				<td>
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/notice/list.do';">새로고침</button>
				    </td>
				    
				<td align="right">
				    <c:if test="${sessionScope.member.userId == 'admin'}">
						<button type="button" class="btn notice-btn" onclick="location.href='${pageContext.request.contextPath}/notice/write.do';">글올리기</button>
				    </c:if>
				</td>
			</tr>
		</table>	
			
				
				<table class="table">
				<tr>		
			        <td class="show-currentPage">${dataCount}개(${page}/${total_page}페이지)</td>
			    </tr>
			   </table>
		
			</div>
		
			<table class="table table-border table-list">
				<tr>
					<th class="num">번호</th>
					<th class="subject">제목</th>
					<th class="uNick">닉네임</th>
					<th class="date">등록일</th>
					<th class="hit">조회수</th>
				</tr>
			<c:forEach var="dto" items="${listNotice}">
				<tr>
					<td><span class="notice">공지</span></td>
					<td class="left"><a href="${articleUrl}&nNum=${dto.nNum}">${dto.subject}</a></td>
					
					<td>${dto.userId}</td>
					<td>${dto.reg_date}</td>
					<td>${dto.hitCount}</td>
				</tr>
			</c:forEach>
			
			<c:forEach var="dto" items="${list}">
				<tr>
					<td>${dto.listNum}</td>
					<td class="left">
					    <a href="${articleUrl}&nNum=${dto.nNum}">${dto.subject}</a>
					    <c:if test="${dto.gap<1}"></c:if>
					</td>
					<td>${dto.userId}</td>
					<td>${dto.reg_date}</td>
					<td>${dto.hitCount}</td>
				</tr>
              </c:forEach>
			</tbody>
			
			
			</table>
			
			<div class="page-box">
				${dataCount == 0 ? "등록된 게시물이 없습니다." : paging }
			</div>	
			
		</div>
	</div>
</main>
	
<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>
</body>
</html>