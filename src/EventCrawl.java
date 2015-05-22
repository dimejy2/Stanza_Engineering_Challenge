import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class EventCrawl{

	public static final int MAXSEARCHDEPTH = 200;
	private Set<String> visited = new HashSet<String>();
	private Queue<String> toVisit = new LinkedList<String>();
	private List <Event> results = new ArrayList<>(); 
	private String homeURL = ""; 

	private String nextUrlToVisit()
	{
		String nextUrl;
        do
        {
            nextUrl = toVisit.poll();
        } while(toIgnore(nextUrl));
        this.visited.add(nextUrl);
        return nextUrl;	
        
	
	}
	
	
	
	private boolean toIgnore(String test){
		
		return visited.contains(test) ||
				test ==""|| 
//				!test.contains(homeURL)||
				test.contains("mailto")
				|| test.contains("mailto:?")
				|| test.contains("/about")
				||test.contains("/visit")
				||test.contains("/member")
				||test.contains("Category")
				||test.contains("collection")
				||test.contains("news");
	}
	
	
	public void search(String url)
    {
		try {
			homeURL = new URL(url).getHost();
		} catch (MalformedURLException e) {
			System.out.println("Please use a valid URL");
		}
		int pauseCounter = 0;
        while(results.size() < 20)
        {
        	 
            String currentUrl;
            EventCrawlHelper help = new EventCrawlHelper();
            if(toVisit.isEmpty())
            {
                currentUrl = url;
                visited.add(url);
            }
            else
            {
                currentUrl = nextUrlToVisit();
            }
            help.crawl(currentUrl); 
            
            if(help.successfulSearch())
            { 
             
            	System.out.printf("possible event found at %s with confidence %d \n", currentUrl, help.getConfidenceScore());
            	if (help.getConfidenceScore() >=3 && help.getConfidenceScore() <= 5 && 
            			!homeURL.toLowerCase().contains("boston")){
            		Event temp = new Event(currentUrl, help.getConfidenceScore());
            		results.add(temp);
            	}
            	
            }
            toVisit.addAll(help.getLinks());
            pauseCounter ++; 
            
            if(pauseCounter == 20){
            	 pauseCounter = 0; 
            	 pauseFor(10);
            }
            
        }
        System.out.println(String.format("Visited %s web page(s) ", visited.size()));
    }
	
	
	public List<Event> getEventResults(){
		
		return results;
	}
	
	private void pauseFor(long s) {
		try {
			Thread.currentThread();
			System.out.printf("Waiting %d seconds \n", s); 
			Thread.sleep(s * 500);

		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Resuming");
	}
	
}