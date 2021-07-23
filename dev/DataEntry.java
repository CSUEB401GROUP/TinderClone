import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

public class DataEntry{
	
	private HashMap<String,String> values;
	private ArrayList<String> keys;
	
	public DataEntry(){
		values = new HashMap<String,String>();
		keys = new ArrayList<String>();
	}
	
	public DataEntry(HashMap<String,String> row){
		values = new HashMap<String,String>();
		keys = new ArrayList<String>();
		row.forEach((key,value) -> addValue(key,value));
	}
	
	public void addValue(String key, String value){
		values.put(key,value);
		keys.add(key);
	}
	
	public String getByField(String field){
		String value = values.get(field);
		return value;
	}
	
	public void setByField(String field,String value){
		values.put(field,value);
	}
	
	public void print(){
		keys.forEach((key) -> System.out.print(values.get(key) + "\t\t"));
		System.out.println();
	}
	
	public void printKeys(){
		for(int i=0;i<keys.size();i++){
			String key = keys.get(i);
			key = key.length() > 8 ? (key + "\t") : key;
			System.out.print(key + "\t");
		}
	}
}