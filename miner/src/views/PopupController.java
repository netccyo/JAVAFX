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
		
		scoreLabel.setText("����� �ɸ� �ð��� " + score /10.0 + "�� �Դϴ�.");
		txtName.requestFocus();
	}
	public void record() {
		String name = txtName.getText();
		if(name.trim().isEmpty()) {
			AppUtil.alert("�̸��� �ʼ� ���Դϴ�.", "�ʼ��� ����");
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
				AppUtil.alert("�����ͺ��̽� ��� �� ����", "����");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			AppUtil.alert("�����ͺ��̽� ��� �� ����", "��Ͽ���");
		}
	}
	public void close() {
		stage.close();
	}

}
