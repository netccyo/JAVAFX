package views;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import application.ScoreVO;
import db.AppUtil;
import db.DBUtil;
import game.MainGame;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import task.TimerThread;

public class MainController {
	@FXML
	private Canvas canvas;
	@FXML
	private Label secLabel;
	
	private MainGame game;
	
	private TimerThread tt;
	
	@FXML
	private ListView<ScoreVO> listView;
	
	private ObservableList<ScoreVO> list = FXCollections.observableArrayList();
	
	@FXML
	private void initialize() {
		listView.setItems(list);
		getRecord();
	}
	
	public void gameStart() {
		
		GraphicsContext gc = canvas.getGraphicsContext2D(); //javascript�� ctx�� ���� �־�
		tt = new TimerThread(secLabel);
		game = new MainGame(gc, 5, 30, tt); //���� ���� ũ�⳪ ��Ÿ�� �����Ҳ��� �Ķ���ͷ� �ٸ��͵� �ִ´�.
		render();
		tt.start();
	}
	
	public void getRecord() {
		String sql = "SELECT * FROM score ORDER BY score ASC LIMIT 0, 10";
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			list.clear(); //���� ����Ʈ�� �ִ� ���� �� �����ϰ� ���Ӱ� �ϳ��� �־��ش�.
			while(rs.next()) {
				ScoreVO vo = new ScoreVO();
				int id = rs.getInt("id");
				vo.setId(id);
				String name = rs.getString("name");
				vo.setName(name);
				long time = rs.getLong("score");
				vo.setTime(time);
				
				list.add(vo);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AppUtil.alert("�����ͺ��̽� ���� ���� : ���ھ �������� ���߽��ϴ�.", "���");
			return;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
			DBUtil.close(con);
		}
		
	}
	
	private void render() {
		game.render();
	}
	
	public void clickHandle(MouseEvent e) {
		game.clickHandle(e); //Ŭ���̺�Ʈ�� ���ΰ������� ������
	}
}
