package com.bms.admin.goods.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bms.admin.goods.dao.AdminGoodsDAO;
import com.bms.goods.dto.GoodsDTO;
import com.bms.goods.dto.ImageFileDTO;


@Service("adminGoodsService")
@Transactional(propagation=Propagation.REQUIRED) // 클래스 위에 선언하며 괄호 생략 가능 / root-context.xml에 transactionManager 선언해야함
public class AdminGoodsServiceImpl implements AdminGoodsService {
	
	@Autowired
	private AdminGoodsDAO adminGoodsDAO;
	
	
	@Override
	public int addNewGoods(Map<String,Object> newGoodsMap) throws Exception{
		
		int goodsId = adminGoodsDAO.insertNewGoods(newGoodsMap); // transcation 적용 : 36번 29번 둘 중 하나만 에러가 나도 모두 취소
		ArrayList<ImageFileDTO> imageFileList = (ArrayList)newGoodsMap.get("imageFileList");
		
		for (ImageFileDTO imageFileDTO : imageFileList) {
			imageFileDTO.setGoodsId(goodsId);
		}
		
		adminGoodsDAO.insertGoodsImageFile(imageFileList);
		
		return goodsId;
		
	}
	
	
	@Override
	public List<GoodsDTO> listNewGoods(Map<String,Object> condMap) throws Exception{
		return adminGoodsDAO.selectNewGoodsList(condMap);
	}
	
	
	@Override
	public Map<String,Object> goodsDetail(int goodsId) throws Exception {
		
		Map<String,Object> goodsMap = new HashMap<String,Object>();
		
		goodsMap.put("goods", adminGoodsDAO.selectGoodsDetail(goodsId));
		goodsMap.put("imageFileList", adminGoodsDAO.selectGoodsImageFileList(goodsId));
		
		return goodsMap;
		
	}
	
	
	@Override
	public List<ImageFileDTO> goodsImageFile(int goodsId) throws Exception{
		return adminGoodsDAO.selectGoodsImageFileList(goodsId);
	}
	
	
	@Override
	public void modifyGoodsInfo(Map<String,String> goodsMap) throws Exception{
		adminGoodsDAO.updateGoodsInfo(goodsMap);
		
	}	
	
	
	@Override
	public void modifyGoodsImage(List<ImageFileDTO> imageFileList) throws Exception{
		adminGoodsDAO.updateGoodsImage(imageFileList); 
	}
	
	
	@Override
	public void removeGoodsImage(int image_id) throws Exception{
		adminGoodsDAO.deleteGoodsImage(image_id);
	}
	
	
	@Override
	public void addNewGoodsImage(List<ImageFileDTO> imageFileList) throws Exception{
		adminGoodsDAO.insertGoodsImageFile(imageFileList);
	}
	
}
