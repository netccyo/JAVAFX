package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import util.DBUtil;

public class MainApp {
	public static void main(String[] args) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = DBUtil.getConnection();
			String sql = "INSERT INTO todos(title, content, date) VALUES (?, ?, ?)";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, "제목입니다3");
			pstmt.setString(2, "내용입니다3");
			pstmt.setDate(3, Date.valueOf( LocalDate.now() ));
			pstmt.executeUpdate();
			
			System.out.println("입력이 완료되었습니다.");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 파일이 존재하지 않습니다.");
		} catch (SQLException e) {
			System.out.println("데이터베이스 연결실패");
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(connection);
		}
		
	}
}
