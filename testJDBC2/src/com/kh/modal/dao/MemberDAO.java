package com.kh.modal.dao;

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

import com.kh.modal.vo.Member;

public class MemberDAO {

	private Properties prop = null;
	
	public MemberDAO() {
		try {
			prop = new Properties();
			prop.load(new FileReader("query.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// 목표 : 쿼리들을 안에 기술하는게 아니라 따로 파일(query.properties)을 만들어 불러오도록 할것.
	public int insertMember(Connection	conn, Member member) {
		/*
		 	이전 프로젝트(testJDBC1)에서 DAO가 맡은 업무
		 		1) JDBC드라이버 등록
		 		2) DB 연결 Connection 객체 생성
		 	3) SQL문 실행 (이것만 DAO에서 기술)
		 		4) 처리 결과에 따른 트랜잭션 처리(commit, rollback)
		 		5) 자원 반환
		 		
		 	실제로 DAO가 처리해야하는 업무는 SQL문을 DB로 전달하여 실행하고 반환 값을 받아오는 3번의 역할만 진행해야 함.
		 	=> 따라서 1, 2, 4, 5번의 역할 분리
		 */
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = prop.getProperty("insertMember");
		
		try {
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getMemberPwd());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getGender()+"");
			pstmt.setString(5, member.getEmail());
			pstmt.setString(6, member.getPhone());
			pstmt.setString(7, member.getAddress());
			pstmt.setInt(8, member.getAge());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
				// template에 있는 close
		}
		return result;		
	}


	public ArrayList<Member> selectAll(Connection conn) {
		
		Statement stmt = null;
		ResultSet rset = null;
		ArrayList<Member> mList = null;
		
		String query = prop.getProperty("selectAll");
		
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			
			mList = new ArrayList<Member>();
			
			Member member = null;
			
			while(rset.next()) {
				String memberId = rset.getString("MEMBER_ID");
				String memberPwd = rset.getString("MEMBER_PWD");
				String memberName = rset.getString("MEMBER_NAME");
				char gender = rset.getString("GENDER").charAt(0);
				String email = rset.getString("EMAIL");
				String phone = rset.getString("PHONE");
				int age = rset.getInt("AGE");
				String address = rset.getString("ADDRESS");
				Date enrollDate = rset.getDate("ENROLL_DATE");
				
				member = new Member(memberId, memberPwd, memberName, gender, email, phone, age, address, enrollDate);
				mList.add(member);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(stmt);
		}
		
		return mList;
	}


	public ArrayList<Member> selectMemberId(Connection conn, String id) {
		
		// Statement와 PreparedStatement 2가지로 해보기.
//		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Member> mList = null;
		
		String query = prop.getProperty("selectMemberId");
		// Statement : SELECT * FROM MEMBER WHERE MEMBER_ID LIKE
		// PreparedStatement : SELECT * FROM MEMBER WHERE MEMBER_ID LIKE ?
		
		mList = new ArrayList<Member>();
		Member mem = null;
		
		try {
			// Statment 
//			stmt = conn.createStatement();
//			query += " '%" + id + "%'";
//			rset = stmt.executeQuery(query);
			
			// PreparedStatement
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "%" + id + "%");
				// PreparedStatement 에는 저절로 싱글쿼테이션('')이 붙여진다.
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				String memberId = rset.getString("MEMBER_ID");
				String memberPwd = rset.getString("MEMBER_PWD");
				String memberName = rset.getString("MEMBER_NAME");
				char gender = rset.getString("GENDER").charAt(0);
				String email = rset.getString("EMAIL");
				String phone = rset.getString("PHONE");
				int age = rset.getInt("AGE");
				String address = rset.getString("ADDRESS");
				Date enrollDate = rset.getDate("ENROLL_DATE");
				
				mem = new Member(memberId, memberPwd, memberName, gender, email, phone, age, address, enrollDate);
				mList.add(mem);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
//			close(stmt);
			close(pstmt);
		}
		return mList;
	}


	public ArrayList<Member> selectMemberGender(Connection conn, char gender) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Member> mList = null;
		
		String query = prop.getProperty("selectGender");
		
		mList = new ArrayList<Member>();
		Member mem = null;
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, String.valueOf(gender));
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				String memberId = rset.getString("MEMBER_ID");
				String memberPwd = rset.getString("MEMBER_PWD");
				String memberName = rset.getString("MEMBER_NAME");
				char mgender = rset.getString("GENDER").charAt(0);
				String email = rset.getString("EMAIL");
				String phone = rset.getString("PHONE");
				int age = rset.getInt("AGE");
				String address = rset.getString("ADDRESS");
				Date enrollDate = rset.getDate("ENROLL_DATE");
				
				mem = new Member(memberId, memberPwd, memberName, mgender, email, phone, age, address, enrollDate);
				mList.add(mem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		return mList;
	}


	public int checkMember(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int check = 0;
			// COUNT를 통해 갯수 확인
		
		String query = prop.getProperty("checkMember");
		// SELECT COUNT(*) FROM MEMBER WHERE MEMBER_ID = ?
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, memberId);
			rset = pstmt.executeQuery();
			
			// 결과값은 primarykey인 ID의 갯수이므로 1, 0 만나옴.
			if(rset.next()) {
				check = rset.getInt(1);
				// 결과 ResultSet의 첫 번째 칼럼 값을 가져와라
				// 왠만해서는(1개이상일때는) 숫자보다는 칼럼명을 기술.
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return check;
	}

	public int updateMember(Connection conn, int sel, String memberId, String input) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		String query = null;
		
		switch(sel){
		case 1: query = prop.getProperty("updatePwd"); break;
		case 2: query = prop.getProperty("updateEmail"); break;
		case 3: query = prop.getProperty("updatePhone"); break;
		case 4: query = prop.getProperty("updateAddress"); break;
		}
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, input);
			pstmt.setString(2, memberId);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}


	public int deleteMember(Connection conn, String memberId) {
		
		Statement stmt = null;
			// Statement도 한번 사용해보자.
		int result = 0;
		String query = null;
		
		query = prop.getProperty("deleteMember");
		try {
			stmt = conn.createStatement();
			query += "'" + memberId + "'";
				// 싱글쿼테이션 ('')잊지말고 해볼것.
			result = stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(stmt);
		}
		
		return result;
	}
}
