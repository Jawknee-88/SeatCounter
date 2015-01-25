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
    private String eventPageHome = "https://www.venuetoolbox.com/barnetfc/ASP/selectArea.asp?bookEvent=true&eventID={229EB7C2-8385-4579-8B8B-1779CEB457A1}&homeArea=home";
    private String eventPageAway = "https://www.venuetoolbox.com/barnetfc/ASP/selectArea.asp?bookEvent=true&eventID={7B8E5B4A-B0F4-4DA1-8B2A-CE3F8D53F71A}&homeArea=away";

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
