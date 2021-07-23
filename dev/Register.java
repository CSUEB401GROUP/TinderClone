public class Register{
	
	DataRequest request_obj;
	
	public Register(DataRequest dr){
		this.request_obj = dr;
	}
	
	public Boolean register(String user, String pass){
		return this.request_obj.register(user,pass);
	}

}