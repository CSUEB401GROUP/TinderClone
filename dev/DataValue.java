import java.io.Serializable;

public class DataValue implements Serializable{
	private String name;
	private String value;
	
	public DataValue(String n, String v){
		this.name = n;
		this.value = v;
	}
	public void setName(String n){
		this.name = n;
	}
	public String getName(){
		return this.name;
	}
	public void setValue(String v){
		this.value = v;
	}
	public String getValue(){
		return this.value;
	}
}