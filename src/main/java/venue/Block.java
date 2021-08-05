package venue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.*;

import static parser.SeatCounter.createNewCookies;

/**
 * Created by Michael on 31/10/2014.
 */
public class Block {

    private String blockName;
    private String blockURL = "https://www.venuetoolbox.com/barnetfc/ASP/";
    private Stand stand;
    private SegregatedArea segregationStatus;
    private SeatingOrStanding seatsOrTerrace;
    private static final Object countLock = new Object();
    volatile Integer blockCount = 0;
    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

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
            doc = Jsoup.connect(baseUrl).cookies(createNewCookies()).get();
            clickableBlocks = doc.select("#venueMap > div");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Element e : clickableBlocks) {
            if(e.text().equals(blockName)) {
                String str = e.attr("onclick");
                str = str.split(String.format(WITH_DELIMITER, "selectArea"), 2)[1];
                str = str.substring(0, str.length()-3);
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
            doc = Jsoup.connect(getBlockUrl()).cookies(createNewCookies()).get();
            occupiedSeats = doc.select("div[onclick*=alert('Grey seats are not available to be purchased.');]");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println(stand.getStandName() + " " + getBlockName() + " Seats sold: " + occupiedSeats.size());

        return occupiedSeats.size();
    }

    public String findOrderTicketURL() {
        Document pricesPage = null;
        String orderTicketURL = null;

        try {
            pricesPage = Jsoup.connect(getBlockUrl()).cookies(createNewCookies()).timeout(10*10000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements ticketLink = pricesPage.select("ul > table > tbody > tr > td > a");

        findOrderTicketLink:
        for(Element el : ticketLink) {
            if(el.text().contains("£")) {
                orderTicketURL = "https://www.venuetoolbox.com/barnetfc/ASP/" + el.attr("href");
                System.out.println(el.text());
                break findOrderTicketLink;
            }
        }

        return orderTicketURL;
    }

    /**
     * Terrace capacity believed to be 865 based on counting total available in the away end far in advance i.e. they
     * wouldn't have sold any.
     *
     * @return number of sold tickets
     */
    public int getStandingSoldTickets() {
        ArrayList<Map<String, String>> allCookies = new ArrayList<Map<String, String>>();

        try {
            final String orderTicketURL = findOrderTicketURL();
            ExecutorService execs = Executors.newFixedThreadPool(10);

            for (int i = 0; i < 300; i++) {
                final Map<String, String> cookies = createNewCookies();
                allCookies.add(cookies);

                Future f = execs.submit(new Runnable() {
                    @Override
                    public void run() {
                        reserveMaximumTicketsForOneUser(orderTicketURL, cookies);
                    }
                });
            }

            execs.shutdown();
            execs.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            //teardownCookies(allCookies);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(getBlockName() + " Sold: " + (865 - blockCount) + " Tickets remaining: " + blockCount);

        return blockCount;
    }

    public void teardownCookies(ArrayList<Map<String, String>> allCookies) throws IOException {
        for(int m = 0; m<allCookies.size(); m++) {
            Jsoup.connect("https://www.venuetoolbox.com/barnetfc/ASP/login.asp?doPublicClearBasket=true&homeArea=home").cookies(allCookies.get(m)).get();
        }
    }

    /**
     * Uses one
     * @return number of tickets that the user has been allowed to reserve
     */
    public void reserveMaximumTicketsForOneUser(String ticketURL, Map<String, String> cookies) {
        int userCount = 0;
        boolean exceededOnlineOrderingLimit = false;
        System.out.println("Started");

        do {
            Elements h3 = null;
            org.jsoup.nodes.Document doc = null;

            try {
                doc = Jsoup.connect(ticketURL).cookies(cookies).timeout(10 * 10000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            h3 = doc.select("h3");

            if (h3.text().contains("Online Ordering Limit")) {
                userCount++;
                exceededOnlineOrderingLimit = true;
            } else if(h3.text().contains(("Ticket No Longer Available"))) {
                if(h3.text().contains(("Ticket No Longer Available"))) {
                    System.out.println("Ticket No Longer Available" + " - Reserved: " + userCount);
                }
                exceededOnlineOrderingLimit = true;
            } else {
                userCount++;
            }
        } while (!exceededOnlineOrderingLimit);

        blockCount = blockCount + userCount;
        System.out.println("User reserved: " + userCount);
    }

    public int getBlockAvailableSeats() {
        Elements availableSeats = null;

        org.jsoup.nodes.Document doc = null;
        try {
            doc = Jsoup.connect(getBlockUrl()).cookies(createNewCookies()).get();
            availableSeats = doc.select("div[onclick*=\"window.location='selectArea.asp?selectArea=true&eventID=\"]");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println(stand.getStandName() + " " + getBlockName() + " Seats Available: " + availableSeats.size());

        return availableSeats.size();
    }


}
