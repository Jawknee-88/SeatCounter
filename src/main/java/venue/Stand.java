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
        NORTH("North Stand"),
        SOUTH("South Terrace"),
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
            case "North Stand":
                Block[] northBlocks = {new Block(this, "Stand Sixty-Six A", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Stand Sixty-Six B", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Stand Sixty-Six C", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Stand Sixty-Six D", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Stand Sixty-Six E", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME)};
                standBlocks.put(getStandName(), northBlocks);
                break;
            case "South Terrace":
                Block[] southBlocks = {new Block(this, "South Terrace", Block.SeatingOrStanding.STANDING, Block.SegregatedArea.HOME)};
                standBlocks.put(getStandName(), southBlocks);
                break;
            case "East":
                Block[] eastBlocks = {new Block(this, "The Hive Stand A", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "The Hive Stand B", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "The Hive Stand C", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Bumble Family Zone D", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Bumble Family Zone E", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Bumble Family Zone F", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Bumble Family Zone G", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Bumble Family Zone H", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME)};
                standBlocks.put(getStandName(), eastBlocks);
                break;
            case "West":
                Block[] westBlocks = {new Block(this, "Legends A", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Legends B", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Legends C", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Legends D", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Legends E", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Legends F", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME),
                        new Block(this, "Legends G", Block.SeatingOrStanding.SEATING, Block.SegregatedArea.HOME)};
                standBlocks.put(getStandName(), westBlocks);
                break;
        }
    }
}
