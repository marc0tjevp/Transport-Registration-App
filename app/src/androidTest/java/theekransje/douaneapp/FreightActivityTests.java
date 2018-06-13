package theekransje.douaneapp;

import android.support.test.espresso.intent.Checks;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import theekransje.douaneapp.Controllers.FreightActivity;
import theekransje.douaneapp.Controllers.LoginActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class FreightActivityTests {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);
    LoginActivityTests loginActivityTests = new LoginActivityTests();


    @Test
    public void checksIfPressingAddButtonAddsMRN() throws InterruptedException {
        loginActivityTests.checkIfLoginIsSuccessfulWithValidCredentials();
        Thread.sleep(250);
        onView(withId(R.id.status_imageButton)).perform(click());
        Thread.sleep(250);
        onView(withText("18IT123457384910TF")).perform(click());
        Thread.sleep(250);
        onView(withId(R.id.buttonSend)).perform(click());
        Thread.sleep(250);
        onView(withText("18IT123457384910TF"));
    }

}
