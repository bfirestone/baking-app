package com.bfirestone.udacity.cookbook.utils;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;

import com.bfirestone.udacity.cookbook.CookBookApplication;
import com.bfirestone.udacity.cookbook.ui.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

public abstract class BaseTest {
    protected CookBookApplication cookBookApplication;
    protected IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        cookBookApplication = (CookBookApplication) activityTestRule.getActivity().getApplicationContext();
        mIdlingResource = cookBookApplication.getIdlingResource();
        // Register Idling Resources
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
