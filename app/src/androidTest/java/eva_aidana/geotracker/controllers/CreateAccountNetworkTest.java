package eva_aidana.geotracker.controllers;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.robotium.solo.SystemUtils;

import controller.CreateAccountActivity;

/**
 * Created by anurla92 on 5/29/2015.
 */
public class CreateAccountNetworkTest extends ActivityInstrumentationTestCase2<CreateAccountActivity> {
    private Solo solo;
    public CreateAccountNetworkTest() {
        super(CreateAccountActivity.class);
    }

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
        solo.enterText(0, "anurla92@uw.edu");
        solo.enterText(1, "password");
        solo.enterText(2, "question");
        solo.enterText(3, "answer");
        solo.clickOnCheckBox(0);
        solo.clickOnButton("Register");
        boolean textFound = solo.searchText("No wifi connection");
        assertTrue("Network is down, message displayed", textFound);
        systemUtils.setWiFiData(true);
    }
}
