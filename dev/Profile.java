import java.io.*;
import java.util.*;
import java.io.Serializable;

public class Profile implements Serializable{
	
	private HashMap<String,Attribute> attributes;
	private Picture user_pic;
	private String uid;
	private String city;
	
	public Profile(String uid, String fname, String lname, String age, String gender, String distance, String age_pref, String gender_pref){
		this.uid = uid;
		this.city = "";
		this.attributes = new HashMap<String,Attribute>();
		this.attributes.put("profile_fname",new Attribute("profile_fname",fname,""));
		this.attributes.put("profile_lname",new Attribute("profile_lname",lname,""));
		this.attributes.put("profile_age",new Attribute("profile_age",age,age_pref));
		this.attributes.put("profile_gender",new Attribute("profile_gender",gender,gender_pref));
		this.attributes.put("profile_distance",new Attribute("profile_distance",distance,distance));
		
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
		//System.out.println("changing " + name + " to " + value + " --------------");
		name = "profile_" + name;
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
			this.attributes.forEach((String name,Attribute attr) -> return_list.put(("profile_pref_" + name.substring(8)),attr.getPrefValue()));
		}else{
			this.attributes.forEach((String name,Attribute attr) -> return_list.put(name,attr.getUserValue()));
		}
		return return_list;
	}
	
	public Picture getPicture(){
		return this.user_pic;
	}
	
	public String getUID(){
		return this.uid;
	}
	
	public void setCity(String c){
		this.city = c;
	}
	
	public String getCity(){
		return this.city;
	}
	
	public void print(){
		System.out.println("Printing profile of... " + this.getAttribute("profile_fname",false));
	}
	
}