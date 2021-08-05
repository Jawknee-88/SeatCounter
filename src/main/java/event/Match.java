package event;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import parser.SitePage;
import venue.Layout;
import venue.Stadium;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Michael on 31/10/2014.
 */
public class Match {

    private Stadium stadium = null;
    private String eventPageHome = "https://www.venuetoolbox.com/barnetfc/asp/selectArea.asp?bookEvent=true&eventID={536163DC-11E1-42AB-A679-D481C5B325DD}&packageID={6F9619FF-8B86-D011-B42D-00C04FC964FF}&homeArea=home";
    private String eventPageAway = "https://www.venuetoolbox.com/barnetfc/asp/selectArea.asp?bookEvent=true&eventID={536163DC-11E1-42AB-A679-D481C5B325DD}&packageID={6F9619FF-8B86-D011-B42D-00C04FC964FF}&homeArea=home";

    public Stadium getStadium() {
        if(stadium == null) {
            setStadium();
        }

        return stadium;
    }

    public void setStadium() {
        stadium = new Stadium(this, "The Hive");
    }

    public String getHomeSitePage() {
        return eventPageHome;
    }

    public String getAwaySitePage() {
        return eventPageAway;
    }

    public int calculateAllOccupiedSeats() {
        return getStadium().getAllOccupiedSeats();
    }

    public int calculateAllAvailableSeats() {
        return getStadium().getAllAvailableSeats();
    }
}
