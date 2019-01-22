# SeatCounter

A Web Scraper built for analysing ticket sales for venues whose ticket sales are managed by VenueToolbox. Currently tailored for Barnet FC but easily customisable for other clubs.

## Requirements
Java JDK 1.8+

Maven 3+

## How to run
There is no UI at present so the easiest way is to run within an IDE. Execute ```SeatCounter.java#main()``` to get all available seats.

To change the event being analysed, find the URL for the ticketing page as you would if you were buying tickets and insert into these parameters in ```Match.java```

```Java
private String eventPageHome = "https://bfc.venuetoolbox.com/VenueManagement/asp/selectArea.asp?bookEvent=true&eventID={325E222F-13AE-4019-A50F-66F647FBD17B}&packageID={6F9619FF-8B86-D011-B42D-00C04FC964FF}&homeArea=home";
private String eventPageAway = "https://bfc.venuetoolbox.com/VenueManagement/asp/selectArea.asp?bookEvent=true&eventID={325E222F-13AE-4019-A50F-66F647FBD17B}&packageID={6F9619FF-8B86-D011-B42D-00C04FC964FF}&homeArea=home";
```

To customise for different clubs, change the Stand and Block names in ```Stand.java#setStandBlocks``` to those shown on the venuetoolbox site.
