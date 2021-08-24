package com.bms.mypage.service;

import java.util.List;
import java.util.Map;

import com.bms.goods.dto.GoodsDTO;
import com.bms.member.dto.MemberDTO;
import com.bms.order.dto.OrderDTO;

public interface MyPageService{
	
	public List<OrderDTO> listMyOrderGoods(String memberId) throws Exception;
	public List<OrderDTO> findMyOrderInfo(String orderId) throws Exception;
	public List<OrderDTO> listMyOrderHistory(Map<String,Object> condMap) throws Exception;
	public MemberDTO  modifyMyInfo(Map<String,String> memberMap) throws Exception;
	public void cancelOrder(String orderId) throws Exception;
	public MemberDTO myDetailInfo(String memberId) throws Exception;
	public List<GoodsDTO> MyOrderDeliveryPrice(String orderId) throws Exception;
}
