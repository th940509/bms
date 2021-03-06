package com.bms.mypage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bms.common.util.CommonUtil;
import com.bms.member.dto.MemberDTO;
import com.bms.mypage.service.MyPageService;
import com.bms.order.dto.OrderDTO;

@Controller("myPageController")
@RequestMapping(value="/mypage")
public class MyPageController  {
	
	@Autowired
	private MyPageService myPageService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private MemberDTO memberDTO;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@RequestMapping(value="/myPageMain.do" , method = RequestMethod.GET)
	public ModelAndView myPageMain(@RequestParam(required = false , value="message") String message, HttpServletRequest request)  throws Exception {
		
		HttpSession session = request.getSession(); // 세션객체 생성
		
		session = request.getSession(); 
		session.setAttribute("side_menu", "my_page"); // tiles의 side
		
		ModelAndView mv = new ModelAndView("/mypage/myPageMain"); // 괄호 안의 jsp 페이지로 이동
		memberDTO=(MemberDTO)session.getAttribute("memberInfo"); // 로그인한 회원 정보 DTO 대입
		
		
		String memberId = "";
		if (memberDTO != null) 	memberId = memberDTO.getMemberId(); // memberDTO가 null이 아닐때 id를 가져와 memberId에 대입
		else 					mv.setViewName("redirect:/main/main.do"); // memberDTO가 null 일때, 세션X경우 -> main.do로 이동
		
		mv.addObject("message", message);
		mv.addObject("myOrderList", myPageService.listMyOrderGoods(memberId)); // T_SHOPPING_ORDER 주문테이블

		return mv;
		
	}
	
	
	@RequestMapping(value="/myOrderDetail.do" , method = RequestMethod.GET)
	public ModelAndView myOrderDetail(@RequestParam("orderId") String orderId, HttpServletRequest request)  throws Exception {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/mypage/myOrderDetail");
		HttpSession session=request.getSession(); // 세션객체 생성
		
		mv.addObject("orderer", (MemberDTO)session.getAttribute("memberInfo"));
		mv.addObject("deliveryPrice", myPageService.MyOrderDeliveryPrice(orderId));
		mv.addObject("myOrderList",myPageService.findMyOrderInfo(orderId));
		
		return mv; 
	
	}
	
	
	@RequestMapping(value="/listMyOrderHistory.do" , method = RequestMethod.GET)
	public ModelAndView listMyOrderHistory(@RequestParam Map<String, String> dateMap, HttpServletRequest request) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/mypage/listMyOrderHistory");
		
		HttpSession session = request.getSession();
		memberDTO = (MemberDTO)session.getAttribute("memberInfo"); // 로그인된 정보 / 아이디 대입
		String memberId = memberDTO.getMemberId();
		
		String fixedSearchPeriod = dateMap.get("fixedSearchPeriod");
		String search_word="";
		String search_type        = dateMap.get("search_type");
		search_word        = dateMap.get("search_word");
		String beginDate = ""; // 시작날짜
		String endDate   = ""; // 현재날짜
		
		if(dateMap.get("beginDate") == null && dateMap.get("endDate") == null){
		String [] tempDate = commonUtil.calcSearchPeriod(fixedSearchPeriod).split(","); // return값 beginDate + "," + endDate; -> split -> , 기준으로 beginDate endDate
		beginDate = tempDate[0];
		endDate   = tempDate[1];
		}
		else {
			beginDate = dateMap.get("beginDate");
			endDate = dateMap.get("endDate");
		}
		
		Map<String,Object> condMap = new HashMap<String,Object>();
		
		condMap.put("beginDate", beginDate);
		condMap.put("endDate", endDate);
		condMap.put("search_type", search_type);
		condMap.put("search_word", search_word);
		condMap.put("memberId", memberId);
		List<OrderDTO> myOrderHistList = myPageService.listMyOrderHistory(condMap);
		mv.addObject("myOrderHistList", myOrderHistList); // 날짜에 맞는 주문 정보 리스트
		
		String beginDate1[] = beginDate.split("-"); // 년 월 일 쪼개기
		String endDate1[]   = endDate.split("-");
		mv.addObject("beginYear",beginDate1[0]); // 시작 년
		mv.addObject("beginMonth",beginDate1[1]); // 월
		mv.addObject("beginDay",beginDate1[2]); // 일
		mv.addObject("endYear",endDate1[0]); // 현재 년
		mv.addObject("endMonth",endDate1[1]); // 월
		mv.addObject("endDay",endDate1[2]); // 일
		return mv;
		
	}	
	
	
	@RequestMapping(value="/cancelMyOrder.do")
	public ModelAndView cancelMyOrder(@RequestParam("orderId") String orderId)  throws Exception {
		
		ModelAndView mv = new ModelAndView();
		myPageService.cancelOrder(orderId); // 주문취소한 주문자아이디 받아오기 post도 처리
		mv.addObject("message", "cancel_order");
		mv.setViewName("redirect:/mypage/myPageMain.do");
		
		return mv;
		
	}
	
	
	@RequestMapping(value="/myDetailInfo.do" , method = RequestMethod.GET)
	public ModelAndView myDetailInfo() throws Exception {
		return new ModelAndView("/mypage/myDetailInfo");
	}	
	
	
	@RequestMapping(value="/modifyMyInfo.do" , method = RequestMethod.POST)
	public ResponseEntity<String>  modifyMyInfo(@RequestParam("attribute")  String attribute,
			                 					@RequestParam("value")  String value,
			                 					HttpServletRequest request)  throws Exception {
		
		Map<String,String> memberMap = new HashMap<String,String>();
		HttpSession session = request.getSession();
		memberDTO = (MemberDTO)session.getAttribute("memberInfo");

		String val[] = null;
		String memberId = memberDTO.getMemberId();
		
		if (attribute.equals("memberPw")) {
			memberMap.put("memberPw" , passwordEncoder.encode(value));
		}
		else if (attribute.equals("memberBirth")){
			val = value.split(",");
			memberMap.put("memberBirthY",val[0]);
			memberMap.put("memberBirthM",val[1]);
			memberMap.put("memberBirthD",val[2]);
			memberMap.put("memberBirthGn",val[3]);
		}
		else if (attribute.equals("tel")){
			val = value.split(",");
			memberMap.put("tel1",val[0]);
			memberMap.put("tel2",val[1]);
			memberMap.put("tel3",val[2]);
		}
		else if (attribute.equals("hp")){
			val = value.split(",");
			memberMap.put("hp1",val[0]);
			memberMap.put("hp2",val[1]);
			memberMap.put("hp3",val[2]);
			memberMap.put("smsstsYn", val[3]);
		}
		else if (attribute.equals("email")){
			val = value.split(",");
			memberMap.put("email1",val[0]);
			memberMap.put("email2",val[1]);
			memberMap.put("emailstsYn", val[2]);
		}
		else if (attribute.equals("address")){
			val = value.split("/");
			memberMap.put("zipcode",val[0]);
			memberMap.put("jibunAddress",val[1]);
			memberMap.put("roadAddress", val[2]);
			memberMap.put("namujiAddress", val[3]);
		}
		else {
			memberMap.put(attribute,value);	
		}
		
		memberMap.put("memberId", memberId);
		
		
		memberDTO = (MemberDTO)myPageService.modifyMyInfo(memberMap);
		session.removeAttribute("memberInfo");
		session.setAttribute("memberInfo", memberDTO);
		
		return new ResponseEntity<String>("modSuccess", new HttpHeaders(), HttpStatus.OK);
		
	}	
	
	@RequestMapping(value="/deleteMyInfo.do")
	public ModelAndView deleteMember(@RequestParam("delYn") String delYn , @RequestParam("memberId") String memberId , HttpServletRequest request)  throws Exception {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/main/main.do");
		
		HttpSession session = request.getSession();
		session.invalidate(); // 세션 끊기
		
		HashMap<String,String> memberMap = new HashMap<String,String>();
		memberMap.put("delYn"   , delYn);
		memberMap.put("memberId", memberId);
		
		myPageService.modifyMyInfo(memberMap);
		
		return mv;
		
	}
		
	
}
