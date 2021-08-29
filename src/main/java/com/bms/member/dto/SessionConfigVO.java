package com.bms.member.dto;

import org.springframework.stereotype.Component;

@Component
public class SessionConfigVO {
	
	private String nickname;
	private String profile_img;
	
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getProfile_img() {
		return profile_img;
	}
	public void setProfile_img(String profile_img) {
		this.profile_img = profile_img;
	}
	
	
}
