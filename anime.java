package anime;

import java.util.Arrays;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;

public class anime extends AnimeFrame {
	
    //Name of user
    static String listName;
    //Title of anime
    static String description;
    //What's the rating
    static String rating;
	
    static anime _this;
	
    //Parse html data
    static Parser parser = new Parser();
    static Parser parser2 = new Parser();
    static Parser parser3 = new Parser();
	
    //Look for these Attributes
    static HasAttributeFilter filter = new HasAttributeFilter("class", "animetitle");
    static HasAttributeFilter filter2 = new HasAttributeFilter("div");
    static HasAttributeFilter filter3 = new HasAttributeFilter("a");
	
    //Initial list
    static NodeList list;
    static NodeList list2;
	
    //Title of anime
    static String[] anArray;
    //Rating of anime
    static String[] anArray2;
	
    //Array for ratings, I honestly can't remember what Array2 does.. IT DOESN'T EVEN DO ANYTHING? IT JUST EXISTS? That's cool I guess.
    static double[] ratingArray;
    static double[] ratingArray2 = new double[300];

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
				//Show the UI
                new AnimeFrame().setVisible(true);
            }
        });
    }
	
    //The best descriptive title for a method
    public static void derp() {
	//Start a listName
        listName = AnimeFrame.text;
        AnimeFrame.jTextArea1.append("Finding your Plan to Watch list!\n");

        try {
	    //Set parser 3 resource to our list
            parser3.setResource("http://myanimelist.net/animelist/" + listName);
	    //look for the "a" attribute and put that into a nodelist for list 3
            NodeList list3 = parser3.parse(filter3);

            //We're going to go through our nodes and attributes until we find status=6 because this is the link to our plan to watch list
            for (int j = 0; j < list3.size(); j++) {
		//set node to "a"
                Node node2 = list3.elementAt(j);
                if (node2 instanceof LinkTag) {
                    LinkTag link2 = (LinkTag) node2;
		    //Look for href so we can get the correct link
                    String link3 = link2.getAttribute("href");
		    //We want the plan to watch list so bring up this listing
                    if (link3.contains("&status=6")) {
                        parser.setResource("http://myanimelist.net" + link3);
                        break;
                    }
                }
            }
			
            //Parse for class and anime title
            list = parser.parse(filter);

            AnimeFrame.jTextArea1.append("Found your list, populating array. This may take a few minutes.\n"
                    + "We have to go to every page that's in your list!\n\n\n");
        } catch (ParserException e) {
            e.printStackTrace();
            AnimeFrame.jTextArea1.setText("Opps");

        }
    }

    public static void foo() {
        try {
            for (int i = 0; i < list.size(); i++) {
	        //Give us a size of the current list
                anArray = new String[list.size()];
	        //I still can't figure out why I did this
                anArray2 = new String[list.size()];
	        //Give us a size for the ratings
                ratingArray = new double[list.size()];
                Node node = list.elementAt(i);

                if (node instanceof LinkTag) {
                    LinkTag link = (LinkTag) node;
		    //set Link1 to href
                    String link1 = link.getAttribute("href");
                    description = list.elementAt(i).toPlainTextString().trim();

                    parser2.setResource(link1);
		    //look for a DIV
                    list2 = parser2.parse(filter2);
                    node = list2.elementAt(i);
                    if (node instanceof Div) {
		        //get the rating from the current page we are on
                        rating = list2.elementAt(20).toPlainTextString().trim();
                        int count = 21;
                        while (!"Score".equals(rating.substring(0, 5))) {
                            rating = list2.elementAt(count).toPlainTextString().trim();
                            count++;
                        }
			//I feel like I could have done a better job at naming these variables...
			//Title of anime in the [i] position
                        anArray[i] = description;
			//Rating of anime in the [i] position, can't quite remember but I believe this took out the Score text
                        anArray2[i] = rating.substring(0, 11) + rating.substring(12);
			//Rating of anime in the [i] position
                        ratingArray[i] = Double.parseDouble(rating.substring(7, 11));
			//Yeah no idea, but I don't want to erase it before I compile it, why do I do this to myself
                        ratingArray2[i] = Double.parseDouble(rating.substring(7, 11));
			//Put the text in the jtextarea make sure not to erase previous text
                        AnimeFrame.jTextArea1.append(anArray[i] + " | " + anArray2[i] + " | " + ratingArray[i]);
                        AnimeFrame.jTextArea1.append("\n");
                        AnimeFrame.jTextArea1.append("---------------------\n");
                        AnimeFrame.jTextArea1.setCaretPosition(jTextArea1.getText().length() - 1);
                    }
                }
            }



        } catch (ParserException e) {
            e.printStackTrace();
            AnimeFrame.jTextArea1.setText("Opps");

        }
    }
}
