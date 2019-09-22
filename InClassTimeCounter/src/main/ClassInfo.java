package main;

public class ClassInfo implements Comparable<ClassInfo>{
	private String professorName;
	private String className;
	private String classID;
	private String timeStr;
	private ClassDetailInfo cInfo;
	public ClassInfo(String pfName, String cName, String cID, String tstring, ClassDetailInfo cInfo) {
		this.professorName = pfName;
		this.className = cName;
		this.classID = cID;
		this.timeStr = tstring;
		this.cInfo = cInfo;
	}
	public String getProfessorName() {
		return professorName;
	}
	public void setProfessorName(String professorName) {
		this.professorName = professorName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassID() {
		return classID;
	}
	public void setClassID(String classID) {
		this.classID = classID;
	}
	public String getTimeStr() {
		return timeStr;
	}
	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}
	public ClassDetailInfo getcInfo() {
		return cInfo;
	}
	public void setcInfo(ClassDetailInfo cInfo) {
		this.cInfo = cInfo;
	}
	@Override
	public int compareTo(ClassInfo c) {
		long cur = this.getcInfo().getDay()* 1440 + this.getcInfo().getStartTime();
		long clo = c.getcInfo().getDay()*1440 + c.getcInfo().getStartTime();
		if(cur<clo) return -1;
		else if(cur>clo) return 1;
		return 0;
	}
}
