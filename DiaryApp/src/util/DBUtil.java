package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection connection = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		String connectionString = "jdbc:mysql://gondr.asuscomm.com/yy_40112"
				+ "?useUnicode=true"
				+ "&characterEncoding=utf8"
				+ "&useSSL=false"
				+ "&serverTimezone=Asia/Seoul";
		String userId ="yy_40112";
		String password = "1234";
		
		connection = DriverManager.getConnection(connectionString, userId, password);
		return connection;
				
	}
	
	public static void close(Connection c) {
		if(c != null) try {c.close();}catch (Exception e) {	}
	}
	
	public static void close(PreparedStatement c) {
		if(c != null) try {c.close();}catch (Exception e) {	}
	}
	
	public static void close(ResultSet c) {
		if(c != null) try {c.close();}catch (Exception e) {	}
	}
}
