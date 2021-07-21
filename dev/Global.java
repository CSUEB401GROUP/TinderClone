package com.tinderclone.client;

import com.tinderclone.client.modules.login.UserModule;
import com.tinderclone.client.modules.login.authenciation.AuthenciationModule;
import com.tinderclone.client.modules.match.MatchSystemModule;

public class Global {

	public static String SERVER_URL = "198.18.39.163";//"192.168.56.1";
	public static int SERVOER_PORT = 60000;
	
	public static AuthenciationModule authciation = new AuthenciationModule();
	public static UserModule user = new UserModule();
	public static MatchSystemModule match = new MatchSystemModule();
	
	public static String username;
	public static String password;
	public static String session_key;
	public static Long lastbeat = null;
	
	public String getUserid() { return username;  }
	public void setUserid(String userid) { this.username = userid; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	
	public String getSession_key() { return session_key; }
	public void setSession_key(String session_key) { this.session_key = session_key; }

}
