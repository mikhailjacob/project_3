/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

import java.util.ArrayList;

/**
 *
 * @author Afshin
 */
public class FameMachine {

    public ArrayList<ArrayList<MinedTweet>> classifybyPolarity(ArrayList<MinedTweet> tweets) {

        ArrayList<MinedTweet> pos = new ArrayList<MinedTweet>();
        ArrayList<MinedTweet> neg = new ArrayList<MinedTweet>();
        ArrayList<MinedTweet> neu = new ArrayList<MinedTweet>();

        ArrayList<ArrayList<MinedTweet>> classified = new ArrayList<ArrayList<MinedTweet>>();
        for (MinedTweet mt : tweets) {
            if (mt.polarity.equals("Positive")) {
                pos.add(mt);
            } else if (mt.polarity.equals("Negative")) {
                neg.add(mt);
            } else {
                neu.add(mt);
            }
        }
        classified.add(pos);
        classified.add(neg);
        classified.add(neu);
        return classified;
    }

    public ArrayList<MinedTweet> analyzeFame(ArrayList<ArrayList<MinedTweet>> classified) {
        ArrayList<MinedTweet> tops = new ArrayList<MinedTweet>();
        int i1 = 0, i2 = 0, i3 = 0, ci = 0;
        float c3 = calculateCoefficient(classified.get(2).get(0));
        if (!classified.get(0).isEmpty()) {
            float c1 = calculateCoefficient(classified.get(0).get(0));
            for (int i = 0; i < classified.get(0).size(); i++) {
                float ct = calculateCoefficient(classified.get(0).get(i));
                if (ct > c1) {
                    c1 = ct;
                    i1 = i;

                }
            }
            ci++;
            tops.add(classified.get(0).get(i1));
        }
        if (!classified.get(1).isEmpty()) {
            float c2 = calculateCoefficient(classified.get(1).get(0));
            for (int j = 0; j < classified.get(1).size(); j++) {
                float ct = calculateCoefficient(classified.get(1).get(j));
                if (ct > c2) {
                    c2 = ct;
                    i2 = j;
                }
            }
            ci++;
            tops.add(classified.get(1).get(i2));
        }
        if (ci < 2 ) {
            for (int i = 0; i < 3-ci; i++) {

                for (int k = 0; k < classified.get(2).size(); k++) {
                    float ct = calculateCoefficient(classified.get(2).get(k));
                    if (ct > c3) {
                        c3 = ct;
                        i3 = k;
                    }
                }

                tops.add(classified.get(2).get(i3));
                classified.get(2).remove(i3);
                i3 = 0;
                c3 = calculateCoefficient(classified.get(2).get(0));
            }


        } else {
            for (int k = 0; k < classified.get(2).size(); k++) {
                float ct = calculateCoefficient(classified.get(2).get(k));
                if (ct > c3) {
                    c3 = ct;
                    i3 = k;
                }
            }

            tops.add(classified.get(2).get(i3));
        }
        return tops;
    }

    public float calculateCoefficient(MinedTweet mt) {
        float coef = 0;
        coef = ((float) mt.RTN+ (float) mt.followers) * mt.polProb;
        if (mt.isVerified) {
            coef = 1.5F * coef;
        }
        return coef;
    }
    

   
}
