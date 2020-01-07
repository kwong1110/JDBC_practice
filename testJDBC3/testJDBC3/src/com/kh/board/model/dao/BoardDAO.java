package com.kh.board.model.dao;

import static com.kh.common.JDBCTemplate.close;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.board.model.vo.Board;

public class BoardDAO {

	private Properties prop = null;
	
	public BoardDAO() {
		
		prop = new Properties();
		try {
			prop.load(new FileReader("query.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<Board> selectAll(Connection conn){
		
		Statement stmt = null;
		ResultSet rset = null;
		ArrayList<Board> bList = null;
		
		String query = prop.getProperty("selectAll");
			// SELECT * FROM BOARD WHERE DELETE_YN = 'N'
			// BOARD 테이블에 있는 삭제여부 칼럼 확인.
		
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			
			bList = new ArrayList<Board>();
			Board board = null;
			
			while(rset.next()) {
//				int bNo = rset.getInt("bno");
//				String title = rset.getString("title");
//				String content = rset.getString("content");
//				Date createDate = rset.getDate("create_date");
//				String writer = rset.getString("writer");			
				int bNo = rset.getInt(1);
				String title = rset.getString(2);
				String content = rset.getString(3);
				Date createDate = rset.getDate(4);
				String writer = rset.getString(5);
					// 순서로 하게되면 실수를 할수 있기 때문에 불편하더라도 이름으로 할 것.
				
				board = new Board(bNo, title, content, createDate, writer);
				bList.add(board);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(stmt);
		}
		return bList;
	}

	public int insertBoard(Connection conn, Board board) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = prop.getProperty("insertBoard");
		// insertBoard=INSERT INTO BOARD(BNO, TITLE, CONTENT, WRITER) VALUES(SEQ_BOARD.NEXTVAL, ?, ?, ?)
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getContent());
			pstmt.setString(3, board.getWriter());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public Board selectOne(Connection conn, int no) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String query = prop.getProperty("selectOne");
		
		Board board = null;
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, no);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
			// if(rset.next()) 해도됨.
				String title = rset.getString("TITLE");
				String writer = rset.getString("WRITER");
				Date date = rset.getDate("CREATE_DATE");
				String content = rset.getString("CONTENT");
				
				board = new Board(no, title, content, date, writer);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return board;
	}

	public int updateBoard(Connection conn, int no, String selStr, String upStr) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		String query = prop.getProperty("updateBoard" + selStr);
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, upStr);
			pstmt.setInt(2, no);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int deleteBoard(Connection conn, int no) {
		PreparedStatement pstmt = null;
		int result = 0;
		String query = prop.getProperty("deleteBoard");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, no);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}
}
