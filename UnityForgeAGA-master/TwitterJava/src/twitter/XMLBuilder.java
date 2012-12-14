/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Afshin
 */
public class XMLBuilder {

    public void buildXML(CharacterData sidekick, CharacterData villain) throws Exception {
        try {
            
            Date date = new Date();
            String fname = new Timestamp(date.getTime()).toString();



            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("TwitterData");
            doc.appendChild(rootElement);
            Element chars = doc.createElement("Characters");
            rootElement.appendChild(chars);
            //Sidekick goes here
            Element scharac = doc.createElement("Character");
            Element sID = doc.createElement("ID");
            sID.appendChild(doc.createTextNode(Integer.toString(1)));
            scharac.appendChild(sID);
            Element sname = doc.createElement("Name");
            sname.appendChild(doc.createTextNode(sidekick.name));
            scharac.appendChild(sname);
            Element simgURL = doc.createElement("ImgURL");
            simgURL.appendChild(doc.createTextNode(sidekick.sidekickImgUrl));
            scharac.appendChild(simgURL);
            chars.appendChild(scharac);
            // Sidekick friend Characters elements
            for (int i = 0; i < sidekick.sidekicknames.size(); i++) {
                Element charac = doc.createElement("Character");
                Element ID = doc.createElement("ID");
                ID.appendChild(doc.createTextNode(Integer.toString(2 + i)));
                charac.appendChild(ID);
                Element name = doc.createElement("Name");
                name.appendChild(doc.createTextNode(sidekick.sidekicknames.get(i)));
                charac.appendChild(name);
                Element likes = doc.createElement("Likes");
                charac.appendChild(likes);
                Element int1 = doc.createElement("int");
                int1.appendChild(doc.createTextNode(Integer.toString(i + 1)));
                likes.appendChild(int1);
                Element imgURL = doc.createElement("ImgURL");
                imgURL.appendChild(doc.createTextNode(sidekick.imgURLS.get(i)));
                charac.appendChild(imgURL);
                chars.appendChild(charac);
            }
            //Villain goes here
            Element vcharac = doc.createElement("Character");
            Element vID = doc.createElement("ID");
            vID.appendChild(doc.createTextNode(Integer.toString(5)));
            vcharac.appendChild(vID);
            Element vname = doc.createElement("Name");
            vname.appendChild(doc.createTextNode(villain.name));
            vcharac.appendChild(vname);
            Element vimgURL = doc.createElement("ImgURL");
            vimgURL.appendChild(doc.createTextNode(villain.sidekickImgUrl));
            vcharac.appendChild(vimgURL);
            chars.appendChild(vcharac);
            for (int i = 0; i < villain.sidekicknames.size(); i++) {
                Element charac = doc.createElement("Character");
                Element ID = doc.createElement("ID");
                ID.appendChild(doc.createTextNode(Integer.toString(6 + i)));
                charac.appendChild(ID);
                Element name = doc.createElement("Name");
                name.appendChild(doc.createTextNode(villain.sidekicknames.get(i)));
                charac.appendChild(name);
                Element imgURL = doc.createElement("ImgURL");
                imgURL.appendChild(doc.createTextNode(villain.imgURLS.get(i)));
                charac.appendChild(imgURL);
                chars.appendChild(charac);
            }
            //Sidekick relationships go here
            Element relat = doc.createElement("Relationships");
            rootElement.appendChild(relat);
            for (int i = 0; i < sidekick.sidekicknames.size(); i++) {
                Element rela = doc.createElement("Relationship");
                Element p1 = doc.createElement("person1");
                p1.appendChild(doc.createTextNode("1"));
                rela.appendChild(p1);
                Element p2 = doc.createElement("person2");
                p2.appendChild(doc.createTextNode(Integer.toString(2 + i)));
                rela.appendChild(p2);
                relat.appendChild(rela);
            }
            //Villain relationships go here    
            for (int i = 0; i < villain.sidekicknames.size(); i++) {
                Element rela = doc.createElement("Relationship");
                Element p1 = doc.createElement("person1");
                p1.appendChild(doc.createTextNode("5"));
                rela.appendChild(p1);
                Element p2 = doc.createElement("person2");
                p2.appendChild(doc.createTextNode(Integer.toString(6 + i)));
                rela.appendChild(p2);
                relat.appendChild(rela);
            }
            //Objects go here
            Element propobs = doc.createElement("PropObjects");
            rootElement.appendChild(propobs);
            for (int i = 0; i < sidekick.likes.size(); i++) {

                Element propo = doc.createElement("PropObject");
                Element id = doc.createElement("ID");
                id.appendChild(doc.createTextNode(Integer.toString(i + 1)));
                propo.appendChild(id);
                Element obname = doc.createElement("Name");
                obname.appendChild(doc.createTextNode(sidekick.likes.get(i)));
                propo.appendChild(obname);
                propobs.appendChild(propo);
            }
            //Villain ID goes here    
            Element villainID = doc.createElement("Villain");
            villainID.appendChild(doc.createTextNode("5"));
            rootElement.appendChild(villainID);
            //Sidekick ID goes here
            Element sidekickID = doc.createElement("Sidekick");
            sidekickID.appendChild(doc.createTextNode("1"));
            rootElement.appendChild(sidekickID);
            
            
            //Sidekick dialogue goes here
            Element sDialogue = doc.createElement("SidekickDialogue");
            sDialogue.appendChild(doc.createTextNode(sidekick.sidekickDialogue));
            rootElement.appendChild(sDialogue);
            //Vilain dialogue goes here
            Element vDialogue = doc.createElement("VillainDialogue");
            vDialogue.appendChild(doc.createTextNode(villain.sidekickDialogue));
            rootElement.appendChild(vDialogue);
            
            
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("../TwitterData/"+sidekick.getHandle()+fname+ ".xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}
