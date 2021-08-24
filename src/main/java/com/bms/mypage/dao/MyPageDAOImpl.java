package com.bms.mypage.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bms.goods.dto.GoodsDTO;
import com.bms.member.dto.MemberDTO;
import com.bms.order.dto.OrderDTO;

@Repository("myPageDAO")
public class MyPageDAOImpl implements MyPageDAO {
	
	@Autowired
	private SqlSession sqlSession;
	
	public List<OrderDTO> selectMyOrderGoodsList(String memberId) throws DataAccessException{
		return sqlSession.selectList("mapper.mypage.selectMyOrderGoodsList",memberId);
	}
	
	
	public List<OrderDTO> selectMyOrderInfo(String orderId) throws DataAccessException{
		return sqlSession.selectList("mapper.mypage.selectMyOrderInfo",orderId);
	}	

	
	public List<OrderDTO> selectMyOrderHistoryList(Map<String,Object> condMap) throws DataAccessException{
		return sqlSession.selectList("mapper.mypage.selectMyOrderHistoryList",condMap);
	}
	
	
	public void updateMyInfo(Map<String,String> memberMap) throws DataAccessException{
		sqlSession.update("mapper.mypage.updateMyInfo",memberMap);
	}
	
	
	public MemberDTO selectMyDetailInfo(String memberId) throws DataAccessException{
		return sqlSession.selectOne("mapper.mypage.selectMyDetailInfo",memberId);
	}

	
	public void updateMyOrderCancel(String orderId) throws DataAccessException{
		sqlSession.update("mapper.mypage.updateMyOrderCancel",orderId);
	}


	@Override
	public List<GoodsDTO> deliveryPrice(String orderId) throws DataAccessException {
		return sqlSession.selectList("mapper.mypage.deliveryPrice", orderId);
	}

}
