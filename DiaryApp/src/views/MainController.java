package views;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.AppUtil;
import util.DBUtil;
import vo.Diary;

public class MainController {
	@FXML
	private DatePicker datePicker;
	@FXML
	private TextField txtTitle;
	@FXML
	private TextArea txtContent;
	
	@FXML
	private ListView<Diary> listView;
	
	private ObservableList<Diary> items;
	
	@FXML
	private void initialize() {
		//초기화 함수로 최초 자동실행된다.
		items = FXCollections.observableArrayList();
		listView.setItems(items);
		
		loadList();		
	}
	
	private void loadList(){
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = DBUtil.getConnection();
			pstmt = connection.prepareStatement("SELECT * FROM todos");
			rs = pstmt.executeQuery(); 
			
			while(rs.next()) {
				Integer id = rs.getInt("id");
				String title = rs.getString("title");
				String content = rs.getString("content");
				LocalDate date = rs.getDate("date").toLocalDate();
				Diary d = new Diary(id,date, title, content);
				items.add(d);
			}
			
		} catch (Exception e) {
			AppUtil.alert("데이터베이스 로드중 오류 발생", "로딩 에러");
			e.printStackTrace();
		} finally {
			DBUtil.close(rs); //아직 없어서 에러 난다.
			DBUtil.close(pstmt);
			DBUtil.close(connection);
		}
	}
	
	public void add() {
		//추가하는 매서드 만들어보자.
		String title = txtTitle.getText(); // 제목 가져오고
		String content = txtContent.getText(); //내용 가져와주고
		LocalDate date = datePicker.getValue(); //날자 가져와서
		
		if(title.equals("") || content.equals("") || date == null) {
			AppUtil.alert("필수 값을 입력하세요", "에러");
			return;
		}
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		
		try {
			connection = DBUtil.getConnection();
			String sql = "INSERT INTO todos(title, content, date) VALUES (?, ?, ?)";
			pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			//오토인크리먼트된 키를 반환하도록 옵션을 넣어준다.
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setDate(3, Date.valueOf(date));
			int cnt = pstmt.executeUpdate(); //int값은 변경된 갯수를 의미한다.
			
			if(cnt == 0) {
				AppUtil.alert("데이터베이스 삽입중 오류 발생", "에러");
				return;
			}
			ResultSet key = pstmt.getGeneratedKeys();
			key.next();
			int id = key.getInt(1);
			
			Diary d = new Diary(id, date, title, content);
			txtTitle.setText("");
			txtContent.setText("");
			datePicker.setValue(null);
			items.add(d);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppUtil.alert("데이터베이스 삽입중 오류 발생", "에러");
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(connection);
		}
		
		
	}
	
	public void del() {
		//리스트뷰에서 골라서 그녀석을 삭제하는거야.
		//지금현재 리스트뷰에서 선택된게 누군지 알아야 해.
		int idx = listView.getSelectionModel().getSelectedIndex();
		
		//만약 아무것도 선택안했다면 경고창을 띄운다.
		if(idx < 0) {
			AppUtil.alert("삭제하고자 하는 아이템을 선택하세요", "선택에러");
			return;
		}
		
		Diary d = items.get(idx);  //삭제할 녀석의 정보가 나온거
		Integer id = d.getId();  //삭제할 녀석의 id값을 알아 낸다.
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			
			connection = DBUtil.getConnection();
			String sql = "DELETE FROM todos WHERE id = ?";
			pstmt = connection.prepareStatement(sql);
			//프리페어한 pstmt에 id를 바인딩 시켜줘야 하고
			pstmt.setInt(1, id);
			//바인딩한 후 executeUpdate or executeQuery중에 적절한 것을 골라 실행하고
			int cnt = pstmt.executeUpdate();
			//결과가 성공적으로 삭제되었다면 int로 카운트값이 반환. 이게 0이아니면 성공한것이니까
			if(cnt == 0) {
				AppUtil.alert("삭제중 오류가 발생했습니다.", "에러");
				return;
			}
			//리스트에서도 삭제해주면 돼. items에서 idx번째애를 삭제하면 된다.
			items.remove(idx);
		} catch (Exception e) {
			AppUtil.alert("삭제중 오류가 발생했습니다.", "에러");
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(connection);
		}

	}
	
	
	public void openDiary() {
		int idx = listView.getSelectionModel().getSelectedIndex();
		//만약 아무것도 선택안했다면 경고창을 띄운다.
		if(idx < 0) {
			AppUtil.alert("보고자 하는 아이템을 선택하세요", "선택에러");
			return;
		}
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/views/PopupLayout.fxml"));
			AnchorPane popup = loader.load();
			
			PopupController p = loader.getController();
			p.setContent(items.get(idx));
			
			Scene scene = new Scene(popup);
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.showAndWait();
			
		} catch (Exception e) {
			AppUtil.alert("팝업창 로드중 오류가 발생했습니다.", "로드에러");
			return;
		}
		
	}
	
}
