package task;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class CountThread extends Thread{
	private Label hour;
	private Label minute;
	private Label second;
	
	private boolean stop = false;
	private int sec = 0;
	private boolean quit = false;
	
	public void setQuit() {
		quit = true;
	}
	
	public void pause() {
		stop = true;
	}
	
	public void go() {
		stop = false;
	}
	public void restart() {
		sec = 0;
		if(stop) {
			stop = false;
		}
	}
	
	
	public CountThread(Label hour, Label minute, Label second) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}
	@Override
	public void run() {
		while(!quit) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(!stop) {
				sec++;	
			}
			
			int hour = sec/3600;
			int min = sec%3600/60; 
			int second = sec%60;
			
			String hStr = hour < 10 ? "0"+hour : "" + hour;
			String mStr = min < 10 ? "0"+min : "" + min;
			String sStr = second < 10 ? "0"+second : "" + second;
			
			//시간이나 초가 표시될때 00 00 00 형태로 나오게
			Platform.runLater(() -> {
				this.hour.setText(hStr);
				this.minute.setText(mStr);
				this.second.setText(sStr);
			});
		}
	}
}
