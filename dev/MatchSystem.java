import java.util.*;

public class MatchSystem{
	
	DataRequest request_obj;
	Filter filter_obj;
	Location location_obj;
	
	public MatchSystem(DataRequest dr){
		this.request_obj = dr;
		this.filter_obj = new Filter();
		this.location_obj = new Location(dr);
	}
	
	public Boolean createMatch(Profile p, String swipe_action){
		return this.request_obj.addMatch(p,swipe_action);
	}
	
	public ProfileList getMatches(Boolean nearby){
		return this.filterProfiles(new ProfileList(this.request_obj.getMatches(nearby)));
	}
	
	public ProfileList getMutualMatches(){
		return new ProfileList(this.request_obj.getMutualMatches());
	}
	
	public ProfileList getMatchHistory(){
		return new ProfileList(this.request_obj.getMatchHistory());
	}
	
	public ProfileList filterProfiles(ProfileList plist){
		return this.filter_obj.filterProfiles(plist);
	}
	
	public int getDistance(String city1, String city2){
		return this.location_obj.getDistance(city1,city2);
	}
	
	public void editFilterSetting(String name, String value){
		this.filter_obj.editFilterSetting(name,value);
	}
	
	
}