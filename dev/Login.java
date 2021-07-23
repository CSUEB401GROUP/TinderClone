public class Login{
	
	DataRequest request_obj;
	
	public Login(DataRequest dr){
		this.request_obj = dr;
	}
	
	public Boolean login(String user, String pass){
		return this.request_obj.login(user,pass);
	}
	
	public Boolean logout(){
		return this.request_obj.logout();
	}	
	
	public Boolean isActive(){
		return this.request_obj.isActive();
	}

}