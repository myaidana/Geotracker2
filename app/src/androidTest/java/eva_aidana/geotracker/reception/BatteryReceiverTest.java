package eva_aidana.geotracker.reception;

import android.content.Context;
import android.content.Intent;

import junit.framework.TestCase;

import reception.BatteryReceiver;

/**
 * Created by anurla92 on 5/26/2015.
 */
public class BatteryReceiverTest extends TestCase {
    BatteryReceiver batteryRec;
    public void setUp(){
        batteryRec = new BatteryReceiver();
    }

    public void testConstructor() {
        assertNotNull(batteryRec);
    }

    public void testOnRecieve(){

    }
}
