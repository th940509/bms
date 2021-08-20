package com.bms.goods.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bms.goods.service.GoodsService;

@Controller("goodsController")
@RequestMapping(value="/goods")
public class GoodsController {
	
	@Autowired
	private GoodsService goodsService;
	
	
	@RequestMapping(value="/goodsDetail.do" ,method = RequestMethod.GET)
	public ModelAndView goodsDetail(@RequestParam("goodsId") String goodsId) throws Exception {
		
		Map<String,Object> goodsMap = goodsService.goodsDetail(goodsId);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/goods/goodsDetail");
		mv.addObject("goodsMap", goodsMap);
		
		return mv;
		
	}
	
	
	
	@RequestMapping(value="/searchGoods.do" ,method = RequestMethod.GET)
	public ModelAndView searchGoods(@RequestParam("searchWord") String searchWord) throws Exception{
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/goods/searchGoods");
		mv.addObject("goodsList", goodsService.searchGoods(searchWord));
		
		return mv;
		
	}
	
}
