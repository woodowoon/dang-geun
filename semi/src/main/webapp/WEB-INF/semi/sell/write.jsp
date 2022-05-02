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
.board {
	margin: 30px auto;
	width: 100%;
}
.img-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, 65px);
	grid-gap: 5px;
}

.img-grid .item {
    object-fit: cover; /* 가로세로 비율은 유지하면서 컨테이너에 꽉 차도록 설정 */
    width: 65px;
    height: 65px;
	cursor: pointer;
}

.img-box {
	max-width: 600px;
	padding: 5px;
	box-sizing: border-box;
	display: flex; /* 자손요소를 flexbox로 변경 */
	flex-direction: row; /* 정방향 수평나열 */
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
	const f = document.sellForm;
	let str;
	
	str = f.subject.value.trim();
	if(! str) {
		alert("제목을 입력하세요.");
		f.subject.focus();
		return;
	}
	
	str = f.price.value.trim();
	if(! str) {
		alert("가격을 입력하세요.");
		f.price.focus();
		return;
	}
	
	str = f.rCode.value.trim();
	if(! str) {
		alert("지역을 입력하세요.");
		f.rCode.focus();
		return;
	}
	
	str = f.content.value.trim();
	if(! str) {
		alert("내용을 입력하세요.");
		f.content.focus();
		return;
	}
	
	str = f.selectFile.value.trim();
	if(${mode == 'write'}) {
		if(! str) {
			alert("첨부파일이 없습니다.");
			f.selectFile.focus();
			return;
		}	
	} else if(${mode == 'update'}) {
		if(! str && $(".img-box").find("img") ) {
			alert("첨부파일이 없습니다.");
			f.selectFile.focus();
			return;
		}
	}
	
	f.action = "${pageContext.request.contextPath}/sell/${mode}_ok.do";
	f.submit();
}

<c:if test="${mode=='update'}">
	function deleteFile(pNum) {
		if( ! confirm("이미지를 삭제하시겠습니까 ? ")) {
			return false;
		}
		
		let query = "num=${dto.code}&pNum="+ pNum + "&page=${page}";
		let url = "${pageContext.request.contextPath}/sell/deletePhoto.do?" + query;
		location.href= url;
	}
</c:if>

$(function() {
	var sel_files = [];
	
	$("body").on("click", ".table-form .img-add", function(e) {
		$("form[name=sellForm] input[name=selectFile]").trigger("click");
	});
	
	$("form[name=sellForm] input[name=selectFile]").change(function() {
		if(! this.files) {
			let dt = new DataTransfer();
			for(file of sel_files) {
				dt.items.add(file);
			}
			document.sellForm.selectFile.files = dt.files;
			
			return false;
		}
		
		const fileArr = Array.from(this.files);
		
		fileArr.forEach((file, index) => {
			sel_files.push(file);
			
			const reader = new FileReader();
			const $img = $("<img>", {class:"item img-item"});
			$img.attr("data-filename", file.name);
			reader.onload = e => {
				$img.attr("src", e.target.result);
			};
			
			reader.readAsDataURL(file);
			
			$(".img-grid").append($img);
		});
		
		let dt = new DataTransfer();
		for(file of sel_files) {
			dt.items.add(file);
		}
		document.sellForm.selectFile.files = dt.files;
	});
	$("body").on("click", ".table-form .img-item", function(e) {
		if(! confirm("선택한 파일을 삭제하시겠습니까 ? ")) {
			return false;
		}
		
		let filename = $(this).attr("data-filename");
		
		for(let i = 0; i < sel_files.length; i++) {
			if(filename === sel_files[i].name) {
				sel_files.splice(i, 1);
				break;
			}
		}
		
		let dt = new DataTransfer();
		for(files of sel_files) {
			dt.items.add(file);
		}
		document.sellForm.selectFile.files = dt.files;
		
		$(this).remove();
	});
});

</script>

<jsp:include page="/WEB-INF/semi/layout/staticHeader.jsp"/>
</head>
<body>

<header>
	<jsp:include page="/WEB-INF/semi/layout/header.jsp"/>
</header>

<main>
	<div class="body-container" style="width: 700px;">
	<div class="board">
		<div class="title">
			<h3><i class="fas fa-chalkboard-teacher"></i> 중고거래 글${mode=='write' ? '쓰기' : '수정'}</h3>
		</div>
        
		<form name="sellForm" method="post" enctype="multipart/form-data">
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
					<td>가&nbsp;&nbsp;&nbsp;&nbsp;격</td>
					<td> 
						<input type="text" name="price" maxlength="50" class="form-control" value="${dto.price}">
					</td>
				</tr>
				<tr> 
					<td>지&nbsp;&nbsp;&nbsp;&nbsp;역</td>
					<td> 
						<select name="rCode" class="form-select" style="width: 15%;">
							<c:forEach var="dto" items="${region}">
								<option value="${dto.rCode}" ${sessionScope.member.rCode == dto.rCode ? "selected='selected'" : ""}>${dto.rName}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr> 
					<td valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
					<td> 
						<textarea name="content" class="form-control">${dto.content}</textarea>
					</td>
				</tr>
		        <tr>
		            <td>이미지</td>
		            <td>
		            	<div class="img-grid"><img class="item img-add" src="${pageContext.request.contextPath}/resource/images/add_photo.png"></div>
		            	<input type="file" name="selectFile" accept="image/*" multiple="multiple" style="display: none;" class="form-control">
		            </td>
		        </tr>
		        
		        <c:if test="${mode=='update'}">
		        	<tr>
		        		<td>등록이미지</td>
		        		<td> 
							<div class="img-box">
								<c:forEach var="vo" items="${listPhoto}">
									<img src="${pageContext.request.contextPath}/uploads/notice/${vo.photoName}"
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
						<button type="button" class="btn" onclick="sendOk();">${mode=='write' ? '등록하기' : '수정하기'}</button>
						<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/sell/list.do';">${mode=='write' ? '등록취소' : '수정취소'}</button>
						<c:if test="${mode=='update'}">
							<input type="hidden" name="code" value="${dto.code}">
							<input type="hidden" name="page" value="${page}">
						</c:if>
					</td>
				</tr>
			</table>
		</form>
    </div>
	</div>
</main>


<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>
</body>

</html>