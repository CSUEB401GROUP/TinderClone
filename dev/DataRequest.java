import java.util.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class DataRequest implements Serializable{
	
	private String server;
	private int port;
	private HashMap<String,String> values;
	private String user_name;
	private String session_key;
	ObjectOutputStream request;
	ObjectInputStream response;
	private long lastbeat;
	private Boolean active;
	
	public DataRequest(){
		try{
			//Server and port information of host
			this.server = "192.168.56.1";
			this.port = 60000;
			//Initialize first heartbeat timestamp
			this.lastbeat = System.currentTimeMillis();
			
			this.active = false;
			
			//Establish socket connections for I/O operations
			Socket connection = new Socket(this.server,this.port);
			request = new ObjectOutputStream(connection.getOutputStream());
			response = new ObjectInputStream(connection.getInputStream());
			
		}catch(UnknownHostException uhe){ 
		
		}catch(IOException ioe){
			
		}
	}
	public void setServer(String s,int p){
		this.server = s;
		this.port = p;
	}
	public String getServer(){
		return this.server;
	}
	public void clear(){
		this.values.clear();
	}
	
	public String getUsername(){
		return this.user_name;
	}
	
	public String getUserID(){
		String uid = null;
		try{

			//Create Stack<DataValue> request object and add parameters as DataValue objects
			Stack<DataValue> req_msg = new Stack<DataValue>();
			req_msg.push(new DataValue("session_key",this.session_key));
			req_msg.push(new DataValue("user_name",this.user_name));
			req_msg.push(new DataValue("request_type","userid"));
			
			//Send stack object over stream to host
			request.writeObject(req_msg);
			
			System.out.println("UserID sent :: Awaiting response object...");
			
			//Read in Stack<DataValue> object and pop parameters off into local variables
			//session_key		encrypted session key string
			//response_value	valid or error
			//response_type		same as request type
			Stack<DataValue> recv_msg = (Stack<DataValue>) response.readObject();
			String resp_type = recv_msg.pop().getValue();
			String resp_value = recv_msg.pop().getValue();
			
			if(resp_value.compareTo("valid")==0){
				uid = recv_msg.pop().getValue();
			}
			
			System.out.println("Response type: " + resp_type);
			System.out.println("Response value: " + resp_value);
			
			return uid;
		}catch(IOException ioe){
			
		}catch(ClassNotFoundException cnfe){
			
		}
		return uid;
	}
	
	public Boolean isActive(){
		return this.active;
	}
	
	//Sends register request message to host with given parameters
	public Boolean register(String name,String pass){
		try{
			//Create encryption string for input password
			String hash = this.generateHash(pass);
			
			//Create Stack<DataValue> request object and add parameters as DataValue objects
			Stack<DataValue> req_msg = new Stack<DataValue>();
			req_msg.push(new DataValue("user_password",hash));
			req_msg.push(new DataValue("user_name",name));
			req_msg.push(new DataValue("request_type","register"));
			
			//Send stack object over stream to host
			request.writeObject(req_msg);
			
			System.out.println("Register sent :: Awaiting response object...");
			
			//Read in Stack<DataValue> object and pop parameters off into local variables
			//session_key		encrypted session key string
			//response_value	valid or error
			//response_type		same as request type
			Stack<DataValue> recv_msg = (Stack<DataValue>) response.readObject();
			String resp_type = recv_msg.pop().getValue();
			String resp_value = recv_msg.pop().getValue();
			this.session_key = recv_msg.pop().getValue();
			this.user_name = name;
			
			System.out.println("Response type: " + resp_type);
			System.out.println("Response value: " + resp_value);
			System.out.println("Session key: " + this.session_key);
			
			return true;
		}catch(IOException ioe){
			
		}catch(ClassNotFoundException cnfe){
			
		}
		return false;
	}
	
	public Boolean login(String name,String pass){
		try{
			//Create encryption string for input password
			String hash = this.generateHash(pass);
			
			//Create Stack<DataValue> request object and add parameters as DataValue objects
			Stack<DataValue> req_msg = new Stack<DataValue>();
			req_msg.push(new DataValue("user_password",hash));
			req_msg.push(new DataValue("user_name",name));
			req_msg.push(new DataValue("request_type","login"));
			
			//Send stack object over stream to host
			request.writeObject(req_msg);
			
			System.out.println("Login sent :: Awaiting response object...");
			
			//Read in Stack<DataValue> object and pop parameters off into local variables
			//session_key		encrypted session key string
			//response_value	valid or error
			//response_type		same as request type
			Stack<DataValue> recv_msg = (Stack<DataValue>) response.readObject();
			String resp_type = recv_msg.pop().getValue();
			String resp_value = recv_msg.pop().getValue();
			
			String key = recv_msg.pop().getValue();
			
			System.out.println("Response type: " + resp_type);
			System.out.println("Response value: " + resp_value);
			System.out.println("Session key: " + key);
			
			//If response is valid, store session key string and 
			//current user name locally for later use
			if(resp_value.compareTo("valid")==0){
				this.session_key = key;
				this.user_name = name;
				this.active = true;
				return true;
			}else{
				return false;
			}
		}catch(IOException ioe){
			
		}catch(ClassNotFoundException cnfe){
			
		}
		return false;
	}
	
	public Boolean logout(){
		try{
			//Create Stack<DataValue> request object and add parameters as DataValue objects
			Stack<DataValue> req_msg = new Stack<DataValue>();
			req_msg.push(new DataValue("session_key",this.session_key));
			req_msg.push(new DataValue("user_name",this.user_name));
			req_msg.push(new DataValue("request_type","logout"));
			
			//Send stack object over stream to host
			request.writeObject(req_msg);
			
			System.out.println("Logout sent :: Awaiting response object...");
				
			//Read in Stack<DataValue> object and pop parameters off into local variables
			//response_value	valid or error
			//response_type		same as request type	
			Stack<DataValue> recv_msg = (Stack<DataValue>) response.readObject();
			String resp_type = recv_msg.pop().getValue();
			String resp_value = recv_msg.pop().getValue();
			
			System.out.println("Response type: " + resp_type);
			System.out.println("Response value: " + resp_value);
			
			if(resp_value.compareTo("valid")==0){
				this.active = false;
			}

			return true;
		}catch(IOException ioe){
			
		}catch(ClassNotFoundException cnfe){
			
		}
		return false;
	}
	
	public Boolean heartbeat(){
		try{
			//Ensures a minimum 2 second delay between heartbeats
			//to avoid a spammed stream of requests
			if((System.currentTimeMillis() - this.lastbeat)/1000 > 2){
				
				//Create Stack<DataValue> request object and add parameters as DataValue objects
				Stack<DataValue> req_msg = new Stack<DataValue>();
				req_msg.push(new DataValue("session_key",this.session_key));
				req_msg.push(new DataValue("user_name",this.user_name));
				req_msg.push(new DataValue("request_type","heartbeat"));
				
				//Send stack object over stream to host
				request.writeObject(req_msg);
				
				System.out.println("Heartbeat sent :: Awaiting response object...");
					
				//Read in Stack<DataValue> object and pop parameters off into local variables
				//response_value	valid or error
				//response_type		same as request type		
				Stack<DataValue> recv_msg = (Stack<DataValue>) response.readObject();
				String resp_type = recv_msg.pop().getValue();
				String resp_value = recv_msg.pop().getValue();
				
				System.out.println("Response type: " + resp_type);
				System.out.println("Response value: " + resp_value);
				
				//Update last heartbeat timestamp
				this.lastbeat = System.currentTimeMillis();
				return true;
			}
			return false;
		}catch(IOException ioe){
			
		}catch(ClassNotFoundException cnfe){
			
		}
		return false;
	}
	
	public Boolean addMatch(Profile prof, String swipe_action){
		try{
			//Create Stack<DataValue> request object and add parameters as DataValue objects
			Stack<DataValue> req_msg = new Stack<DataValue>();
			req_msg.push(new DataValue("match_option",swipe_action));
			req_msg.push(new DataValue("session_key",this.session_key));
			req_msg.push(new DataValue("user_name",this.user_name));
			req_msg.push(new DataValue("request_type","addmatch"));
			
			//Send stack object over stream to host
			request.writeObject(req_msg);
			request.writeObject(prof);
			
			System.out.println("AddMatch sent :: Awaiting response object...");
				
			//Read in Stack<DataValue> object and pop parameters off into local variables
			//response_value	valid or error
			//response_type		same as request type			
			Stack<DataValue> recv_msg = (Stack<DataValue>) response.readObject();
			String resp_type = recv_msg.pop().getValue();
			String resp_value = recv_msg.pop().getValue();
			
			System.out.println("Response type: " + resp_type);
			System.out.println("Response value: " + resp_value);

			return true;
		}catch(IOException ioe){
			
		}catch(ClassNotFoundException cnfe){
			
		}
		return false;
	}

	public int getDistance(String origin,String destination){
		try{
			int distance = -1;
			//Create Stack<DataValue> request object and add parameters as DataValue objects
			Stack<DataValue> req_msg = new Stack<DataValue>();
			req_msg.push(new DataValue("destination",destination));
			req_msg.push(new DataValue("origin",origin));
			req_msg.push(new DataValue("session_key",this.session_key));
			req_msg.push(new DataValue("user_name",this.user_name));
			req_msg.push(new DataValue("request_type","getdistance"));
			
			//Send stack object over stream to host
			request.writeObject(req_msg);
			
			System.out.println("GetDistance sent :: Awaiting response object...");
				
			//Read in Stack<DataValue> object and pop parameters off into local variables
			//distance			distance returned
			//response_value	valid or error
			//response_type		same as request type			
			Stack<DataValue> recv_msg = (Stack<DataValue>) response.readObject();
			String resp_type = recv_msg.pop().getValue();
			String resp_value = recv_msg.pop().getValue();
			distance = Integer.parseInt(recv_msg.pop().getValue());
			
			System.out.println("Response type: " + resp_type);
			System.out.println("Response value: " + resp_value);
		
			return distance;
		}catch(IOException ioe){
			
		}catch(ClassNotFoundException cnfe){
			
		}
		return -1;
	}
	
	public Profile getProfile(){
		try{
			//Create Stack<DataValue> request object and add parameters as DataValue objects
			Stack<DataValue> req_msg = new Stack<DataValue>();
			req_msg.push(new DataValue("session_key",this.session_key));
			req_msg.push(new DataValue("user_name",this.user_name));
			req_msg.push(new DataValue("request_type","getprofile"));
			
			//Send stack object over stream to host
			request.writeObject(req_msg);
			
			System.out.println("GetProfile sent :: Awaiting response object...");
				
			//Read in Stack<DataValue> object and pop parameters off into local variables
			//response_value	valid or error
			//response_type		same as request type			
			Stack<DataValue> recv_msg = (Stack<DataValue>) response.readObject();
			String resp_type = recv_msg.pop().getValue();
			String resp_value = recv_msg.pop().getValue();
			
			System.out.println("Response type: " + resp_type);
			System.out.println("Response value: " + resp_value);
			
			//If response is valid, read in profile object directly over input stream
			if(resp_value.compareTo("valid")==0){
				Profile prof = (Profile) response.readObject();
				return prof;
			}
			
			return null;
		}catch(IOException ioe){
			
		}catch(ClassNotFoundException cnfe){
			
		}
		return null;
	}
	
	public Boolean editProfile(Profile p){
		try{
			//Create Stack<DataValue> request object and add parameters as DataValue objects
			Stack<DataValue> req_msg = new Stack<DataValue>();
			req_msg.push(new DataValue("session_key",this.session_key));
			req_msg.push(new DataValue("user_name",this.user_name));
			req_msg.push(new DataValue("request_type","editprofile"));
			
			//Send stack object over stream to host
			request.writeObject(req_msg);
			
			//Send profile object over stream to host
			request.writeObject(p);
			
			System.out.println("EditProfile sent :: Awaiting response object...");
				
			//Read in Stack<DataValue> object and pop parameters off into local variables
			//response_value	valid or error
			//response_type		same as request type			
			Stack<DataValue> recv_msg = (Stack<DataValue>) response.readObject();
			String resp_type = recv_msg.pop().getValue();
			String resp_value = recv_msg.pop().getValue();
			
			System.out.println("Response type: " + resp_type);
			System.out.println("Response value: " + resp_value);
			
			if(resp_value.compareTo("valid")==0){
				return true;
			}
			
			return false;
		}catch(IOException ioe){
			
		}catch(ClassNotFoundException cnfe){
			
		}
		return null;
	}	
	
	//Return list of profile objects - filtered by distance if nearby flag set to true
	public ArrayList<Profile> getMatches(Boolean nearby){
		try{
			//Create Stack<DataValue> request object and add parameters as DataValue objects
			Stack<DataValue> req_msg = new Stack<DataValue>();
			req_msg.push(new DataValue("local",Boolean.toString(nearby)));
			req_msg.push(new DataValue("session_key",this.session_key));
			req_msg.push(new DataValue("user_name",this.user_name));
			req_msg.push(new DataValue("request_type","getmatches"));
			
			//Send stack object over stream to host
			request.writeObject(req_msg);
			
			System.out.println("GetMatches sent :: Awaiting response object...");
				
			//Read in Stack<DataValue> object and pop parameters off into local variables
			//response_value	valid or error
			//response_type		same as request type			
			Stack<DataValue> recv_msg = (Stack<DataValue>) response.readObject();
			String resp_type = recv_msg.pop().getValue();
			String resp_value = recv_msg.pop().getValue();
			
			System.out.println("Response type: " + resp_type);
			System.out.println("Response value: " + resp_value);
			
			//If response is valid, read in profile objects directly over input stream
			if(resp_value.compareTo("valid")==0){
				ArrayList<Profile> profiles = (ArrayList<Profile>) response.readObject();
				return profiles;
			}

			return null;
		}catch(IOException ioe){
			
		}catch(ClassNotFoundException cnfe){
			
		}
		return null;
	}
	
//Return list of profile objects - filtered by distance if nearby flag set to true
	public ArrayList<Profile> getMutualMatches(){
		try{
			//Create Stack<DataValue> request object and add parameters as DataValue objects
			Stack<DataValue> req_msg = new Stack<DataValue>();
			req_msg.push(new DataValue("session_key",this.session_key));
			req_msg.push(new DataValue("user_name",this.user_name));
			req_msg.push(new DataValue("request_type","getmutualmatches"));
			
			//Send stack object over stream to host
			request.writeObject(req_msg);
			
			System.out.println("GetMutualMatches sent :: Awaiting response object...");
				
			//Read in Stack<DataValue> object and pop parameters off into local variables
			//response_value	valid or error
			//response_type		same as request type			
			Stack<DataValue> recv_msg = (Stack<DataValue>) response.readObject();
			String resp_type = recv_msg.pop().getValue();
			String resp_value = recv_msg.pop().getValue();
			
			System.out.println("Response type: " + resp_type);
			System.out.println("Response value: " + resp_value);
			
			//If response is valid, read in profile objects directly over input stream
			if(resp_value.compareTo("valid")==0){
				ArrayList<Profile> profiles = (ArrayList<Profile>) response.readObject();
				return profiles;
			}

			return null;
		}catch(IOException ioe){
			
		}catch(ClassNotFoundException cnfe){
			
		}
		return null;
	}
	
	//Return list of profile objects - filtered by distance if nearby flag set to true
	public ArrayList<Profile> getMatchHistory(){
		try{
			//Create Stack<DataValue> request object and add parameters as DataValue objects
			Stack<DataValue> req_msg = new Stack<DataValue>();
			req_msg.push(new DataValue("session_key",this.session_key));
			req_msg.push(new DataValue("user_name",this.user_name));
			req_msg.push(new DataValue("request_type","getmatchhistory"));
			
			//Send stack object over stream to host
			request.writeObject(req_msg);
			
			System.out.println("GetMatchHistory sent :: Awaiting response object...");
				
			//Read in Stack<DataValue> object and pop parameters off into local variables
			//response_value	valid or error
			//response_type		same as request type			
			Stack<DataValue> recv_msg = (Stack<DataValue>) response.readObject();
			String resp_type = recv_msg.pop().getValue();
			String resp_value = recv_msg.pop().getValue();
			
			System.out.println("Response type: " + resp_type);
			System.out.println("Response value: " + resp_value);
			
			//If response is valid, read in profile objects directly over input stream
			if(resp_value.compareTo("valid")==0){
				ArrayList<Profile> profiles = (ArrayList<Profile>) response.readObject();
				return profiles;
			}

			return null;
		}catch(IOException ioe){
			
		}catch(ClassNotFoundException cnfe){
			
		}
		return null;
	}
	
	//Generate and return encrypted string from password input
	private String generateHash(String pass){
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.reset();
			
			byte[] hash_hex = pass.getBytes(StandardCharsets.UTF_8);
			md.update(hash_hex);
			
			hash_hex = md.digest();
			String hash_hex_string = Base64.getEncoder().encodeToString(hash_hex);

			return hash_hex_string;
		}catch(NoSuchAlgorithmException nsae){
			
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}