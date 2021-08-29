package com.bms.order.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bms.common.util.CommonUtil;
import com.bms.goods.dto.GoodsDTO;
import com.bms.goods.service.GoodsService;
import com.bms.member.dto.MemberDTO;
import com.bms.order.dto.OrderDTO;
import com.bms.order.service.OrderService;

@Controller("orderController")
@RequestMapping(value="/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private GoodsService goodsService;
	
	
	@RequestMapping(value="/orderEachGoods.do" ,method = RequestMethod.POST)
	public ModelAndView orderEachGoods(@ModelAttribute("orderDTO") OrderDTO odto, HttpServletRequest request)  throws Exception{
		                            //model.addAttribute("orderDTOr", odto);랑 같은 의미?
		// goodDetail.jsp에서 Post로 보냄
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		
		OrderDTO orderDTO;
		Boolean isLogOn = (Boolean)session.getAttribute("isLogOn"); 
		String action = (String)session.getAttribute("action"); //${contextPath}/order/orderEachGoods.do";
		
		if (isLogOn==null || isLogOn==false) {		// 로그인이 안되어 있는 경우				
			session.setAttribute("orderInfo", odto); 
			session.setAttribute("action", "/order/orderEachGoods.do");
			return new ModelAndView("redirect:/member/loginForm.do");
		}
		else { // 로그인이 되어 있는 경우
			 if (action!=null && action.equals("/order/orderEachGoods.do")) {
				orderDTO=(OrderDTO)session.getAttribute("orderInfo");
				session.removeAttribute("action");
			 }
			 else {
				 orderDTO = odto;
			 }
		 }
		
		ModelAndView mv = new ModelAndView();  		
		
		String goodsId = String.valueOf(orderDTO.getGoodsId());
		Map<String, Object> goodsOrderList = new HashMap<String, Object>();
		goodsOrderList.put("goodsInfo", goodsService.goodsDetail(goodsId));
		
		mv.setViewName("/order/orderEachGoods");
		List<OrderDTO> myOrderList = new ArrayList<OrderDTO>();
		myOrderList.add(orderDTO);
		MemberDTO memberInfo = (MemberDTO)session.getAttribute("memberInfo");
		
		
		session.setAttribute("goodsOrderList", goodsOrderList);
		session.setAttribute("myOrderList", myOrderList);
		session.setAttribute("orderer", memberInfo);
		
		return mv;
		
	}
	
	
	@RequestMapping(value="/payToOrderGoods.do")
	public ModelAndView payToOrderGoods(@RequestParam Map<String, String> receiverMap , HttpServletRequest request) throws Exception{
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/order/payToOrderGoods");
		
		HttpSession session = request.getSession();
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("orderer");
		String memberId =  "";
		if (memberDTO != null) 
			memberId = memberDTO.getMemberId();
		
		String ordererName = "";
		if (memberDTO != null)
			ordererName = memberDTO.getMemberName();
		
		String ordererHp = memberDTO.getHp1() + "-" + memberDTO.getHp2() + "-" + memberDTO.getHp3();
		List<OrderDTO> myOrderList=(List<OrderDTO>)session.getAttribute("myOrderList");
		
		Map<String, Object> goodsOrderList=(Map<String, Object>)session.getAttribute("goodsOrderList");
		
		for (int i=0; i<myOrderList.size(); i++){
			OrderDTO orderDTO = (OrderDTO)myOrderList.get(i);
			orderDTO.setMemberId(memberId);
			orderDTO.setOrdererName(ordererName);
			orderDTO.setReceiverName(receiverMap.get("receiverName"));
			orderDTO.setReceiverHp1(receiverMap.get("receiverHp1"));
			orderDTO.setReceiverHp2(receiverMap.get("receiverHp2"));
			orderDTO.setReceiverHp3(receiverMap.get("receiverHp3"));
			orderDTO.setReceiverTel1(receiverMap.get("receiverTel1"));
			orderDTO.setReceiverTel2(receiverMap.get("receiverTel2"));
			orderDTO.setReceiverTel3(receiverMap.get("receiverTel3"));
			
			orderDTO.setDeliveryAddress(receiverMap.get("deliveryAddress"));
			orderDTO.setDeliveryMessage(receiverMap.get("deliveryMessage"));
			orderDTO.setDeliveryMethod(receiverMap.get("deliveryMethod"));
			orderDTO.setGiftWrapping(receiverMap.get("giftWrapping"));
			orderDTO.setPayMethod(receiverMap.get("payMethod"));
			orderDTO.setCardComName(receiverMap.get("cardComName"));
			orderDTO.setCardPayMonth(receiverMap.get("cardPayMonth"));
			orderDTO.setPayOrdererHpNum(receiverMap.get("payOrdererHpNum"));	
			orderDTO.setOrdererHp(ordererHp);	
			myOrderList.set(i, orderDTO); 
		}
		
	    orderService.addNewOrder(myOrderList);
		mv.addObject("myOrderInfo",receiverMap);
		mv.addObject("goodsOrderList" , goodsOrderList);
		mv.addObject("myOrderList", myOrderList);
		
		return mv;
		
	}
	

}
