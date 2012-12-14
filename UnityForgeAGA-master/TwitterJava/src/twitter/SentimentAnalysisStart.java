package twitter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * This class can be accessed as a black-box library of tweet classification methods.
 * Just pass in an ArrayList<String> of tweets or sentences into one of the methods
 * named classify<XYZ>, where <XYZ> is the API Name. Eg. classifySkyttle.
 * 
 * List of API that are usable through this class:
 * 
 * 1. Sentiment140 API - 10000 at a time, unlimited usage - Not very accurate generally
 * 2. Skyttle API - 1 at a time, unlimited usage - Slower, but generally accurate - API of choice!!! :-)
 * 3. Alchemy API - 1 at a time, 30,000 calls / day
 * 4. ViralHeat API - 1 at a time, 5000 calls / day
 * 5. Repustate API - 500 at a time, 25,000 calls / month
 * @author mjacob6
 *
 */
public class SentimentAnalysisStart
{
	ArrayList<String> TweetList;
	ArrayList<String> ClassifiedResponseList;
	
	String urlString;
	
	/**
	 * Default Constructor
	 */
	public SentimentAnalysisStart() {
		ClassifiedResponseList = new ArrayList<String>();
		TweetList = new ArrayList<String>();
		urlString = "";
	}
	
	/**
	 * Constructor sets urlString to passed in String.
	 * @param urlString - Passed in String
	 */
	public SentimentAnalysisStart(String urlString) {
		ClassifiedResponseList = new ArrayList<String>();
		TweetList = new ArrayList<String>();
		this.urlString = urlString;
	}
	
	/**
	 * Constructor sets urlString to passed in String.
	 * @param urlString - Passed in String
	 */
	public SentimentAnalysisStart(ArrayList<String> TweetList, String urlString) {
		ClassifiedResponseList = new ArrayList<String>();
		this.TweetList = new ArrayList<String>(TweetList);
		this.urlString = urlString;
	}
	
	/**
	 * Main class to test Sentiment Analysis methods
	 * @param args
	 * @throws IOException
	 * @throws JSONException
	 */
	public static void main(String[] args) throws IOException, JSONException
	{
		ArrayList<String> TweetList = new ArrayList<String>();
		System.out.println("Enter tweets to analyse for sentiment polarity (Enter 'Done' when complete):\n");
		BufferedReader Reader = new BufferedReader(new InputStreamReader(System.in));
		String InputLine;
		while(!(InputLine = Reader.readLine()).equalsIgnoreCase("Done"))
		{
			TweetList.add(InputLine);
		}
		
		if(TweetList.isEmpty())
		{
			System.out.println("ERROR! No tweet entered to analyse!");
			return;
		}
		
		ArrayList<String> Results;
		
		//Classify List of Tweets
		
		//Sentiment140 API
		//Results = classifySentiment140(TweetList);
		
		//Skyttle API
		//Results = classifySkyttle(TweetList);
		
		Results = retainNamedPeopleTopics(TweetList);
		
		System.out.println("\n");
		
		for(String result: Results)
		{
			System.out.println(result);
		}
	}
	
	/**
	 * Returns the text part of the Classified Tweet Result
	 * @param input - String result of classification via one of the 
	 * sentiment analysis API
	 * @return - String of text which is the original tweet used for 
	 * classification 
	 * @throws JSONException 
	 */
	public static String getText(String input) throws JSONException
	{
		return (new JSONObject(input)).optString("text");
	}
	
	/**
	 * Returns the polarity part of the Classified Tweet Result
	 * @param input - String result of classification via one of the 
	 * sentiment analysis API
	 * @return - String of text which is the classified polarity of the 
	 * tweet. Will be one of Negative, Neutral or Positive
	 * @throws JSONException 
	 */
	public static String getPolarity(String input) throws JSONException
	{
		return (new JSONObject(input)).optString("polarity");
	}
	
	/**
	 * Returns the relevance part of the Classified Tweet Result
	 * @param input - String result of classification via one of the 
	 * sentiment analysis API
	 * @return - String of text which is the classified relevance of the 
	 * tweet.
	 * @throws JSONException 
	 */
	public static String getRelevance(String input) throws JSONException
	{
		return (new JSONObject(input)).optString("relevance");
	}
	
	/**
	 * Returns the Type of Named Entity part of the Classified Tweet Result
	 * @param input - String result of classification via one of the 
	 * sentiment analysis API
	 * @return - String of text which is the classified Type of Named Entity of the 
	 * tweet. Will be a double.
	 * @throws JSONException 
	 */
	public static ArrayList<String> getNERType(String input) throws JSONException
	{
		JSONObject ResponseJSON = new JSONObject(input);
		ArrayList<String> types = new ArrayList<String>();
		
		if(ResponseJSON.optJSONArray("entities") != null && ResponseJSON.optJSONArray("entities").optJSONObject(0) != null)
		{
			JSONArray nerArray = ResponseJSON.getJSONArray("entities");
			for(int i = 0; i < nerArray.length(); i++)
			{
				String type = nerArray.getJSONObject(i).optString("entity_type", "");
				String concType = nerArray.getJSONObject(i).optString("entity_type", "");
				
				if(!type.equalsIgnoreCase(concType))
				{
					types.add(type + "_" + concType);
				}
				else
				{
					types.add(type);
				}
			}
		}
		
		return types;
	}
	
	/**
	 * Returns the Name of the Named Entity part of the Classified Tweet Result
	 * @param input - String result of classification via one of the 
	 * sentiment analysis API
	 * @return - String of text which is the classified Name of the Named Entity of the 
	 * tweet. Will be a double.
	 * @throws JSONException 
	 */
	public static ArrayList<String> getNEREntity(String input) throws JSONException
	{
		JSONObject ResponseJSON = new JSONObject(input);
		ArrayList<String> types = new ArrayList<String>();
		
		if(ResponseJSON.optJSONArray("entities") != null && ResponseJSON.optJSONArray("entities").optJSONObject(0) != null)
		{
			JSONArray nerArray = ResponseJSON.getJSONArray("entities");
			for(int i = 0; i < nerArray.length(); i++)
			{
				types.add(nerArray.getJSONObject(i).optString("canonical", ""));
			}
		}
		
		return types;
	}
	
	/**
	 * Uses Sentiment140 API to classify tweets as Positive, Neutral or Negative.
	 * Takes in an ArrayList<String> of Tweets and returns an ArrayList<String> 
	 * of JSON-convertible, text and polarity of the input list of tweets.    
	 * @param Tweets - ArrayList<String> of Tweets
	 * @return - ArrayList<String> of JSON-convertible, text and polarity of the input
	 * list of tweets
	 * @throws JSONException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static ArrayList<String> classifySentiment140(ArrayList<String> Tweets) throws JSONException, ClientProtocolException, IOException
	{
		SentimentAnalysisStart Runner = new SentimentAnalysisStart(Tweets, "http://www.sentiment140.com/api/bulkClassifyJson?appid=mjacob6@gatech.edu");
		JSONObject jsonObj = Runner.JSONifySentiment140(Runner.TweetList);
		Runner.httpPost(Runner.urlString, jsonObj.toString(), true, false);
		return Runner.Sentiment140StringToText();
	}
	
	/**
	 * Converts tweets into JSON compatible with Sentiment140 API
	 * @param ParamList - Input ArrayList<String> of Tweets to be converted
	 * @return - JSONObject of the compatible JSON code
	 * @throws JSONException
	 */
 	public JSONObject JSONifySentiment140(ArrayList<String> ParamList) throws JSONException
	{
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("data", new JSONArray());
		JSONArray dataArray = jsonObj.getJSONArray("data");
		
		for(int i = 0; i < ParamList.size(); i++)
		{
			String Tweet = ParamList.get(i);
			
			JSONObject tempJSONObj = new JSONObject();
			tempJSONObj.put("text", Tweet);
			tempJSONObj.put("id", i+1);
			
			dataArray.put(tempJSONObj);
		}
		
		return jsonObj; 
	}
	
 	/**
 	 * Does an HTTP POST request to the requisite API URL String and calls 
 	 * handleResponse once a response is received.
 	 * 
 	 * NOTE: At the moment this is specific to Sentiment140, but may be reused 
 	 * if the same format is usable for other API as well.
 	 * 
 	 * @param urlString - String with the URL of the API to be called.
 	 * @param paramValue - String containing any parameters to pass to the API.
 	 * @param useJSON - Toggle usage of JSON for body of request.
 	 * @param useMashape TODO
 	 * @throws ClientProtocolException
 	 * @throws IOException
 	 */
	public void httpPost(String urlString, String paramValue, Boolean useJSON, Boolean useMashape) throws ClientProtocolException, IOException
	{
		if(paramValue.equalsIgnoreCase(null))
		{
			System.out.println("Error: Parameter returned null");
			return;
		}
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(urlString);
        
		if(useJSON)
		{
			httpPost.setEntity(new StringEntity(paramValue, org.apache.http.entity.ContentType.APPLICATION_JSON));
		}
		
		if(useMashape)
		{

			httpPost.addHeader("X-Mashape-Authorization", "Y3RtcmRuaGJiemgzdnpvbmVzd2xsMXB5dnhydWFxOmZhYmU4Y2VmNzkyNzRiNTQyNWYyZDg5M2RiYzUyOTU2MTg1YjM2YTM=");
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("text", paramValue));
			postParameters.add(new BasicNameValuePair("lang", "en"));
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
			httpPost.setEntity(formEntity);
		}
        
        HttpResponse response2 = httpclient.execute(httpPost);

        try {
            //System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            
            handleResponse(entity2);
            
            EntityUtils.consume(entity2);
        } finally {
            httpPost.releaseConnection();
        }
	}
	
	/**
	 * Adds the received response to the ClassifiedResponse ArrayList<String>
	 * @param ResponseEntity - The HTTPEntity that contains the response from 
	 * the API.
	 * @throws IOException
	 */
	public void handleResponse(HttpEntity ResponseEntity) throws IOException
	{
		InputStream ResponseContentStream = ResponseEntity.getContent();
		BufferedReader Reader = new BufferedReader(new InputStreamReader(ResponseContentStream));
		
		int startTweet = ClassifiedResponseList.size();
		try
		{
			String InputLine = "";
			while((InputLine = Reader.readLine()) != null)
			{
				ClassifiedResponseList.add(InputLine);
			}
			
			int endTweet = ClassifiedResponseList.size();

			//System.out.println("Received:");
			
			for(int index = startTweet; index < endTweet; index++)
			{
				String Response = ClassifiedResponseList.get(index);
				//System.out.println(Response);
			}
		}
		finally
		{
			ResponseContentStream.close();
		}
	}
	
	/**
	 * Formats and converts the classified tweet into human readable text 
	 * and returns it as an ArrayList<String>.
	 * @return - Formatted and translated ArrayList<String> of classified tweets.
	 * @throws JSONException
	 */
	public ArrayList<String> Sentiment140StringToText() throws JSONException
	{
		ArrayList<String> Results = new ArrayList<String>();
		
		for(String Response: ClassifiedResponseList)
		{
			JSONObject ResponseJSON = new JSONObject(Response);
			
			JSONArray dataArray = ResponseJSON.getJSONArray("data");
			
			for(int index = 0; index < dataArray.length(); index++)
			{
				JSONObject AnalysedTweetObject = dataArray.getJSONObject(index);
				
				String Text = AnalysedTweetObject.getString("text");
				int Polarity = AnalysedTweetObject.getInt("polarity");
				String PolarityString = "";
				
				switch(Polarity)
				{
				case 0:
				{
					PolarityString = "Negative";
					break;
				}
				case 2:
				{
					PolarityString = "Neutral";
					break;
				}
				case 4:
				{
					PolarityString = "Positive";
					break;
				}
				default:
					PolarityString = "Undefined";
				}
				
				Results.add("{Text: '" + Text + "', Sentiment: '" + PolarityString + "'}");
			}
		}
		
		return Results;
	}
	
	/**
	 * Uses Skyttle API to classify tweets as Positive, Neutral or Negative.
	 * Takes in an ArrayList<String> of Tweets and returns an ArrayList<String> 
	 * of JSON-convertible, text and polarity of the input list of tweets.    
	 * @param Tweets - ArrayList<String> of Tweets
	 * @return - ArrayList<String> of JSON-convertible, text and polarity of the input
	 * list of tweets
	 * @throws JSONException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static ArrayList<String> classifySkyttle(ArrayList<String> Tweets) throws JSONException, ClientProtocolException, IOException
	{
		SentimentAnalysisStart Runner = new SentimentAnalysisStart(Tweets, "https://marketsentinel-skyttle-api.p.mashape.com/api/nlp/te/");
		
		for(String tweet : Tweets)
		{
			Runner.httpPost(Runner.urlString, tweet, false, true);
		}
		
		return Runner.SkyttleStringToText();
	}
	
	/**
	 * Removes apostrophes ( ' ) from list of tweets
	 */
	/*public void SanitiseTweetList()
	{
		ArrayList<String> CopyList = new ArrayList<String>(TweetList);
		for(int i = 0; i < CopyList.size(); i++)
		{
			TweetList.set(i, CopyList.get(i).replace("'", ""));
		}
		
		TweetList = CopyList;
	}*/
	
	/**
	 * Formats and converts the classified tweet into human readable text 
	 * and returns it as an ArrayList<String>.
	 * @return - Formatted and translated ArrayList<String> of classified tweets.
	 * @throws JSONException
	 */
	public ArrayList<String> SkyttleStringToText() throws JSONException
	{
		ArrayList<String> Results = new ArrayList<String>();
		
		for(int i = 0; i < ClassifiedResponseList.size(); i++)
		{
			String Response = ClassifiedResponseList.get(i);
			String Text = TweetList.get(i);
			
			JSONObject ResponseJSON = new JSONObject(Response);
			JSONObject resultObj = new JSONObject();
			
			/*
			 * "sentiment":
			 * [
        			{
            			"canonical": "too feeble",
            			"count": 5,
            			"intensity_mod_freq": "",
            			"normalised": "feeble",
            			"polarity": "+",
            			"relevance": 0.076630935
        			}
    			]
			 * */
			
			if(ResponseJSON.optJSONArray("sentiment") == null || ResponseJSON.optJSONArray("sentiment").optJSONObject(0) == null)
			{
				//System.out.println("ERROR!!! No Sentiment Data Found!!! :(");
				
				resultObj.put("text", Text);
				resultObj.put("relevance", "null");
				resultObj.put("polarity", "null");
			}
			else
			{

				JSONArray sentimentJSONArray = ResponseJSON.getJSONArray("sentiment");

				JSONObject sentimentData = sentimentJSONArray.getJSONObject(0);

				String Polarity = sentimentData.getString("polarity");

				if(Polarity.equalsIgnoreCase("+"))
				{
					Polarity = "Positive";
				}
				else if(Polarity.equalsIgnoreCase("-"))
				{
					Polarity = "Negative";
				}
				else
				{
					Polarity = "Neutral";
				}

				resultObj.put("text", Text);
				resultObj.put("relevance", sentimentData.optString("relevance", "NAN"));
				resultObj.put("polarity", Polarity);
			}
			
			if(ResponseJSON.optJSONArray("entities") == null || ResponseJSON.optJSONArray("entities").optJSONObject(0) == null)
			{
				//System.out.println("ERROR!!! No NER Data Found!!! :(");
				
				resultObj.put("entities", new JSONArray());
			}
			else
			{
				JSONArray nerJSONArray = ResponseJSON.getJSONArray("entities");
				resultObj.put("entities", nerJSONArray);
			}
			
			Results.add(resultObj.toString());
		}
		
		return Results;
	}
	
	/**
	 * Takes in a list of topics and returns the ones that are names of people.
	 * @param Tweets
	 * @return ArrayList<String> of topics that are names of people
	 * @throws ClientProtocolException
	 * @throws JSONException
	 * @throws IOException
	 */
	public static ArrayList<String> retainNamedPeopleTopics(ArrayList<String> Tweets) throws ClientProtocolException, JSONException, IOException
	{
		ArrayList<String> tempResults = new ArrayList<String>(Tweets);
		ArrayList<String> results = new ArrayList<String>();
		
		tempResults = classifySkyttle(tempResults);
		
		for(int i = 0; i < tempResults.size(); i++)
		{
			String result = tempResults.get(i);
			ArrayList<String> nerTypes = getNERType(result);
			if(nerTypes.size() == 1)
			{
				ArrayList<String> nerEntities = getNEREntity(result);
				
				if(nerTypes.get(0).equalsIgnoreCase("person") && nerEntities.get(0).equalsIgnoreCase(getText(result)))
				{
					results.add(result);
				}
			}
		}
		
		return results;
	}
}