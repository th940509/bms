<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<head>
 <title>검색 도서 목록 페이지</title>
</head>
<body>
	<hgroup>
		<h1>"${searchWord }" 로 검색한 결과</h1>
	</hgroup>
	<h3 align="center">오늘의 책</h3>
	<section id="new_book">
		<div id="left_scroll">
			<a href='javascript:slide("left");'><img src="${contextPath}/resources/image/left.gif"></a>
		</div>
		<div id="carousel_inner">
			<ul id="carousel_ul">
			<c:forEach var="item2" items="${todayBookList }">
				<c:choose>
					<c:when test="${empty item2}">
						<li>
							<div id="book">
									<h1>제품이없습니다.</h1>
							</div>
						</li> 
					</c:when>
					<c:otherwise>
						<li>
							<div id="book">
							<a href="${contextPath}/goods/goodsDetail.do?goodsId=${item2.goodsId}">
							<img width="75" alt="상품명" src="${contextPath}/thumbnails.do?goodsId=${item2.goodsId}&fileName=${item2.goodsFileName}">
							</a>
							<div class="sort">[${item2.goodsSort }]</div>
							<div class="title">
								<a href="${contextPath}/goods/goodsDetail.do?goodsId=${item2.goodsId}">
								  ${item2.goodsTitle}
								</a>
							</div>
							<div class="writer">${item2.goodsWriter} | ${item2.goodsPublisher}</div>
							<div class="price">
								<span><fmt:formatNumber  value="${item2.goodsPrice}" type="number" /></span> <br>
								<fmt:formatNumber value="${item2.goodsSalesPrice}" type="number"  />
					            (<fmt:formatNumber value="${100-(item2.goodsSalesPrice*100) div item2.goodsPrice  }" pattern="00"/> %할인)
							</div>
							</div>
						</li>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			</ul>
		</div>
		<div id="right_scroll">
			<a href='javascript:slide("right");'><img  src="${contextPath}/resources/image/right.gif"></a>
		</div>
		<input id="hidden_auto_slide_seconds" type="hidden" value="0">
		<div class="clear"></div>
	</section>
	<table id="list_view"> <!-- 검색결과 -->
		<tbody>
		  <c:forEach var="item" items="${goodsList}"> 
			<tr align="center">
				<td class="goods_image">
					<a href="${contextPath}/goods/goodsDetail.do?goodsId=${item.goodsId}">
						   <img width="75" alt="" src="${contextPath}/thumbnails.do?goodsId=${item.goodsId}&fileName=${item.goodsFileName}">
					</a>
				</td>
				<td class="goods_description" >
					<h2>
						<a href="${contextPath}/goods/goodsDetail.do?goodsId=${item.goodsId}">${item.goodsTitle}</a>
					</h2>
					<div class="writer_press"  >${item.goodsWriter }저 &emsp; |
						&emsp; ${item.goodsPublisher } | &emsp; 
						<fmt:formatDate value="${item.goodsPublishedDate }" pattern="yyyy-MM-dd"/>  
					</div>
				</td>
				<td class="price">
					<span><fmt:formatNumber  value="${item.goodsPrice}" type="number" /></span> <br>
					<fmt:formatNumber value="${item.goodsSalesPrice}" type="number"  /><br>
		            (<fmt:formatNumber value="${100-(item.goodsSalesPrice*100) div item.goodsPrice  }" pattern="00"/>%할인)
				</td>
			</tr>
			</c:forEach>
			
		</tbody>
	</table>