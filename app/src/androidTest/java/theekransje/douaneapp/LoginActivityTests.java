package theekransje.douaneapp;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTests {

    //makes sure that theh test starts on this activity
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);
    private static final String username = "djimmeke";
    private static final String password = "hallo?";
    @Test
    public void checksIfUniqueIDIsDisplayed(){
        onView(withId(R.id.login_imei)).check(matches(not(ViewMatchers.withText("TextView"))));
    }

    @Test
    public void checkIfLoginIsSuccessfulWithValidCredentials() throws InterruptedException {
        onView(withId(R.id.login_user)).perform(typeText(username));
        Thread.sleep(250);
        onView(withId(R.id.login_passwd)).perform(typeText(password));
        Thread.sleep(250);
        onView(withId(R.id.login_button)).perform(click());
    }

    @Test
    public void checkIfLoginIsUnsuccessfulWithInvalidCredentials() throws InterruptedException {
        onView(withId(R.id.login_user)).perform(typeText(username + "sahdklhjsfsad"));
        Thread.sleep(250);
        onView(withId(R.id.login_passwd)).perform(typeText(password + "sahdklhjsfsad"));
        Thread.sleep(250);
        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(1000);
        onView(withText(R.string.invalid_credentials));
    }
}
