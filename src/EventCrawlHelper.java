import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class EventCrawlHelper {


	private List<String> links = new LinkedList<String>(); 
	private Document htmlDocument;
	private int confidence = 0; 

	private static final String USER_AGENT =
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private static final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	private static final String[] months  = {"January", "February", "March", "April", "May", "June", 
		"July", "August", "September", "October", "November", "December"};
	private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");
	private static final Pattern filters = Pattern.compile("(.(html))");

	private static final Pattern TIMEPATTERN = Pattern.compile("(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)");

	public void crawl(String nextURL){
		try
		{
			System.out.println(nextURL);
			Connection connection = Jsoup.connect(nextURL).userAgent(USER_AGENT);
			connection.timeout(7000);
			htmlDocument = connection.get();


			Elements linksOnPage = htmlDocument.select("a[href]");

			for(Element link : linksOnPage)
			{
				if(!toIgnore(link.baseUri())){
					links.add(link.absUrl("href"));
				}
			}
		}
		catch(IOException ioe)
		{
			// We were not successful in our HTTP request
			System.out.println("Error in out HTTP request " + ioe);
		}	


	} 

	public boolean successfulSearch(){

		confidence += ((matcher(months))? 1 : 0) + ((matcher(days))? 1 : 0) + ((matchTime())? 1 : 0);
		return matcher(months) ||matcher(days) || matchTime(); 
	}


	public List<String> getLinks(){
		return links;  

	} 


	private boolean matcher(String[] toMatch){
		Boolean toReturn = false; 
		String bodyText = htmlDocument.body().text().toLowerCase();

		for( String s : toMatch){
			if(bodyText.contains(s.toLowerCase())) {
				toReturn = true; 
				confidence ++; 
			} 
		}

		return toReturn; 
	}


	private boolean matchTime(){
		String bodyText = htmlDocument.body().text().toLowerCase();
		Matcher matchTime = TIMEPATTERN.matcher(bodyText); 
		if(matchTime.find()) confidence ++; 
		return matchTime.find(); 
	}

	public int getConfidenceScore(){
		return confidence; 
	}

	private boolean toIgnore(String test){

		return  IMAGE_EXTENSIONS.matcher(test).find() ||
				filters.matcher(test).find(); 
	}
}
