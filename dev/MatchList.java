import java.util.*;

public class MatchList{
	
	ArrayList<Match> matches;
	int curr_index;
	
	public MatchList(){
		this.matches = new ArrayList<Match>();
		this.curr_index = -1;
	}
	
	public Boolean addMatch(Match m){
		this.matches.add(m);
		return true;
	}

	public Match getMatch(int i){
		if(this.matches.size() > 0){
			return this.matches.get(i);
		}else{
			return null;
		}
	}	
	
	public Match getNextMatch(){
		if(this.matches.size() > 0){
			if(this.curr_index < this.matches.size() - 1){
				this.curr_index++;
				return this.matches.get(this.curr_index);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
}