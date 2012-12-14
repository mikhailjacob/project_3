/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import twitter4j.*;

/**
 *
 * @author Afshin
 */
public class TwitterMiner {

    Twitter t;

    public TwitterMiner() {
        this.t = new TwitterFactory().getInstance();
    }

    public ArrayList<String> getTrends() throws TwitterException {
        ArrayList<String> theTrends = new ArrayList<String>();
        try {
            int woeid = 23424977;
            Trends trends = t.getLocationTrends(woeid);
            System.out.println("DATE=" + trends.getAsOf());
            System.out.println("Showing trends");
            for (int i = 0; i < trends.getTrends().length; i++) {
                //System.out.println("TREND=" + trends.getTrends()[i].getQuery());
                if (!theTrends.contains(trends.getTrends()[i].getQuery())) {
                    theTrends.add(trends.getTrends()[i].getQuery());
                }

            }
            System.out.println("done.");
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get trends: " + te.getMessage());
            System.exit(-1);
        }
        return theTrends;
    }

    public ArrayList<MinedTweet> getTweetsByTopicPageN(String query, int nPages) {
        ArrayList<ArrayList<MinedTweet>> queryables = new ArrayList<ArrayList<MinedTweet>>();
        for (int i = 0; i < nPages; i++) {
            ArrayList<MinedTweet> tmp = getTweetsByTopic(query, i);
            queryables.add(tmp);
        }
        ArrayList<MinedTweet> tweets = new ArrayList<MinedTweet>();
        for (int i = 0; i < queryables.size(); i++) {
            for (int j = 0; j < queryables.get(i).size(); j++) {
                tweets.add(queryables.get(i).get(j));
            }
        }
        return tweets;
    }

    public ArrayList<MinedTweet> getTweetsByTopic(String queryString, int nPage) {
        ArrayList<MinedTweet> theTweets = new ArrayList<MinedTweet>();
        ArrayList<String> stata = new ArrayList<String>();
        ArrayList<String> users = new ArrayList<String>();
        ArrayList<Long> ids = new ArrayList<Long>();

        try {
            Query query = new Query(queryString);
            query.setRpp(100);
            query.setPage(nPage + 1);
            QueryResult result;
            result = t.search(query);
            List<Tweet> tweets = result.getTweets();
            System.out.println("THERE ARE " + tweets.size() + " TWEETS");
            for (Tweet tweet : tweets) {
                // System.out.println("@" + tweet.getFromUser() + " - " + tweet.getText());
                stata.add(tweet.getText());
                users.add(tweet.getFromUser());
                ids.add(tweet.getId());
            }
            String[] s2 = users.toArray(new String[users.size()]);
            List<User> users2 = t.lookupUsers(s2);
            for (int i = 0; i < users2.size(); i++) {
                //System.out.println("ID="+ids.get(i));
                //Status s = t.showStatus(ids.get(i));
                MinedTweet mt = new MinedTweet();
                mt.setStatus(stata.get(i));
                mt.setHandle(users.get(i));
                mt.setFollowers(users2.get(i).getFollowersCount());
                mt.setRTN(users2.get(i).getStatus().getRetweetCount());
                mt.setIsRetweet(false);
                mt.setIsVerified(users2.get(i).isVerified());
                theTweets.add(mt);
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
        return theTweets;
    }

    public ArrayList<String> extractTextandSanitize(ArrayList<MinedTweet> mt) {
        ArrayList<String> text = new ArrayList<String>();
        for (MinedTweet mt1 : mt) {
            text.add(mt1.status);
        }
        return text;
    }

    public void setPolarities(ArrayList<MinedTweet> mt, ArrayList<String> pol) throws Exception {
        for (int i = 0; i < mt.size(); i++) {
            String polar = SentimentAnalysisStart.getPolarity(pol.get(i));
            String relev = SentimentAnalysisStart.getRelevance(pol.get(i));
            //System.out.println("POL=" + polar + " REL=" + relev);
            if (!polar.equals("null")) {
                mt.get(i).setPolarity(polar);
            } else {
                mt.get(i).setPolarity("Neutral");
            }

            float f = 0F;
            if (!relev.equals("null")) {
                f = Float.parseFloat(relev);
            } else {
                f = -1F;
            }
            mt.get(i).setPolProb(f);


        }
    }

    public ArrayList<String> sanitizeTopics(ArrayList<String> topics) {
        ArrayList<String> san = new ArrayList<String>();
        for (int i = 0; i < topics.size(); i++) {
            String san2 = topics.get(i);
            san2 = san2.replaceAll("%20", " ");
            san2 = san2.replaceAll("%23", "");
            san2 = san2.replaceAll("%22", "");
            san.add(san2);
        }
        return san;
    }

    public CharacterData getCharacterInfo(MinedTweet mt) throws Exception {
        CharacterData cd = new CharacterData();
        ArrayList<List<Status>> tweets = new ArrayList<List<Status>>();
        try {
            for (int i = 0; i < 1; i++) {
                Paging p = new Paging(i + 1, 50);
                List<Status> timeline = t.getUserTimeline(mt.getHandle(), p);
                tweets.add(timeline);
            }
            ArrayList<Status> stata = new ArrayList<Status>();
            for (int j = 0; j < tweets.size(); j++) {
                for (int k = 0; k < tweets.get(j).size(); k++) {
                    stata.add(tweets.get(j).get(k));
                }
            }
            ArrayList<String> text = new ArrayList<String>();
            for (Status s : stata) {
                text.add(s.getText().replaceAll("[^a-zA-Z\\s0-9\\'\\?\\.\\,\\'\\!]+", ""));
            }

            ArrayList<String> friendh = getFriendsFromData(text, stata);
            String[] friends = friendh.toArray(new String[friendh.size()]);
            List<User> us = t.lookupUsers(friends);
            ArrayList<String> names = new ArrayList<String>();
            ArrayList<String> urlsI = new ArrayList<String>();
            for (int l = 0; l < us.size(); l++) {
                BufferedImage image = null;
                names.add(us.get(l).getName());
                URL img = us.get(l).getProfileImageURL();

                System.out.println("Friend Name = " + names.get(l));
                System.out.println("URL for Profile Picture = " + img.toString());
                image = ImageIO.read(img);
                String path = "../Assets/ImgTex/" + mt.handle + "sidekick" + l + ".jpg";
                String p2 =  "Assets/ImgTex/" + mt.handle + "sidekick" + l + ".jpg";
                urlsI.add(p2);
                File f = new File(path);
                ImageIO.write(image, "jpg", f);

            }
            if (names.size() != 3) {
                int delta = 3 - names.size();
                if (delta < 0) {
                    for (int i = 2; i < 2+Math.abs(delta); i++) {
                    names.remove(i);
                    friendh.remove(i);
                    urlsI.remove(i);
                    
                    }
                }
                else{
                   String [] firstn= {"Juan","Ahmed","Jane","Inderjeet","Takeshi","Michelle"};
                   String [] lastn= {"Jones","Yamaguchi","Schneider","Al-Otaibi","Gupta","Garcia"};
                   Random r = new Random(); 
                   for(int i = 0; i < delta; i++){
                   names.add(firstn[r.nextInt(firstn.length)]+" "+lastn[r.nextInt(lastn.length)]);
                   friendh.add("default"+i);
                   urlsI.add("Assets/ImgTex/Default.jpg");
                   }
              
                 }
            }
            cd.setSidekicknames(names);
            cd.setSidekicks(friendh);
            cd.setHandle(mt.handle);
            String[] u = new String[1];
            u[0] = mt.handle;
            List<User> u3 = t.lookupUsers(u);
            URL im = u3.get(0).getProfileImageURL();
            cd.setImgURLS(urlsI);
            BufferedImage image = null;
            image = ImageIO.read(im);
            String path = "../Assets/ImgTex/" + mt.handle + ".jpg";
            String p2 = "Assets/ImgTex/" + mt.handle + ".jpg"; 
            ImageIO.write(image, "jpg", new File(path));
            cd.setSidekickImgUrl(p2);
            cd.setName(u3.get(0).getName());
            cd.setSidekickDialogue(mt.status);


        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }

        return cd;
    }

    public void setLikesDislikesChars(CharacterData cd) throws Exception {

        ArrayList<String> p = cd.getSidekicknames();
        ArrayList<String> rp = new ArrayList<String>();

        rp.add("loves");
        rp.add("likes");
        rp.add("appreciates");
        rp.add("cares+for");
        rp.add("cares+about");
        rp.add("enjoys");

        ArrayList<String> resultsp = ReVerbStart.getObjectsFromPeopleAndRelationships(p, rp);
        ArrayList<ArrayList<String>> processed = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < p.size(); i++) {
            ArrayList<String> pp = new ArrayList<String>();
            for (int j = 0; j < rp.size(); j++) {
                int index = (i * rp.size()) + j;
                if (!resultsp.get(index).equals("[]")) {
                    ArrayList<String> st = ReVerbStart.getObjects(resultsp.get(index));
                    for (String s1 : st) {
                        pp.add(s1);
                    }
                } else {
                  //  System.out.println("EMPTY!");
                }
            }
            if (!pp.isEmpty()) {
                processed.add(pp);
            } //Nothing here. Go to default case
            else {
                Random r = new Random();
//                ArrayList<String> a = new ArrayList<String>();
//                a.add("everyone"); a.add("everybody"); a.add("society");
//                ArrayList<String> b = new ArrayList<String>();
//                b.add("loves");b.add("likes");b.add("appreciates");b.add("enjoys");
                //ArrayList<String> c = ReVerbStart.getObjectsFromPeopleAndRelationships(a, b);
                ArrayList<String> c = FileCacher.getCachedResults();
                int i1 = r.nextInt(c.size());
                //ArrayList<String> c1 = ReVerbStart.getObjects(c.get(i1));
                String a2 = c.get(i1);
                pp.add(a2);
                processed.add(pp);
            }
        }

        ArrayList<String> likes = new ArrayList<String>();
        Random r = new Random();
        for (int i = 0; i < processed.size(); i++) {
            String a = processed.get(i).get(r.nextInt(processed.get(i).size()));
            System.out.println("LIKE FOR SIDEKICK" + (i + 1) + "= " + a);
            likes.add(a);
        }
        cd.setLikes(likes);
        System.out.println("LIKES=" + cd.getLikes().size());

    }

    public ArrayList<String> getFriendsFromData(ArrayList<String> text, ArrayList<Status> stata) throws Exception {

        ArrayList<String> dat = SentimentAnalysisStart.classifySkyttle(text);
        ArrayList<String> pol = new ArrayList<String>();
        ArrayList<String> people = new ArrayList<String>();
        ArrayList<Float> relat = new ArrayList<Float>();
        ArrayList<String> chars = new ArrayList<String>();

        //First Run, let's get some numbers and names to aggreggate
        for (int i = 0; i < stata.size(); i++) {
            String rel = SentimentAnalysisStart.getRelevance(dat.get(i));
            String pola = SentimentAnalysisStart.getPolarity(dat.get(i));
            float rele = 0F;
            if (!rel.equals("null")) {
                rele = Float.parseFloat(rel);
                if (pola.equals("Negative")) {
                    rele = -1F * rele;
                } else if (pola.equals("Neutral")) {
                    rele = 0.5F * rele;
                }
            }
            relat.add(rele);
            if (!people.contains(stata.get(i).getInReplyToScreenName())) {
                if (stata.get(i).getInReplyToScreenName() == null) {
                    //System.out.println("NULL!!!!! GO DIE!!!!!");
                } else {
                    people.add(stata.get(i).getInReplyToScreenName());
                    //System.out.println("PERSON=" + stata.get(i).getInReplyToScreenName());
                }
            }

        }
        if (people.isEmpty()) {

            IDs friendIDs = t.getFriendsIDs(stata.get(0).getUser().getId(), -1);
            long[] idArray = friendIDs.getIDs();
            long[] idTrunc = new long[100];

            if (idArray.length >= 100) {
                for (int i = 0; i < idTrunc.length; i++) {
                    idTrunc[i] = idArray[i];
                }
            } else {
                idTrunc = new long[idArray.length];
                for (int i = 0; i < idTrunc.length; i++) {
                    idTrunc[i] = idArray[i];
                }
            }
            List<Friendship> frien = t.lookupFriendships(idTrunc);
            Random r = new Random();
            for (int i = 0; i < 3; i++) {
                int ind = r.nextInt(idTrunc.length);
                chars.add(frien.get(ind).getScreenName());

            }
        } //Second Run, let's get people and aggregate stuff!
        else {
            ArrayList<Float> polars = new ArrayList<Float>();
            for (int i = 0; i < people.size(); i++) {
                float acum = 0;
                for (int j = 0; j < stata.size(); j++) {
                    //System.out.println("P="+people.get(i)+" S="+stata.get(j).getInReplyToScreenName());
                    if (stata.get(j).getInReplyToScreenName() == null) {
                        //System.out.println("DIE");
                    } else if (stata.get(j).getInReplyToScreenName().equals(people.get(i))) {

                        acum += relat.get(j);
                        stata.remove(j);
                        relat.remove(j);
                    } else {
                    }

                }
                polars.add(acum);
                System.out.println("ANALYSIS FOR FRIEND " + people.get(i) + " SCORE =" + polars.get(i));
            }

            if (people.size() < 3) {
                for (int i = 0; i < people.size(); i++) {
                    float max = 0;
                    int ind = 0;
                    for (int j = 0; j < people.size(); j++) {
                        if (polars.get(j) > max) {
                            max = polars.get(j);
                            ind = j;
                        }

                    }

                    String friend = people.get(ind);
                    System.out.println("FRIEND WITH RANK " + i + 1 + " = " + friend);
                    chars.add(friend);
                    people.remove(ind);
                    polars.remove(ind);
                }
                IDs friendIDs = t.getFriendsIDs(stata.get(0).getUser().getId(), -1);
                long[] idArray = friendIDs.getIDs();
                long[] idTrunc = new long[100];

                if (idArray.length >= 100) {
                    for (int i = 0; i < idTrunc.length; i++) {
                        idTrunc[i] = idArray[i];
                    }
                } else {
                    idTrunc = new long[idArray.length];
                    for (int i = 0; i < idTrunc.length; i++) {
                        idTrunc[i] = idArray[i];
                    }
                }
                List<Friendship> frien = t.lookupFriendships(idTrunc);
                Random r = new Random();
                for (int i = 0; i < 3 - people.size(); i++) {
                    int ind = r.nextInt(idTrunc.length);
                    chars.add(frien.get(ind).getScreenName());
                }

            } else {
                int ranks = 3;
                for (int i = 0; i < ranks; i++) {
                    float max = 0;
                    int ind = 0;
                    for (int j = 0; j < people.size(); j++) {
                        if (polars.get(j) > max) {
                            max = polars.get(j);
                            ind = j;
                        }

                    }

                    String friend = people.get(ind);
                    System.out.println("FRIEND WITH RANK " + i + 1 + " = " + friend);
                    chars.add(friend);
                    people.remove(ind);
                    polars.remove(ind);
                }
            }
        }
        return chars;
    }
}
