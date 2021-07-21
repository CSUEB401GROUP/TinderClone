package com.tinderclone.common.entity;

import java.util.ArrayList;
import java.util.List;

public class MatchList {

	private String user_id;
	private List<Match> matches = new ArrayList<Match>();
	
	public boolean addMatch() {
		return true;
	}
	
	public Match getNextMatch() {
		return null;
	}
	
	public Match getMatch(int index) {
		return matches.get(index);
	}
}
