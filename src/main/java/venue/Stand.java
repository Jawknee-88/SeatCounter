package venue;

import java.util.HashMap;

/**
 * Created by Michael on 31/10/2014.
 */
public class Stand {

    private HashMap<String, Block[]> standBlocks = new HashMap<String, Block[]>();
    private Stadium stadium;
    StandCompassOrientation standOrientation;

    public Stand(Stadium st, StandCompassOrientation orientation) {
        standOrientation = orientation;
        stadium = st;
    }

    public enum StandCompassOrientation {
        NORTH("North Terrace. Away Standing"),
        SOUTH("South Terrace. Home Standing"),
        EAST("East"),
        WEST("West");

        String orientation = null;

        StandCompassOrientation(String name) {
            orientation = name;
        }

        public String getOrientation() {
            return orientation;
        }
    }

    public StandCompassOrientation getStandOrientation() {
        return standOrientation;
    }

    public String getStandName() {
        return getStandOrientation().getOrientation();
    }

    public String getHomeEventUrl() {
        return stadium.getHomeSitePage();
    }

    public String getAwayEventUrl() {
        return stadium.getAwaySitePage();
    }

    public int getAllOccupiedSeats() {
        int occupiedSeats = 0;

        for(Block[] b : getStandBlocks().values()) {
            for(int i = 0; i < b.length; i++) {
                occupiedSeats = occupiedSeats + b[i].getBlockSoldTickets();
            }
        }

        return occupiedSeats;
    }

    public int getAllAvailableSeats() {
        int availableSeats = 0;

        for(Block[] b : getStandBlocks().values()) {
            for(int i = 0; i < b.length; i++) {
                availableSeats = availableSeats + b[i].getBlockAvailableSeats();
            }
        }

        return availableSeats;
    }

    public HashMap<String, Block[]> getStandBlocks() {
        //Singleton
        if(standBlocks.isEmpty()) {
            setStandBlocks();
        }

        return standBlocks;
    }

    /**
     * Use lazily to set up the stands. Should not be used in the constructor as "this" is not yet assigned which will
     * lead to NPE.
     */
    public void setStandBlocks() {
        switch (getStandName()) {
            case "North Terrace. Away Standing":
                Block[] northBlocks = {new Block(this, "North Terrace. Away Standing", Block.SeatingOrStanding.STANDING, Block.SegregatedArea.AWAY)};
                standBlocks.put(getStandName(), northBlocks);
                break;
            case "South Terrace. Home Standing":
                Block[] southBlocks = {new Block(this, "South Terrace. Home Standing", Block.SeatingOrStanding.STANDING, Block.SegregatedArea.HOME)};
                standBlocks.put(getStandName(), southBlocks);
                break;
            case "East":
                Block[] eastBlocks = {new Block(this, "East Stand A", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "East Stand B", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME), new Block(this, "East Stand C", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "East Stand D", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME), new Block(this, "East Stand E", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "East Stand F", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME), new Block(this, "East G UNCOVER", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "East Stand H (UNCOVERED SEATS)", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME)};
                standBlocks.put(getStandName(), eastBlocks);
                break;
            case "West":
                Block[] westBlocks = {new Block(this, "West Stand A", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME), new Block(this, "West Stand B", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "West Stand C", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME), new Block(this, "West Stand D", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "West Stand E", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME), new Block(this, "West Away F", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.AWAY),
                        new Block(this, "West Away G", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.AWAY)};
                standBlocks.put(getStandName(), westBlocks);
                break;
        }
    }
}
