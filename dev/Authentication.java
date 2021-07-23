public class Authentication{
	
	private Login login_obj;
	private Register register_obj;
	private DataRequest request_obj;

	public Authentication(DataRequest dr){
		login_obj = new Login(dr);
		register_obj = new Register(dr);
		request_obj = dr;
	}
	
	public Boolean login(String user, String pass){
		return login_obj.login(user,pass);
	}
	
	public Boolean logout(){
		return login_obj.logout();
	}
	
	public Boolean register(String user, String pass){
		return register_obj.register(user,pass);
	}
	
	public Boolean isActive(){
		return login_obj.isActive();
	}
		
	public void sendHeartbeat(){
		this.request_obj.heartbeat();
	}
}