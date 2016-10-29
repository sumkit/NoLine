package summer.noline.Database;

import android.media.Image;

/**
 * Created by sumkit on 10/29/16.
 */

public class User {
    private String name, city;
    private Image image;
    public User(String name, String city, Image image) {
        this.name = name;
        this.city = city;
        this.image = image;
    }
    public String getName() {
        return name;
    }

    //Update user's city
    public void changeCity(String newCity) {
        this.city = newCity;
    }
}
