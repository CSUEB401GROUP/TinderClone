import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataStore{
	private String path;
	private Boolean connected;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	public DataStore(String path){
		//Path to DB file
		this.path = path;
		//Status of read/write connection (true = open, false = closed)
		this.connected = false;
	}
	
	public void setPath(String path){
		this.path = path;
	}
	
	public String getPath(){
		return this.path;
	}
	
	//Open a read connection to the DB
	public Boolean openReadDB(){
		try{
			this.reader = new BufferedReader(new FileReader(this.getPath()));
			this.connected = true;
			return true;
		}catch(IOException ioe){
			
		}
		return false;
	}
	//Close read connection to the DB
	public Boolean closeReadDB(){
		try{
			if(this.connected){
				this.reader.close();
				this.connected = false;
				return true;
			}
		}catch(IOException ioe){
			
		}
		return false;
	}
	//Open a write connection to the DB
	public Boolean openWriteDB(Boolean append){
		try{
			this.writer = new BufferedWriter(new FileWriter(this.getPath(),append));
			this.connected = true;
			return true;
		}catch(IOException ioe){
			
		}
		return false;
	}
	
	//Close write connection to the DB
	public Boolean closeWriteDB(){
		try{
			if(this.connected){
				this.writer.close();
				this.connected = false;
				return true;
			}
		}catch(IOException ioe){
			
		}
		return false;
	}
	
	private void updateDB(ArrayList<HashMap<String, String>> row_list, String[] headers) {

		try{
			String filepath = this.getPath();
			File source = new File(filepath);
			List<String> lines = Files.readAllLines(source.toPath());
			if(row_list.size()>0){
				for(HashMap<String,String> row : row_list){
					String line = "";
					for(String header : headers){
						line+=row.get(header)+",";
					}
					
					line = line.substring(0,line.length()-1);
					String row_num = row.get(headers[0]);
					
					lines.set(Integer.parseInt(row_num), line);
				}
				Files.write(source.toPath(), lines);
				this.closeWriteDB();
			}
		}catch(IOException ioe){
			
		}		
	}
	
	//Selects a row via the provided selector criteria and updates its values
	public Boolean setDataEntry(String selector,String select_value, HashMap<String,String> new_values){
		try{
			this.openReadDB();
			if(this.connected){
				String[] headers = this.reader.readLine().split(",");
				ArrayList<HashMap<String,String>> row_list = new ArrayList<HashMap<String,String>>();
				while(this.reader.ready()){
					HashMap<String,String> row = new HashMap<String,String>();
					String[] values = this.reader.readLine().split(",");
					Set<String> keys = new_values.keySet();
					Boolean found = false;
					for(int i=0;i<values.length;i++){
						if(headers[i].compareTo(selector)==0&&values[i].compareTo(select_value)==0){
							found = true;
						}
						row.put(headers[i],values[i]);
					}
					if(found){
						for(String key : keys){
							row.put(key,new_values.get(key));
						}
					}
					row_list.add(row);
				}
				this.closeReadDB();
				if(row_list.size()>0){
					this.updateDB(row_list,headers);
					return true;
				}else{
					return false;
				}
			}
		}catch(IOException ioe){
			
		}		
		return false;
	}
	
	//Retrieves multiple rows based on the provided selector parameters
	public DataEntryList getDataEntryList(HashMap<String,String> selectors){
		try{
			this.openReadDB();
			if(this.connected){
				String[] headers = this.reader.readLine().split(",");
				ArrayList<HashMap<String,String>> row_list = new ArrayList<HashMap<String,String>>();
				while(this.reader.ready()){
					HashMap<String,String> row = new HashMap<String,String>();
					String[] values = this.reader.readLine().split(",");
					for(int i=0;i<values.length;i++){
						row.put(headers[i],values[i]);
					}
					if(!selectors.isEmpty()){
						Set<String> keys = selectors.keySet();
						Boolean valid = true;
						for(String key : keys){
							if(!(row.get(key).compareTo(selectors.get(key))==0)){
								valid = false;
							}
						}
						if(valid){
							row_list.add(row);
						}
					}else{
						row_list.add(row);
					}
				}
				this.closeReadDB();
				
				if(row_list.size()>0){
					return new DataEntryList(row_list);
				}else{
					return null;
				}
			}
		}catch(IOException ioe){
			
		}
		return null;
	}
	
	//Pulls all rows from the DB
	public DataEntryList getAllEntries(){
		try{
			this.openReadDB();
			if(this.connected){
				String[] headers = this.reader.readLine().split(",");
				ArrayList<HashMap<String,String>> row_list = new ArrayList<HashMap<String,String>>();
				while(this.reader.ready()){
					HashMap<String,String> row = new HashMap<String,String>();
					String[] values = this.reader.readLine().split(",");
					for(int i=0;i<values.length;i++){
						row.put(headers[i],values[i]);
					}
					row_list.add(row);
				}
				this.closeReadDB();
				
				DataEntryList del = new DataEntryList();
				if(row_list.size()>0){
					del = new DataEntryList(row_list);
				}
				return del;
			}
		}catch(IOException ioe){
			
		}
		return null;
	}
	
	//Adds entry to the DB
	public Boolean addEntry(HashMap<String,String> row,String[] headers){
		Boolean result = false;
		try{
			this.openWriteDB(true);
			if(this.connected){
				String line = "";
				for(int i=0;i<headers.length;i++){
					line += row.get(headers[i]);
					if(i < headers.length-1){
						line+=",";
					}
				}
				this.writer.write(line+"\n");
				this.closeWriteDB();
				result = true;
			}
		}catch(IOException ioe){
			
		}
		return result;
	}
	
	//Check if row exists given the provided parameters
	public Boolean entryExists(HashMap<String,String> row){
		DataEntryList del = this.getDataEntryList(row);
		if(del!=null){
			return true;
		}else{
			return false;
		}
	}
}