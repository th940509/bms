package com.bms.member.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bms.member.dto.MemberDTO;
import com.bms.member.dto.SessionConfigVO;
import com.bms.member.service.MemberService;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;


@Controller("memberController")
@RequestMapping(value="/member")
public class MemberController {
	
	/* NaverLoginBO */
	private NaverLoginBO naverLoginBO;
	private String apiResult = null;
	@Autowired
	private void setNaverLoginBO(NaverLoginBO naverLoginBO) {
	this.naverLoginBO = naverLoginBO;
	}
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberDTO memberDTO;
	

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	
	
	
	
	@RequestMapping(value = "/navercallback.do", method = { RequestMethod.GET, RequestMethod.POST })
		public String navercallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException, ParseException {
		System.out.println("????????? callback");
		OAuth2AccessToken oauthToken;
		oauthToken = naverLoginBO.getAccessToken(session, code, state);
		session.setAttribute("oauthToken", oauthToken);
		//1. ????????? ????????? ????????? ????????????.
		apiResult = naverLoginBO.getUserProfile(oauthToken); //String????????? json?????????
		/** apiResult json ??????
		{"resultcode":"00",
		"message":"success",
		"response":{"id":"33666449","nickname":"shinn****","age":"20-29","gender":"M","email":"sh@naver.com","name":"\uc2e0\ubc94\ud638"}}
		**/
		//2. String????????? apiResult??? json????????? ??????
		JsonParser parser = new JsonParser();
		Object obj = parser.parse(apiResult);
		JsonObject jsonObj = (JsonObject) obj;
		//3. ????????? ??????
		//Top?????? ?????? _response ??????
		JsonObject response_obj = (JsonObject)jsonObj.get("response");
		//response??? nickname??? ??????
		String nickname = response_obj.get("nickname").toString();
		String username = response_obj.get("name").toString();
		System.out.println(nickname);
		System.out.println(username);
		//4.?????? ????????? ???????????? ??????
		session.setAttribute("navernickname",nickname); //?????? ??????
		session.setAttribute("naverusername",username); //?????? ??????
		session.setAttribute("isLogOn",true); //?????? ??????
		model.addAttribute("isLogOn",true);
		model.addAttribute("naverusername",username);
		model.addAttribute("result", apiResult);
		return "redirect:/";
	}
	
	@RequestMapping(value="/main/kakao_login.ajax")
	public String kakaoLogin() {
		StringBuffer loginUrl = new StringBuffer();
		loginUrl.append("https://kauth.kakao.com/oauth/authorize?client_id=");
		loginUrl.append("92cee331f59ec3f9d00edce444e0347e"); 
		loginUrl.append("&redirect_uri=");
		loginUrl.append("http://localhost:8090/member/kakaocallback"); 
		loginUrl.append("&response_type=code");
		
		return "redirect:"+loginUrl.toString();
	}
	
	@RequestMapping(value = "/kakaocallback", method = RequestMethod.GET)
    public String redirectkakao(@RequestParam String code, HttpSession session) throws Exception {
            System.out.println(code);
            
            //???????????? get
            String kakaoToken = memberService.getReturnAccessToken(code);
            
            //????????? ?????? get
            Map<String,Object> result = memberService.getUserInfo(kakaoToken);
            System.out.println("???????????? ??????"+result.get("nickname")+result.get("profile_image"));
           // SessionConfigVO configVO =new SessionConfigVO();
            //configVO.setNickname((String)result.get("nickname"));
           //configVO.setProfile_img((String)result.get("profile_image"));
            //session.setAttribute("sessionConfigVO", configVO);
            String nickname = (String) result.get("nickname");
            String profile_image = (String)result.get("profile_image");
            session.setAttribute("nickname", nickname);
            session.setAttribute("profile_image", profile_image);
            session.setAttribute("isLogOn", true);
            /*???????????? ?????? ???, ????????? ?????? ???*/
            session.setAttribute("kakaoToken", kakaoToken);
        return "redirect:/";
    }

	
	
	
	
	
	@RequestMapping(value="/login.do" , method = RequestMethod.POST)
	public ModelAndView login(@RequestParam Map<String, String> loginMap, HttpServletRequest request) throws Exception {
			
		ModelAndView mv = new ModelAndView();
		
		memberDTO = memberService.login(loginMap);		
		
		if (memberDTO != null && memberDTO.getDelYn().equals("N")) { 	// ????????? ????????? ?????????

			HttpSession session = request.getSession();		// ?????? ?????? ??????
			session.setAttribute("isLogOn", true);			// ????????? true
			session.setAttribute("memberInfo",memberDTO);	// memberInfo??? ???????????? ????????? ????????????
			String action = (String)session.getAttribute("action");
			
			if (action!=null && action.equals("/order/orderEachGoods.do")){ // ?????????????????? ??????
				mv.setViewName("forward:"+action); // ???????????? ????????? ?????? 
			}
			else {
				mv.setViewName("redirect:/main/main.do");	// ???????????? ??????
			}
		}
		else { // ????????? ????????? ?????????
			mv.addObject("message", "???????????? ?????????????????????.");
			mv.setViewName("/member/loginForm");
			
		}
		return mv;
		
	}
	
	

	
	@RequestMapping(value="/logout.do" , method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		HttpSession session = request.getSession();
		
		session.setAttribute("isLogon", false);
		session.removeAttribute("memberInfo");
		
		
		//????????? ???????????? session.setAttribute("SessionConfigVO", null);
		String kakaoToken = (String)session.getAttribute("kakaoToken");
		if(session.getAttribute("nickname") != null) {
		memberService.getLogout(kakaoToken);
		session.removeAttribute("nickname");
		session.removeAttribute("profile_image");
		session.removeAttribute("kakaoToken");}
			
		
		mv.setViewName("redirect:/main/main.do");
		
		return mv;
	
	}
	
	@RequestMapping("/naverlogout.do")
	public String naverlogout(HttpSession session,Model model) throws IOException {
		
		String ACCESS_TOKEN = session.getAttribute("oauthToken")+toString();
		
		System.out.println("ACCESS_TOKEN>>>>"+ACCESS_TOKEN);
		
		String deleteTokenUrl =naverLoginBO.deleteToken(ACCESS_TOKEN);
		model.addAttribute("deleteTokenUrl",deleteTokenUrl);
		session.invalidate();
		
		return "redirect:/";
	}
	
	

	
	
	@RequestMapping(value="/addMember.do" ,method = RequestMethod.POST)
	public ResponseEntity<String> addMember(@ModelAttribute("memberDTO") MemberDTO memberDTO,
			                HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		if (memberDTO.getEmailstsYn() == null)  memberDTO.setEmailstsYn("N");
		if (memberDTO.getSmsstsYn() == null)    memberDTO.setSmsstsYn("N");
		
		memberDTO.setMemberPw(passwordEncoder.encode(memberDTO.getMemberPw())); // passwordEncoder.encode ????????? ?????????
		
		response.setContentType("text/html; charset=UTF-8"); // ???????????? ??????
		request.setCharacterEncoding("utf-8");// ???????????? ??????
		String message = "";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
		    memberService.addMember(memberDTO);
		    message  = "<script>";
		    message +=" alert('???????????????????????????.');";
		    message += " location.href='"+request.getContextPath()+"/member/loginForm.do';";
		    message += " </script>";
		    
		} catch(Exception e) {
			message  = "<script>";
		    message +=" alert('??????????????? ?????????????????????.');";
		    message += " location.href='"+request.getContextPath()+"/member/memberForm.do';";
		    message += " </script>";
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(message, responseHeaders, HttpStatus.OK);
		
	}
	
	
	@RequestMapping(value="/overlapped.do" ,method = RequestMethod.POST)
	public ResponseEntity<String> overlapped(@RequestParam("id") String id) throws Exception{
		return new ResponseEntity<String>(memberService.overlapped(id), HttpStatus.OK);
	}
	

	@RequestMapping(value="/loginForm.do" , method = RequestMethod.GET)
	public ModelAndView loginForm(Model model,HttpSession session) throws Exception {
		
		/* ????????????????????? ?????? URL??? ???????????? ????????? naverLoginBO???????????? getAuthorizationUrl????????? ?????? */
		String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session);
		//https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=sE***************&
		//redirect_uri=http%3A%2F%2F211.63.89.90%3A8090%2Flogin_project%2Fcallback&state=e68c269c-5ba9-4c31-85da-54c16c658125
		System.out.println("?????????:" + naverAuthUrl);
		//?????????
		model.addAttribute("url", naverAuthUrl);
		
		return new ModelAndView("/member/loginForm");
	}

	
	@RequestMapping(value="/memberForm.do" , method = RequestMethod.GET)
	public ModelAndView memberForm() throws Exception {
		return new ModelAndView("/member/memberForm");
	}
	
	

}
