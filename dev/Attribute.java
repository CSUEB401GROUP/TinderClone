package com.tinderclone.common.entity;

import java.io.Serializable;

public class Attribute implements Serializable {
	
	private static final long serialVersionUID = -8107936620925255755L;

	private String name;
	
	private int age;
	private String gender;
	private String bio;
	private String userValue;
	private String prefValue;
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public int getAge() { return age; }
	public void setAge(int age) { this.age = age; }
	
	public String getGender() { return gender; }
	public void setGender(String gender) { this.gender = gender; }
	
	public String getBio() { return bio; }
	public void setBio(String bio) { this.bio = bio; }
	
	public String getUserValue() { return userValue; }
	public void setUserValue(String userValue) { userValue = userValue; }
	
	public String getPrefValue() { return prefValue; }
	public void setPrefValue(String prefValue) { prefValue = prefValue; }
	
}
