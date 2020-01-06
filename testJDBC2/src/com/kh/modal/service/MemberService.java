package com.kh.modal.service;

// 직접 작성 import
import static com.kh.common.JDBCTemplate.commit;

import static com.kh.common.JDBCTemplate.getConnection;
import static com.kh.common.JDBCTemplate.rollback;
import static com.kh.common.JDBCTemplate.close;

// * : 모든, 모든 메소드를 import 하겠다는 의미.
import java.sql.Connection;
import java.util.ArrayList;

import com.kh.modal.dao.MemberDAO;
import com.kh.modal.vo.Member;

public class MemberService {
	
	// 1) Controller로부터 인자를 전달 받음
	// 2) Connection객체 생성
	// 3) DAO 객체 생성
	// 4) DAO에 생성된 Connection객체와 인자 전달
	// 5) DAO 수행결과를 가지고 비즈니스 로직 및 트랜잭션 관리
		
	// Controller -> service -> DAO 다리역할.
	
	public int insertMember(Member member) {
		
		Connection conn = getConnection();
		//	JDBCTemplate.getConnection(); 
		//	이렇게 해도 상관없지만, 이렇게 입력할 시 매 매소드마다 클래스명을 작성해주어야해서
		// 	다른 매소드에서도 사용해서 import static해줌.
		
		MemberDAO mDAO = new MemberDAO();	
		// MemberDAO 의 기본생성자는 properties를 load해 주는 것인데
		// 필드에 적어주게되면 처음 한번만 불러오고 업데이트가 되지 않은 MemberDAO를 사용하게되서
		// 원하는 대로 작동이 안됨.
		int result = mDAO.insertMember(conn, member);
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		
		return result;
	}

	public ArrayList<Member> selectAll() {
		Connection conn = getConnection();
		
		MemberDAO mDAO = new MemberDAO();
		ArrayList<Member> mList = mDAO.selectAll(conn);
		
		return mList;
	}

	public ArrayList<Member> selectMemberId(String id) {
		Connection conn = getConnection();
		
		MemberDAO mDAO = new MemberDAO();
		ArrayList<Member> mList = mDAO.selectMemberId(conn, id);
		
		// 셀렉트하는거기때문에 롤백,커밋 필요하지않음.
		return mList;
	}

	public ArrayList<Member> selectGender(char gender) {
		Connection conn = getConnection();
		
		MemberDAO mDAO = new MemberDAO();
		ArrayList<Member> mList = mDAO.selectMemberGender(conn, gender);
		
		return mList;
	}

	public int checkMember(String memberId) {
		Connection conn = getConnection();
		MemberDAO mDAO = new MemberDAO();
		
		int check = mDAO.checkMember(conn, memberId);
		return check;
	}

	public int updateMember(int sel, String memberId, String input) {
		Connection conn = getConnection();
		MemberDAO mDAO = new MemberDAO();
		
		int result = mDAO.updateMember(conn, sel, memberId, input);
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		
		return result;
	}

	public int deleteMember(String memberId) {
		Connection conn = getConnection();
		MemberDAO mDAO = new MemberDAO();
		
		int result = mDAO.deleteMember(conn, memberId);
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		
		return result;
	}
	
	public void exitProgram() {
		Connection conn = getConnection();
		close(conn);
	}
}
