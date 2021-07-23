import java.util.*;

public class Filter{
	
	HashMap<String,FilterSetting> settings;
	
	public Filter(){
		this.settings = new HashMap<String,FilterSetting>();
	}
	
	public Boolean editFilterSetting(String name, String value){
		this.settings.put(name,new FilterSetting(name,value));
		return true;
	}
	
	public ProfileList filterProfiles(ProfileList input_list){
		
		ProfileList return_list = null;
		
		Profile p = input_list.getNextProfile();
		
		while(p != null){
			return_list = new ProfileList();
			Boolean valid = true;
			ArrayList<FilterSetting> fsets = new ArrayList<FilterSetting>();
			this.settings.forEach((String name,FilterSetting fs) -> fsets.add(fs));
			for(FilterSetting f : fsets){
				if(f.compare(p.getAttribute(f.getName(),true))==false){
					valid = false;
				}
			}
			if(valid){
				return_list.addProfile(p);
			}
			valid = true;
			p = input_list.getNextProfile();
		}
		return return_list;
	}
	
}