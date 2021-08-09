package venue;

import event.Match;
import parser.SitePage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Michael on 31/10/2014.
 */
public class Stadium {

    private static String stadiumName;
    private static Match match;
    private HashMap<String, Stand> stands = new HashMap<String, Stand>();



    public Stadium(Match ma, String sName) {
        stadiumName = sName;
        match = ma;
    }

    public String getHomeSitePage() {
        return match.getHomeSitePage();
    }

    public String getAwaySitePage() {
        return match.getAwaySitePage();
    }

    public static String getStadiumName() {
        return stadiumName;
    }

    public static Match getMatch() {
        return match;
    }

    public int getAllOccupiedSeats() {
        int occupiedSeats = 0;

        for(Stand s : getStands().values()) {
            occupiedSeats = occupiedSeats + s.getAllOccupiedSeats();
        }

        return occupiedSeats;
    }

    public int getAllAvailableSeats() {
        int availableSeats = 0;

        for(Stand s : getStands().values()) {
            availableSeats = availableSeats + s.getAllAvailableSeats();
        }

        return availableSeats;
    }

    public HashMap<String, Stand> getStands() {
        //Singleton to only initialise stands once
        if(stands.isEmpty()) {
            setupStands();
        }

        return stands;
    }

    /**
     * Use lazily to set up the stands. Should not be used in the constructor as "this" is not yet assigned which will
     * lead to NPE.
     */
    public void setupStands() {
        //stands.put("North", new Stand(this, Stand.StandCompassOrientation.NORTH));
        //stands.put("South", new Stand(this, Stand.StandCompassOrientation.SOUTH));
        //stands.put("East", new Stand(this, Stand.StandCompassOrientation.EAST));
        stands.put("West", new Stand(this, Stand.StandCompassOrientation.WEST));
    }
}
