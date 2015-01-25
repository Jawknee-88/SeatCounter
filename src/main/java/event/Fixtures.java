package event;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Michael on 04/11/2014.
 */
public class Fixtures {

    HashMap<String, Match> fixtures = new HashMap();

    public Fixtures() {
        compileFixtures();
    }

    public void compileFixtures() {
        try {
            Document doc = Jsoup.connect("https://www.venuetoolbox.com/barnetfc/ASP/bookTickets.asp?dept=Spectators").get();
            //Elements eventTitles = doc.select();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
