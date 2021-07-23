import java.io.Serializable;
import java.util.*;

public class ProfileList implements Serializable{
	
	ArrayList<Profile> profiles;
	int curr_index;
	
	public ProfileList(){
		this.profiles = new ArrayList<Profile>();
		this.curr_index = -1;
	}
	
	public ProfileList(ArrayList<Profile> plist){
		this.profiles = plist;
		this.curr_index = -1;
	}	
	
	public Boolean addProfile(Profile m){
		this.profiles.add(m);
		return true;
	}

	public Profile getProfile(){
		if(this.profiles.size() > 0){
			return this.profiles.get(this.curr_index);
		}else{
			return null;
		}
	}	

	public void resetIndex(){
		this.curr_index = -1;
	}

	public Profile getProfile(int i){
		if(this.profiles.size() > 0){
			return this.profiles.get(i);
		}else{
			return null;
		}
	}	
	
	public Profile getNextProfile(){
		if(this.profiles.size() > 0){
			if(this.curr_index < this.profiles.size() - 1){
				this.curr_index++;
				return this.profiles.get(this.curr_index);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
}