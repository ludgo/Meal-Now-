package com.ludgo.android.mealnow.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * A table within ActiveAndroid database,
 * includes current user info and credentials
 */
@Table(name = "my_info")
public class MyInfo extends Model {

    // Temporary data describing currently signed user
    @Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int my_id;
    @Column
    public String my_token; // this is server token, not provider token
    @Column
    public String my_name;
    @Column
    public String my_email;
    @Column
    public String my_picture;
    @Column
    public String my_provider_id;

    public MyInfo() {
        // Required empty public constructor
    }

    public MyInfo(
            int my_id,
            String my_token,
            String my_name,
            String my_email,
            String my_picture,
            String my_provider_id) {

        this.my_id = my_id;
        this.my_token = my_token;
        this.my_name = my_name;
        this.my_email = my_email;
        this.my_picture = my_picture;
        this.my_provider_id = my_provider_id;
    }

    public static List<MyInfo> getAll() {
        return new Select()
                .from(MyInfo.class)
                .execute();
    }

    public static List<MyInfo> deleteAll() {
        return new Delete()
                .from(MyInfo.class)
                .execute();
    }
}
