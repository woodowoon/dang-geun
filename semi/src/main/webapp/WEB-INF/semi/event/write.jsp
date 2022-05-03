<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>당근마켓 - 이벤트 작성/수정</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<style type="text/css">
.img-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, 65px);
	grid-gap: 5px;
}

.img-grid .item {
    object-fit: cover;
    width: 65px;
    height: 65px;
	cursor: pointer;
}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript">
$(function(){
	$("body").on("click",".img-add",function(){
		$("form input[name=selectFile]").trigger("click");
	});
	
	$("form input[name=selectFile]").change(function(){
		$(".img-grid").empty();//이미 있는거 지우기
		let $add = $("<img>", {class:"item img-add"});
		$add.attr("src","add_photo.png");
		$(".img-grid").append($add);
		
		if(! this.files){
			return false;
		}
		
		const fileArr = Array.from(this.files); //유사배열을 배열로 변환
		
		fileArr.forEach((file, index)=>{
			const reader = new FileReader();
			let $img = $("<img>", {class:"item img-item"});
			$img.attr("data-filename",file.name);
			reader.onload = e => {
				$img.attr("src",e.target.result);
			}
			reader.readAsDataURL(file);
			$(".img-grid").append($img);
		});
		
	});
	
	
	$("body").on("click",".img-item", function(){
		if(! confirm("선택한 파일을 취소하시겠습니까? ")){
			return false;
		}
		
		let selectFiles = document.boardForm.selectFile.files;
		const fileArr = Array.from(selectFiles);
		let filename = $(this).attr("data-filename");
		
		for(let i = 0 ; i<fileArr.length; i++){
			if(filename === fileArr[i].name){
				fileArr.splice(i,1);
				break;
			}
		}
		
		let dt = new DataTransfer();
		for(file of fileArr) {
			dt.items.add(file);
		}
		document.boardForm.selectFile.files = dt.files;
		
		$(this).remove();
	});
	
});

function sendOk(){
	const f = document.boardForm;
	let str;
	
	str = f.subject.value.trim();
	if(!str){
		alert("제목을 입력하세요.");
		f.subject.focus();
		return;
	}

	str = f.content.value.trim();
	if(!str){
		alert("내용을 입력하세요.");
		f.content.focus();
		return;
	}
	
	f.action = "${pageContext.request.contextPath}/event/${mode}_ok.do";
	f.submit();
}

<c:if test="${mode=='update'}">	
function deleteFile(pNum){
	if(confirm('파일을 삭제하시겠습니까?')){
		let query = "eNum=${dto.eNum}&page=${page}&pNum="+pNum;
		let url = "${pageContext.request.contextPath}/event/deletePhoto.do?"+query;
		location.href=url;
	}
	
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
			<h3><i class="fa-solid fa-carrot"></i> 이벤트 ${mode == "update" ? "수정" : "등록" }하기 </h3>
		</div>
        
		<form name="boardForm" method="post" enctype="multipart/form-data">
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
					<td valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
					<td> 
						<c:if test="${mode == 'update'}">
							<c:forEach var="vo" items="${listPhoto}">
								<img src = "${pageContext.request.contextPath}/uploads/event/${vo.savePhotoname}" width="80%;">
							</c:forEach>
						</c:if>
						<textarea name="content" class="form-control">${dto.content}</textarea>
					</td>
				</tr>
				
				<tr>
					<td>기간</td>
					<td>
						시작일&nbsp;&nbsp;&nbsp;&nbsp;<input type="date" name="startDate" class="form-control" value="${dto.startDate}" style="width: 30%;">&nbsp;&nbsp;&nbsp;&nbsp;
						종료일&nbsp;&nbsp;&nbsp;&nbsp;<input type="date" name="endDate" class="form-control" value="${dto.endDate}" style="width: 30%;">
					</td>
				</tr>
				
				<tr>
					<td>첨&nbsp;&nbsp;&nbsp;&nbsp;부</td>
					<td> 
						<div class="img-grid"><img class="item img-add" src="${pageContext.request.contextPath}/resource/images/add_photo.png"></div> 
						<input type="file" name="selectFile" class="form-control" multiple="multiple" accept="image/*" style="display: none;">
					</td>
				</tr>
				<c:if test="${mode =='update'}">
					<c:forEach var = "vo" items="${listPhoto}">
						<tr>
							<td>첨부된 파일</td>
							<td > 
								<p>
									<a href="javascript:deleteFile('${vo.pNum}');"><i class="far fa-trash-alt"></i></a>
									${vo.savePhotoname}
								</p>
							</td>
						</tr>
					</c:forEach>
				</c:if>
		
			</table>
			
			<table class="table">
				<tr> 
					<td align="center">
						<button type="button" class="btn" onclick="sendOk();">${mode == "update" ? "수정" : "작성" }</button>
						<button type="reset" class="btn">다시입력</button>
						<c:choose>
							<c:when test="${mode == 'write'}">
								<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/event/list.do';">작성취소</button>
							</c:when>
							<c:when test="${mode == 'update'}">
								<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/event/article.do?page=${page}&eNum=${dto.eNum}';">수정취소</button>
							</c:when>						
						</c:choose>
						<c:if test="${mode=='update'}">
							<input type="hidden" name = "eNum" value = "${dto.eNum}">
							<input type="hidden" name = "page" value = "${page}">
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