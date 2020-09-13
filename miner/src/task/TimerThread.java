package task;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class TimerThread extends Thread {
	
	public long time = 0;
	private boolean quit = false;
	
	private Label timeLabel;
	
	public TimerThread(Label timeLabel) {
		this.timeLabel = timeLabel;
	}
	public void setQuit() {
		quit = true;
	}
	//15∏∏ø¯ 
	@Override
	public void run() {
		
		while(!quit) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			time ++;
			
			Platform.runLater( () -> {
				timeLabel.setText( time / 10.0 + "√ ");
			});
		}
		
	}
}
