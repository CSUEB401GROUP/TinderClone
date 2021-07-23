import java.util.*;

public class FilterSetting {
	
	String name;
	String value;
	
	public FilterSetting(String n, String v){
		this.name = n;
		this.value = v;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String n){
		this.name = n;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public void setValue(String v){
		this.value = v;
	}	
	
	public Boolean compare(String attr){
		return this.getValue().compareTo(attr)==0;
	}
}