package com.tinderclone.server.core;
import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

public class DataEntryList{
	private Integer index;
	private ArrayList<DataEntry> entries;
	private ArrayList<String> keys;
	
	public DataEntryList() {
		index = 0;
		entries = new ArrayList<DataEntry>();
		keys = new ArrayList<String>();
	}
	
	public DataEntryList(ArrayList<HashMap<String,String>> row_list) {
		index = 0;
		entries = new ArrayList<DataEntry>();
		keys = new ArrayList<String>();
		if (row_list.size()>0) {
			row_list.forEach((row) -> addEntry(new DataEntry(row)));
			if (keys.size()==0) {
				row_list.get(0).forEach((key,value) -> keys.add(key));
			}
		}
	}
	
	public void addEntry(DataEntry entry) {
		entries.add(entry);
	}
	
	public DataEntry getNextEntry() {
		DataEntry de = null;
		if (index<entries.size()-1) {
			index++;
			de = entries.get(index);
		}
		return de;
	}
	
	public DataEntry currentEntry() {
		if (entries.size()>0 && index <= entries.size()-1) {
			return entries.get(index);
		} else {
			return null;
		}
	}
	
	public DataEntry getEntryAt(Integer i) {
		DataEntry de;
		de = entries.get(i);
		return de;
	}
	
	public int size() {
		return entries.size();
	}
	
	public void print() {
		if (entries.size()>0) {
			this.printKeys();
			System.out.println();
			entries.forEach((de) -> de.print());
		}
		System.out.println();
	}
	
	public void printKeys() {
		for(int i=0;i<keys.size();i++) {
			String key = keys.get(i);
			key = key.length() > 14 ? (key + "\t\t") : key;
			System.out.print(key + "\t");
		}		
	}
	
}