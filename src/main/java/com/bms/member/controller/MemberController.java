package com.bms.member.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
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


@Controller("memberController")
@RequestMapping(value="/member")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberDTO memberDTO;
	

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
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
		String kakaoToken = (String)session.getAttribute("kakaoToken");
		
		memberService.getLogout(kakaoToken);
		
		//session.setAttribute("SessionConfigVO", null);
		session.removeAttribute("nickname");
		session.removeAttribute("profile_image");
		session.removeAttribute("kakaoToken");
			
			
		session.removeAttribute("memberInfo"); // 세션 끊기
		
		
		mv.setViewName("redirect:/main/main.do");
		
		return mv;
	
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
		return new ModelAndView("/member/loginForm");
	}

	
	@RequestMapping(value="/memberForm.do" , method = RequestMethod.GET)
	public ModelAndView memberForm() throws Exception {
		return new ModelAndView("/member/memberForm");
	}
	
	

}
