import java.io.*;
import java.util.*;

public class SimpleDriver{
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
			Authentication auth = new Authentication(dr);
			User user = new User(dr);
			MatchSystem ms = new MatchSystem(dr);
			MainWindow win = new MainWindow(ms);
			Boolean valid = false;
			
			//Call appropriate methods (login or register) from DataRequest object
			//based on the parameters passed
			if(args[0].compareTo("login")==0){
				valid = auth.login(args[1],args[2]);
			}else if(args[0].compareTo("register")==0){
				valid = auth.register(args[1],args[2]);
			}
			
			//If the login/register succeeds
			if(valid){
				//Create ArrayList to hold potential match profiles
				ProfileList profiles;
				Boolean loop = true;
				long timestart = System.currentTimeMillis();
				long timeend = System.currentTimeMillis();
				long difference = timeend - timestart;
				
				user.editProfileSetting("profile_age",Integer.toString(new Random().nextInt(50)),false);
				user.saveAll();

				
				//Interval to resend heartbeat message in seconds
				int beat_interval = 5;
				
				//Infinite loop to keep client connected
				while(loop){
					//Checks difference in time between now and last beat
					//If it's been 15 seconds it sends a heartbeat request
					if(difference >= beat_interval * 1000){
						//Reset time marker and send heartbeat
						timestart = System.currentTimeMillis();
						auth.sendHeartbeat();
						//win.updateText("Heartbeat sent");
						System.out.println("-------------------------------------");
						//Retrieve profiles
						//The 'true' option specifies only nearby matches
						System.out.println("Active profiles near me: ");
						profiles = ms.getMatches(true);
						//If there are any profiles in the results, print them
						if(profiles!=null){
							Profile p = profiles.getNextProfile();
							while(p != null){
								p.print();
								if(p.getAttribute("profile_fname",false).compareTo("amon")==0 && user.getProfile().getAttribute("profile_fname",false).compareTo("amon")!=0){
									ms.createMatch(p,"right");
								}
								p = profiles.getNextProfile();
							}
						}
						System.out.println("-------------------------------------");
						System.out.println("Previously rated profiles: ");
						profiles = ms.getMatchHistory();
						//If there are any profiles in the results, print them
						if(profiles!=null){
							Profile p = profiles.getNextProfile();
							while(p != null){
								p.print();
								p = profiles.getNextProfile();
							}
						}
						System.out.println("-------------------------------------");
						System.out.println("Mutually matched profiles: ");
						profiles = ms.getMutualMatches();
						//If there are any profiles in the results, print them
						if(profiles!=null){
							Profile p = profiles.getNextProfile();
							while(p != null){
								p.print();
								p = profiles.getNextProfile();
							}
						}
						System.out.println("-------------------------------------");
					}else{
						if(difference%1000==0){
							//Line below counts down heartbeat timer
							//System.out.println(Long.toString(10 - difference/1000) + " seconds to heartbeat...");
						}
					}
					timeend = System.currentTimeMillis();
					difference = timeend - timestart;
					
				}
				auth.logout();
			}
		}else{
			//If the user starts the program with no parameters
			//run the driver in host mode by creating a DataHost
			//object instead of a DataRequest object
			DataHost dh = new DataHost();
		}

	}
}