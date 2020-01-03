package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.dao.EmployeeDAO;
import com.kh.model.vo.Employee;
import com.kh.view.Menu;

public class EmployeeController {
	
	private EmployeeDAO empDAO = new EmployeeDAO();
	private Menu menu = new Menu();
	
	public void selectAll() {
		ArrayList<Employee> empList = empDAO.selectAll();
		
		if(!empList.isEmpty()) {
			menu.selectAll(empList);
		} else {
			menu.displayError("조회 결과가 없습니다.");
		}
	}

	public void selectEmployee() {
		
		int empNo = menu.selectEmpNo();	// 사번 입력 view 호출
			// 사번을 받아야하는 기능이 사원 조회와 수정, 삭제가 있으므로
			// 사번 입력 view를 별도로 생성하여 중복 줄임.
		
		Employee emp = empDAO.selectEmployee(empNo);
		
		if(emp != null) {
			menu.selectEmployee(emp);
		} else {
			menu.displayError("해당 사번의 검색 결과가 없습니다.");
		}
	}

	public void insertEmployee() {
		Employee emp = menu.insertEmployee();
		
		int result = empDAO.insertEmployee(emp);
		
		if(result > 0) {
			menu.diplaySuccess(result + "개의 행이 추가되었습니다.");
		} else {
			menu.displayError("데이터 삽입 과정 중 오류 발생");
		}
	}

	public void updateEmployee() {
		int empNo = menu.selectEmpNo();
		
		Employee emp = menu.updateEmployee();
		
		emp.setEmpNo(empNo);
		
		int result = empDAO.updateEmployee(emp);
				
		if(result > 0) {
			menu.diplaySuccess(result + "개의 행이 수정되었습니다.");
		} else {
			menu.displayError("데이터 수정 과정 중 오류 발생");
		}
	}

	public void deleteEmployee() {
		int empNo = menu.selectEmpNo();

//		Employee emp = new Employee();
//		emp.setEmpNo(empNo);
		// -> 값 하나만 보낼것이기 때문에 굳이 객체생성을 할 필요가 없음.
		
		int result = empDAO.deleteEmployee(empNo);
		
		if(result > 0) {
			menu.diplaySuccess(result + "개의 행이 삭제되었습니다.");
		} else {
			menu.displayError("데이터 삭제 과정 중 오류 발생");
		}
		
	}
}
