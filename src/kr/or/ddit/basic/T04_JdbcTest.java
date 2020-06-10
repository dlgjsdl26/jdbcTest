package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/*
 * 	LPROD 테이블에 새로운 데이터 추가하기
 * 
 * 	lprod_gu와 lprod_nm은 직접 입력받아 처리하고,
 *  lprod_id는 현재의 lprod_id들 중 제일 큰값보다 1 중가된 값으로 한다.
 *  (기타사항 : lprod_gu도 중복되는지 검사한다.)
 */

public class T04_JdbcTest {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {	
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String userId = "PC99";
			String password = "java";
			
			conn = DriverManager.getConnection(url, userId, password);
			
			
			//id 최대값 1 증가
		stmt = conn.createStatement();
		String sql = "select max(lprod_id) from lprod ";
		rs = stmt.executeQuery(sql);
		int idMax = 0;
		
		if(rs.next()) {
			idMax = rs.getInt("max(lprod_id)");
		}
		idMax++;
		
		//-------------------gu 중복없이 삽입------------
		String gu;
		while(true) {
		System.out.println("lprod_gu에 넣을 데이터를 입력하세요 >>>");
		gu = s.nextLine();
		
		String sql3 = "select count(*) from lprod where lprod_gu = ?";
		pstmt = conn.prepareStatement(sql3);
		pstmt.setString(1, gu);
		rs = pstmt.executeQuery();
		
		if(rs.next()) {
			if(rs.getInt(1) == 0) {
				break;
			}
		}else 
			System.out.println("중복된 값이 있습니다.");
		
		
		}
		
		
		String sql2 = "insert into lprod " + " (lprod_id, lprod_gu, lprod_nm) " + " values(?, ?, ?)";
		pstmt = conn.prepareStatement(sql2);
		
		System.out.println("lprod_nm에 넣을 데이터를 입력하세요  >>>");
		String nm = s.nextLine();
		
		pstmt.setInt(1, idMax);
		pstmt.setString(2, gu);
		pstmt.setString(3, nm);
		
		int cnt = pstmt.executeUpdate();
		System.out.println(cnt);
		
		
		}catch(ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패!!");
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(stmt != null) try {stmt.close();}catch(SQLException e) {}
			if(pstmt != null) try {stmt.close();}catch(SQLException e) {}
			if(conn != null) try {conn.close();}catch(SQLException e) {}
		}
		}
}
