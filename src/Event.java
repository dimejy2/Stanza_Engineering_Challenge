
public class Event {
	
	private String myURL; 
	private int myConfidenceScore ; 
	
	
	public Event(String url, int confidenceScore){
		myURL = url; 
		myConfidenceScore = confidenceScore; 
	}
	
	public String getURL() {
		return myURL;
	}
	
	public int getConfidenceScore() {
		return myConfidenceScore;
	}
	
	public String toString(){
		
		return  myConfidenceScore +", "+myURL; 
	}
}
