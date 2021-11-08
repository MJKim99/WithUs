<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%-- JSTL 버젼으로 바뀌니, import 의 번잡함 사라진다! JAVA 변수 선언도 사라진다! --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>프로필 수정</title>
<link href="${pageContext.request.contextPath }CSS/profileMgt.css" rel="stylesheet" type="text/css">

<style>
body {
	text-align: center;
}
@font-face {
    font-family: 'Pretendard-Light';
    src: url('https://cdn.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Light.woff') format('woff');
    font-weight: 300;
    font-style: normal;
}
div,label, h1, h2, h3{
	font-family: 'Pretendard-Light';
	font-size: 18px;
}
input{
	font-family: 'Pretendard-Light';
}


</style>
</head>

  <script type="text/javascript">
    function pwValid() {
      var p1 = document.getElementById('password1').value;
      var p2 = document.getElementById('password2').value;
      var p3 = document.getElementById('password3').value;
     
      if(p1 = null && p2 = null) {
        return true;
      } else if( p1 != p2 ){
        alert("비밀번호가 일치 하지 않습니다");
        return false;
      }else if(p1 = p2){
    	  p3 = p1;
    	  return true;
      }

    }
  </script>
  
<body>
<header>
		<%@include file="header.jsp"%>
	</header>
	
<h1> 프로필 관리</h1> <br>

<div>사용자 이메일 : ${prc }</div><br>

<input type="hidden" name="email" value="${prc }">

<h3>이름 : ${user.name }</h3>

<div style="font-size: 10px">※ 개명을 했을 경우 위드어스 문의를 이용해주세요!※</div>

<div>
<form  name="frm" action="profileUpdateOk" onsubmit="return pwValid()" method="POST"><br>

<div>
<label >주소 </label><br>
<input class="input_1" type="text" name="addr" placeholder="주소 입력" value="${user.addr }">
</div>
<br>


<div>
<label >핸드폰 번호 </label><br>
<input class="input_1" type="text" name="phone" value="${user.phone }">
</div>
<br>


<div >
<label>비밀번호 변경 </label> <br>
<input class="input_1" type="password" name="password1" id="password1" >
</div>
<br>

<div >
<label >비밀번호 확인  </label><br>
<input class="input_1" type="password" name="password2" id="password2">
<input class="input_1" type="hidden" name="pw" id="password3" value="${user.pw }">
</div>
<br><br>
<input class="input_2"  type="submit" value="수정">

<!-- form 안의 button 은 type=button 이 명시 안되면 submit 동작을 하니 주의! -->

</form>
</div>
<br>

<form action="deleteUser" method="POST">
<input class="input_2" type="submit" value="회원탈퇴" ></form>

<br><br><br>

<jsp:include page="/WEB-INF/views/main/footer.jsp"></jsp:include>
	
</body>
</html>