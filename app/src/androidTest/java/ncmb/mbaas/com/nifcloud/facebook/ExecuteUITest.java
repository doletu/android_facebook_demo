package ncmb.mbaas.com.nifcloud.facebook;


import android.app.Application;
import android.app.Notification;
import android.icu.text.Edits;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertThat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExecuteUITest {

    ViewInteraction tvHead1;
    ViewInteraction tvHead2;
    ViewInteraction btnLogin;
    ViewInteraction tvHead3;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() throws InterruptedException {
        tvHead1 = onView(withId(R.id.textView2));
        tvHead2 = onView(withId(R.id.textView));
        btnLogin = onView(withId(R.id.login_button));
        tvHead3 = onView(withId(R.id.textView4));

        if (TextHelpers.getText(btnLogin).equalsIgnoreCase("Log out")) {
            btnLogin.perform(click());
            onView(withText("LOG OUT")).perform(click());
        }

    }

    @Test
    public void initialScreen() {
        tvHead1.check(matches(withText("NIFCLOUD")));
        tvHead2.check(matches(withText("mobile backend")));
        btnLogin.check(matches(withText("Log in")));
        tvHead3.check(matches(withText("Touch [Login] to login using Facebook account.")));
    }

    /**
     * refer: https://gist.github.com/FisherKK/8e7b6489d182c1998612993544681c25
     * @throws Exception
     */
    @Test
    public void testFacebook_correctData_enterNotificationCenter() throws Exception {

        final String FB_EMAIL = "YOUR_EMAIL@mail.com";
        final String FB_PASS = "YOUR_PASS_WORD";


        final UiDevice mDevice =
                UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        final int timeOut = 1000 * 60;

        // Login Activity
        btnLogin.perform(click());





        if (TextHelpers.getText(btnLogin).equalsIgnoreCase("Log out")) {
            btnLogin.check(matches(isDisplayed()));
        }else{
            // Set Login
            UiObject emailInput = mDevice.findObject(new UiSelector().instance(0).text("Phone or email"));

            emailInput.waitForExists(timeOut);
            emailInput.setText(FB_EMAIL);


            // Set Password
            UiObject passwordInput = mDevice.findObject(new UiSelector()
                    .instance(0)
                    .className(EditText.class).textContains("Password"));

            passwordInput.waitForExists(timeOut);
            passwordInput.setText(FB_PASS);

            // Confirm Button Click
            UiObject buttonLogin = mDevice.findObject(new UiSelector()
                    .instance(0)
                    .descriptionContains("Log In"));

            buttonLogin.waitForExists(timeOut);
            buttonLogin.click();




            // should be properly synchronised with Espresso via IdlingResource,
            // ConditionWatcher or any similar waiting solution
            Thread.sleep(15000);

            btnLogin.check(matches(withText("Log out")));
        }

    }

    private static class TextHelpers {
        static String getText(ViewInteraction matcher){
            final String[] text = new String[1];
            ViewAction va = new ViewAction() {

                @Override
                public Matcher<View> getConstraints() {
                    return isAssignableFrom(TextView.class);
                }

                @Override
                public String getDescription(){
                    return "Text of the view";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    TextView tv = (TextView) view;
                    text[0] = tv.getText().toString();
                }
            };

            matcher.perform(va);

            return text[0];
        }
    }
}