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
		System.out.println("여기는 callback");
		OAuth2AccessToken oauthToken;
		oauthToken = naverLoginBO.getAccessToken(session, code, state);
		session.setAttribute("oauthToken", oauthToken);
		//1. 로그인 사용자 정보를 읽어온다.
		apiResult = naverLoginBO.getUserProfile(oauthToken); //String형식의 json데이터
		/** apiResult json 구조
		{"resultcode":"00",
		"message":"success",
		"response":{"id":"33666449","nickname":"shinn****","age":"20-29","gender":"M","email":"sh@naver.com","name":"\uc2e0\ubc94\ud638"}}
		**/
		//2. String형식인 apiResult를 json형태로 바꿈
		JsonParser parser = new JsonParser();
		Object obj = parser.parse(apiResult);
		JsonObject jsonObj = (JsonObject) obj;
		//3. 데이터 파싱
		//Top레벨 단계 _response 파싱
		JsonObject response_obj = (JsonObject)jsonObj.get("response");
		//response의 nickname값 파싱
		String nickname = response_obj.get("nickname").toString();
		String username = response_obj.get("name").toString();
		System.out.println(nickname);
		System.out.println(username);
		//4.파싱 닉네임 세션으로 저장
		session.setAttribute("navernickname",nickname); //세션 생성
		session.setAttribute("naverusername",username); //세션 생성
		session.setAttribute("isLogOn",true); //세션 생성
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
            
            //접속토큰 get
            String kakaoToken = memberService.getReturnAccessToken(code);
            
            //접속자 정보 get
            Map<String,Object> result = memberService.getUserInfo(kakaoToken);
            System.out.println("컨트롤러 출력"+result.get("nickname")+result.get("profile_image"));
           // SessionConfigVO configVO =new SessionConfigVO();
            //configVO.setNickname((String)result.get("nickname"));
           //configVO.setProfile_img((String)result.get("profile_image"));
            //session.setAttribute("sessionConfigVO", configVO);
            String nickname = (String) result.get("nickname");
            String profile_image = (String)result.get("profile_image");
            session.setAttribute("nickname", nickname);
            session.setAttribute("profile_image", profile_image);
            session.setAttribute("isLogOn", true);
            /*로그아웃 처리 시, 사용할 토큰 값*/
            session.setAttribute("kakaoToken", kakaoToken);
        return "redirect:/";
    }

	
	
	
	
	
	@RequestMapping(value="/login.do" , method = RequestMethod.POST)
	public ModelAndView login(@RequestParam Map<String, String> loginMap, HttpServletRequest request) throws Exception {
			
		ModelAndView mv = new ModelAndView();
		
		memberDTO = memberService.login(loginMap);		
		
		if (memberDTO != null && memberDTO.getDelYn().equals("N")) { 	// 조회된 결과가 있으면

			HttpSession session = request.getSession();		// 세션 객체 생성
			session.setAttribute("isLogOn", true);			// 로그인 true
			session.setAttribute("memberInfo",memberDTO);	// memberInfo에 로그인한 계정의 정보등록
			String action = (String)session.getAttribute("action");
			
			if (action!=null && action.equals("/order/orderEachGoods.do")){ // 주문상품으로 이동
				mv.setViewName("forward:"+action); // 해당하는 파일로 이동 
			}
			else {
				mv.setViewName("redirect:/main/main.do");	// 메인으로 이동
			}
		}
		else { // 조회된 결과가 없으면
			mv.addObject("message", "로그인에 실패하였습니다.");
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
		
		
		//카카오 로그아웃 session.setAttribute("SessionConfigVO", null);
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
		
		memberDTO.setMemberPw(passwordEncoder.encode(memberDTO.getMemberPw())); // passwordEncoder.encode 단방향 암호화
		
		response.setContentType("text/html; charset=UTF-8"); // 안쓰이는 코드
		request.setCharacterEncoding("utf-8");// 안쓰이는 코드
		String message = "";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
		    memberService.addMember(memberDTO);
		    message  = "<script>";
		    message +=" alert('회원가입되었습니다.');";
		    message += " location.href='"+request.getContextPath()+"/member/loginForm.do';";
		    message += " </script>";
		    
		} catch(Exception e) {
			message  = "<script>";
		    message +=" alert('회원가입에 실패하였습니다.');";
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
		
		/* 네이버아이디로 인증 URL을 생성하기 위하여 naverLoginBO클래스의 getAuthorizationUrl메소드 호출 */
		String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session);
		//https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=sE***************&
		//redirect_uri=http%3A%2F%2F211.63.89.90%3A8090%2Flogin_project%2Fcallback&state=e68c269c-5ba9-4c31-85da-54c16c658125
		System.out.println("네이버:" + naverAuthUrl);
		//네이버
		model.addAttribute("url", naverAuthUrl);
		
		return new ModelAndView("/member/loginForm");
	}

	
	@RequestMapping(value="/memberForm.do" , method = RequestMethod.GET)
	public ModelAndView memberForm() throws Exception {
		return new ModelAndView("/member/memberForm");
	}
	
	

}
