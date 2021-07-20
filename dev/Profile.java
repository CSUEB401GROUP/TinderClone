import java.io.*;
import java.io.Serializable;

public class Profile implements Serializable{
	private String name;
	public Profile(String n){
		this.name = n;
	}
	public String getID(){
		return "6";
	}	
	public String getName(){
		return this.name;
	}
	public void setName(String n){
		this.name = n;
	}
	public void print(){
		System.out.println("Printing profile of... " + this.getName());
	}
}