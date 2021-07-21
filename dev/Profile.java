package com.tinderclone.common.entity;

import java.io.Serializable;

public class Profile implements Serializable {
	
	private static final long serialVersionUID = -3796265508510487052L;

	public Profile(String name) {
		super();
	}

	private Attribute attribute = new Attribute();
	private Picture picture;
	private Location location;
	private String ID;
	
	public void displayAll() {
		System.out.println(this.getAttribute().toString());
		System.out.println(this.getPicture().toString());
	}
	
	public Attribute getAttribute() {
		return this.attribute;
	}
	
	public void editAttribute(String name) {
		this.attribute = new Attribute();
		this.attribute.setName(name);
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		this.ID = iD;
	}

	public Picture getPicture() {
		return picture;
	}
	
	public Location getLocation() {
		return this.location;
	}
}
