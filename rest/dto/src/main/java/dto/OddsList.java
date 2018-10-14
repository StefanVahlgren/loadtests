package dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OddsList {
	List<Odds> oddsList = new ArrayList<Odds>();
	
	public OddsList(){
		oddsList = Arrays.asList(
				new Odds("Sverige", "Danmark"), 
				new Odds("USA", "Ryssland"), 
				new Odds("Syd Korea", "Nord Korea"));
	}

	public List<Odds> getOddsList() {
		return oddsList;
	}

	public void setOddsList(List<Odds> oddsList) {
		this.oddsList = oddsList;
	}
	
	
}
