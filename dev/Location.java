public class Location{
	
	DataRequest request_obj;
	
	public Location(DataRequest dr){
		this.request_obj = dr;
	}
	
	public int getDistance(String city1, String city2){
		return this.request_obj.getDistance(city1,city2);
	}
	
}