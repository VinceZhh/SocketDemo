import java.io.Serializable;

public class Info implements Serializable{
	private String From;
	private int type;
	private String Info;
	private String TO;
	
	public String getFrom() {
		return From;
	}
	public void setFrom(String from) {
		From = from;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getInfo() {
		return Info;
	}
	public void setInfo(String info) {
		Info = info;
	}
	public String getTO() {
		return TO;
	}
	public void setTO(String tO) {
		TO = tO;
	}
}
