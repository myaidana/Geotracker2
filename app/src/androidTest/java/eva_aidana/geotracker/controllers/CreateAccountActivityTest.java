package eva_aidana.geotracker.controllers;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.robotium.solo.SystemUtils;

import controller.CreateAccountActivity;

/**
 * Created by Aidana on 5/20/2015.
 */
public class CreateAccountActivityTest extends ActivityInstrumentationTestCase2<CreateAccountActivity> {
    private Solo solo;

    public CreateAccountActivityTest() {
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


    public void testOrientation() {
        solo.enterText(0, "myaidana@gmail.com");
        solo.enterText(1, "password");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        boolean textFound = solo.searchText("myaidana@gmail.com");
        assertTrue("Orientation change failed", textFound);
        solo.setActivityOrientation(Solo.PORTRAIT);
        textFound = solo.searchText("myaidana@gmail.com");
        assertTrue("Orientation change failed", textFound);
    }

    //    public void testRequiredFields() {
//        solo.unlockScreen();
//        solo.enterText(0, "");
//        solo.clickOnButton("Create Account");
//        solo.clickOnButton("Register");
//        boolean textFound = solo.searchText("You must agree to the terms and conditions before continuing");
//        assertTrue("Required fields validation failed", textFound);
//    }


    public void testRegisterButton() {
        solo.enterText(0, "anurla92@uw.edu");
        solo.enterText(1, "password");
        solo.enterText(2, "question");
        solo.enterText(3, "answer");
        solo.clickOnCheckBox(0);
        solo.clickOnButton("Register");
        boolean textFound = solo.searchText("Registration is in process...");
        assertTrue("Registration failed", textFound);
    }

}
