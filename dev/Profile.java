import java.io.*;
import java.util.*;
import java.io.Serializable;

public class Profile implements Serializable{
	
	private HashMap<String,Attribute> attributes;
	private Picture user_pic;
	
	public Profile(String fn, String ln, String a, String g){
		this.attributes = new HashMap<String,Attribute>();
		this.attributes.put("profile_fname",new Attribute("profile_fname",fn));
		this.attributes.put("profile_lname",new Attribute("profile_lname",ln));
		this.attributes.put("profile_age",new Attribute("profile_age",a));
		this.attributes.put("profile_gender",new Attribute("profile_gender",g));
		this.user_pic = new Picture("male_small.jpg");
	}
	public String getAttribute(String name, Boolean pref){
		if(pref){
			return this.attributes.get(name).getPrefValue();
		}else{
			return this.attributes.get(name).getUserValue();
		}
	}	
	public void editAttribute(String name,String value,Boolean pref){
		if(pref){
			Attribute a = this.attributes.get(name);
			a.setPrefValue(value);
		}else{
			Attribute a = this.attributes.get(name);
			a.setUserValue(value);
		}
	}
	
	public HashMap<String,String> getAllAttributes(Boolean pref){
		HashMap<String,String> return_list = new HashMap<String,String>();
		if(pref){
			this.attributes.forEach((String name,Attribute attr) -> return_list.put(name,attr.getPrefValue()));
		}else{
			this.attributes.forEach((String name,Attribute attr) -> return_list.put(name,attr.getUserValue()));
		}
		return return_list;
	}
	
	public Picture getPicture(){
		return this.user_pic;
	}
	
	public void print(){
		System.out.println("Printing profile of... " + this.getAttribute("profile_fname",false));
	}
	
}