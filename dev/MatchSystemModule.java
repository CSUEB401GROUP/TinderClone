package com.tinderclone.client.modules.match;

import java.util.ArrayList;

import com.tinderclone.client.Global;
import com.tinderclone.client.core.DataRequest;
import com.tinderclone.client.core.DataResponse;
import com.tinderclone.common.entity.MatchList;
import com.tinderclone.common.entity.Profile;
import com.tinderclone.common.entity.ProfileList;

public class MatchSystemModule {

	private MatchList matchList;
	private boolean newMatch;
	private MatchList mutualMatches;
	private String getMatch; 
	
	public MatchList getMatchHistory() {
		return this.matchList;
	}
	
	public MatchList getMutualMatches() {
		return mutualMatches;
	}
	
	public boolean createMatch() {
		return true;
	}
	
	public Boolean addMatch(Profile prof) {
		
		DataRequest request = new DataRequest();
		if (prof != null) {
			request.addValue("profile_id", prof.getID()); //findmatch on doc
		}
		request.addValue("session_key", Global.session_key);
		request.addValue("user_name", Global.username);
		request.addValue("request_type", "addmatch");
		DataResponse result = request.send();
		
		if (result.isSuccess()) {
			String resp_type = result.getValue("response_type");
			if (resp_type.equals("addmatch")) {
				String resp_value = result.getValue("response_value");
				if (resp_value.equals("valid")) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public ProfileList getMatches(Boolean nearby) {
		
		ProfileList result = null;
		//Find Match
		DataRequest request = new DataRequest();
		
		request.addValue("local", Boolean.toString(nearby));
		request.addValue("session_key", Global.session_key);
		request.addValue("user_name", Global.username);
		request.addValue("request_type", "getmatches"); //findmatch on doc
		ArrayList<Profile> profiles = (ArrayList<Profile>) request.send2Object();
		if (profiles != null) {
			result = new ProfileList(profiles);
		}
		return result;
	}
}
