<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<c:if test="${message=='cancel_order'}">
	<script>
		window.onload=function() {
			alert("주문을 취소했습니다.");
		}
	</script>
</c:if>
<script>

	function fn_cancel_order(orderId){
		
		var answer = confirm("주문을 취소하시겠습니까?");
		
		if (answer) {
			var formObj = document.createElement("form");
			var i_orderId = document.createElement("input");
			
		    i_orderId.name = "orderId";
		    i_orderId.value = orderId;
			
		    formObj.appendChild(i_orderId);
		    document.body.appendChild(formObj); 
		    formObj.method="post";
		    formObj.action="${contextPath}/mypage/cancelMyOrder.do";
		    formObj.submit();	
		}
		
	}

</script>
<link href="${contextPath }/resources/css/myStyle.css" rel="stylesheet" />
</head>
<body>
<h1>최근주문내역<a href="#"> <img src="${contextPath}/resources/image/btn_more_see.jpg"></a></h1>
<table class="list_view">
	<tbody align=center >
		<tr style="background:#33ff00" >
			<td>주문번호</td>
			<td>주문일자</td>
			<td>주문상품</td>
			<td>주문상태</td>
			<td>주문취소</td>
		</tr>
      <c:choose>
         <c:when test="${ empty myOrderList  }"> <!-- 주문내역이 없을때 -->
		  <tr>
		    <td colspan=5 class="fixed">
				  <strong>주문한 상품이 없습니다.</strong> <!-- 굵은 텍스트 표현 -->
		    </td>
		  </tr>
        </c:when>
        <c:otherwise> <!-- 주문내역이 있을때 -->
	      <c:forEach var="item" items="${myOrderList }"  varStatus="i"> <!-- 반복문 -->
	       <c:choose> 
              <c:when test="${ preOrderId != item.orderId}"> <!--  <c:set  var="pre_orderId" value="${item.orderId}" /> -->
                <c:choose>
	              <c:when test="${item.deliveryState=='deliveryPrepared' }">
	                <tr  bgcolor="lightgreen">    
	              </c:when>
	              <c:when test="${item.deliveryState=='finishedDelivering' }">
	                <tr  bgcolor="lightgray">    
	              </c:when>
	              <c:otherwise>
	                <tr  bgcolor="orange">   
	              </c:otherwise>
	            </c:choose> 
            <tr>
             <td> <!-- 주문번호 -->
		       <a href="${contextPath}/mypage/myOrderDetail.do?orderId=${item.orderId }"><span>${item.orderId }</span>  </a> <!-- 주문번호 클릭 시 myOrderDetail.do로 이동 -->
		     </td>
		     <!-- 주문일자 -->
		    <td><fmt:formatDate value="${item.payOrderTime }" pattern="yyyy-MM-dd"/></td>
		    <!-- 주문상품 -->
			<td align="center">
			   <strong>
			      <c:forEach var="item2" items="${myOrderList}" varStatus="j"> <!-- 2번째 반복문 -->
			          <c:if  test="${item.orderId ==item2.orderId}" > <!-- 주문번호가 같다면 -->
			            <a href="${contextPath}/goods/goodsDetail.do?goodsId=${item2.goodsId }">${item2.goodsTitle }/${item.orderGoodsQty }개</a><br> <!-- 상품 이름 / 주문 수량 -->
			         </c:if>   
				 </c:forEach>
				</strong>
			</td>
			<td>
			  <c:choose>
			    <c:when test="${item.deliveryState=='deliveryPrepared' }">
			       <div class="badge badge-info badge-pill">배송준비중</div>
			    </c:when>
			    <c:when test="${item.deliveryState=='delivering' }">
			       <div class="badge badge-secondary badge-pill">배송중</div>
			    </c:when>
			    <c:when test="${item.deliveryState=='finishedDelivering' }">
			       <div class="badge badge-primary badge-pill">배송완료</div>
			    </c:when>
			    <c:when test="${item.deliveryState=='cancelOrder' }">
			       <div class="badge badge-danger badge-pill">주문취소</div>
			    </c:when>
			    <c:when test="${item.deliveryState=='returningGoods' }">
			       <div class="badge badge-yellow badge-pill">반품완료</div>
			    </c:when>
			  </c:choose>
			</td>
			<td>
			  <c:choose>
			   <c:when test="${item.deliveryState=='deliveryPrepared'}">
			       <input  type="button" onClick="fn_cancel_order('${item.orderId}')" value="주문취소"  />
			   </c:when>
			   <c:otherwise>
			      <input  type="button" onClick="fn_cancel_order('${item.orderId}')" value="주문취소" disabled />
			   </c:otherwise>
			  </c:choose>
			</td>
			</tr>
          <c:set  var="pre_orderId" value="${item.orderId}" />
           </c:when>
      </c:choose>
	   </c:forEach>
	  </c:otherwise>
    </c:choose> 	    
</tbody>
</table>

<br><br><br>	
<h1>나의 정보
    <a href="#"> <img src="${contextPath}/resources/image/btn_more_see.jpg" /></a>
</h1>
<table style="border: 0; width: 100%;">
  <tr>
    <td>이메일:</td>
    <td><strong>${memberInfo.email1 }@${memberInfo.email2 }</strong></td>
   </tr>
   <tr>
    <td>전화번호 </td>
    <td><strong>${memberInfo.hp1 }-${memberInfo.hp2}-${memberInfo.hp3 }</strong></td>
   </tr>
   <tr>
    <td>주소 </td>
    <td>
		도로명:  &nbsp;&nbsp; <strong>${memberInfo.roadAddress }</strong>  <br>
		지번:   &nbsp;&nbsp; <strong>${memberInfo.jibunAddress }</strong> 
   </td>
   </tr>
</table>
</body>
</html>
