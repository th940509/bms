package com.bms.main;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.bms.common.util.CommonUtil;
import com.bms.goods.dto.GoodsDTO;
import com.bms.goods.service.GoodsService;

@Controller("mainController")
@EnableAspectJAutoProxy
public class MainController {
	
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping(value= "/")
	public ModelAndView home() throws Exception{
		
		return new ModelAndView("redirect:/main/main.do");
	}
	
	
	@RequestMapping(value= "/main/main.do")
	public ModelAndView main(HttpServletRequest request) throws Exception{
		
		ModelAndView mv = new ModelAndView(); // ModelAndView 클래스
		mv.setViewName("/main/main");
		// ModelAndView = new ModelAndView("/main/main"); 
		HttpSession session = request.getSession();
		session.setAttribute("side_menu", "user");
		
		// 카카오로그인정보가져오기
		if(session.getAttribute("nickname") != null && session.getAttribute("profile_image") != null) {
			//mv.addObject("sessionConfigVO",session.getAttribute("sessionConfigVO"));
			mv.addObject("nickname", session.getAttribute("nickname"));
			mv.addObject("profile_image", session.getAttribute("profile_image"));
			mv.addObject("isLogOn", session.getAttribute("isLogOn"));
		}
		
		// 네이버로그인정보 가져오기
		if(session.getAttribute("naverusername") != null) {
			System.out.println(session.getAttribute("naverusername"));
			mv.addObject("naverusername",session.getAttribute("naverusername"));
			mv.addObject("isLogOn", session.getAttribute("isLogOn"));
		}
		
		
		Map<String,List<GoodsDTO>> goodsMap = goodsService.listGoods();
		mv.addObject("goodsMap", goodsMap);
		
		return mv;
		
	}
}
