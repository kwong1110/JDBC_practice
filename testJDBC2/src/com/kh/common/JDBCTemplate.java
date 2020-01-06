package com.kh.common;

import java.io.FileNotFoundException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {
	
	// 싱글톤(Single Tone) 패턴
	// 프로그램 구동 시 메모리에 객체를 딱 한 개만 기록되게 하는 디자인 패턴
	// 모든 필드와 메소드를 static으로 선언하여
	// static영역에 공용으로 사용할 수 있는 단 하나의 객체를 만듦.
	
	private static Connection conn = null;
	
	private JDBCTemplate() {}
		// 객체 생성 불가능하게 막아 놓음.
	
//	double d = Math.random(); 
		// 싱글톤 패턴으로 되어있는 대표적인 클래스.
		// 객체 생성을 할수 없다.
	
	
	// DB연결을 위하여 Connection객체 반환해주는 메소드
	public static Connection getConnection() {
		
		// 최초로 메소드 호출 시 Connection객체는 DB와 연결이 되어있지 않으므로 DB연결 작업 진행.
		if(conn == null) {
			try {
				
				Properties prop = new Properties();
				
				prop.load(new FileReader("driver.properties"));
				
				Class.forName(prop.getProperty("driver"));
				conn = DriverManager.getConnection(prop.getProperty("url"),
												   prop.getProperty("user"),
												   prop.getProperty("password"));
				conn.setAutoCommit(false);
					// 기본값 : true 
					// 자동적으로 커밋되는걸 방지.
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	// commit, rollback, close 역시 공통적으로 사용하는 것이기 때문에
	// static으로 미리 선언해두어 코드길이 감소, 재사용성 증가.
	public static void commit(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) {
				// 연결이되어있고 끝나지않았으면
				conn.commit();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void rollback(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.rollback();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 오버로딩 적용.
	public static void close(ResultSet rset) {
		try {
			if(rset != null && !rset.isClosed()) {
				rset.close();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	// PrepareStatement는 Statement의 후손이기 때문에 Statement만 기술.
	public static void close(Statement stmt) {
		try {
			if(stmt != null && !stmt.isClosed()) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
