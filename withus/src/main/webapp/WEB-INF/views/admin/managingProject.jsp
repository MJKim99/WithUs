<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>프로젝트 관리</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/CSS/admin/admin.css" />
</head>
<body>
    <nav>
      <ul class="nav-ul">
        <li class="nav-li"><a href="main">메인으로</a></li>
        <li class="nav-li now"><a href="#">펀딩 프로젝트 관리</a></li>
        <li class="nav-li"><a href="managingUser">사용자 관리</a></li>
        <li class="nav-li"><a href="managingNotice">글 관리</a></li>
        <li class="nav-end-li">${prc }</li>
        <li class="nav-end-li"><a href="${pageContext.request.contextPath }/logout">로그아웃</a></li>
      </ul>
    </nav>
    <main>
	<h1>프로젝트 관리</h1>
	<hr>
	<br>
	<table>
		<tr>
			<th>프로젝트 번호</th>
			<th>판매자</th>
			<th>분류</th>
			<th>제목</th>
			<th>등록일</th>
		</tr>
		<c:choose>
			<c:when test="${empty list || fn:length(list) == 0 }"></c:when>
			<c:otherwise>
				<c:forEach var="dto" items="${list }">
					<tr>
						<td>${dto.prnum }</td>
						<td>${dto.selid }</td>
						<td>${dto.category }</td>
						<td><a href="viewProject?prnum=${dto.prnum }" id="title">${dto.title }</a></td>						
						<td>${dto.regDate }</td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</table>
	<br>
	
	<div style="display: inline-block; width: 30%; text-align: center; margin: 0 22%;">
		<c:if test="${paging.startPage != 1 }">
			<a
				href="managingProject?nowPage=${paging.startPage - 1 }&cntPerPage=${paging.cntPerPage}">&lt;</a>
		</c:if>
		<c:forEach begin="${paging.startPage }" end="${paging.endPage }"
			var="p">
			<c:choose>
				<c:when test="${p == paging.nowPage }">
					<b>${p }</b>
				</c:when>
				<c:when test="${p != paging.nowPage }">
					<a href="managingProject?nowPage=${p }&cntPerPage=${paging.cntPerPage}">${p }</a>
				</c:when>
			</c:choose>
		</c:forEach>
		<c:if test="${paging.endPage != paging.lastPage}">
			<a
				href="managingProject?nowPage=${paging.endPage +1 }&cntPerPage=${paging.cntPerPage}">&gt;</a>
		</c:if>
	</div>
	
	
	</main>
</body>
</html>