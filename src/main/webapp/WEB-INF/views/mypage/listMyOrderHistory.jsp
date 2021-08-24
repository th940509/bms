<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<!DOCTYPE html >
<html>
<head>
<meta   charset="utf-8">
<script>

	function search_order_history(fixedSearchPeriod){ //url에 표시됨.
		
		location.href="${contextPath}/mypage/listMyOrderHistory.do?fixedSearchPeriod="+fixedSearchPeriod;
	    
	}
	
	function fn_enable_detail_search(r_search){
	
		var frm_order_history = document.frm_order_history;
		s_search_types = frm_order_history.s_search_types;
		t_search_word = frm_order_history.t_search_word;
		btn_search    = frm_order_history.btn_search;
		t_bYear    = frm_order_history.bYear;
		t_bMonth    = frm_order_history.bMonth;
		t_bDay    = frm_order_history.bDay;
		t_eYear    = frm_order_history.eYear;
		t_eMonth    = frm_order_history.eMonth;
		t_eDay    = frm_order_history.eDay;
		
		
		if (r_search.value == 'detail_search'){
			s_search_types.disabled = false;
			t_search_word.disabled = false;
			btn_search.disabled    = false;
			t_bYear.disabled = false;
			t_bMonth.disabled = false;
			t_bDay.disabled = false;
			t_eYear.disabled = false;
			t_eMonth.disabled = false;
			t_eDay.disabled = false;
		}
		else if (r_search.value == 'simple_search'){
			s_search_types.disabled  = true;
			t_search_word.disabled  = true;
			btn_search.disabled     = true;
			t_bYear.disabled = true;
			t_bMonth.disabled = true;
			t_bDay.disabled = true;
			t_eYear.disabled = true;
			t_eMonth.disabled = true;
			t_eDay.disabled = true;
		}
	}
	
	function fn_detail_search(){
		var frm_order_history = document.frm_order_history;
		search_type = frm_order_history.s_search_types.value;
		search_word = frm_order_history.t_search_word.value;
		bYear    = frm_order_history.bYear.value;
		bMonth    = frm_order_history.bMonth.value;
		bDay    = frm_order_history.bDay.value;
		eYear    = frm_order_history.eYear.value;
		eMonth    = frm_order_history.eMonth.value;
		eDay    = frm_order_history.eDay.value;
		
		if (bYear <10) bYear = "0" + bYear;
		if (bMonth <10) bMonth = "0" + bMonth;
		if (bDay <10) bDay = "0" + bDay;
		if (eYear <10) eYear = "0" + eYear;
		if (eMonth <10) eMonth = "0" + eMonth;
		if (eDay <10) eDay = "0" + eDay;
		
		var url = "${contextPath}/mypage/listMyOrderHistory.do?";
			url += "beginDate=" + bYear + "-" + bMonth + "-" + bDay;
			url += "&endDate=" + eYear + "-" + eMonth + "-" + eDay;
			url += "&search_type=" + search_type;
			url += "&search_word=" + search_word;
			
			location.href=url;
	}
	

	
	function fn_cancel_order(orderId){
		
		var answer = confirm("주문을 취소하시겠습니까?");
		
		if (answer) {
		    location.href = "${contextPath}/mypage/cancelMyOrder.do?orderId=" + orderId;
		}
		
	}

</script>
</head>
<body>
	<H3>주문 배송 조회</H3>
	<form  name="frm_order_history" method="post" action="${contextPath }/mypagelistMyOrderHistory.do">	
		<table>
			<tbody>
				<tr>
					<td>
						<input type="radio" name="r_search_option"  value="simple_search" checked onClick="fn_enable_detail_search(this)"/> 간단조회 &nbsp;&nbsp;&nbsp;
						<input type="radio" name="r_search_option" value="detail_search" onClick="fn_enable_detail_search(this)" /> 상세조회  &nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				<tr>
					<td>
					  <select name="curYear" disabled>
					    <c:forEach var="i" begin="0" end="5" >
					      <c:choose>
					        <c:when test="${endYear==endYear-i }">
					          <option value="${endYear}" selected>${endYear}</option>
					        </c:when>
					        <c:otherwise>
					          <option value="${endYear-i }">${endYear-i }</option>
					        </c:otherwise>
					      </c:choose>
					    </c:forEach>
					</select>년 
					<select name="curMonth" disabled>
						 <c:forEach   var="i" begin="1" end="12">
					      <c:choose>
					        <c:when test="${endMonth==i }">
					          <option value="${i }"  selected>${i }</option>
					        </c:when>
					        <c:otherwise>
					          <option value="${i }">${i }</option>
					        </c:otherwise>
					      </c:choose>
					    </c:forEach>					
					</select>월
					
					 <select name="curDay" disabled>
					  <c:forEach   var="i" begin="1" end="31">
					      <c:choose>
					        <c:when test="${endDay==i }">
					          <option value="${i }"  selected>${i }</option>
					        </c:when>
					        <c:otherwise>
					          <option value="${i }">${i }</option>
					        </c:otherwise>
					      </c:choose>
					    </c:forEach>	
					</select>일  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
					<a href="javascript:search_order_history('today')">
					   <img src="${contextPath}/resources/image/btn_search_one_day.jpg">
					</a>
					<a href="javascript:search_order_history('one_week')">
					   <img src="${contextPath}/resources/image/btn_search_1_week.jpg">
					</a>
					<a href="javascript:search_order_history('two_week')">
					   <img src="${contextPath}/resources/image/btn_search_2_week.jpg">
					</a>
					<a href="javascript:search_order_history('one_month')">
					   <img src="${contextPath}/resources/image/btn_search_1_month.jpg">
					</a>
					<a href="javascript:search_order_history('two_month')">
					   <img src="${contextPath}/resources/image/btn_search_2_month.jpg">
					</a>
					<a href="javascript:search_order_history('three_month')">
					   <img src="${contextPath}/resources/image/btn_search_3_month.jpg">
					</a>
					<a href="javascript:search_order_history('four_month')">
					   <img src="${contextPath}/resources/image/btn_search_4_month.jpg">
					</a>
					&nbsp;까지 조회
					</td>
				</tr>
				<tr>
				  <td>
					조회 기간 &nbsp;&nbsp;
					<select name="bYear" disabled>
					 <c:forEach   var="i" begin="0" end="5">
					      <c:choose>
					        <c:when test="${beginYear==beginYear-i}">
					          <option value="${beginYear-i}" selected>${beginYear-i}</option>
					        </c:when>
					        <c:otherwise>
					          <option value="${beginYear-i}">${beginYear-i}</option>
					        </c:otherwise>
					      </c:choose>
					    </c:forEach>
					</select>년 
					<select name="bMonth" disabled >
						 <c:forEach var="i" begin="1" end="12">
					      <c:choose>
					        <c:when test="${beginMonth==i}">
					          <option value="${i }"  selected>${i}</option>
					        </c:when>
					        <c:otherwise>
					          <c:choose>
					            <c:when test="${i <10}">
					              <option value="0${i}">0${i}</option>
					            </c:when>
					            <c:otherwise>
					              <option value="${i}">${i}</option>
					            </c:otherwise>
					          </c:choose>
					        </c:otherwise>
					      </c:choose>
					    </c:forEach>					
					</select>월
					 <select name="bDay" disabled >
					  <c:forEach var="i" begin="1" end="31">
					      <c:choose>
					        <c:when test="${beginDay==i }">
					          <option value="${i}"  selected>${i}</option>
					        </c:when>
					        <c:otherwise>
					          <c:choose>
					            <c:when test="${i <10}">
					              <option value="0${i}">0${i}</option>
					            </c:when>
					            <c:otherwise>
					              <option value="${i}">${i}</option>
					            </c:otherwise>
					          </c:choose>
					        </c:otherwise>
					      </c:choose>
					    </c:forEach>	
					</select>일  &nbsp; ~
					
					<select name="eYear" disabled >
					 <c:forEach   var="i" begin="0" end="5">
					      <c:choose>
					        <c:when test="${endYear==endYear-i }">
					          <option value="${2021-i}" selected>${2021-i }</option>
					        </c:when>
					        <c:otherwise>
					          <option value="${2021-i}">${2021-i}</option>
					        </c:otherwise>
					      </c:choose>
					    </c:forEach>
					</select>년 
					<select name="eMonth" disabled >
						 <c:forEach   var="i" begin="1" end="12">
					      <c:choose>
					        <c:when test="${endMonth==i}">
					          <option value="${i}"  selected>${i}</option>
					        </c:when>
					        <c:otherwise>
					          <c:choose>
					            <c:when test="${i <10}">
					              <option value="0${i}">0${i}</option>
					            </c:when>
					            <c:otherwise>
					             <option value="${i}">${i}</option>
					            </c:otherwise>
					          </c:choose>
					        </c:otherwise>
					      </c:choose>
					    </c:forEach>					
					</select>월
					 <select name="eDay" disabled >
					  <c:forEach var="i" begin="1" end="31">
					      <c:choose>
					        <c:when test="${endDay==i}">
					          <option value="${i}" selected>${i}</option>
					        </c:when>
					        <c:otherwise>
					          <c:choose>
					            <c:when test="${i<10}">
					              <option value="0${i}">0${i}</option>
					            </c:when>
					            <c:otherwise>
					            <option value="${i}">${i}</option>
					            </c:otherwise>
					          </c:choose>
					        </c:otherwise>
					      </c:choose>
					    </c:forEach>	
					</select>						 
				  </td>
				</tr>
				<tr>
				  <td>
				    <select name="s_search_types" disabled>
						<option value="2015" selected>전체</option>
						<option value="2014">수령자</option>
						<option value="2013">주문자</option>
						<option value="2012">주문번호</option>
					</select>
					<input type="text" size="30" id="t_search_word" disabled/>  
					<input type="button"  value="조회" id="btn_search" onclick="fn_detail_search()" disabled/>
				  </td>
				</tr>
			</tbody>
		</table>
		<div class="clear">
	</div>
</form>	
<div class="clear"></div>
<table class="list_view">
		<tbody align=center >
			<tr style="background:#33ff00" >
				<td class="fixed" >주문번호</td>
				<td class="fixed">주문일자</td>
				<td>주문내역</td>
				<td>주문금액/수량</td>
				<td>주문상태</td>
				<td>주문자</td>
				<td>수령자</td>
				<td>주문취소</td>
			</tr>
   <c:choose>
     <c:when test="${empty myOrderHistList }">			
			<tr>
		       <td colspan=8 class="fixed">
				  <strong>주문한 상품이 없습니다.</strong>
			   </td>
		     </tr>
	 </c:when>
	 <c:otherwise> 
     <c:forEach var="item" items="${myOrderHistList }" varStatus="i">
        <c:choose>
          <c:when test="${item.orderId != pre_orderId }">   
            <tr>       
				<td>
				  <a href="${contextPath}/mypage/myOrderDetail.do?orderId=${item.orderId }"><strong>${item.orderId }</strong>  </a>
				</td>
				<td >
				 <strong><fmt:formatDate value="${item.payOrderTime }" pattern="yyyy-MM-dd"/> </strong> 
				</td>
				<td> 
				    <strong>
					   <c:forEach var="item2" items="${myOrderHistList}" varStatus="j">
				          <c:if  test="${item.orderId ==item2.orderId}" >
				            <a href="${contextPath}/goods/goodsDetail.do?goodsId=${item2.goodsId }">${item2.goodsTitle }</a><br>
				         </c:if>   
					 </c:forEach>
					 </strong>
				</td>
				<td>
				   <strong>
				      <c:forEach var="item2" items="${myOrderHistList}" varStatus="j">
				          <c:if  test="${item.orderId ==item2.orderId}" >
				             ${item.goodsSalesPrice * item.orderGoodsQty }원 &emsp;/ &emsp; ${item.orderGoodsQty }(개)<br>
				         </c:if>   
					 </c:forEach>
				   </strong>
				</td>
				<td>
				  <strong>
				    <c:choose>
					    <c:when test="${item.deliveryState=='deliveryPrepared'}">
					       배송준비중
					    </c:when>
					    <c:when test="${item.deliveryState=='delivering'}">
					       배송중
					    </c:when>
					    <c:when test="${item.deliveryState=='finishedDelivering'}">
					       배송완료
					    </c:when>
					    <c:when test="${item.deliveryState=='cancelOrder'}">
					       주문취소
					    </c:when>
					    <c:when test="${item.deliveryState=='returningGoods'}">
					       반품
					    </c:when>
				  </c:choose>
				  </strong>
				</td>
				<td>
				 <strong>${item.ordererName }</strong> 
				</td>
				<td>
					<strong>${item.receiverName }</strong>
				</td>
				<td>
			     <c:choose>
			   <c:when test="${item.deliveryState=='deliveryPrepared'}">
			       <input type="button" onClick="fn_cancel_order('${item.orderId}')" value="주문취소"  />
			   </c:when>
			   <c:otherwise>
			      <input type="button" onClick="fn_cancel_order('${item.orderId}')" value="주문취소" disabled />
			   </c:otherwise>
			  </c:choose>
			    </td>
			</tr>
			<c:set var="pre_orderId" value="${item.orderId }" />
		   </c:when>	
	  </c:choose>		
	</c:forEach>
	</c:otherwise>
  </c:choose>			   
		</tbody>
	</table>
	<div class="clear"></div>
</body>
</html>