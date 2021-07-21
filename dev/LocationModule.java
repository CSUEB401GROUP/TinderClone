package com.tinderclone.client.modules.location;

import com.tinderclone.client.Global;
import com.tinderclone.client.core.DataRequest;
import com.tinderclone.client.core.DataResponse;

public class LocationModule {

	public int getDistance(String city1, String city2) {
		//Get Profile
		DataRequest request = new DataRequest();
		request.addValue("request_type", "getdistance");
		request.addValue("origin", city1);
		request.addValue("destination", city2);
		request.addValue("user_name", Global.username);
		request.addValue("session_key", Global.session_key);
		DataResponse result = request.send();

		int distance = 0;
		if (result != null) {
			if (result.isSuccess()) {
				String resp_type = result.getValue("response_type");
				if (resp_type.equals("getdistance")) {
					String resp_value = result.getValue("response_value");
					distance = (int) Math.round(Double.parseDouble(resp_value));
				}
			}
		}
		return distance;
	}
}
