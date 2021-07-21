package com.tinderclone.client.modules.login.authenciation.component;

import com.tinderclone.client.Global;
import com.tinderclone.client.core.DataRequest;
import com.tinderclone.client.core.DataResponse;

public class LoginModule {
	
	public boolean isActive() {
		return true;
	}
	
	public boolean login(String username, String password) {
		
		DataRequest request = new DataRequest();
		
		request.addValue("user_password", password);
		request.addValue("user_name", username);
		request.addValue("request_type", "login");
		
		DataResponse result = request.send();
		
		if (result != null) {
			if (result.isSuccess()) {
				String resp_type = result.getValue("response_type");
				if (resp_type.equals("login")) {
					String resp_value = result.getValue("response_value");
					if (resp_value.equals("valid")) {
						Global.username = username;
						Global.session_key = result.getValue("session_key");
						System.out.println("sessionKey=" + Global.session_key);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void logout() {
		DataRequest request = new DataRequest();
		request.addValue("user_name", Global.username);
		request.addValue("request_type", "logout");
		request.addValue("session_key", Global.session_key);
		DataResponse result = request.send();
		if (result != null) {
			if (result.isSuccess()) {
				String resp_type = result.getValue("response_type");
				if (resp_type.equals("logout")) {
					String resp_value = result.getValue("response_value");
					if (resp_value.equals("valid")) {
						System.out.println("logout success");
					}
				}
			}
		}
	}
	
}
