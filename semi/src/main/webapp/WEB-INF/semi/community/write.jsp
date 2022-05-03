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
/*
.table-form td {
	padding: 7px 0;
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
}

.table-form p {
	line-height: 200%;
}

.table-form tr:first-child {
    border-top: 2px solid #FF8A3D;
}

.table-form tr > td:nth-child(2) {
	padding-left: 10px;
}

.table-form input[type=text], .table-form input[type=file], .table-form textarea {
	width: 96%;
}

.table-form tr>td {
	padding: 7px 0;
}

.table-form tr>td:first-child {
	width: 110px; text-align: center; background: #FF8A3D;
	color: #fff;
}

.form-control {
	border: 1px solid #999; border-radius: 4px; background-color: #fff;
	padding: 5px 5px; 
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
	vertical-align: baseline;
}

.form-control[readonly] { background-color:#FF8A3D; }

textarea.form-control { height: 170px; resize : none; }
textarea:focus, input:focus { outline: none; }

.table { width: 100%; border-spacing: 0; border-collapse: collapse; }
.table th, .table td { 
	padding-top: 10px; padding-bottom: 10px; 
}

.td-border td { border: 1px solid #FF8A3D; }

.left{ text-align: left; padding-left: 10px; }
.right{ text-align: right; padding-right: 10px; }
.center{ text-align: center; }

.clear { clear: both; }
.clear:after { content:''; display:block; clear: both; }

.btn{
	background: #FF8A3D;
	color:#fff;
	border: none;
	padding: 5px;
	border-radius: 5px;
	margin-top: 20px;
	
}
*/
</style>

<script type="text/javascript">
function sendOk() {
	const f = document.commuForm;
	let str;
	
	str = f.subject.value.trim();
	if(!str){
		alert("제목을 입력하세요");
		f.subject.focus();
		return;
	}
	
	str = f.content.value.trim();
	if(!str){
		alert("내용을 입력하세요");
		f.content.focus();
		return;
	}
	
	if(confirm("${mode=='write' ? '등록' : '수정'}하시겠습니까?")){
		f.action = "${pageContext.request.contextPath}/community/${mode}_ok.do";
		f.submit();
	} 

	
}


<c:if test="${mode=='update'}">
	function deletePhoto(fileNum){
		if(!confirm("이미지를 삭제하시겠습니까?")){
			return;
		}
		
		let query = "num=${dto.num}&fileNum="+fileNum+"&page=${page}";
		let url = "${pageContext.request.contextPath}/community/deletePhoto.do?"+query;
		location.href = url;
	}
</c:if>

</script>
<jsp:include page="/WEB-INF/semi/layout/staticHeader.jsp"/>
</head>
<body>

<header>
	<jsp:include page="/WEB-INF/semi/layout/header.jsp"></jsp:include>
</header>

<main>
	<div class="body-container">
		<div class="title">
			<h3><i class="fas fa-chalkboard-teacher"></i> 커뮤니티글쓰기 </h3>
		</div>
        
		<form name="commuForm" method="post" enctype="multipart/form-data">
			<table class="table table-border table-form">
				<tr> 
					<td>제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
					<td> 
						<input type="text" name="subject" maxlength="100" class="form-control" value="${dto.subject}">
					</td>
				</tr>
				
				<tr> 
					<td>작성자</td>
					<td> 
						<p>${sessionScope.member.uNick}</p>
					</td>
				</tr>
				
				<tr> 
					<td>지&nbsp;&nbsp;&nbsp;&nbsp;역</td>
					<td> 
						<p>${rName}</p>
					</td>
				</tr>
				
				<tr> 
					<td valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
					<td> 
						<textarea name="content" class="form-control">${dto.content}</textarea>
					</td>
				</tr>
				
				<tr>
					<td>첨&nbsp;&nbsp;&nbsp;&nbsp;부</td>
					<td> 
						<input type="file" name="selectFile" class="form-control" accept="image/*" multiple="multiple">
					</td>
				</tr>
				
				<c:if test="${mode=='update'}">
	                <c:forEach var="vo" items="${listPhoto}">
				        <tr>
				            <td>첨부된 파일</td>
				            <td>
				                <p>
				                   <a href="javascript:deletePhoto('${vo.fileNum}');"><i class="far fa-trash-alt"></i></a>
				                   ${vo.imageFilename}
				                </p>
				            </td>
				        </tr>
	                </c:forEach>
		        </c:if>
		        
			</table>
			
			<table class="table">
				<tr> 
					<td align="center">
						<button type="button" class="btn" onclick="sendOk();">${mode=='write' ? '등록' : '수정'}</button>
						<button type="reset" class="btn">다시입력</button>
						<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/community/list.do';">${mode=='write' ? '등록취소' : '수정취소'}</button>
						<c:if test="${mode=='update'}">
							<input type="hidden" name="num" value="${dto.num}">
							<input type="hidden" name="page" value="${page}">
						</c:if>
					</td>
				</tr>
			</table>
		</form>
        
	</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>
</body>
</html>