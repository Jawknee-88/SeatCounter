package venue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static parser.SeatCounter.getCookies;

/**
 * Created by Michael on 31/10/2014.
 */
public class Block {

    private String blockName;
    private String blockURL = "https://www.venuetoolbox.com/barnetfc/ASP/";
    private Stand stand;
    private SegregatedArea segregationStatus;
    private SeatingOrStanding seatsOrTerrace;

    public Block (Stand st, String name, SeatingOrStanding seatsOrStand, SegregatedArea homeOrAway) {
        blockName = name;
        stand = st;
        segregationStatus = homeOrAway;
        seatsOrTerrace = seatsOrStand;
        setBlockURL();
    }

    public enum SegregatedArea {
        HOME,
        AWAY;
    }

    public enum SeatingOrStanding {
        SEATING,
        STANDING;
    }

    public SegregatedArea getSegregationStatus() {
        return segregationStatus;
    }

    public String getBlockName() {
        return blockName;
    }

    public String getBlockUrl() {
        return blockURL;
    }

    public void setBlockURL() {
        String baseUrl = null;
        Document doc;
        Elements clickableBlocks = null;

        if(segregationStatus == SegregatedArea.HOME) {
            baseUrl = stand.getHomeEventUrl();
        } else if(segregationStatus == SegregatedArea.AWAY) {
            baseUrl = stand.getAwayEventUrl();
        }

        try {
            doc = Jsoup.connect(baseUrl).cookies(getCookies()).get();
            clickableBlocks = doc.select("#venueMap > div[onclick*=\"window.location='selectArea.asp?selectArea=true&eventID=\"]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Element e : clickableBlocks) {
            if(e.text().equals(blockName)) {
                String str = e.attr("onclick");
                str = str.substring(17, str.length()-2);
                blockURL = blockURL + str;
                break;
            }
        }
    }

    public int getBlockSoldTickets() {
        int sold = 0;
        if(seatsOrTerrace == SeatingOrStanding.SEATING) {
            sold = getSeatingSoldTickets();
        } else if(seatsOrTerrace == SeatingOrStanding.STANDING) {
            getStandingSoldTickets();
        }

        return sold;
    }

    public int getSeatingSoldTickets() {
        Elements occupiedSeats = null;

        org.jsoup.nodes.Document doc = null;
        try {
            doc = Jsoup.connect(getBlockUrl()).cookies(getCookies()).get();
            occupiedSeats = doc.select("div[onclick*=\"Grey seats are sold and are not available to be purchased.\"]");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println(stand.getStandName() + " " + getBlockName() + " Seats sold: " + occupiedSeats.size());

        return occupiedSeats.size();
    }

    public int getStandingSoldTickets() {
        ArrayList<Map<String, String>> allCookies = new ArrayList<Map<String, String>>();
        int count = 0;



        try {
            //Get the link on the page to add ticket to basket. Don't need to worry about caching the
            //cookies here.
            Document pricesPage = Jsoup.connect(getBlockUrl()).cookies(getCookies()).timeout(10*1000).get();
            Elements ticketLink = pricesPage.select("ul > table > tbody > tr > td > a");
            String orderTicketURL = null;

            findOrderTicketLink:
            for(Element el : ticketLink) {
                if(el.text().contains("£")) {
                    orderTicketURL = "https://www.venuetoolbox.com/barnetfc/ASP/" + el.attr("href");
                    System.out.println(el.text());
                    break findOrderTicketLink;
                }
            }


            mainloop:
            for(int i = 0; i<128; i++) {
                Map<String, String> cookies = getCookies();
                allCookies.add(cookies);
                for(int c = 0; c<6; c++) {
                    Elements h3 = null;
                    org.jsoup.nodes.Document doc = null;

                    doc = Jsoup.connect(orderTicketURL).cookies(cookies).timeout(10*1000).get();
                    h3 = doc.select("h3");
                    count++;
                    System.out.println(count);

                    if(h3.text().contains("Ticket No Longer Available")) {
                        break mainloop;
                    }
                }

            }

            for(int m = 0; m<allCookies.size(); m++) {
                Jsoup.connect("https://www.venuetoolbox.com/barnetfc/ASP/login.asp?doPublicClearBasket=true&homeArea=home").cookies(allCookies.get(m)).get();
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        System.out.println(getBlockName() + " Still available: " + (764-count) + " Tickets remaining: " + count);

        return count;
    }

    public int getBlockAvailableSeats() {
        Elements availableSeats = null;

        org.jsoup.nodes.Document doc = null;
        try {
            doc = Jsoup.connect(getBlockUrl()).cookies(getCookies()).get();
            availableSeats = doc.select("div[onclick*=\"window.location='selectArea.asp?selectArea=true&eventID=\"]");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println(stand.getStandName() + " " + getBlockName() + " Seats Available: " + availableSeats.size());

        return availableSeats.size();
    }


}
