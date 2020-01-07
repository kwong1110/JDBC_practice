package com.kh.board.controller;

import java.util.ArrayList;


import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Board;
import com.kh.view.View;

public class BoardController {

	private BoardService bService = new BoardService();
	private View view = new View();
	
	public void selectAll() {
		
		ArrayList<Board> bList = bService.selectAll();
		
		if(!bList.isEmpty()) {
			view.selectAll(bList);
		} else {
			view.displayError("조회 결과가 없습니다.");
		}
	}

	public void selectOne() {
		// 글 번호 입력
		int no = view.inputBNO();
		
		Board board = bService.selectOne(no);
		
		if(board != null) {
			view.selectOne(board);
		} else {
			view.displayError("해당 글이 존재하지 않습니다.");
		}
		
	}

	public void insertBoard() {
		Board board = view.insertBoard();
		
		int result = bService.insertBoard(board);
		
		if(result > 0) {
			view.displaySuccess("게시글이 등록되었습니다.");
		} else {
			view.displayError("게시글 삽입 과정 중 오류 발생");
		}
	}

	public void updateBoard() {
		int no = view.inputBNO();
		
		// 수정하려는 글이 존재하는지 확인
		Board board = bService.selectOne(no);
		
		if(board != null) {
			String memberId = view.getMemberId();
			
			if(board.getWriter().equals(memberId)) {
				// 수정 가능
				int sel = view.updateMenu();
				
				String selStr = null;
				String upStr = null;
				
				switch(sel) {
				case 1:
					selStr = "Title";
					upStr = view.updateTitle();
					
					break;
				case 2:
					selStr = "Content";
					upStr = view.updateContent();
						// 내용은 반복해서 받아왔었음으로 따로 메소드 생성.
					break;
				case 0: return;
				}
				
				int result = bService.updateBoard(no, selStr, upStr);
				
				if(result > 0) {
					view.displaySuccess("게시글의"+ selStr + "이(가) 수정이되었습니다.");
				} else {
					view.displayError("게시글 수정 과정 중 오류 발생");
				}
			} else {
				view.displayError("해당 글을 수정할 수 없습니다. 작성자와 일치하지 않습니다.");
			}
		} else {
			view.displayError("해당 번호의 글이 존재하지 않습니다.");
		}
	}

	public void deleteBoard() {
		
		int no = view.inputBNO();
		
		Board board = bService.selectOne(no);
		
		if(board != null) {
			String memberId = view.getMemberId();
			
			if(board.getWriter().equals(memberId)) {
								
				char yn = view.checkDelete();
				
//				if(yn == 'Y') {
				
				if(yn == 'N') return;
				
				int result = bService.deleteBoard(no);
			
				if(result > 0) {
					view.displaySuccess("게시글(게시글번호:" + no + ") 가 삭제되었습니다.");
				} else {
					view.displayError("게시글 수정 과정 중 오류 발생");
				}
//				}
			} else {
				view.displayError("해당 글을 삭제할 수 없습니다. 작성자와 일치하지 않습니다.");
			}
		} else {
			view.displayError("해당 번호의 글이 존재하지 않습니다.");
			}
		}

		public void exitProgram() {
			bService.exitProgram();		
		}
}
