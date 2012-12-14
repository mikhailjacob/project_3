/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Afshin
 */
public class MainTweet {

    public static void main(String[] args) throws Exception {

        ArrayList<String> trends = getTrends(); 
        runTweetsByString(trends.get(new Random().nextInt(trends.size())));

    }

    public static ArrayList<String> getTrends() throws Exception {
        TwitterMiner tm = new TwitterMiner(); 
        ArrayList<String> theTrends = new ArrayList<String>();
        theTrends = tm.getTrends();
        theTrends = tm.sanitizeTopics(theTrends);
        return theTrends;
    }

    public static void runTweetsByString(String topic) throws Exception{
        FameMachine fm = new FameMachine(); 
        TwitterMiner tm = new TwitterMiner(); 
        ArrayList<MinedTweet> tweets = new ArrayList<MinedTweet>();
        tweets = tm.getTweetsByTopicPageN(topic, 1);
        ArrayList<String> t1 = new ArrayList<String>();
        for (MinedTweet mt : tweets) {
            System.out.println("ST=" + mt.status + " USER=" + mt.handle + " RTN=" + mt.RTN + " FOLL=" + mt.followers + " isRT=" + mt.isRetweet + " isVerified=" + mt.isVerified);
            t1.add(mt.status.replaceAll("[^a-zA-Z\\s0-9\\'\\?\\.\\,\\'\\!]+", ""));
        }

        ArrayList<String> a = SentimentAnalysisStart.classifySkyttle(t1);
        tm.setPolarities(tweets, a);
        ArrayList<ArrayList<MinedTweet>> mtc = fm.classifybyPolarity(tweets);
        ArrayList<MinedTweet> tops = fm.analyzeFame(mtc);
        System.out.println("TOP POSITIVE,NEGATIVE, AND ");
        for (MinedTweet mt : tops) {
            System.out.println("ST=" + mt.status + " USER=" + mt.handle + " RTN=" + mt.RTN + " FOLL=" + mt.followers + " isRT=" + mt.isRetweet + " isVerified=" + mt.isVerified);

        }
        System.out.println("SIDEKICK INFO");
        CharacterData sidekick = tm.getCharacterInfo(tops.get(0));
        tm.setLikesDislikesChars(sidekick);
        System.out.println("VILLAIN INFO");
        CharacterData villain = tm.getCharacterInfo(tops.get(1));
        System.out.println("XML!");
        XMLBuilder xm = new XMLBuilder();
        xm.buildXML(sidekick, villain);
        
    }
}
