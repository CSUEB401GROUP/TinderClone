import java.io.*;
import java.util.*;

public class SimpleDriver{
	public static void main(String[] args){
		
		
		//User should provide parameters: host or client
		//(Examples are running program from command line)
		//Example: java SimpleDriver client				client mode
		//Example: java SimpleDriver host				host mode
		if(args.length == 1 && args[0].compareTo("client")==0){
			//Create a DataRequest object to serve as the client
			DataRequest dr = new DataRequest();
			Authentication auth = new Authentication(dr);
			User user = new User(dr);
			MatchSystem ms = new MatchSystem(dr);

			Boolean valid = false;
			SplashWindow splash = new SplashWindow(auth,user,ms);
			Boolean loop = true;

			long timestart = System.currentTimeMillis();
			long timeend = System.currentTimeMillis();
			long difference;
			int beat_interval = 15;
			
			while(loop){
				//Interval to resend heartbeat message in seconds
				timeend = System.currentTimeMillis();
				difference = timeend - timestart;

				//Checks difference in time between now and last beat
				//If it's been 15 seconds it sends a heartbeat request
				if(difference >= beat_interval * 1000){
					//Reset time marker and send heartbeat
					timestart = System.currentTimeMillis();
					auth.sendHeartbeat();
				}
				timeend = System.currentTimeMillis();
				difference = timeend - timestart;
			}
		}else if(args.length == 1 && args[0].compareTo("host")==0){
			//If the user starts the program with no parameters
			//run the driver in host mode by creating a DataHost
			//object instead of a DataRequest object
			DataHost dh = new DataHost();
		}

	}
}