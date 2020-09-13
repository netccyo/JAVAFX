package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import util.DBUtil;

public class SelectTest {
	public static void main(String[] args) {
		
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			connection = DBUtil.getConnection();
			String sql = "SELECT * FROM todos ORDER BY id DESC";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String content = rs.getString("content");
				LocalDate date = rs.getDate("date").toLocalDate();
				
				System.out.println(id + ", " + title + ", " + content + ", " + date);
			}
			
		} catch (Exception e) {
			System.out.println("아무튼 에러..");
		}
				
				
	}
}
