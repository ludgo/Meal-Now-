package com.ludgo.android.mealnow.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * A table within ActiveAndroid database,
 * includes meal offers and app users who requested them
 */
@Table(name = "public_offers")
public class PublicOffer extends Model {

    // Details associated with an offer
    @Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int offer_id;
    @Column
    public String offer_time_created;
    @Column
    public String offer_meal;
    @Column
    public String offer_location;
    @Column
    public double offer_latitude;
    @Column
    public double offer_longitude;
    @Column
    public int offer_filled; // boolean

    // Details associated with an offer's creator
    @Column
    public int user_id;
    @Column
    public String user_name;
    @Column
    public String user_email;
    @Column
    public String user_picture;

    public PublicOffer() {
        // Required empty public constructor
    }

    public PublicOffer(
            int offer_id,
            String offer_time_created,
            String offer_meal,
            String offer_location,
            double offer_latitude,
            double offer_longitude,
            int offer_filled,
            int user_id,
            String user_name,
            String user_email,
            String user_picture) {

        this.offer_id = offer_id;
        this.offer_time_created = offer_time_created;
        this.offer_meal = offer_meal;
        this.offer_location = offer_location;
        this.offer_latitude = offer_latitude;
        this.offer_longitude = offer_longitude;
        this.offer_filled = offer_filled;

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_picture = user_picture;
    }

    public static List<PublicOffer> getActiveOffers() {
        return new Select()
                .from(PublicOffer.class)
                .where("offer_filled = ?", 0)
                .orderBy("offer_time_created ASC")
                .limit(10)
                .execute();
    }
}
