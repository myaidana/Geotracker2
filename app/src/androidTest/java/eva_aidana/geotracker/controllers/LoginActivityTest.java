package eva_aidana.geotracker.controllers;
import com.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;
import controller.LoginActivity;

/**
 * Created by Aidana on 5/15/15.
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity>  {
    private Solo solo;
    public LoginActivityTest(){super(LoginActivity.class);}
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
        solo.enterText(0,"");
        solo.clickOnButton("Login");
        boolean textFound = solo.searchText("Email should have at least 4 characters");
        assertTrue("Required fields validation failed", textFound);
    }

    public void testOrientation(){
        solo.enterText(0, "myaidana@gmail.com");
        solo.enterText(1, "password");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        boolean textFound = solo.searchText("myaidana@gmail.com");
        assertTrue("Orientation change failed", textFound);
        solo.setActivityOrientation(Solo.PORTRAIT);
        textFound = solo.searchText("myaidana@gmail.com");
        assertTrue("Orientation change failed", textFound);
    }

    public void testLoginButton() {
        solo.enterText(0, "myaidana@gmail.com");
        solo.enterText(1, "password");
        solo.clickOnButton("Login");
        boolean textFound = solo.searchText("Logged successfully");
        assertTrue("Login failed", textFound);
    }

    public void testCreateAccountButton(){
        solo.clickOnButton("Create Account");
        boolean textFound = solo.searchText("Transferring to registration page");
        assertTrue("Registration failed", textFound);
    }

    public void testForgotPasswordButton(){
        solo.clickOnButton("Forgot Password");
        boolean textFound = solo.searchText("Transferring to Forgot Password page");
        assertTrue("Forgot Password failed", textFound);
    }

}