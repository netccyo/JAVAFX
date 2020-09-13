package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AppUtil {
	public static void alert(String msg, String title) {
		Alert a = new Alert(AlertType.ERROR);
		a.setTitle(title);
		a.setHeaderText(null);
		a.setContentText(msg);
		a.show();
	}
}
