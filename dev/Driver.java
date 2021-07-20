import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

public class Driver{
	public static void main(String[] args){

		
		//User should provide parameters: login or register action,
		//credentials for client mode and no parameters for host mode.
		//(Examples are running program from command line)
		//Example: java Driver login chris 2222			client mode, login
		//Example: java Driver register chris 2222		client mode, register
		//Example: java Driver							host mode
		if(args.length == 3){
			//Create a DataRequest object to serve as the client
			DataRequest dr = new DataRequest();
			Boolean valid = false;
			
			//Call appropriate methods (login or register) from DataRequest object
			//based on the parameters passed
			if(args[0].compareTo("login")==0){
				valid = dr.login(args[1],args[2]);
			}else if(args[0].compareTo("register")==0){
				valid = dr.register(args[1],args[2]);
			}
			
			//If the login/register succeeds
			if(valid){
				//Create ArrayList to hold potential match profiles
				ArrayList<Profile> profiles;
				Boolean loop = true;
				long timestart = System.currentTimeMillis();
				long timeend = System.currentTimeMillis();
				long difference = timeend - timestart;
				
				//Interval to resend heartbeat message in seconds
				int beat_interval = 15;
				
				//Infinite loop to keep client connected
				while(loop){
					//Checks difference in time between now and last beat
					//If it's been 15 seconds it sends a heartbeat request
					if(difference >= beat_interval * 1000){
						//Reset time marker and send heartbeat
						timestart = System.currentTimeMillis();
						dr.heartbeat();
						//Retrieve profiles
						//The 'true' option specifies only nearby matches
						profiles = dr.getMatches(true);
						//If there are any profiles in the results, print them
						if(profiles!=null){
							System.out.println("Active profiles near me: ");
							profiles.forEach((p) -> p.print());
						}
					}else{
						if(difference%1000==0){
							//Line below counts down heartbeat timer
							//System.out.println(Long.toString(10 - difference/1000) + " seconds to heartbeat...");
						}
					}
					timeend = System.currentTimeMillis();
					difference = timeend - timestart;
					
				}
				dr.logout();
			}
		}else{
			//If the user starts the program with no parameters
			//run the driver in host mode by creating a DataHost
			//object instead of a DataRequest object
			DataHost dh = new DataHost();
		}

	}
}