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
    private String eventPageHome = "https://www.venuetoolbox.com/barnetfc/ASP/selectArea.asp?bookEvent=true&eventID={BFB0F7D2-B5D3-4329-A537-B91F79468A57}&packageID=&homeArea=home";
    private String eventPageAway = "https://www.venuetoolbox.com/barnetfc/ASP/selectArea.asp?bookEvent=true&eventID={BFB0F7D2-B5D3-4329-A537-B91F79468A57}&packageID={6F9619FF-8B86-D011-B42D-00C04FC964FF}&homeArea=away";

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
