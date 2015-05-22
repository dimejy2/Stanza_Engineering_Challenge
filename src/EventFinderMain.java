import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class EventFinderMain {
	
	public static void helper(){
		Scanner sc = new Scanner(System.in); 
		System.out.println("Enter a valid web address: ");
		String input = sc.next();
		List<Event> tempList = new ArrayList<>() ;
		try {
			@SuppressWarnings("unused")
			URL inputURL = new URL(input);
			EventCrawl toCrawl = new EventCrawl(); 
			
			toCrawl.search(input);
			
			tempList = toCrawl.getEventResults(); 
			
			for( Event e : tempList){
				
				System.out.println(e.toString()); 
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			helper(); 
			
		} 
		
		
		for( Event e : tempList){
			
			System.out.println(e.toString()); 
		}
		sc.close(); 	
		
		
	}
	
	
	public static void main(String[] args){
		
		helper(); 
	}
	

}
