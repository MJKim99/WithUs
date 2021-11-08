<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%-- JSTL 버젼으로 바뀌니, import 의 번잡함 사라진다! JAVA 변수 선언도 사라진다! --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>프로필 수정</title>

<link href="${pageContext.request.contextPath }CSS/profileMgt.css"
	rel="stylesheet" type="text/css">

<style>
@font-face {
	font-family: 'Pretendard-Light';
	src:
		url('https://cdn.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Light.woff')
		format('woff');
	font-weight: 300;
	font-style: normal;
}
form,div,h1,h2,h3,input{
font-family: 'Pretendard-Light';

}

</style>

</head>



<body>

	<main>
		<div>
			<div>
				<a href="/main"><img width="100px" height="80px"
					style="display: block; margin: auto;"
					src="${pageContext.request.contextPath }/images/logo.png"></a>
			</div>
		</div>
		<hr>
		<div class="container">
			<h1 style="text-align: center">회원 탈퇴</h1><br><br>
			<form name="frm" action="deleteUserOk" onsubmit="return pwValid()"
				method="POST" style="text-align: center">
	
				<h3>${prc }<br> <input type="hidden" name="email"
					value="${prc }"></h3><br>
				<h3>${user.name }</h3>
				<br><br>
				아직 여러분을 만나지 못한 다양한 아이템들이 기다리고 있습니다 <br> 정말 탈퇴하시겠습니까? 탈퇴 후에는 모든
				정보가 사라집니다. <br><br><br>
				
				<table class="input_table">
				<tr>
					<td>기존 비밀번호 입력 : </td>
					<td class="input_pw"><input type="password" name="pw" id="password1"></td>
				</tr>
				<tr>
					<td>비밀번호 확인 : </td>
					<td class="input_pw"><input type="text" name="pw2" id="password2" value="${user.pw }"></td>
				</tr>
				</table>
				<br><br>
				<input type="hidden" name="pw3" id="password3" value="${user.pw }">
				<input type="submit" value="회원탈퇴" class="delete_button">
			</form>
		</div>
		<jsp:include page="/WEB-INF/views/main/footer.jsp"></jsp:include>



	</main>


</body>
</html>