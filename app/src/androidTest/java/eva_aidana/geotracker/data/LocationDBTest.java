package eva_aidana.geotracker.data;

import android.content.Context;

import junit.framework.TestCase;

import java.sql.SQLException;

import data.LocationDB;
import model.LocationData;

/**
 * Created by Aidana on 5/26/2015.
 */
public class LocationDBTest extends TestCase {
    Context context;
    LocationDB db;

    public void setUp() {
        db = new LocationDB(context);
    }

    public void testConstructor() {
        assertNotNull(db);
    }

    public void testInsert() {
        try {
            db.insert("", 0, 0, 0, 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
