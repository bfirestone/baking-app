package com.bfirestone.udacity.cookbook;

import android.content.Context;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.bfirestone.udacity.cookbook.models.Recipe;
import com.bfirestone.udacity.cookbook.ui.activities.RecipeStepDetailActivity;
import com.bfirestone.udacity.cookbook.utils.BaseTest;
import com.bfirestone.udacity.cookbook.utils.Navigation;
import com.bfirestone.udacity.cookbook.utils.RecipeUtils;
import com.orhanobut.logger.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ThreadLocalRandom;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class BakingRecipesTests extends BaseTest {

    @Test
    public void clickRecyclerViewItemHasIntentExtraWithRecipeKey() {
        //Checks if the key is present
        Intents.init();

        Navigation.getMeToRecipeInfo(0);
        intended(hasExtraWithKey(Config.RECIPE_KEY));

        Intents.release();

    }

    @Test
    public void clickOnRecyclerViewItem_opensRecipeInfoActivity() {

        Navigation.getMeToRecipeInfo(0);

        onView(ViewMatchers.withId(R.id.ingredient_list_text))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recipe_step_list))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickRecyclerViewStep_opensRecipeDetailStepActivity() {
        int randomRecipe = ThreadLocalRandom.current().nextInt(0, 4);
        Logger.i("selecting recipe: " + randomRecipe);
        Navigation.getMeToRecipeInfo(randomRecipe);

        boolean multiPaneMode = cookBookApplication.getResources().getBoolean(R.bool.multiPaneMode);

        if (!multiPaneMode) {
            Intents.init();
            Navigation.selectRecipeStep(1);

            intended(hasComponent(RecipeStepDetailActivity.class.getName()));
            intended(hasExtraWithKey(Config.RECIPE_KEY));
            intended(hasExtraWithKey(Config.RECIPE_STEP_KEY));
            Intents.release();

            onView(withId(R.id.recipe_step_tab_layout))
                    .check(matches(isCompletelyDisplayed()));
        } else {
            Navigation.selectRecipeStep(1);

            onView(withId(R.id.exo_player_view))
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void checkAddWidgetButtonAddsRecipeWidget() {
        cookBookApplication.getSharedPreferences(RecipeUtils.SHARED_PREFS_KEY, Context.MODE_PRIVATE).edit()
                .clear()
                .commit();

        Navigation.getMeToRecipeInfo(0);

        onView(withId(R.id.action_add_to_widget))
                .check(matches(isDisplayed()))
                .perform(click());

        Recipe recipe = RecipeUtils.loadRecipe(cookBookApplication);

        assertNotNull(recipe);
    }

}
