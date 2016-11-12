package summer.noline.Database;

/**
 * Created by sumkit on 10/29/16.
 */

public class Venue {
    private String imageUrl;
    private double ticketPrice;
    private String address, name;

    public Venue(String n, String u, double tp, String a) {
        name = n;
        imageUrl = u;
        ticketPrice = tp;
        address = a;
    }
    public String getName() {
        return name;
    }
    public String getImage() {
        return imageUrl;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public String getAddress() {
        return address;
    }
}