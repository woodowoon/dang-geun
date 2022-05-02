<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>semi-나눔글</title>
<jsp:include page="/WEB-INF/semi/layout/staticHeader.jsp"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/css/jquery-ui.min.css" type="text/css">
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v6.1.0/css/all.css">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<style type="text/css">
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
	border-bottom: 1px solid #FF8A3D;
}

.container table .border_rgt {
	border-right: 1px solid #FF8A3D;
}

.container table .border_last {
	border-bottom: 2px solid #FF8A3D;
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

.ui-widget-header { 
	background: none;
	border: none;
	border-bottom: 1px solid #ccc;
	border-radius: 0;
}
.ui-dialog .ui-dialog-title {
	padding-top: 5px; padding-bottom: 5px;
}
.ui-widget-content { 
   border-color: #ccc; 
}

.img-box {
	max-width: 700px;
	padding: 5px;
	box-sizing: border-box;
	border: 1px solid #ccc;
	display: flex; /* 자손요소를 flexbox로 변경 */
	flex-direction: row; /* 정방향 수평나열 */
	flex-wrap: nowrap;
	overflow-x: auto;
}
.img-box img {
	width: 300px;
	margin-right: 5px;
	flex: 0 0 auto;
	cursor: pointer;
}
.container .button {
	display: flex;
	justify-content: space-between;
	margin: 5px 8px;
}


</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>

<script type="text/javascript">
function deleteShare() {
    if(confirm("게시글을 삭제 하시 겠습니까 ? ")) {
	    let query = "code=${dto.code}&page=${page}";
	    let url = "${pageContext.request.contextPath}/share/delete.do?" + query;
    	location.href = url;
    }
}

function imageViewer(img) {
	const viewer = $(".photo-layout");
	let s="<img src='"+img+"'>";
	viewer.html(s);
	
	$(".dialog-photo").dialog({
		title:"이미지",
		width: 600,
		height: 530,
		modal: true
	});
}

</script>

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
				<td colspan="3">${dto.subject}</td>
			</tr>
			<tr class="border_btn">
				<td class="border_rgt t_header">닉네임</td>
				<td width="25%" class="border_rgt">${dto.uNick}</td>
				<td class="border_rgt t_header">지역</td>
				<td width="25%">${dto.rCode_name}</td>
				<td class="border_rgt t_header">조회수</td>
				<td>${dto.hitCount}</td>
			</tr>
			<tr>
				<td rowspan="2" class="border_rgt t_header">내용</td>
				<td colspan="3" class="content">
					<div class="img-box">
						<c:forEach var="vo" items="${listFile}">
							<img src="${pageContext.request.contextPath}/uploads/share/${vo.photoName}"
								onclick="imageViewer('${pageContext.request.contextPath}/uploads/share/${vo.photoName}');">
						</c:forEach>
					</div>
				</td>
			</tr>
			<tr class="border_last">
				<td colspan="3" class="content">${dto.content}</td>
			</tr>
			<tr>
				<td colspan="6" class="reg_date">${dto.reg_date}</td>
			</tr>
		</table>
		<div class="button">
			<div class="btn_left">
			
			<c:choose>
				<c:when test="${sessionScope.member.userId==dto.userId}">
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/share/update.do?code=${dto.code}&page=${page}';">수정</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn">나눔신청</button>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.uRole==1}">
					<button type="button" class="btn" onclick="deleteShare();">삭제</button>
				</c:when>
				<c:otherwise>
			    	
			    </c:otherwise>
			</c:choose>
			</div>
			<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/share/list.do';">리스트</button>
		</div>
	</div>
</div>
</main>

<div class="dialog-photo">
      <div class="photo-layout"></div>
</div>


<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery-ui.min.js"></script>

</body>
</html>