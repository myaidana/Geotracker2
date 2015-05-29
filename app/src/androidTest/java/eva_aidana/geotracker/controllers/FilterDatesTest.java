package eva_aidana.geotracker.controllers;

import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;
import controller.FilterDatesActivity;

/**
 * Created by anurla92 on 5/20/2015.
 */
public class FilterDatesTest  extends ActivityInstrumentationTestCase2<FilterDatesActivity> {
    private Solo solo;
    public FilterDatesTest(){super(FilterDatesActivity.class);}
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

    public void testOrientation() {
        solo.clickOnButton("Start Date");
        solo.clickOnButton("Done");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        boolean textFound = solo.searchText("2015-5-29");
        assertTrue("Orientation change failed", textFound);
        solo.setActivityOrientation(Solo.PORTRAIT);
        textFound = solo.searchText("2015-5-29");
        assertTrue("Orientation change failed", textFound);
    }

    public void testStartDateButton() {
        solo.clickOnButton("Start Date");
        boolean textFound = solo.searchText("Done");
        assertTrue("Start Date failed", textFound);
    }

    public void testStartTimeButton() {
        solo.clickOnButton("Start Time");
        boolean textFound = solo.searchText("Done");
        assertTrue("Start Time failed", textFound);
    }

    public void testEndDateButton() {
        solo.clickOnButton("End Date");
        boolean textFound = solo.searchText("Done");
        assertTrue("End Date failed", textFound);
    }

    public void testEndTimeButton() {
        solo.clickOnButton("End Time");
        boolean textFound = solo.searchText("Done");
        assertTrue("End Time failed", textFound);
    }

    public void testMyProfileButton() {
        solo.clickOnButton("My Profile");
        boolean textFound = solo.searchText("Start Service");
        assertTrue("My Profile Button failed", textFound);
    }

}
