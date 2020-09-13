package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vo.Diary;

public class PopupController {
	
	private Diary item;
	
	@FXML
	private TextField txtTitle;
	@FXML
	public TextArea txtContent;
	
	public void setContent(Diary d) {
		//데이터베이 연동관련해서 작업합니다.
		this.item = d;
		
		//데이터 로드를 여기서 해주면 된다.
		txtTitle.setText(item.getTitle());
		txtContent.setText(item.getContent());
	}
	
	public void close(ActionEvent event) {
		Node source = (Node)event.getSource();
		Stage stage = (Stage)source.getScene().getWindow();
		stage.close();
	}
}
