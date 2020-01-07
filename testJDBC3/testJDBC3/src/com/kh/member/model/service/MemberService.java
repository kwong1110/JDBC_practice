package com.kh.member.model.service;

import static com.kh.common.JDBCTemplate.*;

import java.sql.Connection;

import com.kh.member.model.dao.MemberDAO;
import com.kh.member.model.vo.Member;

public class MemberService {

	public int login(Member mem) {
		
		Connection conn = getConnection();
		MemberDAO mDAO = new MemberDAO();
		
		int result = mDAO.login(conn, mem);
		
		return result;
	}

	public void exitProgram() {
		close(getConnection());
			// conn을 받지않고 받아온 메소드 자체를 가져와 닫아줌.
	}

}
