package vo;

import java.time.LocalDate;

public class Diary {
	private Integer id;
	private LocalDate date;
	private String title;
	private String content;
	
	public Diary() {
		
	}
	
	public Diary(Integer id, LocalDate date, String title, String content) {
		this.id = id;
		this.date = date;
		this.title = title;
		this.content = content;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}	
	
	@Override
	public String toString() {
		return title + "[" + date + "]";
	}
}
