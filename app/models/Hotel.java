package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by swan on 4/28/2016.
 */
@Entity
@Table(name="wukong_ctrip")
public class Hotel extends Model {
    String country;
    String city;
    int hotelId;
    int mapped;

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public int getHotelId() {
        return hotelId;
    }

    public int getMapped() {
        return mapped;
    }

    public Hotel(String country, String city, int hotelId, int mapped) {
        this.country = country;
        this.city = city;
        this.hotelId = hotelId;
        this.mapped = mapped;
    }
}
