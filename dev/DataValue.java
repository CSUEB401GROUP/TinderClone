package com.tinderclone.common.entity;

import java.io.Serializable;

public class DataValue implements Serializable {

	private static final long serialVersionUID = -1195639008049531271L;
	
	private String name;
	private String value;

	public DataValue(String n, String v) {
		this.name = n;
		this.value = v;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
