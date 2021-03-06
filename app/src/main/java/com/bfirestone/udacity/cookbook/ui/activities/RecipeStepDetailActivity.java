package com.bfirestone.udacity.cookbook.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bfirestone.udacity.cookbook.Config;
import com.bfirestone.udacity.cookbook.R;
import com.bfirestone.udacity.cookbook.adapters.StepsFragmentPagerAdapter;
import com.bfirestone.udacity.cookbook.models.Recipe;
import com.bfirestone.udacity.cookbook.utils.SnackbarUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single RecipeStep detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeInfoActivity}.
 */
public class RecipeStepDetailActivity extends AppCompatActivity {
    @BindView(R.id.recipe_step_tab_layout)
    TabLayout mTlRecipeStep;
    @BindView(R.id.recipe_step_viewpager)
    ViewPager mVpRecipeStep;
    @BindView(android.R.id.content)
    View mParentLayout;

    private Recipe mRecipe;
    private int mStepSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Config.RECIPE_KEY) && bundle.containsKey(Config.RECIPE_STEP_KEY)) {
            mRecipe = bundle.getParcelable(Config.RECIPE_KEY);
            mStepSelectedPosition = bundle.getInt(Config.RECIPE_STEP_KEY);
        } else {
            SnackbarUtils.makeSnackBar(this, mParentLayout, getString(R.string.failed_to_load_recipe), true);
            finish();
        }

        // Show the Up button in the action bar.
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mRecipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        StepsFragmentPagerAdapter adapter = new StepsFragmentPagerAdapter(getApplicationContext(), mRecipe.getSteps(), getSupportFragmentManager());
        mVpRecipeStep.setAdapter(adapter);
        mTlRecipeStep.setupWithViewPager(mVpRecipeStep);
        mVpRecipeStep.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (actionBar != null) {
                    actionBar.setTitle(mRecipe.getSteps().get(position).getShortDescription());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mVpRecipeStep.setCurrentItem(mStepSelectedPosition);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy");
    }

}
