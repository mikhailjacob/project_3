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
public class CharacterData {
    
    ArrayList<String> sidekicks;
    ArrayList<String> sidekicknames;
    ArrayList<String> imgURLV;
    ArrayList<String> imgURLS;
    String sidekickImgUrl;
    String villainImgUrl; 
    String villainDialogue;


    public String getSidekickDialogue() {
        return sidekickDialogue;
    }

    public void setSidekickDialogue(String sidekickDialogue) {
        this.sidekickDialogue = sidekickDialogue;
    }

    public ArrayList<String> getImgURLS() {
        return imgURLS;
    }

    public void setImgURLS(ArrayList<String> imgURLS) {
        this.imgURLS = imgURLS;
    }

    public ArrayList<String> getImgURLV() {
        return imgURLV;
    }

    public void setImgURLV(ArrayList<String> imgURLV) {
        this.imgURLV = imgURLV;
    }

    public String getSidekickImgUrl() {
        return sidekickImgUrl;
    }

    public void setSidekickImgUrl(String sidekickImgUrl) {
        this.sidekickImgUrl = sidekickImgUrl;
    }

    public String getVillainImgUrl() {
        return villainImgUrl;
    }

    public void setVillainImgUrl(String villainImgUrl) {
        this.villainImgUrl = villainImgUrl;
    }

    public String getVillainDialogue() {
        return villainDialogue;
    }

    public void setVillainDialogue(String villainDialogue) {
        this.villainDialogue = villainDialogue;
    }
    String sidekickDialogue; 
    String handle;
    String name;

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public ArrayList<String> getHates() {
        return hates;
    }

    public void setHates(ArrayList<String> hates) {
        this.hates = hates;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getSidekicknames() {
        return sidekicknames;
    }

    public void setSidekicknames(ArrayList<String> sidekicknames) {
        this.sidekicknames = sidekicknames;
    }

    public ArrayList<String> getSidekicks() {
        return sidekicks;
    }

    public void setSidekicks(ArrayList<String> sidekicks) {
        this.sidekicks = sidekicks;
    }
    ArrayList<String> likes;
    ArrayList<String> hates; 
    
}
