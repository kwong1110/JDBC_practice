package com.kh.member.controller;

import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;
import com.kh.view.View;

public class MemberController {

	private MemberService mService = new MemberService();
	private View view = new View();
	
	public void login() {
		Member mem = view.inputLogin();
		
		int result = mService.login(mem);
			// int result 까지 생성해주면 반환값까지 생성됨
			// 객체 null 받아주는건 화면단에서 함. 지금은 result
		if(result > 0) {
			view.displayLoginSuccess();
		} else {
			view.displayLoginError();
		}
	}

	public void exitProgram() {
		mService.exitProgram();		
	}
}
