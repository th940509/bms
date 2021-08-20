package com.bms.member.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bms.member.dao.MemberDAO;
import com.bms.member.dto.MemberDTO;

@Service("memberService")
@Transactional(propagation=Propagation.REQUIRED)
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberDAO memberDAO;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Override
	public MemberDTO login(Map<String,String> loginMap) throws Exception{
		
		MemberDTO mdto = memberDAO.login(loginMap);

		if (mdto != null) {
			String rawPassword = loginMap.get("memberPw");
			String encodedPassword = mdto.getMemberPw();
			
			// 암호화되지 않은 비밀번호(raw-)와 암호화된 비밀번호(encoded-)가 일치하는지 비교 -> true면 mdto 반환 false면 null
			if (passwordEncoder.matches(rawPassword, encodedPassword)) {
				return mdto;
			}
		}
		
		return null;
		
	}
	
	
	@Override
	public void addMember(MemberDTO memberDTO) throws Exception{
		memberDAO.insertNewMember(memberDTO);
	}
	
	
	@Override
	public String overlapped(String id) throws Exception{
		return memberDAO.selectOverlappedID(id);
	}
	
}
