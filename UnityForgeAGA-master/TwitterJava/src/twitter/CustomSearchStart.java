package twitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * This class can be accessed as a black-box library of relationship mining methods.
 * Just pass in two ArrayList<String> of people and relationships into the method
 * named extractRelationshipData.
 * @author mikhailjacob
 *
 */
public class CustomSearchStart {

	static String urlString = "http://www.google.com/search?safe=on&q=";
	static String matchRegExStart = "\\<.*?\\>(";
	static String matchRegExEnd = ")\\</.*?\\>([a-zA-Z\\s0-9\\']*)";
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws JSONException 
	 */
	public static void main(String[] args) throws ClientProtocolException, IOException, JSONException
	{
		ArrayList<String> TweetList = new ArrayList<String>();
		ArrayList<String> Relationships = new ArrayList<String>();
		
		System.out.println("Enter people to get corpus to extract Binary Relationships from (Enter 'Done' when complete):\n");
		BufferedReader Reader = new BufferedReader(new InputStreamReader(System.in));
		
		String InputLine;
		while(!(InputLine = Reader.readLine()).equalsIgnoreCase("Done"))
		{
			TweetList.add(InputLine);
		}
		
		if(TweetList.isEmpty())
		{
			System.out.println("ERROR! No people entered to analyse!");
			return;
		}
		
		System.out.println("Enter relationship predicates to get corpus to extract Binary Relationships from (Enter 'Done' when complete):\n");
		
		while(!(InputLine = Reader.readLine()).equalsIgnoreCase("Done"))
		{
			Relationships.add(InputLine);
		}
		
		if(Relationships.isEmpty())
		{
			System.out.println("ERROR! No relationships entered to use!");
			return;
		}
		
		ArrayList<String> Results = extractRelationshipData(TweetList, Relationships);
		
		for(String result : Results)
		{
			System.out.println(result);
		}
	}
	
	public static ArrayList<String> extractRelationshipData(ArrayList<String> People, ArrayList<String> Relationships) throws ClientProtocolException, IOException, JSONException
	{
		ArrayList<String> Results = new ArrayList<String>();
		
		for(String person : People)
		{
			for(String relationship : Relationships)
			{
				JSONArray resultsArray = new JSONArray();
				
				HttpClient httpclient = new DefaultHttpClient();
		        try
		        {
		        	String personURL = person.replaceAll("\\s", "+");
		        	String relationshipClean = relationship.replaceAll("\\s", "+");
		        	String currentURLString = urlString + "%22" + personURL + "+" + relationshipClean + "%22";
		            HttpGet httpget = new HttpGet(currentURLString);

		            //System.out.println("executing request " + httpget.getURI());

		            // Create a response handler
		            ResponseHandler<String> responseHandler = new BasicResponseHandler();
		            String responseBody = httpclient.execute(httpget, responseHandler);
		            
		            String unsafe = responseBody;
		            String safe = Jsoup.clean(unsafe, Whitelist.basic());
		            
		            //safe = safe.replaceAll("\\<.*?\\>", "");
		            //safe = safe.replaceAll("\\{.*?\\}", "");
		            
		            //System.out.println("----------------------------------------");
		            //System.out.println(safe);
		            //System.out.println("----------------------------------------");
		            
		            String regEx = matchRegExStart + person + ") (" + relationship + matchRegExEnd;
		            
		            //System.out.println(regEx);
		            
		            Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		            Matcher matcher = pattern.matcher(safe);
		            //int count = 0;
		            
		            ArrayList<String> objectsMined = new ArrayList<String>();
		            
		            while (matcher.find())
		            {
		                //System.out.println(count++ + " - " + matcher.group(1) + ", " + matcher.group(2) + ". " + matcher.group(3));
		            	
		            	String object = matcher.group(3).trim();
		            	
		            	JSONObject currentResultObj = new JSONObject();
		            	currentResultObj.put("person", person.trim());
		            	currentResultObj.put("relationship", relationship.trim());
		            	currentResultObj.put("object", object);
		            	
		            	if(object.equalsIgnoreCase("") || objectsMined.contains(object.toLowerCase()))
		            	{
		            		continue;
		            	}
		            	
		            	objectsMined.add(object.toLowerCase());
		            	
		            	resultsArray.put(currentResultObj);
		            }/*
		            //Goddamnit Afshin!!! Making me comment this out... Grrr... :P
		            if(objectsMined.isEmpty())
		            {
		            	JSONObject currentResultObj = new JSONObject();
		            	currentResultObj.put("person", person.trim());
		            	currentResultObj.put("relationship", relationship.trim());
		            	currentResultObj.put("object", "unknown");
		            	
		            	resultsArray.put(currentResultObj);
		            }*/
		        }
		        finally
		        {
		            // When HttpClient instance is no longer needed,
		            // shut down the connection manager to ensure
		            // immediate deallocation of all system resources
		            httpclient.getConnectionManager().shutdown();
		        }
				
				Results.add(resultsArray.toString());
			}
		}
		
		return Results;
	}
}
