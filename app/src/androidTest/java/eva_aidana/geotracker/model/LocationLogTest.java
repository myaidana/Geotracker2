package eva_aidana.geotracker.model;

import junit.framework.TestCase;

import java.util.List;

import model.LocationData;
import model.LocationLog;

/**
 * Created by Aidana on 5/26/2015.
 */
public class LocationLogTest extends TestCase {
    private LocationLog mLog;
    private List<LocationData> mLocationList;

    public void setUp() {
        mLog = new LocationLog();
    }

    public void testConstructor() {
        LocationLog log = new LocationLog();
        assertNotNull(log);
    }

    public void testAddLocation() {
        LocationData data = new LocationData(0, 0, 0, "", "");
        mLog.addLocation(data);
        assertEquals(true, mLog.getLocationList().contains(data));
    }



}
