<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/semi/layout/staticHeader.jsp"/>
<style type="text/css">
.container .title {
	margin: 30px 0 15px;
	color: #FF8A3D;
}
.container .info {
	width: 100%;
	height: 150px;
	border-collapse: collapse;
	border-top: 2px solid #FF8A3D;
	border-bottom: 2px solid #FF8A3D;
}
.container .info th {
	color: #fff;
	background: #FF8A3D;
	width: 15%;
	height: 50%;
}

.container .info th + td {
	padding-left: 10px;
}

.mypage{
	width: 100%;
	border-collapse: collapse;
}

.mypage td{
	padding: 8px 0;
	text-align: center;
}


.mypage tr {
	border-bottom: 1px solid #FF8A3D;
}

.mypage tr:first-child {
	height: 40px;
	background: #FF8A3D;
	color: #fff;
	border: none;
}

.form .img-viewer {
	cursor: pointer;
	border: 1px solid #ccc;
	width: 70px;
	height: 70px;
	border-radius: 70px;
	border-color:#FF8A3D;
	background-image: url("${pageContext.request.contextPath}/resource/images/add_photo.png");
	position: relative;
	z-index: 9999;
	background-repeat : no-repeat;
	background-size : cover;
}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript">
$(function(){
	$(".form .img-viewer").click(function(){
		$("form[name=mypageForm] input[name=selectFile]").trigger("click");
	});
	
	$("form[name=mypageForm] input[name=selectFile]").change(function(){
		let img;
		
		let file = this.files[0];
		if(! file) { //선택한 파일이 없으면
			$(".form .img-viewer").empty();
			img = "${pageContext.request.contextPath}/resource/images/add_photo.png";
			$(".form .img-viewer").css("background-image","url("+img+")");
			return false;
		}
		
		let reader = new FileReader();
		reader.onload = function(e){
			$(".form .img-viewer").empty();
			$(".form .img-viewer").css("background-image","url("+e.target.result+")");
		};
		reader.readAsDataURL(file);
	});
});

</script>
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/semi/layout/header.jsp"></jsp:include>
</header>
<div class="body-container">
	<div class="container">
		<div class="title">
			<h3><i class="fa-solid fa-carrot"></i> 마이페이지 </h3>
		</div>
	
		<form name="mypageForm">
			<table class = "info" >
				<tr>
					<td style="text-align: center;" rowspan="2">
						<div class= "form" >
							<div class= "img-viewer" style="margin: 0 auto;">
							</div>
							<input type="file" name="selectFile" accept="image/*" style="display: none;">
							<p>프로필 사진</p>
						</div>
					</td>
					<th>
						닉네임
					</th>
					<td>
						${sessionScope.member.uNick}
					</td>
					<th>
						당근 지역
					</th>
					<td>
						${rName}
					</td>
					
				</tr>
				<tr>
					<th>
						당근 판매량
					</th>		
					<td>
						000
					</td>
					<th>
						커뮤니티<br>활동량
					</th>
					<td>
						글 ${cmmuCount}개
					</td>
				</tr>
			
			</table>
			<div class="title">
				<h3><i class="fa-solid fa-carrot"></i> 판매중인 상품 </h3>
			</div>
			<table class= "mypage mySell">
				<tr>
					<th style="width: 20%">제품사진</th>
					<th>제목</th>
					<th style="width: 15%">판매가격</th>
					<th style="width: 15%">등록일</th>
					<th style="width: 15%">판매상태</th>
				</tr>
			<c:forEach var ="dto" items="${sellList}">
				<tr style="cursor:pointer;" onclick="location.href='';">
					<td>	
						<img src="${pageContext.request.contextPath}/uploads/notice/${dto.photoName}" style="width:100px; height:100px;">
					</td>
					<td>${dto.subject}</td>
					<td><fmt:formatNumber type="number" maxFractionDigits="3" value="${dto.price}" /></td>
					<td>${fn:substring(dto.reg_date, 0, 11)}</td>
					<td>
						<c:choose>
							<c:when test="${dto.status == 0}">
								<button type="button" class="btn" onclick="location.href='';">판매중</button>
							</c:when>
							<c:when test="${dto.status == 1}">
								<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/sell/sellRequest.do?num=${dto.code}&flag=complete&${query}';" style="margin-bottom: 10px;">거래완료</button>
								<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/sell/sellRequest.do?num=${dto.code}&flag=cancel&${query}';" style="background: #777;">거래취소</button>
								
							</c:when>
						</c:choose>
					</td>
				</tr>
			</c:forEach>	
			</table>
			<div class="page-box">
					${sellCount == 0 ? "현재 판매중인 상품이 없습니다." : sellPaging}
			</div>
			
			
			<div class="title">
				<h3><i class="fa-solid fa-carrot"></i> 판매완료 상품 </h3>
			</div>
			<table class= "mypage mySold">
				<tr>
					<th style="width: 20%">제품사진</th>
					<th style="">제품명</th>
					<th style="width: 15%">판매가격</th>
					<th style="width: 15%">등록일</th>
					<th style="width: 15%">판매일</th>
				</tr>
				<tr>
					<td>판매완료</td>
				<tr>
			</table>
			<div class="page-box">
					${dataCount == 0 ? "판매 완료된 상품이 없습니다." : paging}
			</div>
			
			
			<div class="title">
				<h3><i class="fa-solid fa-carrot"></i> 커뮤니티 </h3>
			</div>
			<table class= "mypage myComm">
				<tr>
					<th style="width: 10%">지역</th>
					<th>제목	</th>
					<th style="width: 15%">등록일</th>
				</tr>
				<c:if test="${cmmuList != null}">
					<c:forEach var="cmmu" items="${cmmuList}">
					<tr>
						<td>${cmmu.rName}</td>
						<td><a href="${articleUrl}&num=${cmmu.num}">${cmmu.subject}</a></td>
						<td>${cmmu.reg_date}</td>
					<tr>
					</c:forEach>
				</c:if>
			</table>
			<div class="page-box">
					${dataCount == 0 ? "게시한 커뮤니티 글이 없습니다." : paging}
			</div>
			
			<div class="title">
				<h3><i class="fa-solid fa-carrot"></i> 나눔중인 상품 </h3>
			</div>
			<table class= "mypage mySell">
				<tr>
					<th style="width: 20%">제품사진</th>
					<th>제품명</th>
					<th style="width: 15%">등록일</th>
					<th style="width: 15%">나눔완료</th>
				</tr>
				<c:forEach var="vo" items="${listSell}">
					<tr>
						<td>${vo.subject }</td>
					<tr>
					
				</c:forEach>
					<tr></tr>			
			</table>
			<div class="page-box">
					${dataCount == 0 ? "현재 판매중인 상품이 없습니다." : paging}
			</div>
			
			<div class="title">
				<h3><i class="fa-solid fa-carrot"></i> 나눔완료 상품 </h3>
			</div>
			<table class= "mypage mySold">
				<tr>
					<th style="width: 20%">제품사진</th>
					<th style="">제품명</th>
					<th style="width: 15%">등록일</th>
					<th style="width: 15%">판매일</th>
				</tr>
				<tr>
					<td>판매완료</td>
				<tr>
			</table>
			<div class="page-box">
					${dataCount == 0 ? "판매 완료된 상품이 없습니다." : paging}
			</div>
		</form>
	</div>
</div>
<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>

</body>
</html>