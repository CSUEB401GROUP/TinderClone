import java.io.Serializable;

public class Picture implements Serializable {

	String path;

	public Picture(String p){
		this.path = p;
	}
	
	public String getPath(){
		return this.path;
	}
	
	public void setPath(String p){
		this.path = p;
	}

}