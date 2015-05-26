package eva_aidana.geotracker.controllers;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import controller.MyProfileActivity;

/**
 * Created by Aidana on 5/20/2015.
 */
public class MyProfileTest extends ActivityInstrumentationTestCase2<MyProfileActivity> {
    private Solo solo;

    public MyProfileTest() {
        super(MyProfileActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testOrientation(){
        solo.enterText(0, "5");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        boolean textFound = solo.searchText("5");
        assertTrue("Orientation change failed", textFound);
        solo.setActivityOrientation(Solo.PORTRAIT);
        textFound = solo.searchText("5");
        assertTrue("Orientation change failed", textFound);
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }


    public void testChangeSampleRateButton() {
        solo.enterText(0, "20");
        solo.clickOnButton("Change Sampling Rate");
        boolean textFound = solo.searchText("Sample rates is set for 20 seconds");
        assertTrue("Change Sample Rate failed", textFound);
    }

    public void testChangeUploadRateButton() {
        solo.enterText(1, "0");
        solo.clickOnButton("Change Upload Rate");
        boolean textFound = solo.searchText("Sample rates must be between 1 hour " +
                "and 1 day");
        assertTrue("Change Upload Rate failed", textFound);
    }
    public void testStartServiceButton() {
        solo.clickOnButton("Start Service");
        boolean textFound = solo.searchText("Service started successfully");
        assertTrue("Service Start failed", textFound);
    }

    public void testStopServiceButton() {
        solo.clickOnButton("End Service");
        boolean textFound = solo.searchText("Service has been stopped");
        assertTrue("Service End failed", textFound);
    }

}
