package views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import task.CountThread;

public class MainController {
	@FXML
	private Label hour;
	@FXML
	private Label minute;
	@FXML
	private Label second;
	
	
	private CountThread c = null;
	
	public void start() {
		if(c==null) {
			c = new CountThread(hour, minute, second);
			c.start();
		}
		else {
			c.go();
			//쓰레드에서 재싲가 해주도록
		}
	}
	
	public void pause() {
		if(c!=null) {
			c.pause();
		}
	}
	
	public void restart() {
		if(c!=null) {
			c.restart();
		}
	}
	
	public void stopThread() {
		if(c!=null) {
			c.setQuit();
		}
	}
}
