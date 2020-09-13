package game;

import java.util.Random;

import db.AppUtil;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import task.TimerThread;
import views.PopupController;

/* 
 * 과제 1. 지뢰찾기가 재귀함수를 이용해서 펼쳐지는 것을 구현해봅시다. 먼저풀면 영상녹화해서 카톡으로 선착순 2명까지
 * 과제 2. 알림창을 구현
 * 과제 3. 현재 남아있는 깃발 갯수를 출력하는 라벨을 만드세요.
 * 
 * 지뢰찾기 로드맵
 * => 쓰레드 => 타이머앱을 개발 => 지뢰찾기에 적용 => 클리어시의 기록을 만들어 => DB 에 저장 => 게임접속시 Top10레코드 나올수록
 * => 테트리스 =>
 */

public class MainGame {
	private GraphicsContext gc;
	private int gap = 5;  //지뢰찾기 블럭간의 갭 
	private int size = 25; //지뢰찾기 블럭의 가로세로 크기
	private int boardSize = 10;
	
	private Integer[][] board;
	private Integer[][] reveal;
	
	private boolean debug = false;
	private int mineCnt = 10; //초기 지뢰의 개수
	private int flagCnt = 0; //꽂은 깃발 개수
	private boolean gameOver = false; //게임오버 상태를 저장하는 변수
	private TimerThread tt;
	
	public MainGame(GraphicsContext gc) {
		this.gc = gc;
		initGame();
	}
	
	public MainGame(GraphicsContext gc, int gap, int size, TimerThread tt) {
		this.gc = gc;
		this.gap = gap;
		this.size = size;
		this.tt = tt;
		initGame();
	}
	
	public void setGameOver(String msg) {
		gameOver = true;
		tt.setQuit();
//		System.out.println(tt.time);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/views/PopupLayout.fxml"));
		try {
			AnchorPane root = loader.load();
			
			PopupController pc = loader.getController();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			pc.SetData(tt.time, stage);
			
			stage.showAndWait();
			
		}catch(Exception e) {
			e.printStackTrace();
			AppUtil.alert("팝업창 로딩둥 오류 발생", "에러 발생");
		}
	}
	
	public void initGame() {
		board = new Integer[boardSize][boardSize];
		reveal = new Integer[boardSize][boardSize];
		
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				board[i][j] = 0;
				reveal[i][j] = MineStatus.LOCKED;
			}
		}
		
		Random rnd = new Random();
		
		int blockCnt = boardSize * boardSize;
		int[] minePos = new int[blockCnt];
		for(int i = 0; i < blockCnt; i++) {
			minePos[i] = i;
		}
		
		for(int i = 0; i < mineCnt; i++) {
			int idx = rnd.nextInt(blockCnt - i);
			int pos = minePos[idx];
			minePos[idx] = minePos[blockCnt - i - 1];
			
			int x = pos / 10;  //행
			int y = pos % 10; //열
			
			board[x][y] = -1;
		}
		
		//지뢰의 위치에 맞게 수치들을 다 조정해줘야 해
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				if(board[i][j] == -1) continue;
				board[i][j] = checkCount(j, i);
			}
		}
	}
	
	private int checkCount(int x, int y){
		int cnt = 0; //지뢰의 갯수
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(x + j < 0 || x + j >= boardSize || y + i < 0 || y + i >= boardSize || (i == 0 && j == 0))
					continue;
				
				if(board[y+i][x+j] == -1) cnt++;  //내주변 칸중에 하나가 -1이라면 지뢰 증가
			}
		}
		return cnt;
	}
	
	public void render() {
		
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setStroke(Color.WHITE);
		
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				int x = (j+1) * gap + j * size;
				int y = (i+1) * gap + i * size;
				
				gc.setFill(Color.rgb(86, 98, 112));
				gc.fillRect(x, y , size, size);
				
				gc.setFill(Color.rgb(165, 147, 224));
				gc.fillRect(x + 2, y + 2, size - 4, size - 4);
				
				if(debug ) {
					gc.strokeText(board[i][j].toString(), x + size / 2, y + size / 2 );
				}else if(reveal[i][j] == MineStatus.REVEAL) {
					gc.strokeText(board[i][j].toString(), x + size / 2, y + size / 2 );
				}else if(reveal[i][j] == MineStatus.FLAGED) {
					gc.setFill(Color.rgb(40,  89,  67));
					gc.fillRoundRect(x + 6, y + 6, size - 12, size - 12, 3, 3);
				}
				
			}
		}
	}
	
	public void clickHandle(MouseEvent e){
		if(gameOver) return;  //게임오버시에는 입력을 받지 않는다.
		
		double mouseX = e.getX();  //마우스의 클릭된 좌표를 알아낸다.
		double mouseY = e.getY();
		
		int blockSize = gap + size;
		
		if(mouseX % blockSize < gap || mouseY % blockSize < gap) {
			return; //경계 클릭은 처리하지 않는다.
		}
		
		int x = (int)(mouseX / blockSize);  //x는 j하고 연동되는 거다.
		int y = (int)(mouseY / blockSize);  //y는 i하고 연동되는 거고
		
		if( x >= boardSize || y >= boardSize) {
			return; //게임판을 벗어난것은 처리하지 않는다.
		}
		
		System.out.println(board[y][x] + "가 클릭되었습니다.");
		
		MouseButton btn = e.getButton();
		if(btn == MouseButton.SECONDARY) {
			rightClick(x, y);
		}else if(btn == MouseButton.PRIMARY){
			leftClick(x, y);
		}
		render();
	}
	
	private void leftClick(int x, int y) {
		if(reveal[y][x] == MineStatus.REVEAL || reveal[y][x] == MineStatus.FLAGED) {
			return;
		}
		
		reveal[y][x] = MineStatus.REVEAL;
		
		if(board[y][x] == -1) {
			//게임오버는 sysout이 아닌 다른것으로 교체해야 합니다.
			setGameOver("게임오버");
		}
	}
	
	private void rightClick(int x, int y) {
		if(reveal[y][x] == MineStatus.FLAGED) {
			reveal[y][x] = MineStatus.LOCKED;
			flagCnt--;
			return;
		}
		
		if(flagCnt >= mineCnt) {
			System.out.println("설정할 수 있는 깃발의 최대치를 초과했습니다");
			return;
		}
		
		if(reveal[y][x] == MineStatus.REVEAL) {
			System.out.println("해당 위치에는 깃발을 꽂을 수 없습니다.");
			return;
		}
		
		reveal[y][x] = MineStatus.FLAGED;
		flagCnt++;
		
		//게임이 클리어 되었는지 확인하는 로직을 넣어주어야 해.
		if(checkGameClear()) {
			setGameOver("게임 클리어");
			return;
		}
	}
	
	private boolean checkGameClear() {
		if(flagCnt != mineCnt) return false;
		//게임 클리어 로직 써야 해
		int cnt = 0;
		for(int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if(board[i][j] == -1 && reveal[i][j] == MineStatus.FLAGED)
					cnt++;
			}
		}
		
		return cnt == mineCnt;
	}
}
