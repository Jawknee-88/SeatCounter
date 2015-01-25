package parser;

import event.Match;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.text.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Michael on 31/10/2014.
 */
public class SeatCounter {

    public static Map<String, String> getCookies() {
        Connection.Response res = null;
        try {
            res = Jsoup
                    .connect("https://www.venuetoolbox.com/barnetfc/ASP/login.asp?nonMember=true&eventID={7E8FAFC5-E637-40EE-8D11-F66223AFBD91}")
                    .timeout(10*1000)
                    .method(Connection.Method.POST)
                    .execute();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

//This will get you cookies
        Map<String, String> cookies = res.cookies();

        return cookies;
    }

    public static int getNumberOfOccupiedSeats() {
        Match match = new Match();

        return match.calculateAllOccupiedSeats();

        //org.jsoup.nodes.Document doc = null;
        //try {
        //    doc = Jsoup.connect(match.getHomeSitePage().getPageURL()).cookies(getCookies()).get();
        //    occupiedSeats = doc.select("div[onclick*=\"Grey seats are sold and are not available to be purchased.\"]");
        //} catch (IOException e1) {
        //    e1.printStackTrace();
        //}

        //return occupiedSeats.size();
    }

    public static int getNumberOfAvailableSeats() {
        Match match = new Match();

        return match.calculateAllAvailableSeats();
    }

    public static void main(String args[]) {
        System.out.println(getNumberOfOccupiedSeats());
        //System.out.println(getNumberOfAvailableSeats());
    }
}
