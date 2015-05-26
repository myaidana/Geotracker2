package eva_aidana.geotracker.controllers;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import controller.CreateAccountActivity;
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

    public void testOrientation(){
        solo.enterText(0, "2015-5-20");
        solo.enterText(1, "21:33");
        solo.enterText(2, "2015-5-20");
        solo.enterText(3, "21:34");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        boolean textFound = solo.searchText("2015-5-20");
        textFound = solo.searchText("21:33");
        textFound = solo.searchText("21:34");
        assertTrue("Orientation change failed", textFound);
        solo.setActivityOrientation(Solo.PORTRAIT);
        textFound = solo.searchText("2015-5-20");
        assertTrue("Orientation change failed", textFound);
    }
}
