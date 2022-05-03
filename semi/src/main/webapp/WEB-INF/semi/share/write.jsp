<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>semi-나눔</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<style type="text/css">

.img-box {
	max-width: 600px;
	padding: 5px;
	box-sizing: border-box;
	display: flex; 
	flex-direction: row; 
	flex-wrap: nowrap;
	overflow-x: auto;
}
.img-box img {
	width: 37px; height: 37px;
	margin-right: 5px;
	flex: 0 0 auto;
	cursor: pointer;
}

</style>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
function sendOk() {
	const f = document.shareForm;

	let str;
	
	str = f.subject.value.trim();
	if(! str) {
		alert("제목을 입력하세요");
		f.subject.focus();
		return;
	}

	if(! f.rCode.value) {
		alert("지역을 선택하세요");
		f.rCode.focus();
		return;
	}
	
	str = f.content.value.trim();
	if(! str) {
		alert("내용을 입력하세요");
		f.rCode.focus();
		return;
	}
	
	str = f.selectFile.value.trim();
	let mode = "${mode}";
    if( (mode === "write") && (!f.selectFile.value) ) {
        alert("이미지 파일을 추가 하세요. ");
        f.selectFile.focus();
        return;
    } else if(${mode == 'update'}) {
		if((! str) && !( $('.reg_photo').length) ) {
			alert("첨부파일이 없습니다.");
			f.selectFile.focus();
			return;
		}
	}
    
    f.action = "${pageContext.request.contextPath}/share/${mode}_ok.do";
    f.submit();
}

<c:if test="${mode=='update'}">
	function deleteFile(pNum) {
		if(! confirm("이미지를 삭제하시겠습니까?")) {
			return;
		}
		
		let query = "code=${dto.code}&pNum=" + pNum + "&page=${page}";
		let url = "${pageContext.request.contextPath}/share/deleteFile.do?" + query;
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
			<h3><i class="fas fa-chalkboard-teacher"></i> 나눔 </h3>
		</div>
        
		<form name="shareForm" method="post" enctype="multipart/form-data">
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
					<c:if test="${mode=='write'}">
						<select name="rCode" class="selectField">
							<option value="0">::전체::</option>
								<c:forEach var="vo" items="${regionList}">
									<option value="${vo.rCode}"  ${sessionScope.member.rCode == vo.rCode ? "selected='selected'" : ""}  >
										 ${vo.rCode_name}
									</option>
								</c:forEach>
						</select>
					</c:if>
					
					<c:if test="${mode=='update'}">
						<select name="rCode" class="selectField">
							<option value="0">::전체::</option>
								<c:forEach var="vo" items="${regionList}">
									<option value="${vo.rCode}" ${vo.rCode == dto.rCode ? "selected='selected'":""} >
										 ${vo.rCode_name}
									</option>
								</c:forEach>
						</select>
					</c:if>
					</td>
				</tr>
				
				<tr> 
					<td valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
					<td> 
						<textarea name="content" class="form-control">${dto.content}</textarea>
					</td>
				</tr>
				
				<tr>
					<td>이&nbsp;&nbsp;미&nbsp;&nbsp;지</td>
					<td> 
						<input type="file" name="selectFile" class="form-control" accept="image/*" multiple="multiple">
					</td>
				</tr>
		        <c:if test="${mode=='update'}">
					<tr>
						<td>등록이미지</td>
						<td> 
							<div class="img-box">
								<c:forEach var="vo" items="${listFile}">
									<img class="reg_photo" src="${pageContext.request.contextPath}/uploads/share/${vo.photoName}"
										onclick="deleteFile('${vo.pNum}');">
								</c:forEach>
							</div>
						</td>
					</tr>
				</c:if>
			</table>
			
			<table class="table">
				<tr> 
					<td align="center">
						<button type="button" class="btn" onclick="sendOk();">${mode =='write' ? '등록' : '수정' }</button>
						<button type="reset" class="btn">다시입력</button>
						<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/share/list.do';">${mode =='write' ? '등록' : '수정' }취소</button>
						<c:if test="${mode=='update'}">
							<input type="hidden" name="code" value="${dto.code}">
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