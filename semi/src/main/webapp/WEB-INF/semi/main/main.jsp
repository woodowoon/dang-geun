<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<style type="text/css">
.feature { position: relative; width: 1024px; height: 760px; margin: 0 auto; }
.feature img { position: absolute; }
main > div:first-child { background-color: #FBF7F2; }
main > div:nth-child(3) { background-color: #E6F3E6; }
.f01 img { right: -84px; bottom: 0; width: 804px; height: 685px;}
.f02 img { width: 532px; height: 684px; }
.f03 img { right: 0; width: 546px; height: 740px; }
.f04 img { width: 526px; height: 735px; }
.txt { position: absolute; }
.txt .title { font-size: 40px; line-height: 54px; font-weight: 700; color: #000; margin-bottom: 24px; }
.txt .desc { font-size: 16px; line-height: 24px; letter-spacing: -0.3px; }
.f01 .txt { top: 275px; left: 35px; }
.f02 .txt { top: 230px; right: 30px; }
.f03 .txt { top: 240px; left: 50px; }
.f04 .txt { top: 250px; right: 30px; }
.linkBtn {
	display: inline-block; 
	font-size: 18px; font-weight: 700;
	background-color: #F1F3F5;
    color: #212529;
    border: none;
    border-radius: 6px;
    text-decoration: none;
    padding: 15px 25px;
    margin-top: 32px;
}
.f03 .linkBtn { background-color: #e1e1e1; }
.linkBtn:hover { background-color: #E9ECEF; color: #212529; text-decoration: none; }
</style>
<title>당근마켓</title>

<jsp:include page="/WEB-INF/semi/layout/staticHeader.jsp"/>

</head>
<body>

<header>
    <jsp:include page="/WEB-INF/semi/layout/header.jsp"></jsp:include>
</header>

<main>
	<div>
	    <div class="feature f01">
	    	<img src="https://d1unjqcospf8gs.cloudfront.net/assets/home/main/3x/image-top-d6869a79bc4cb58ea59aa5a408decfdf4a4ba60ac639837081da12861083cdbb.webp " src="https://d1unjqcospf8gs.cloudfront.net/assets/home/main/3x/image-top-68ba12f0da7b5af9a574ed92ca8b3a9c0068db176b566dd374ee50359693358b.png">
	    	<div class="txt">
	    		<p class="title">당신 근처의<br>당근마켓</p>
	    		<p class="desc">중고 거래부터 동네 정보까지, 이웃과 함께해요.<br>
	    		가깝고 따뜻한 당신의 근처를 만들어요.</p>
	    	</div>
	    </div>
	</div>
	<div>
	    <div class="feature f02">
	    	<img src="https://d1unjqcospf8gs.cloudfront.net/assets/home/main/3x/image-1-cc678e9a217b96f5cb459f7f0684f5ba67706f9889801618b8cf879fbc2c0ea7.webp " src="https://d1unjqcospf8gs.cloudfront.net/assets/home/main/3x/image-1-39ac203e8922f615aa3843337871cb654b81269e872494128bf08236157c5f6a.png">
	    	<div class="txt">
	    		<p class="title">우리 동네<br>중고 직거래 마켓</p>
	    		<p class="desc">동네 주민들과 가깝고 따뜻한 거래를 지금 경험해보세요.</p>
	    		<a href="${pageContext.request.contextPath}/sell/list.do" class="linkBtn">중고거래 하기</a>
	    	</div>
	    </div>
	</div>
	<div>
	    <div class="feature f03">
	    	<img src="https://d1unjqcospf8gs.cloudfront.net/assets/home/main/3x/image-2-91a2286453bdf82dea16a7f0ee4ceb9dd325eae0e5a2a9967ba72c344bf8f2fc.webp " src="https://d1unjqcospf8gs.cloudfront.net/assets/home/main/3x/image-2-f286322ab98acedf914a05bf77e84c67dcb897c8ccb543e66f6afea9d366d06d.png">
	    	<div class="txt">
	    		<p class="title">이웃과 함께 하는<br>동네생활</p>
	    		<p class="desc">우리 동네의 다양한 이야기를 이웃과 함께 나누어요.</p>
	    		<a href="${pageContext.request.contextPath}/community/list.do" class="linkBtn">동네 커뮤니티</a>
	    	</div>
	    </div>
    </div>
    <div>
	    <div class="feature f04">
	    	<img src="https://d1unjqcospf8gs.cloudfront.net/assets/home/main/3x/image-3-5fd6fb61d603ab919a45566b2ea6b505c83a93ec218f34ddcd5cb482543e2317.webp " alt="내 근처에서 찾는 동네가게" src="https://d1unjqcospf8gs.cloudfront.net/assets/home/main/3x/image-3-0c8b631ac2294ac5a3b3e7a3a5580c3e68a3303ad2aded1e84aa57a2e1442786.png">
	    	<div class="txt">
	    		<p class="title">내 근처에서 찾는<br>동네가게</p>
	    		<p class="desc">우리 동네 가게를 찾고 있나요?<br>
	    		동네 주민이 남긴 진짜 후기를 함께 확인해보세요!</p>
	    	</div>
	    </div>
	</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/semi/layout/footer.jsp"></jsp:include>
</footer>

</body>
</html>