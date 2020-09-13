package views;

import java.sql.Connection;
import java.sql.PreparedStatement;

import db.AppUtil;
import db.DBUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupController {
	@FXML
	private Label scoreLabel;
	@FXML
	private TextField txtName;
	
	private long score;
	private Stage stage;
	
	public void SetData(long score, Stage stage) {
		this.score = score;
		this.stage = stage;
		
		scoreLabel.setText("당신이 걸린 시간은 " + score /10.0 + "초 입니다.");
		txtName.requestFocus();
	}
	public void record() {
		String name = txtName.getText();
		if(name.trim().isEmpty()) {
			AppUtil.alert("이름은 필수 값입니다.", "필수값 에러");
			return;
		}
		try {
			Connection con = DBUtil.getConnection();
			
			String sql = "INSERT INTO score(name, score) VALUES(?, ?)";
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, name);
			pstmt.setLong(2, score);
			
			int row = pstmt.executeUpdate();
			
			if(row == 1) {
				close();
			}else {
				AppUtil.alert("데이터베이스 기록 중 오류", "에러");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			AppUtil.alert("데이터베이스 기록 중 오류", "기록에러");
		}
	}
	public void close() {
		stage.close();
	}

}
