package application;

public class ScoreVO {
	private int id;
	private String name;
	private long time;
	
	
	@Override
	public String toString() {
		return time / 10.0 + "√ :" + name; 
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	
}
