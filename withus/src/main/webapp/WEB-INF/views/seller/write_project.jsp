<%@ page language="java" contentType="text/html; charset=UTF-8"

   pageEncoding="UTF-8"%>
   <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>스토리 작성</title>
<script src="${pageContext.request.contextPath }/ckeditor/ckeditor.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath }/CSS/header.css">
<script src="${pageContext.request.contextPath }JS/header.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath }/CSS/tailwind/public/tailwind.css">
<script src="${pageContext.request.contextPath }/CSS/tailwind/public/tailwind.config.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath }/CSS/writeProject.css">
</head>
<script>
   function chkSubmit() {
	   /* CKEDITOR.instances.editor.getData(); */
      frm = document.forms['frm'];

      var title = frm['title'].value.trim();
      
      var content = frm['content'].value.trim();
      var itemprice = frm['itemprice'].value.trim();
      var price = frm['price'].value.trim();
      var category = frm['category'].value.trim();
      var startDate = frm['startDate'].value.trim();
      var endDate = frm['endDate'].value.trim();

      if (title == "" || title == null) {
         alert("제목을 입력해주세요 !");
         frm['title'].focus();
         return false;
      }
      if (content == "" || content == null) {
         alert("내용을 입력해주세요 !");
         frm['content'].focus();
         return false;
      }
      if (itemprice == "" || itemprice == null) {
         alert("아이템 가격을 입력해주세요 !");
         frm['itemprice'].focus();
         return false;
      }
      if (price == "" || price == null) {
         alert("목표 금액을 입력해주세요 !");
         frm['price'].focus();
         return false;
      }
      if(parseInt(price)<=parseInt(itemprice)){
    	  alert("물품가격이 목표금액과 같거나 높을 수 없습니다 ! \n낮은 금액을 입력해주세요 !");
    	  frm['itemprice'].focus();
    	  return false;
      }
      if (category == "" || category == null) {
         alert("카테고리를 입력해주세요 !");
         frm['category'].focus();
         return false;
      }
      if (startDate == ""|| startDate == null) {
         alert("시작날짜를 입력해주세요 !");
         frm['startDate'].focus();
         return false;
      }
      if (endDate == "" || endDate == null) {
         alert("끝낼 날짜를 입력해주세요 !");
         frm['endDate'].focus();
         return false;
      }
      if(startDate>=endDate){
     	  alert("종료날짜보다 시작날짜가 같거나 작을 수 없습니다.");
     	  frm['endDate'].focus();
     	  return false;
       }
      
      return true;
   } // end chkSubmit()
   
   </script>

<body>
	<header>
		<%@include file="../main/header.jsp"%>
	</header>
	<div class="px-8 py-16">
	<p class="text-4xl text-black-700" id="story">스토리 작성</p>
	<form name="frm" action="write_projectOk" method="post"
		onsubmit="return chkSubmit()" enctype="multipart/form-data">

		<p class="font-black focus:ring-2 focus:ring-blue-600 py-4">판매자아이디 </p> <input  name="selid" value="${selid }" readonly class="form-input w-1/2 xl:w-1/5 h-10 "><br>
		<p class="font-black focus:ring-2 focus:ring-blue-600 py-4">제목</p> <input type="text" name="title" class="form-input w-1/2 xl:w-1/5 h-10 "/><br>
		<p class="font-black focus:ring-2 focus:ring-blue-600 py-4">썸네일</p> <input type="file" name="filename" class="form-input w-1/2 xl:w-1/5 h-10 "/><br>
		<p class="font-black focus:ring-2 focus:ring-blue-600 py-4">내용</p>
		<textarea name="content" id="content"></textarea>
		<script>
         CKEDITOR
               .replace(
                     'content',
                     {
                        width : '900px',
                        height : '250px',
                        filebrowserImageUploadUrl : '${pageContext.request.contextPath}/seller/imageUpload',
                        font_names : "맑은 고딕/Malgun Gothic;굴림/Gulim;돋움/Dotum;바탕/Batang;궁서/Gungsuh;Arial/Arial;Comic Sans MS/Comic Sans MS;Courier New/Courier New;Georgia/Georgia;Lucida Sans Unicode/Lucida Sans Unicode;Tahoma/Tahoma;Times New Roman/Times New Roman;MS Mincho/MS Mincho;Trebuchet MS/Trebuchet MS;Verdana/Verdana",
                        font_defaultLabel : "맑은 고딕/Malgun Gothic",
                        fontSize_defaultLabel : "12"

                     });
           // $('#submit').click(function(){
            //alert(CKEDITOR.instances.editor.getData())
       //  });
         
      </script>
		<p class="font-black py-4">물품 가격  </p> <input type="number" name="itemprice" class="form-input w-1/2 xl:w-1/5 h-10 "><label>&nbsp;원</label><br>
			<p class="font-black py-4">목표 금액 </p> <input type="number" name="price" class="form-input w-1/2 xl:w-1/5 h-10 "><label>&nbsp;원</label><br>
		<p class="font-black py-4">카테고리  <br>
        <select name="category" class="form-input w-1/2 xl:w-1/5 h-10">
			<option value="가전">가전</option>
			<option value="뷰티">뷰티</option>
			<option value="푸드">푸드</option>
			<option value="여행">여행</option>
		</select> <br>
		<c:set var="now" value="<%=new java.util.Date()%>" />
		<fmt:formatDate var="start" value="${now}" pattern="yyyy-MM-dd" />
		<p class="font-black py-4">시작일 <br> <input type="date" id="start" name="startDate" min="${start }"  class="form-input w-1/2 xl:w-1/5 h-10 "><br>
		<p class="font-black py-4">종료일 <br> <input type="date" id="end" name="endDate" min="${start }" class="form-input w-1/2 xl:w-1/5 h-10 "><br><br>
		<input class="float-left  flex flex-wrap justify-center content-center bg-indigo-500 hover:bg-indigo-600 focus:outline-none focus:ring-2 focus:ring-purple-600 focus:ring-opacity-50  py-4 text-white w-1/2 xl:w-1/5 h-10" type="submit" id="submit" value="등록하기"><br><br>
		
	</form>
	<button class="float-left  flex flex-wrap justify-center content-center bg-indigo-500 hover:bg-indigo-600 focus:outline-none focus:ring-2 focus:ring-purple-600 focus:ring-opacity-50   text-white w-1/2 xl:w-1/5 h-10"  onclick="location.href='/seller/myProject'">목록보기</button>
	</div>
</body>
</html>