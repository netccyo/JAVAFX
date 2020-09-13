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
		
		GraphicsContext gc = canvas.getGraphicsContext2D(); //javascript의 ctx와 같은 애야
		tt = new TimerThread(secLabel);
		game = new MainGame(gc, 5, 30, tt); //만약 판의 크기나 기타를 조절할꺼면 파라메터로 다른것도 넣는다.
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
			
			list.clear(); //기존 리스트에 있는 값을 다 제거하고 새롭게 하나씩 넣어준다.
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
			AppUtil.alert("데이터베이스 연결 실패 : 스코어를 가져오지 못했습니다.", "경고");
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
		game.clickHandle(e); //클릭이벤트를 메인게임으로 전달한
	}
}
