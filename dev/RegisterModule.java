package com.tinderclone.client.modules.login.authenciation.component;

import com.tinderclone.client.core.DataRequest;
import com.tinderclone.client.core.DataResponse;

public class RegisterModule {

	public boolean register(String username, String password) {
		DataRequest request = new DataRequest();
		request.addValue("user_password", password);
		request.addValue("user_name", username);
		request.addValue("request_type", "register");
		DataResponse result = request.send();
		
		if (result != null) {
			if (result.isSuccess()) {
				String resp_type = result.getValue("response_type");
				if (resp_type.equals("register")) {
					String resp_value = result.getValue("response_value");
					if (resp_value.equals("valid")) {
						System.out.println("register is success");
						return true;
					}
				}
			}
		}
		return false;
	}
}
