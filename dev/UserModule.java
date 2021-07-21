package com.tinderclone.client.modules.login;

import com.tinderclone.client.Global;
import com.tinderclone.client.core.DataRequest;
import com.tinderclone.client.core.DataResponse;
import com.tinderclone.common.entity.Account;
import com.tinderclone.common.entity.Profile;

public class UserModule {

	private boolean active;
	private Profile profile;

	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }

	public Profile getProfile() {
		//Get Profile
		DataRequest request = new DataRequest();
		request.addValue("request_type", "getprofile");
		request.addValue("user_id", Global.username);
		request.addValue("session_key", Global.session_key);
		DataResponse result = request.send();

		if (result != null) {
			if (result.isSuccess()) {
				String resp_type = result.getValue("response_type");
				if (resp_type.equals("getprofile")) {
					String resp_value = result.getValue("response_value");
					if (resp_value.equals("found")) {
						//getProfile Object(need code)
							
					}
				}
			}
		}
		return this.profile;
	}

	public boolean editAttribute(Profile profile) {
		//Edit Profile
		DataRequest request = new DataRequest();
		request.addValue("session_key", Global.session_key);
		request.addValue("user_name", Global.username);
		request.addValue("request_type", "editprofile");
		//set Profile Ojbject(need code)
		
		DataResponse result = request.send(profile);
		if (result != null) {
			if (result.isSuccess()) {
				String resp_type = result.getValue("response_type");
				if (resp_type.equals("editprofile")) {
					String resp_value = result.getValue("response_value");
					if (resp_value.equals("valid")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public String getUsername() {
		return null;
	}

	public String getRandomLocation() {
		return null;
	}

	public String getUserID() {
		return null;
	}

	public Account getAccount() {
		return null;
	}
}
