package com.tinderclone.server.core;
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;

import com.tinderclone.common.entity.Profile;

import java.sql.Timestamp;

public class DataManager{
	
	//Define each of the databases
	private DataStore userDB;
	private DataStore profileDB;
	private DataStore matchDB;
	private DataStore locationDB;

	public DataManager() {
		
		//Instantiate each of the databases
		this.userDB = new DataStore("user_db.txt");
		this.profileDB = new DataStore("profile_db.txt");
		this.matchDB = new DataStore("match_db.txt");
		this.locationDB = new DataStore("location_db.txt");
		long now = System.currentTimeMillis();
		Timestamp curr = new Timestamp(now);
	}
	
	//Checks user credentials against userDB for a login action
	public Boolean login(String user,String pass) {

		Boolean valid = false;
		
		//Create a parameter hashmap with key -> value pairs
		HashMap<String,String> params = new HashMap<String,String>();
		
		//Set parameters and sends to DB
		//if it exists with those credentials it's valid
		params.put("user_name",user);
		params.put("user_password",pass);
		valid = this.userDB.entryExists(params);
		
		return valid;
	}
	
	//Adds a profile to profileDB
	public Boolean addProfile(String user) {
		
		Boolean valid;
		
		//Create a parameter hashmap with key -> value pairs
		HashMap<String,String> params = new HashMap<String,String>();
		
		//Get the user_id via the user_name
		String uid = this.getUserID(user);
		
		//Determine new profile_id by counting the current number of 
		//profiles already in the profileDB
		int pid = this.profileDB.getAllEntries().size() + 1;
		
		//Set all of the parameters according to the fields in profileDB
		params.put("profile_id",Integer.toString(pid));
		params.put("profile_uid",uid);
		params.put("profile_fname",user);
		params.put("profile_lname","");
		params.put("profile_gender","male");
		params.put("profile_age",Integer.toString(pid*3));
		params.put("profile_pref_distance",Integer.toString(new Random().nextInt(750)));
		
		//Specify header fields in an array for the addEntry method
		String[] headers = {"profile_id","profile_uid","profile_fname","profile_lname","profile_age","profile_gender","profile_pref_distance"};
		valid = this.profileDB.addEntry(params,headers);	
		return valid;
	}
	
	//Adds a new match to matchDB
	public Boolean addMatch(String user, String pid, String option) {
		
		Boolean valid = false;
		
		//Create a parameter hashmap with key -> value pairs
		HashMap<String,String> params = new HashMap<String,String>();
		
		//Get the user_id via the user_name
		String uid1 = this.getUserID(user);
		
		//Get the ratee user_id by supplying the profile_id
		params.put("profile_id",pid);
		String uid2 = this.profileDB.getDataEntryList(params).currentEntry().getByField("profile_uid");
		params.clear();
		
		//Set the rater and ratee ids and check if a match already exists in matchDB
		params.put("match_rater",uid1);
		params.put("match_ratee",uid2);
		Boolean exist = this.matchDB.entryExists(params);
		params.clear();

		//If this is a new match, continue 
		if (!exist) {
			//Retrieve the next match_id by counting the entries already in matchDB
			int mid = this.matchDB.getAllEntries().size() + 1;
			
			//Set the params needed by the matchDB to create a match
			params.put("match_id",Integer.toString(mid));
			params.put("match_rater",uid1);
			params.put("match_ratee",uid2);
			params.put("match_option",option);
			
			//Store headers in an array for the addEntry method
			String[] headers = {"match_id","match_rater","match_ratee","match_option"};
			valid = this.matchDB.addEntry(params,headers);	
		}
		return valid;
	}	
	
	//Adds a user to userDB
	public Boolean register(String user,String pass) {
		
		Boolean valid = false;
		
		//Create a parameter hashmap with key -> value pairs
		HashMap<String,String> params = new HashMap<String,String>();
		
		//Check to see if user_name already exists in userDB
		params.put("user_name",user);
		Boolean found = this.userDB.entryExists(params);
		
		//If an account with that user_name does not already exist, continue
		if (!found) {
			
			//Retrieve the next user_id by counting the entries already in userDB
			int uid = this.userDB.getAllEntries().size() + 1;
			
			//Set the params needed by the userDB to create a new user
			params.clear();
			params.put("user_id",Integer.toString(uid));
			params.put("user_name",user);
			params.put("user_password",pass);
			String[] headers = {"user_id","user_name","user_password"};
			
			//Adds user to userDB and stores result in return value
			valid = this.userDB.addEntry(params,headers);
			
			//Set return value to result of the final profile add
			if (valid) {
				valid = this.addProfile(user);
			}
		}
		return valid;
	}
	
	//Retrieves a profile from profileDB via user_name
	public Profile getProfile(String name) {
		//Create a parameter hashmap with key -> value pairs
		HashMap<String,String> params = new HashMap<String,String>();
		
		//Get the user_id via the user_name
		String uid = this.getUserID(name);
		
		params.put("profile_uid",uid);
		Boolean found = this.profileDB.entryExists(params);
		Profile prof = null;
		
		//If profile exists, store in return object
		if (found) {
			DataEntry de = this.profileDB.getDataEntryList(params).currentEntry();
			prof = new Profile(de.getByField("profile_fname"));
		}
		return prof;
	}
	
	//Edits profile data for a given user using a provided Profile object
	public Boolean editProfile(String name, Profile prof) {
		//Create a parameter hashmap with key -> value pairs
		HashMap<String,String> params = new HashMap<String,String>();
		
		//Get the user_id via the user_name
		String uid = this.getUserID(name);
		
		//params.put("profile_gender","male");
		Boolean success = this.profileDB.setDataEntry("profile_uid",uid,params);

		return success;
	}
	
	//Returns a list of profile objects of online users with no distance restrictions
	public ArrayList<Profile> getMatches(String name) {
		
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		DataEntryList results = null;
		
		ArrayList<String> matched = new ArrayList<String>();
		
		//Create a parameter hashmap with key -> value pairs
		HashMap<String,String> params = new HashMap<String,String>();
		
		String uid = this.getUserID(name);
		
		//Retrieve user ids of profiles already rated by the specified user
		//and filter from return list
		params.put("match_rater",uid);
		results = this.matchDB.getDataEntryList(params);
		params.clear();
		if (results!=null) {
			DataEntry de = results.currentEntry();
			matched = new ArrayList<String>();
			while (de != null) {
				matched.add(de.getByField("match_ratee"));
				de = results.getNextEntry();
			}
		}		
		
		//Test parameter to retrieve only female profiles
		params.put("profile_gender","female");
		results = this.profileDB.getDataEntryList(params);
		params.clear();
		
		if (results!=null) {
			DataEntry de = results.currentEntry();
			profiles = new ArrayList<Profile>();
			while (de != null) {
				if (!matched.contains(de.getByField("profile_uid"))) {
					profiles.add(new Profile(de.getByField("profile_fname") + " " + de.getByField("profile_lname")));
				}
				de = results.getNextEntry();
			}
		}
		return profiles;
	}
	
	//Returns a list of Profile objects of online users within the local range of the specified user
	//The input HashMap is a list of users online at the moment
	public ArrayList<Profile> getNearbyMatches(String user, HashMap<String,String> users) {
		
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		DataEntryList results = null; 
		
		ArrayList<String> matched = new ArrayList<String>();
		
		//Create a parameter hashmap with key -> value pairs
		HashMap<String,String> params = new HashMap<String,String>();
		
		//Get the user_id via the user_name
		String uid = this.getUserID(user);
		
		//Retrieve user ids of profiles already rated by the specified user
		//and filter from return list		
		params.put("match_rater",uid);
		results = this.matchDB.getDataEntryList(params);
		params.clear();
		if (results!=null) {
			DataEntry de = results.currentEntry();
			matched = new ArrayList<String>();
			while (de != null) {
				matched.add(de.getByField("match_ratee"));
				de = results.getNextEntry();
			}
		}	
		
		for(String name : users.keySet()) {
			params.clear();
			params.put("user_name",name);
			results = this.userDB.getDataEntryList(params);
			uid = this.getUserID(name);
			params.clear();
			if (results!=null) {
				params.clear();
				params.put("profile_uid",uid);
				if (!matched.contains(uid)) {
					Boolean found = this.profileDB.entryExists(params);
					DataEntry de = this.profileDB.getDataEntryList(params).currentEntry();
					if (found != null) {
						profiles.add(new Profile(de.getByField("profile_fname") + " " + de.getByField("profile_lname")));
					}
				}
			}
		}

		return profiles;
	}
	
	//Retrieves user_id from userDB for a given user_name
	private String getUserID(String name) {
		//Create a parameter hashmap with key -> value pairs
		HashMap<String, String> params = new HashMap<String, String>();
		
		params.put("user_name", name);
		String uid = this.userDB.getDataEntryList(params).currentEntry().getByField("user_id");
		params.clear();
		
		return uid;
	}
	
	//Returns maximum specified search range for a given user
	public int getRange(String name) {
		//Create a parameter hashmap with key -> value pairs
		HashMap<String, String> params = new HashMap<String, String>();
		
		String uid = this.getUserID(name);
		
		params.put("profile_uid", uid);
		DataEntryList results = this.profileDB.getDataEntryList(params);
		if (results != null) {
			return Integer.parseInt(results.currentEntry().getByField("profile_pref_distance"));
		}
		return -1;
	}
	
	//Returns a random location string from the locationDB
	public String getRandomLocation() {
		DataEntryList locations = this.locationDB.getAllEntries();
		int index = new Random().nextInt(locations.size());
		String location = locations.getEntryAt(index).getByField("location_origin");
		return location;
	}
	
	//Returns the distance between two given cities
	public int getDistance(String origin, String destination) {
		origin = origin.toLowerCase();
		destination = destination.toLowerCase();
		if (origin.compareTo(destination)==0) {
			return 0;
		}
		
		//Create a parameter hashmap with key -> value pairs
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("location_origin", origin);
		params.put("location_destination", destination);
		DataEntryList del = this.locationDB.getDataEntryList(params);
		if (del!=null) {
			int d = Integer.parseInt(del.currentEntry().getByField("location_distance"));
			return d;
		} else {
			return -1;
		}
	}
	
	
}