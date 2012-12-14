package twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import twitter4j.internal.org.json.JSONException;

/**
 * Reads and Writes a data cache to file
 * @author mjacob6
 *
 */
public class FileCacher {

	static final String filePath = "words.txt"; 
	static ArrayList<String> cachedResults;
	static boolean isCacheEmpty;
	
	static
	{
		cachedResults = new ArrayList<String>();
		isCacheEmpty = true;
		
		readCache();
	}
	
	/**
	 * Gets the cache from memory.
	 * @return
	 */
	public static ArrayList<String> getCachedResults() {
		return cachedResults;
	}
	
	/**
	 * Sets the cache in memory.
	 * @param cachedResults
	 */
	public static void setCachedResults(ArrayList<String> cachedResults) {
		FileCacher.cachedResults = new ArrayList<String>(cachedResults);
		FileCacher.isCacheEmpty = FileCacher.cachedResults.isEmpty();
		writeCache();
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static void main(String[] args) throws IOException, JSONException
	{
		
//		ArrayList<String> People = new ArrayList<String>();
//		ArrayList<String> Relationships = new ArrayList<String>();
//		System.out.println("Enter people for analysing Binary Relationships (Enter 'Done' when complete):\n");
//		BufferedReader Reader = new BufferedReader(new InputStreamReader(System.in));
//		String InputLine;
//		while(!(InputLine = Reader.readLine()).equalsIgnoreCase("Done"))
//		{
//			People.add(InputLine);
//		}
//		
//		if(People.isEmpty())
//		{
//			System.out.println("ERROR! No tweet entered to analyse!");
//			return;
//		}
//		
//		System.out.println("Enter relationship predicates to get corpus to extract Binary Relationships from (Enter 'Done' when complete):\n");
//		
//		while(!(InputLine = Reader.readLine()).equalsIgnoreCase("Done"))
//		{
//			Relationships.add(InputLine);
//		}
//		
//		if(Relationships.isEmpty())
//		{
//			System.out.println("ERROR! No relationships entered to use!");
//			return;
//		}
//		
//		//Testing only ReVerb
//		//ArrayList<String> Results = ReVerbStart.classifyBinaryRelations(TweetList);
//		
//		//Testing ReVerb + Google Custom Search
//		ArrayList<String> Results = ReVerbStart.getObjectsFromPeopleAndRelationships(People, Relationships);
//		
//		//Testing only Google Custom Search
//		//ArrayList<String> Results = ReVerbStart.getObjectsFromPeopleAndRelationshipsPureGoogle(People, Relationships);
		
		System.out.println("\n");
		
		//setCachedResults(Results);
		readCache();
	}
	
	/**
	 * Writes data from cache to file
	 * @param input
	 * @return
	 */
	public static boolean writeCache()
	{
		BufferedWriter bw = null;
		
		try
		{	
			File file = new File(filePath);
 
			// if file doesn't exists, then create it
			if (!file.exists())
			{
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			
			if(!isCacheEmpty)
			{
				for(String currentLine : cachedResults)
				{
					bw.write(currentLine);
				}
			}
			
			System.out.println("Done");
 
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				bw.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	/**
	 * Reads data from file to file
	 * @param input
	 * @return
	 */
	public static boolean readCache()
	{
		BufferedReader br = null;
		boolean readFile = false;
		
		try
		{
			File file = new File(filePath);
			 
			// if file doesn't exists, then create it
			if (!file.exists())
			{
				file.createNewFile();
			}
			
			br = new BufferedReader(new FileReader(filePath));
			String currentLine = "";
			boolean isFirstLine = true;
			while ((currentLine = br.readLine()) != null)
			{
				if(isFirstLine)
				{
					cachedResults.clear();
					isCacheEmpty = true;
				}
				
				readFile = true;
				System.out.println(currentLine);
				cachedResults.add(currentLine);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			isCacheEmpty = cachedResults.isEmpty();
					
			if(br != null)
			{
				try
				{
					br.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return readFile;
	}
        
        public static ArrayList<String> readCacheFile()
	{
            
		BufferedReader br = null;
		boolean readFile = false;
		ArrayList<String> lines = new ArrayList<String>();
		try
		{
			File file = new File(filePath);
			 
			// if file doesn't exists, then create it
			if (!file.exists())
			{
				file.createNewFile();
			}
			
			br = new BufferedReader(new FileReader(filePath));
			String currentLine = "";
			boolean isFirstLine = true;
			while ((currentLine = br.readLine()) != null)
			{
				if(isFirstLine)
				{
					cachedResults.clear();
					isCacheEmpty = true;
				}
				
				readFile = true;
				System.out.println(currentLine);
				cachedResults.add(currentLine);
                                lines.add(currentLine);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			isCacheEmpty = cachedResults.isEmpty();
					
			if(br != null)
			{
				try
				{
					br.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return lines;
	}
}
