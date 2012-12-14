package twitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;



import edu.washington.cs.knowitall.extractor.ReVerbExtractor;
import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunction;
import edu.washington.cs.knowitall.extractor.conf.ReVerbConfFunction;
//import edu.washington.cs.knowitall.extractor.conf.ReVerbIndependentConfFunction;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.OpenNlpSentenceChunker;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * This class can be accessed as a black-box library of tweet classification methods.
 * Just pass in an ArrayList<String> of tweets or sentences into the method
 * named classifyBinaryRelations.
 * @author mjacob6
 *
 */
public class ReVerbStart
{	
	/**
	* @param args
	* @throws IOException 
	* @throws JSONException 
	*/
	public static void main(String[] args) throws IOException, JSONException
	{
		ArrayList<String> People = new ArrayList<String>();
		ArrayList<String> Relationships = new ArrayList<String>();
		System.out.println("Enter people for analysing Binary Relationships (Enter 'Done' when complete):\n");
		BufferedReader Reader = new BufferedReader(new InputStreamReader(System.in));
		String InputLine;
		while(!(InputLine = Reader.readLine()).equalsIgnoreCase("Done"))
		{
			People.add(InputLine);
		}
		
		if(People.isEmpty())
		{
			System.out.println("ERROR! No tweet entered to analyse!");
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
		
		//Testing only ReVerb
		//ArrayList<String> Results = classifyBinaryRelations(TweetList);
		
		//Testing ReVerb + Google Custom Search
		ArrayList<String> Results = getObjectsFromPeopleAndRelationships(People, Relationships);
		
		//Testing Pure Google Custom Search
		//ArrayList<String> Results = getObjectsFromPeopleAndRelationshipsPureGoogle(People, Relationships);
		
		System.out.println("\n");
		
		for(String result: Results)
		{
			System.out.println(result);
		}
	}
	
	/**
	 * Gets the person part of the Binary Relationship
	 * @param input
	 * @return String containing person's name
	 * @throws JSONException
	 */
	public static String getPerson(String input) throws JSONException
	{
		JSONArray inputObj = new JSONArray(input);
		String result = inputObj.optJSONObject(0).optString("person");
		
		if(result.equalsIgnoreCase(null))
		{
			result = "unknown";
		}
		
		return result;
	}
	
	/**
	 * Gets the relationship part of the Binary Relationship
	 * @param input
	 * @return String containing relationship
	 * @throws JSONException
	 */
	public static String getRelationship(String input) throws JSONException
	{
		JSONArray inputObj = new JSONArray(input);
		String result = inputObj.optJSONObject(0).optString("relationship");
		
		if(result.equalsIgnoreCase(null))
		{
			result = "unknown";
		}
		
		return result;
	}
	
	/**
	 * Gets an ArrayList<String> of objects that are extracted from a (person, relationship) pair.
	 * @param input
	 * @return ArrayList<String> containing mined objects 
	 * @throws JSONException
	 */
	public static ArrayList<String> getObjects(String input) throws JSONException
	{
		if(input.equalsIgnoreCase("[]"))
		{
			return new ArrayList<String>();
		}
		
		ArrayList<String> Results = new ArrayList<String>();
		JSONArray inputObj = new JSONArray(input);
		
		for(int index = 0; index < inputObj.length(); index++)
		{
			JSONObject objectObj = inputObj.getJSONObject(index);
			String result = objectObj.optString("object");

			if(result.equalsIgnoreCase(null))
			{
				result = "unknown";
			}
			
			Results.add(result);
		}
		
		return Results;
	}
	
	/**
	 * Uses ReVerb to extract Binary Relationships from Tweets.
	 * Takes in an ArrayList<String> of Tweets and returns an ArrayList<String> 
	 * of JSON-convertible, extracted binary relationships from the input list of tweets.
	 * @param Tweets
	 * @return ArrayList<String> of Binary Relationships extracted from Input Tweets
	 * @throws IOException
	 * @throws JSONException
	 */
	public static ArrayList<String> classifyBinaryRelations(ArrayList<String> Tweets) throws IOException, JSONException
	{
		ArrayList<String> Results = new ArrayList<String>();
		
		// Looks on the classpath for the default model files.
		OpenNlpSentenceChunker chunker = new OpenNlpSentenceChunker();
		
		for(String tweet : Tweets)
		{
			ChunkedSentence sent = chunker.chunkSentence(tweet.replaceAll("\\'", ""));

			// Prints out the (token, tag, chunk-tag) for the sentence
			//System.out.println(tweet);
			/*for (int i = 0; i < sent.getLength(); i++)
			{
				String token = sent.getToken(i);
				String posTag = sent.getPosTag(i);
				String chunkTag = sent.getChunkTag(i);
				System.out.println(token + " " + posTag + " " + chunkTag);
			}*/

			// Prints out extractions from the sentence.
			ReVerbExtractor reverb = new ReVerbExtractor();
			ConfidenceFunction confFunc = new ReVerbConfFunction();
			
			JSONObject resultObj = new JSONObject();
			JSONArray relationsArray = new JSONArray();
			
			for (ChunkedBinaryExtraction extr : reverb.extract(sent))
			{
				double conf = confFunc.getConf(extr);
				System.out.println("Arg1=" + extr.getArgument1());
				System.out.println("Rel=" + extr.getRelation());
				System.out.println("Arg2=" + extr.getArgument2());
				System.out.println("Conf=" + conf);
				
				if(conf < 0.9)
				{
					System.out.println("REJECTED DUE TO LOW CONFIDENCE!");
					continue;
				}
				
				JSONObject containerObj = new JSONObject();
				
				containerObj.put("argument1", extr.getArgument1().toString());
				containerObj.put("argument2", extr.getArgument2().toString());
				containerObj.put("relation", extr.getRelation().toString());
				containerObj.put("confidence", conf);
				
				relationsArray.put(containerObj);
			}
			
			resultObj.put("relations", relationsArray);
			Results.add(resultObj.toString());
		}
		
		return Results;
	}
	
	/**
	 * Get list of Objects from Google first and then processed by ReVerb.
	 * @param People - ArrayList<String> of People on whom to search Google
	 * @param Relationships - ArrayList<String> of Relationships on which to search Google
	 * @return - ArrayList<String> of JSON compatible Strings that contain person, relationship and object data.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static ArrayList<String> getObjectsFromPeopleAndRelationships(ArrayList<String> People, ArrayList<String> Relationships) throws ClientProtocolException, IOException, JSONException
	{
		ArrayList<String> Results = CustomSearchStart.extractRelationshipData(People, Relationships);
		ArrayList<String> classifiedResults = new ArrayList<String>();
		
		int i = 0;
		for(String person : People)
		{
			for(String relationship : Relationships)
			{
				ArrayList<String> tempResults = new ArrayList<String>();
				String tempResultString = "";
				String googleResults = Results.get(i++);
				ArrayList<String> Objects = getObjects(googleResults);
				for(String object : Objects)
				{
					String source = person + " " + relationship + " " + object + ".";
					System.out.println(source);
					
					tempResults.add(source);
				}
				
				tempResultString = mergeJSONResults(classifyBinaryRelations(tempResults));
				classifiedResults.add(tempResultString);
			}
		}
		
		return classifiedResults;
	}
	
	/**
	 * Get list of Objects from Google ONLY.
	 * @param People - ArrayList<String> of People on whom to search Google + ReVerb
	 * @param Relationships - ArrayList<String> of Relationships on which to search Google + ReVerb
	 * @return - ArrayList<String> of JSON compatible Strings that contain person, relationship and object data.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static ArrayList<String> getObjectsFromPeopleAndRelationshipsPureGoogle(ArrayList<String> People, ArrayList<String> Relationships) throws ClientProtocolException, IOException, JSONException
	{
		return CustomSearchStart.extractRelationshipData(People, Relationships);
	}
	
	/**
	 * Merges and Converts from ArrayList<String> of JSON to String of JSON that is same as expected Google Custom Search JSON Format
	 * @param results - String of JSON which is merged and converted version of input 
	 * @return
	 * @throws JSONException
	 */
	public static String mergeJSONResults(ArrayList<String> results) throws JSONException
	{
		JSONArray resultsArray = new JSONArray();
		
		for(String result : results)
		{
			JSONObject tempresultObj = new JSONObject(result);
			
			JSONArray resultArray = tempresultObj.optJSONArray("relations");
			
			if(resultArray == null)
			{
				continue;
			}
			
			for(int index = 0; index < resultArray.length(); index++)
			{
				JSONObject resultObject = resultArray.optJSONObject(index);
				
				if(resultObject == null)
				{
					continue;
				}
				
				String person = resultObject.getString("argument1");
				String relationship = resultObject.getString("relation");
				String object = resultObject.getString("argument2");
				
				JSONObject currentResultObj = new JSONObject();
            	currentResultObj.put("person", person.trim());
            	currentResultObj.put("relationship", relationship.trim());
            	currentResultObj.put("object", object);
            	
            	if(resultsArray.toString().indexOf(currentResultObj.toString()) > -1)
            	{
                	continue;
            	}
            	
            	resultsArray.put(currentResultObj);
			}
		}
		
		
		return resultsArray.toString();
	}
}
