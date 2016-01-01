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
    public String my_id;
    @Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String my_token;
    @Column
    public String my_name;
    @Column
    public String my_email;
    @Column
    public String my_picture;

    public MyInfo() {
        // Required empty public constructor
    }

    public MyInfo(
            String my_id,
            String my_token,
            String my_name,
            String my_email,
            String my_picture) {

        this.my_id = my_id;
        this.my_token = my_token;
        this.my_name = my_name;
        this.my_email = my_email;
        this.my_picture = my_picture;
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
