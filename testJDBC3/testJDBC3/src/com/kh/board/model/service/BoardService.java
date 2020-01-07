package com.kh.board.model.service;

import static com.kh.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.board.model.dao.BoardDAO;
import com.kh.board.model.vo.Board;

public class BoardService {

	public ArrayList<Board> selectAll() {
		
		Connection conn = getConnection();
		BoardDAO bDAO = new BoardDAO();
		
		ArrayList<Board> blist = bDAO.selectAll(conn);
		
		return blist;
	}

	public int insertBoard(Board board) {
		Connection conn = getConnection();
		BoardDAO bDAO = new BoardDAO();
		
		int result = bDAO.insertBoard(conn, board);
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		return result;
	}

	public Board selectOne(int no) {
		Connection conn = getConnection();
		BoardDAO bDAO = new BoardDAO();
		
		Board board = bDAO.selectOne(conn, no);
	
		return board;
	}

	public int updateBoard(int no, String selStr, String upStr) {
		Connection conn = getConnection();
		BoardDAO bDAO = new BoardDAO();
		
		int result = bDAO.updateBoard(conn, no, selStr, upStr);
		
		if(result > 0) commit(conn);
		else rollback(conn);
		
		return result;
	}

	public int deleteBoard(int no) {
		Connection conn = getConnection();
		BoardDAO bDAO = new BoardDAO();
		
		int result = bDAO.deleteBoard(conn, no);
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		return result;
	}

	public void exitProgram() {
		close(getConnection());
	}

}
