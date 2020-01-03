package com.kh.controller;

import java.util.ArrayList;

import com.kh.modal.service.MemberService;
import com.kh.modal.vo.Member;
import com.kh.view.MemberMenu;

public class MemberController {

	private MemberMenu menu = new MemberMenu();
//	private MemberDAO mDAO = new MemberDAO();
	private MemberService mService = new MemberService();
	
	public void insertMember() {
		
		// 새로운 회원 정보를 입력 받기 위한 view
		Member member = menu.insertMember();
		
		int result = mService.insertMember(member);
		if(result > 0) {
			menu.displaySuccess(result + "개의 행이 추가되었습니다.");
		} else {
			menu.displayError("데이터 삽입 과정 중 오류 발생");
		}
	}

	public void selectAll() {
		ArrayList<Member> mList = mService.selectAll();
		
		if(!mList.isEmpty()) {
			menu.displayMember(mList);
		} else {
			menu.displayError("조회 결과가 없습니다.");
		}
	}

	public void selectMember() {
		// 특정 조건 회원 검색
		// ID, 성별로 회원 조회
		
		// 어떤 조건으로 받아올건지를 menu에서 받아와야함.
		int sel = menu.selectMember(); 
		
		ArrayList<Member> mList = null;
		
		switch(sel) {
		case 1: // 아이디로 회원 조회
			String id = menu.inputMemberId();
			mList = mService.selectMemberId(id);
			break;
		case 2: 
			char gender = menu.inputGender();
			mList = mService.selectGender(gender);
			break;
		case 0:
			return;
		}
		
		if(!mList.isEmpty()) {
			menu.displayMember(mList);
		} else {
			menu.displayError("조회 결과가 없습니다.");
		}
	}

	public void updateMember() {
		String memberId = menu.inputMemberId();
		
		// 아이디가 존재 여부 파악. DB를 통해 확인
		int check = mService.checkMember(memberId);
		
		if(check != 1) {
			menu.displayError("입력한 아이디가 존재하지 않습니다.");
		} else {
			int sel = menu.updateMember();
			
			if(sel == 0) return;
			
			String input = menu.inputUpdate();
			
			// sel : 어떤것을 바꿀건지, memberId : 누구를, Input : 어떤 내용을 
			// sel을 가지고 키값을 다르게 지정해주어야함.
			int result = mService.updateMember(sel, memberId, input);
			
			if(result > 0) {
				menu.displaySuccess(result + "개의 행이 수정되었습니다.");
			} else {
				menu.displayError("데이터 수정 과정 중 오류 발생");
			}
		}
		
	}
	
	public void deleteMember() {
		
		String memberId = menu.inputMemberId();
		
		int check = mService.checkMember(memberId);
		
		int result = mService.deleteMember(memberId);
		
		if(check != 1)
			menu.displayError("입력한 아이디가 존재하지 않습니다.");
		else if(result > 0)
			menu.displaySuccess(result + "개의 행이 삭제되었습니다.");
		else
			menu.displayError("데이터 삭제 과정 중 오류 발생");
	
	}	
	
	public void exitProgram() {
		mService.exitProgram();		
	}

}
