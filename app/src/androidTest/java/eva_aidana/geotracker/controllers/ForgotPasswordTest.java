package eva_aidana.geotracker.controllers;

import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;
import com.robotium.solo.SystemUtils;

import controller.ForgotPasswordActivity;

/**
 * Created by Aidana on 5/20/2015.
 */
public class ForgotPasswordTest extends ActivityInstrumentationTestCase2<ForgotPasswordActivity> {
    private Solo solo;
    public ForgotPasswordTest(){super(ForgotPasswordActivity.class);}
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

    public void testRequiredFields() {
        solo.unlockScreen();
        solo.enterText(0, "");
        solo.clickOnButton("Submit");
        boolean textFound = solo.searchText("Please enter a valid email");
        assertTrue("Required fields validation failed", textFound);
    }


    public void testOrientation(){
        solo.enterText(0, "myaidana@gmail.com");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        boolean textFound = solo.searchText("myaidana@gmail.com");
        assertTrue("Orientation change failed", textFound);
        solo.setActivityOrientation(Solo.PORTRAIT);
        textFound = solo.searchText("myaidana@gmail.com");
        assertTrue("Orientation change failed", textFound);
    }


    public void testSubmitButton() {
        solo.enterText(0, "myaidana@gmail.com");
        solo.clickOnButton("Submit");
        boolean textFound = solo.searchText("An email with instructions");
        assertTrue("Password Reset failed", textFound);
    }

}
