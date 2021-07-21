package com.tinderclone.client.modules.login.authenciation;

import com.tinderclone.client.Global;
import com.tinderclone.client.core.DataRequest;
import com.tinderclone.client.core.DataResponse;
import com.tinderclone.client.modules.login.authenciation.component.LoginModule;
import com.tinderclone.client.modules.login.authenciation.component.RegisterModule;

public class AuthenciationModule {

	private LoginModule login = new LoginModule();
	private RegisterModule register = new RegisterModule();
	
	public boolean authenciation(String username, String password) {
		if (!login.isActive()) {
			login.login(username, password);
		}
		return true;
	}
	
	public void setTarget(String dbPath) {
		
	}
	
	public LoginModule getLogin() {
		return login;
	}
	
	public RegisterModule getRegister() {
		return register;
	}
	
	public Boolean heartbeat() {
			
		DataRequest request = new DataRequest();
			
		request.addValue("session_key", Global.session_key);
		request.addValue("user_name", Global.username);
		request.addValue("request_type", "heartbeat");
		
		DataResponse result = request.send();
		
		if (result != null) {
			if (result.isSuccess()) {
				String resp_type = result.getValue("response_type");
				String resp_value = result.getValue("response_value");
				
				System.out.println("Response type: " + resp_type);
				System.out.println("Response value: " + resp_value);
				return true;
			}
		}
		return false;
	}
}
