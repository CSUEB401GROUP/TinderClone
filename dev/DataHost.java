import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.sql.Timestamp;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.net.InetAddress;


public class DataHost{
	
	private int max_clients;
	private HashMap<String,String> keys;
	private HashMap<String,String> locations;
	private HashMap<String,Long> heartbeats;
	
	public DataHost(){
		//Maximum number of clients allowed before logins are refused
		this.max_clients = 10;
		//Session keys stored by user_name
		this.keys = new HashMap<String,String>();
		//User locations stored by user_name
		this.locations = new HashMap<String,String>();
		//Timestamps of last heartbeat requests stored by user_name
		this.heartbeats = new HashMap<String,Long>();
		this.listen();
	}
	
	public void setMaxClients(int max){
		this.max_clients = max;
	}
	
	//Main method to start server and wait for a request
	private void listen(){
		try{
			ServerSocket host = new ServerSocket(60000);
			InetAddress localhost = InetAddress.getLocalHost(); 
			System.out.println("IPV4 Address : " + (localhost.getHostAddress()).trim());
			while(true){
				Socket connection = host.accept();
				PortManager pm = new PortManager(connection,this);
				new Thread(pm).start();
			}
		}catch(IOException ioe){
			
		}
	}
	
	//Updates the last heartbeat timestamp for a given user
	private void resetHeartbeat(String client){
		this.heartbeats.put(client,System.currentTimeMillis());
	}
	
	//Updates time elapsed since last heartbeat for each user
	private void updateHeartbeats(){
		Iterator<String> iterator = this.heartbeats.keySet().iterator();
		ArrayList<String> expired = new ArrayList<String>();
		while(iterator.hasNext()){
			String client = iterator.next();
			long lastbeat = this.heartbeats.get(client);
			long elapsed = (System.currentTimeMillis() - lastbeat)/1000;
			if(elapsed > 30){
				expired.add(client);
			}
		}
		for(String e : expired){
			this.removeClient(e);
		}
	}
	
	//Returns an encrypted session key string based on the current timestamp string
	private String generateKey(){
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			long now = System.currentTimeMillis();
			Timestamp curr = new Timestamp(now);
			byte[] res = md.digest(curr.toString().getBytes(StandardCharsets.UTF_8));
			
			String session_key = Base64.getEncoder().encodeToString(res);
			
			return session_key;
		}catch(NoSuchAlgorithmException nsae){
			
		}
		return null;
	}
	
	//Returns random city string from possible locations
	private String getRandomLocation(){
		DataManager dm = new DataManager();
		String location = dm.getRandomLocation();
		return location;
	}
	
	//Adds a client to the active clients list and returns the session key assigned
	public String addClient(String id){
		String key = null;
		if(this.keys.size()<this.max_clients){
			key = this.generateKey();
			String location = this.getRandomLocation();
			this.keys.put(id,key);
			this.locations.put(id,location);
			this.heartbeats.put(id,System.currentTimeMillis());
			System.out.println("adding client " + id + " with location " + location + " and key " + key);
		}
		return key;
	}
	
	//Checks to see if a user is connected
	public Boolean hasClient(String name){
		Boolean found = false;
		found = this.keys.containsKey(name);
		return found;
	}
	
	//Removes user from the active user list
	public Boolean removeClient(String id){
		this.keys.remove(id);
		this.locations.remove(id);
		this.heartbeats.remove(id);
		return true;
	}
	
	//Checks that provided session key is valid for the given user
	public Boolean validateClient(String id, String key){
		String currkey = this.keys.get(id);
		if(currkey!=null){
			if(currkey.compareTo(key)==0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	//Returns active users near a given user's location based on their distance settings
	public HashMap<String,String> getNearbyClients(String id){
		System.out.println("getNearbyClients :: " + id + " - total active: " + this.keys.size());
		String origin = this.locations.get(id);
		DataManager dm = new DataManager();
		int range = dm.getRange(id);
		HashMap<String,String> nearby = new HashMap<String,String>();
		for(String client : this.keys.keySet()){
			String destination = this.locations.get(client);
			int d = dm.getDistance(origin,destination);
			if(d >= 0){
				if(d < range && client.compareTo(id)!=0){
					nearby.put(client,Integer.toString(d));
					System.out.println("Range is " + range);
					System.out.println(this.locations.get(id) + " to " + this.locations.get(client) + " is " + d);
					System.out.println("Adding " + client + " to nearby list");
				}
			}
		}
		
		return nearby;
	}
	
	//Prints select server info
	public void serverStatus(){
		System.out.println("# of active clients: " + this.keys.size());
		this.locations.forEach((client,location) -> System.out.println("\t " + client + " :: " + location + " - seconds since last heartbeat: " + Long.toString((System.currentTimeMillis() - this.heartbeats.get(client))/1000)));
	}
	
	//Inner class used for multithreading
	private static class PortManager implements Runnable{
		
		private Socket connection;
		private Boolean login;
		private DataHost dh;
		
		public PortManager(Socket connection,DataHost d){
			this.connection = connection;
			this.login = false;
			this.dh = d;
		}
		
		public void run(){
			try{
				
				//Create object I/O stream connections
				ObjectInputStream in = new ObjectInputStream(this.connection.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(this.connection.getOutputStream());
				
				//Create DataManager object to interact with the DB objects
				//and fetch/alter records
				DataManager dm = new DataManager();
				
				//Infinite loop
				while(true){
					
					//Initial state : waiting for a Stack<DataValue> object
					System.out.println("Awaiting new request object...");
					
					//Load stack from input stream
					Stack<DataValue> recv_msg = (Stack<DataValue>) in.readObject();
					//Pop off first element, which will be the 'request_type' DataValue
					String req_type = recv_msg.pop().getValue();
					
					System.out.println("Request type: " + req_type);
					
					//***************************************************************
					//Begin: series of if/else ifs for each possible request message type
					
					if(req_type.compareTo("register")==0){
						
						//Next 2 DataValues are popped from the stack:
						//user_password		encrypted password string
						//user_name			user name
						String name = recv_msg.pop().getValue();
						String pass = recv_msg.pop().getValue();
						
						//Send to the DataManager object using its register method
						Boolean success = dm.register(name,pass);
						
						//The response value will be either valid or error
						//depending on whether the register method fails or not
						String response = (success) ? "valid" : "error";
						
						//If the response is valid, add this username to the host's client list
						String key = (success) ? this.dh.addClient(name) : null;
						
						//Create the Stack<DataValue> response object to send back to the client
						Stack<DataValue> resp_msg = new Stack<DataValue>();
						
						//Add the DataValue parameters to the response object
						//The session key will either be an encrypted string or null
						resp_msg.push(new DataValue("session_key",key));
						resp_msg.push(new DataValue("response_value",response));
						//The response_type will match the request type
						resp_msg.push(new DataValue("response_type","register"));
						
						//Send the object over the output stream
						out.writeObject(resp_msg);
						System.out.println("Register :: sending response obj...");
						
					}else if(req_type.compareTo("login")==0){
						
						//Next 2 DataValues are popped from the stack:
						//user_password		encrypted password string
						//user_name			user name
						String name = recv_msg.pop().getValue();
						String pass = recv_msg.pop().getValue();
						Boolean success = false;
						
						//Check that user is not already logged in
						if(!this.dh.hasClient(name)){
							success = dm.login(name,pass);
						}else{
							success = false;
						}
						
						//If the response is valid, add this username to the host's client list
						String response = (success) ? "valid" : "error";
						String key = (success) ? this.dh.addClient(name) : null;
						
						//Create the Stack<DataValue> response object to send back to the client
						Stack<DataValue> resp_msg = new Stack<DataValue>();
						
						//Add the DataValue parameters to the response object
						//The session key will either be an encrypted string or null
						resp_msg.push(new DataValue("session_key",key));
						resp_msg.push(new DataValue("response_value",response));
						//The response_type will match the request type
						resp_msg.push(new DataValue("response_type","login"));
						
						//Send the object over the output stream
						out.writeObject(resp_msg);
						System.out.println("Login :: sending response obj...");
						
					}else if(req_type.compareTo("logout")==0){
						
						//Next 2 DataValues are popped from the stack:
						//user_name				user name
						//session_key			encrypted session key string
						String name = recv_msg.pop().getValue();
						String key = recv_msg.pop().getValue();
						Boolean valid = this.dh.validateClient(name,key);
						Boolean success = false;
						if(valid){
							//If client is logged in, remove them from the active client list
							success = this.dh.removeClient(name);
						}
						String response = (success) ? "valid" : "error";
						
						//Create the Stack<DataValue> response object to send back to the client
						Stack<DataValue> resp_msg = new Stack<DataValue>();
						
						//Add the DataValue parameters to the response object
						resp_msg.push(new DataValue("response_value",response));
						//The response_type will match the request type
						resp_msg.push(new DataValue("response_type","logout"));
						out.writeObject(resp_msg);
						System.out.println("Logout :: sending response obj...");
						
					}else if(req_type.compareTo("userid")==0){
						
						//Next 2 DataValues are popped from the stack:
						//user_name				user name
						//session_key			encrypted session key string
						String name = recv_msg.pop().getValue();
						String key = recv_msg.pop().getValue();
						Boolean valid = this.dh.validateClient(name,key);
						Boolean success = false;
						
						String uid = null;
						
						if(valid){
							//If client is logged in, remove them from the active client list
							uid = dm.getUserID(name);
						}
						String response = (success) ? "valid" : "error";
						
						//Create the Stack<DataValue> response object to send back to the client
						Stack<DataValue> resp_msg = new Stack<DataValue>();
						
						//Add the DataValue parameters to the response object
						resp_msg.push(new DataValue("user_id",uid));
						resp_msg.push(new DataValue("response_value",response));
						//The response_type will match the request type
						resp_msg.push(new DataValue("response_type","userid"));
						out.writeObject(resp_msg);
						System.out.println("UserID :: sending response obj...");
						
					}else if(req_type.compareTo("addmatch")==0){
						
						//Next 2 DataValues are popped from the stack:
						//user_name				user name
						//session_key			encrypted session key string
						String name = recv_msg.pop().getValue();
						String key = recv_msg.pop().getValue();
						String option = recv_msg.pop().getValue();
						Boolean valid = this.dh.validateClient(name,key);
						Boolean success = false;
						
						if(valid){
							Profile prof = (Profile) in.readObject();
							success = dm.addMatch(name,prof,option);
						}
						String response = (success) ? "valid" : "error";
						
						//Create the Stack<DataValue> response object to send back to the client
						Stack<DataValue> resp_msg = new Stack<DataValue>();
						
						//Add the DataValue parameters to the response object
						resp_msg.push(new DataValue("response_value",response));
						//The response_type will match the request type
						resp_msg.push(new DataValue("response_type","addmatch"));
						
						//Send the object over the output stream
						out.writeObject(resp_msg);
						System.out.println("AddMatch :: sending response obj...");
						
					}else if(req_type.compareTo("getdistance")==0){
						
						//Next 2 DataValues are popped from the stack:
						//user_name				user name
						//session_key			encrypted session key string
						String name = recv_msg.pop().getValue();
						String key = recv_msg.pop().getValue();
						Boolean valid = this.dh.validateClient(name,key);
						Boolean success = false;
						int distance = -1;
						if(valid){
							//Next DataValue determines whether request params
							//are 2 cities or 2 user ids
							String by_city = recv_msg.pop().getValue();
							
							String origin;
							String destination;
							
							if(by_city.compareTo("true")==0){
								//Next 2 DataValues are popped from the stack:
								//origin				origin city
								//destination			destination city
								origin = recv_msg.pop().getValue();
								destination = recv_msg.pop().getValue();
							}else{
								//Next DataValues are popped from the stack:
								//origin				origin city
								//destination			destination city
								String uname1 = recv_msg.pop().getValue();
								String uid2 = recv_msg.pop().getValue();
								String uname2 = dm.getUserName(uid2);
								
								origin = this.dh.locations.get(uname1);
								destination = this.dh.locations.get(uname2);
							}
							//Retrieve distance between input cities
							distance = dm.getDistance(origin,destination);
							
							//Failures return -1, so set the return to false if this is the case
							success = (distance >= 0);
						}
						String response = (success) ? "valid" : "error";
						
						//Create the Stack<DataValue> response object to send back to the client
						Stack<DataValue> resp_msg = new Stack<DataValue>();
						
						//Add the DataValue parameters to the response object
						resp_msg.push(new DataValue("distance",Integer.toString(distance)));
						resp_msg.push(new DataValue("response_value",response));
						//The response_type will match the request type
						resp_msg.push(new DataValue("response_type","getdistance"));
						
						//Send the object over the output stream
						out.writeObject(resp_msg);
						System.out.println("GetDistance :: sending response obj...");
						
					}else if(req_type.compareTo("getprofile")==0){
						
						//Next 2 DataValues are popped from the stack:
						//user_name				user name
						//session_key			encrypted session key string
						String name = recv_msg.pop().getValue();
						String key = recv_msg.pop().getValue();
						Boolean valid = this.dh.validateClient(name,key);
						Boolean success = false;
						Profile prof = null;
						
						//If user is logged in
						if(valid){
							//Return user profile as profile object
							prof = dm.getProfile(name);
							success = (prof != null);
							if(success){
								prof.setCity(this.dh.locations.get(name));
							}
						}
						
						String response = (success) ? "valid" : "error";
						
						//Create the Stack<DataValue> response object to send back to the client
						Stack<DataValue> resp_msg = new Stack<DataValue>();
						
						//Add the DataValue parameters to the response object
						resp_msg.push(new DataValue("response_value",response));
						//The response_type will match the request type
						resp_msg.push(new DataValue("response_type","getprofile"));
						
						//Send the object over the output stream
						out.writeObject(resp_msg);
						
						//Additionally, if the request was successful, then send the
						//profile object directly over the stream
						if(success){
							out.writeObject(prof);
							System.out.println("GetProfile :: sending response obj...");
						}
						
					}else if(req_type.compareTo("editprofile")==0){
						
						//Next 2 DataValues are popped from the stack:
						//user_name				user name
						//session_key			encrypted session key string
						String name = recv_msg.pop().getValue();
						String key = recv_msg.pop().getValue();
						Boolean valid = this.dh.validateClient(name,key);
						Boolean success = false;
						Profile prof = null;
						
						//If user is logged in
						if(valid){
							//Read profile object directly over input stream
							prof = (Profile) in.readObject();
							
							//System.out.println(prof.getAttribute("profile_fname"));
							//Update return value based on success of editProfile method
							success = dm.editProfile(name,prof);
						}
						
						String response = (success) ? "valid" : "error";
						
						//Create the Stack<DataValue> response object to send back to the client
						Stack<DataValue> resp_msg = new Stack<DataValue>();
						
						//Add the DataValue parameters to the response object
						resp_msg.push(new DataValue("response_value",response));
						//The response_type will match the request type
						resp_msg.push(new DataValue("response_type","editprofile"));
						
						//Send the object over the output stream
						out.writeObject(resp_msg);
						System.out.println("EditProfile :: sending response obj...");
						
					}else if(req_type.compareTo("getmatches")==0){
						
						//Next 2 DataValues are popped from the stack:
						//user_name				user name
						//session_key			encrypted session key string
						String name = recv_msg.pop().getValue();
						String key = recv_msg.pop().getValue();
						Boolean valid = this.dh.validateClient(name,key);
						Boolean success = false;
						ArrayList<Profile> profiles = null;
						
						//If user is logged in
						if(valid){
							//Next DataValue popped from the stack:
							//local				true/false
							String local = recv_msg.pop().getValue();
							
							//If local is set to true, filter matches by distance settings
							if(local.compareTo("true")==0){
								profiles = dm.getNearbyMatches(name,this.dh.getNearbyClients(name));
								for(Profile p : profiles){
									p.setCity(this.dh.locations.get(dm.getUserName(p.getUID())));
								}
							}else{
								profiles = dm.getMatches(name);
							}
							success = (profiles != null);
						}
						
						String response = (success) ? "valid" : "error";
						
						//Create the Stack<DataValue> response object to send back to the client
						Stack<DataValue> resp_msg = new Stack<DataValue>();
						
						//Add the DataValue parameters to the response object
						resp_msg.push(new DataValue("response_value",response));
						//The response_type will match the request type
						resp_msg.push(new DataValue("response_type","getmatches"));
						
						//Send the object over the output stream
						out.writeObject(resp_msg);
						if(success){
							out.writeObject(profiles);
							System.out.println("GetMatches :: sending response obj...");
						}
						
						//Print server status
						this.dh.serverStatus();
					}else if(req_type.compareTo("getmutualmatches")==0){
						
						//Next 2 DataValues are popped from the stack:
						//user_name				user name
						//session_key			encrypted session key string
						String name = recv_msg.pop().getValue();
						String key = recv_msg.pop().getValue();
						Boolean valid = this.dh.validateClient(name,key);
						Boolean success = false;
						ArrayList<Profile> profiles = null;
						
						//If user is logged in
						if(valid){
							profiles = dm.getMutualMatches(name);
							success = (profiles != null);
						}
						
						String response = (success) ? "valid" : "error";
						
						//Create the Stack<DataValue> response object to send back to the client
						Stack<DataValue> resp_msg = new Stack<DataValue>();
						
						//Add the DataValue parameters to the response object
						resp_msg.push(new DataValue("response_value",response));
						//The response_type will match the request type
						resp_msg.push(new DataValue("response_type","getmatches"));
						
						//Send the object over the output stream
						out.writeObject(resp_msg);
						if(success){
							out.writeObject(profiles);
							System.out.println("GetMatches :: sending response obj...");
						}
						
					}else if(req_type.compareTo("getmatchhistory")==0){
						
						//Next 2 DataValues are popped from the stack:
						//user_name				user name
						//session_key			encrypted session key string
						String name = recv_msg.pop().getValue();
						String key = recv_msg.pop().getValue();
						Boolean valid = this.dh.validateClient(name,key);
						Boolean success = false;
						ArrayList<Profile> profiles = null;
						
						//If user is logged in
						if(valid){
							profiles = dm.getMatchHistory(name);
							success = (profiles != null);
						}
						
						String response = (success) ? "valid" : "error";
						
						//Create the Stack<DataValue> response object to send back to the client
						Stack<DataValue> resp_msg = new Stack<DataValue>();
						
						//Add the DataValue parameters to the response object
						resp_msg.push(new DataValue("response_value",response));
						//The response_type will match the request type
						resp_msg.push(new DataValue("response_type","getmatches"));
						
						//Send the object over the output stream
						out.writeObject(resp_msg);
						if(success){
							out.writeObject(profiles);
							System.out.println("GetMatches :: sending response obj...");
						}
						
					}else if(req_type.compareTo("heartbeat")==0){
						
						//Next 2 DataValues are popped from the stack:
						//user_name				user name
						//session_key			encrypted session key string
						String name = recv_msg.pop().getValue();
						String key = recv_msg.pop().getValue();
						Boolean success = this.dh.validateClient(name,key);
						
						String response = (success) ? "valid" : "error";
						
						//Create the Stack<DataValue> response object to send back to the client
						Stack<DataValue> resp_msg = new Stack<DataValue>();
						
						//Add the DataValue parameters to the response object
						resp_msg.push(new DataValue("response_value",response));
						//The response_type will match the request type
						resp_msg.push(new DataValue("response_type","heartbeat"));
						
						//Send the object over the output stream
						out.writeObject(resp_msg);
						System.out.println("Heartbeat :: sending response obj...");
						
						//If heartbeat request succeeds, reset timestamp for corresponding user
						if(success){
							this.dh.resetHeartbeat(name);
						}
					}else{
						System.out.println("Action not recognized...");
					}
					
					//***************************************************************
					//End: series of if/else ifs for each possible request type
					
					
					this.dh.updateHeartbeats();
				}
			}catch(IOException ioe){
				
			}catch(ClassNotFoundException cnfe){
				
			}
		}
	}
	
}