package com.kh.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.model.vo.Employee;

public class EmployeeDAO {

	public ArrayList<Employee> selectAll() {
		
		Connection conn = null;
		// DB 연결 정보를 담은 객체 : JDBC드라이버와 DB사이를 연결해주는 일종의 길.
		
		Statement stmt = null;
		// Connection을 통해 DB에 SQL문을 전달하여 실행시키고 결과 값을 반환 받는 역할을 하는 객체.
		
		ResultSet rset = null;
		// Select문을 사용한 질의 성공 시 반환되는 객체.
		// SQL질의에 의해 생성된 select결과를 담고 있음.
		
		ArrayList<Employee> empList = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
				// 해당 DB에 대한 라이브러리(JDBC드라이버) 등록 작업.
				// 드라이버 종류에 따라 클래스 명이 다름.
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "SCOTT", "SCOTT");
				// 데이터베이스와 연결 작업
				// jdbc:oracle:thin:
				// 		-> JDBC드라이버가 oracle 타입.
				// @127.0.0.1
				//		-> 오라클이 설치된 서버의 ip가 자신의 컴퓨터임을 알림.
				//		-> @localhost로 대체 가능.
				// 1521
				//		-> 오라클 리스너 포트번호.
				// xe
				// 		-> 접속할 오라클 DB명.
				// id / pwd
				//		-> 사용자 계정 이름, 비밀번호.
			
//			System.out.println(conn);
			
			String query = "SELECT * FROM EMP";	// 쿼리 작성
								// FROM EMP; (X) 세미콜론 붙이면 안됨.
			
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			
			empList = new ArrayList<Employee>();	// 전체 조회 결과를 저장할 ArrayList 객체 생성
			Employee emp = null;	// 조회 결과 '한 행'의 값을 저장할 임시vo
			
			while(rset.next()) {	// 조회 결과 다음 값이 있으면
				// ResultSet.next() : 반환 받은 조회 결과에 한 행씩 접근하여 행이 존재하면 true, 없으면 false 반환
				
				// empno	ename	job		mgr		hiredate	sal		comm	deptno
				//	7369	SMITH	CLERK	7902	80/12/17	800				20
				
				int empNo = rset.getInt("empno");	// empno라는 칼럼에서 가져온 값을 empNo에 저장하겠다.
				String eName = rset.getString("ename");
				String job = rset.getString("job");
				int mgr = rset.getInt("mgr");
				Date hireDate = rset.getDate("hiredate");
				int sal = rset.getInt("sal");
				int comm = rset.getInt("comm");
				int deptNo = rset.getInt("deptno");
				
				emp = new Employee(empNo, eName, job, mgr, hireDate, sal, comm, deptNo);
				empList.add(emp);

			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 늦게 만든것을 먼저 닫아 주어야함.
				rset.close();
				stmt.close();				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return empList;
	}

	public Employee selectEmployee(int empNo) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
			// 위치홀더를 사용하여 쿼리를 보내고 싶을 때 사용.
		ResultSet rset = null;
		
		Employee emp = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SCOTT", "SCOTT");
			
			String query = "SELECT * FROM EMP WHERE EMPNO = ?";
//			String query = "SELECT * FROM EMP WHERE EMPNO = " + empNo;
				// 이렇게 해주어도 상관없지만
				// 받아올게 많아지면 empNo + + ... 이어주어야하니까
				// 위치홀더 '?'를 사용하자!
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, empNo);
				// 1 index 기반
				// 위치 홀더에 들어갈 값을 완성시킴(쿼리문 완성)
				// set자료형(?순번, 적용할 값);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				// 순서에 맞게 입력 할 것!
				String empName = rset.getString("ename");
				String job = rset.getString("job");
				int mgr = rset.getInt("mgr");
				Date hireDate = rset.getDate("hiredate");
				int sal = rset.getInt("sal");
				int comm = rset.getInt("comm");
				int deptNo = rset.getInt("deptno");
				
				emp = new Employee(empNo, empName, job, mgr,
								hireDate, sal, comm, deptNo);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return emp;
	}

	public int insertEmployee(Employee emp) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SCOTT", "SCOTT");
			
			// 위치홀더 없이 연결 하면...
//			String query = "INSERT INTO EMP VALUES (" + emp.getEmpNo()
//								+ ", " + emp.getEmpName() + ", " ....)
			
			String query = "INSERT INTO EMP VALUES(?, ?, ?, ?, SYSDATE, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, emp.getEmpNo());
			pstmt.setString(2, emp.getEmpName());
			pstmt.setString(3, emp.getJob());
			pstmt.setInt(4, emp.getMgr());
			pstmt.setInt(5, emp.getSal());
			pstmt.setInt(6, emp.getComm());
			pstmt.setInt(7, emp.getDeptNo());
			
			result = pstmt.executeUpdate();
				// insert 가 실패 0, 성공 1 반환
			
			if(result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public int updateEmployee(Employee emp) {
		
		Connection conn = null;
			// DB연결정보를 담은 객체.
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SCOTT", "SCOTT");
			
			String query = "UPDATE EMP SET JOB = ?, SAL = ?, COMM = ? WHERE EMPNO = ?";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, emp.getJob());
			pstmt.setInt(2, emp.getSal());
			pstmt.setInt(3, emp.getComm());
			pstmt.setInt(4, emp.getEmpNo());
			
			result = pstmt.executeUpdate();
			
			if(result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public int deleteEmployee(int empNo) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SCOTT", "SCOTT");
			
			String query = "DELETE FROM EMP WHERE EMPNO = ?";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, empNo);
			
			result = pstmt.executeUpdate();
			
			if(result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
}
