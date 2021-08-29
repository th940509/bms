package com.bms.member.service;

import java.util.Map;

import com.bms.member.dto.MemberDTO;

public interface MemberService {
	
	public MemberDTO login(Map<String,String> loginMap) throws Exception;
	public void addMember(MemberDTO memberDTO) throws Exception;
	public String overlapped(String id) throws Exception;
	public String getReturnAccessToken(String code) throws Exception;
	public Map<String,Object> getUserInfo(String access_token) throws Exception;
	public void getLogout(String access_Token) throws Exception;
}
