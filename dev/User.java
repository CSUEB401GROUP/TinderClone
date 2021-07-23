public class User{
	
	DataRequest request_obj;
	Profile profile_obj;
	
	public User(DataRequest dr){
		this.request_obj = dr;
	}
	
	public Profile getProfile(){
		if(profile_obj==null){
			this.profile_obj = this.request_obj.getProfile();
		}
		return this.profile_obj;
	}
	
	public String getUsername(){
		return this.request_obj.getUsername();
	}	
	
	public String getUserID(){
		return this.request_obj.getUserID();
	}	
	
	public void editProfileSetting(String name,String value,Boolean pref){
		Profile pobj = this.getProfile();
		pobj.editAttribute(name,value,pref);
	}
	
	public Boolean saveAll(){
		return this.request_obj.editProfile(this.profile_obj);
	}
}