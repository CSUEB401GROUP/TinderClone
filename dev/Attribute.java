import java.io.Serializable;

public class Attribute implements Serializable {
	
	String name;
	String user_value;
	String pref_value;
	
	public Attribute(String n, String uv){
		this.name = n;
		this.user_value = uv;
		this.pref_value = uv;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String n){
		this.name = n;
	}
	
	public String getUserValue(){
		return this.user_value;
	}
	
	public void setUserValue(String uv){
		this.user_value = uv;
	}
	
	public String getPrefValue(){
		return this.pref_value;
	}
	
	public void setPrefValue(String pv){
		this.pref_value = pv;
	}	
	
}