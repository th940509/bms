<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<%request.setCharacterEncoding("UTF-8");%>  


 <c:if test="${isLogOn eq true }">
	<c:if test="${nickname ne null and profile_image ne null}">
	                <div align="right" >
	                <img src="${contextPath}/resources/image/kakaobtn.png" style="width:10px;">
	                <a style="color:#8B6331;text-decoration:none;font-size:0.4em;"class="link_path">KAKAO계정 로그인 '${nickname}님' 접속중 </a>
	                </div>
     </c:if>
</c:if>
<c:if test="${isLogOn eq true }">
	<c:if test="${naverusername ne null}">
	                <div align="right" >
	                <img src="${contextPath}/resources/image/naver_btn2.png" style="width:10px;">
	                <a style="color:#369F36;text-decoration:none;font-size:0.4em;"class="link_path">NAVER계정 로그인 '${naverusername}님' 접속중 </a>
	                </div>
    </c:if>
</c:if>
	
		
            
<div id="ad_main_banner">
	<ul class="bjqs"> <!-- 맨 위 1번째 배너 -->
	    <li><img width="775" height="145" src="${contextPath}/resources/image/main_banner01.jpg"></li>
		<li><img width="775" height="145" src="${contextPath}/resources/image/main_banner02.jpg"></li>
		<li><img width="775" height="145" src="${contextPath}/resources/image/main_banner03.jpg"></li>
	</ul>
</div>
<div class="main_book">
   <c:set var="goodsCount" value="0" />
	<h3>베스트셀러 Bestseller</h3>
	<div align="right"><font size="3"> <a href="${contextPath}/goods/searchGoods.do?searchWord=bestseller">more</a></font></div>
	<c:forEach var="item" items="${goodsMap.bestseller }">
	   <c:set var="goodsCount" value="${goodsCount+1 }" />
		<div class="book">
			<a href="${contextPath}/goods/goodsDetail.do?goodsId=${item.goodsId }"><img class="link"  src="${contextPath}/resources/image/1px.gif"> </a> 
			<img width="121" height="154" src="${contextPath}/thumbnails.do?goodsId=${item.goodsId}&fileName=${item.goodsFileName}">
			<div class="title">${item.goodsTitle }</div>
			<div class="price"><fmt:formatNumber  value="${item.goodsPrice}" type="number" var="goodsPrice" />${goodsPrice}원</div>
		</div>
  </c:forEach>
</div>
<div class="clear"></div>
<div id="ad_sub_banner"> <!-- 2번째 배너 -->
	<img width="770" height="117" src="${contextPath}/resources/image/sub_banner01.jpg">
</div>
<div class="main_book" >
<c:set  var="goodsCount" value="0" />
 <h3>신간 Newbook</h3>
 <div align="right"><font size="3"> <a href="${contextPath}/goods/searchGoods.do?searchWord=newbook">more</a></font></div>
 <c:forEach var="item" items="${goodsMap.newbook }" >
   <c:set var="goodsCount" value="${goodsCount+1 }" />
   <div class="book">
   	<a href="${contextPath}/goods/goodsDetail.do?goodsId=${item.goodsId }">
   	 <img class="link"  src="${contextPath}/resources/image/1px.gif">
   	</a>
   <img width="121" height="154" src="${contextPath}/thumbnails.do?goodsId=${item.goodsId}&fileName=${item.goodsFileName}">
   <div class="title">${item.goodsTitle }</div>
   <div class="price"><fmt:formatNumber  value="${item.goodsPrice}" type="number" var="goodsPrice" />${goodsPrice}원</div>
   </div>
   
 </c:forEach>
</div>

<div class="clear"></div>
<div id="ad_sub_banner"> <!-- 3번째 배너 -->
	<img width="770" height="117" src="${contextPath}/resources/image/sub_banner02.jpg">
</div>


<div class="main_book" >
<c:set var="goodsCount" value="0" />
 <h3>스테디셀러 Steadyseller</h3>
 <div align="right"><font size="3"> <a href="${contextPath}/goods/searchGoods.do?searchWord=steadyseller">more</a></font></div>
  <c:forEach var="item" items="${goodsMap.steadyseller }" >
  <c:set var="goodsCount" value="${goodsCount+1 }" />
  <div class="book">
   <a href="${contextPath}/goods/goodsDetail.do?goodsId=${item.goodsId }">
    <img class="link"  src="${contextPath}/resources/image/1px.gif"> 
   </a>
   <img width="121" height="154" src="${contextPath}/thumbnails.do?goodsId=${item.goodsId}&fileName=${item.goodsFileName}">
   <div class="title">${item.goodsTitle }</div>
   <div class="price">
    <fmt:formatNumber  value="${item.goodsPrice}" type="number" var="goodsPrice" />${goodsPrice}원</div>
   </div>
 </c:forEach>
</div>

   
   