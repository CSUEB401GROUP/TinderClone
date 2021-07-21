package com.tinderclone.client;

import com.tinderclone.common.entity.Profile;
import com.tinderclone.common.entity.ProfileList;

public class Tester {
	
	public static void main(String[] args) {

		Tester tester = new Tester();
		tester.registerTest();
		tester.loginTest();
		tester.editProfileTest();
		tester.findMatchTest();
		tester.addMatchTest();
		tester.logoutTest();
	}

	private void registerTest() {
		//register test
		String userid = "djelloul";
		String userpwd = "test";
		boolean result = Global.authciation.getRegister().register(userid, userpwd);
		System.out.println("register test is" + (result ? "success" : "failed"));
	}
	
	
	private void loginTest() {
		//login test
		String userid = "djelloul";
		String userpwd = "test";
		boolean result = Global.authciation.getLogin().login(userid, userpwd);
		System.out.println("login test is" + (result ? "success" : "failed"));
	}


	private void editProfileTest() {
		boolean result = Global.user.editAttribute(new Profile("jim"));
		System.out.println("edit profile test is" + (result ? "success" : "failed"));
	}

	private void addMatchTest() {
		Global.match.addMatch(null);
	}

	private void findMatchTest() {

		//Create ArrayList to hold potential match profiles
		ProfileList profiles;
		
		Boolean loop = true;
		
		long timestart = System.currentTimeMillis();
		long timeend = System.currentTimeMillis();
		
		long difference = timeend - timestart;

		int beat_interval = 15;
		//Infinite loop to keep client connected
		while (loop) {
			//Checks difference in time between now and last beat
			//If it's been 15 seconds it sends a heartbeat request
			if (difference >= beat_interval * 1000) {
				//Reset time marker and send heartbeat
				timestart = System.currentTimeMillis();
				
				if (Global.authciation.heartbeat()) {
					profiles = Global.match.getMatches(true);
					if (profiles!=null) {
						System.out.println("Active profiles near me: ");
//						profiles.forEach((p) -> p.print());
					}
				}
			} else {
				if (difference%1000 == 0) {
					//Line below counts down heartbeat timer
					//System.out.println(Long.toString(10 - difference/1000) + " seconds to heartbeat...");
				}
			}
			timeend = System.currentTimeMillis();
			difference = timeend - timestart;
		}
	}

	private void logoutTest() {
		Global.authciation.getLogin().logout();
	}
}
