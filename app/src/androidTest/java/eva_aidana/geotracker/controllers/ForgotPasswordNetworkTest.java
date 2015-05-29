package eva_aidana.geotracker.controllers;

import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;
import com.robotium.solo.SystemUtils;

import controller.ForgotPasswordActivity;

/**
 * Created by Aidana on 5/20/2015.
 */
public class ForgotPasswordNetworkTest extends ActivityInstrumentationTestCase2<ForgotPasswordActivity> {
    private Solo solo;
    public ForgotPasswordNetworkTest(){super(ForgotPasswordActivity.class);}
    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }


    public void testNetworkDown() {
        SystemUtils systemUtils = new SystemUtils(getInstrumentation());
        systemUtils.setWiFiData(false);
        solo = new Solo(getInstrumentation(), getActivity());
        solo.clickOnButton("Submit");
        boolean textFound = solo.searchText("No wifi connection");
        assertTrue("Network is down, message displayed", textFound);
        systemUtils.setWiFiData(true);
    }
}
