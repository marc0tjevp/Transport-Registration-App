package theekransje.douaneapp;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import theekransje.douaneapp.Controllers.FreightActivity;

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
    public ActivityTestRule<FreightActivity> mActivityRule =
            new ActivityTestRule<>(FreightActivity.class);

    String testMRN = "1235sdsa";

    @Test
    public void checksIfPressingAddButtonAddsMRN() throws InterruptedException {
        onView(withId(R.id.mrnEditText)).perform(typeText(testMRN));
        Thread.sleep(250);
        onView(withId(R.id.buttonAdd)).perform(click());
        onView(withId(R.id.list_view)).check(matches(Matchers.withListSize(1)));
    }

    @Test
    public void checkIfPressingOnListItemOpensSnackbar() throws InterruptedException {
        checksIfPressingAddButtonAddsMRN();
        onView(withText(testMRN)).perform(click());
        Thread.sleep(250);
        onView(withText(R.string.want_to_delete))
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                )));
    }


    @Test
    public void checkIfPressingDeleteOnSnackbarDeletesItem() throws InterruptedException {
        checkIfPressingOnListItemOpensSnackbar();
        onView(withText(R.string.delete)).perform(click());
        onView(withId(R.id.list_view)).check(matches(Matchers.withListSize(0)));
    }
}
