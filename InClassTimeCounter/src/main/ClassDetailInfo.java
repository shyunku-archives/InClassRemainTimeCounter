package main;

public class ClassDetailInfo {
	private long startTime;	//오전 12시 기준 0
	private long endTime;
	private int day;
	private String place;
	
	public ClassDetailInfo(String day, String startT, String endT, String place) {
		this.day = Integer.parseInt(day);
		this.place = place;
		this.startTime = Integer.parseInt(startT)*5;
		this.endTime = Integer.parseInt(endT)*5;
	}
	
	private final String[] dayStr = {"월","화","수","목","금","토","일"};
	public String getDayInString() {
		return dayStr[this.day];
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String[] getDayStr() {
		return dayStr;
	}
	public long getRelativeStartTime() {
		return this.getDay()*86400000 + this.getStartTime()*60000;
	}
	
	public long getRelativeEndTime() {
		return this.getDay()*86400000 + this.getEndTime()*60000;
	}
}
