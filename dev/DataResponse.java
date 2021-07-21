package com.tinderclone.client.core;

import java.util.List;
import java.util.Optional;

import com.tinderclone.common.entity.DataValue;

public class DataResponse {
	private List<DataValue> data;

	public DataResponse(List<DataValue> data) {
		this.data = data;
	}

	public boolean isSuccess() {
		if (this.data == null || this.data.size() <= 0) {
			return false;
		}
		Optional<DataValue> opt_result = this.data.stream().filter(data -> data.getName().equals("response_value")).findFirst();
		if (!opt_result.isPresent()) {
			return false;
		}
		if (!opt_result.get().getValue().equals("error")) {
			return true;
		}
		return false;
	}
	
	public String getValue(String key) {
		if (this.data == null || this.data.size() <= 0) {
			return null;
		}
		Optional<DataValue> opt_result = this.data.stream().filter(data -> data.getName().equals(key)).findFirst();
		if (!opt_result.isPresent()) {
			return null;
		}
		return opt_result.get().getValue();
	}

}
