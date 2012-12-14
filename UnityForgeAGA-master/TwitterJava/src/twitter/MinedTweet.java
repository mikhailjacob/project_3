/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

/**
 *
 * @author Afshin
 */
public class MinedTweet {
    String status;
    String handle;
    String polarity;
    long RTN;
    int followers;
    float polProb;
    boolean isVerified;
    boolean isRetweet;

    public long getRTN() {
        return RTN;
    }

    public void setRTN(long RTN) {
        this.RTN = RTN;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public boolean isIsRetweet() {
        return isRetweet;
    }

    public void setIsRetweet(boolean isRetweet) {
        this.isRetweet = isRetweet;
    }

    public boolean isIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public float getPolProb() {
        return polProb;
    }

    public void setPolProb(float polProb) {
        this.polProb = polProb;
    }

    public String getPolarity() {
        return polarity;
    }

    public void setPolarity(String polarity) {
        this.polarity = polarity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    

public MinedTweet(){

}


}


