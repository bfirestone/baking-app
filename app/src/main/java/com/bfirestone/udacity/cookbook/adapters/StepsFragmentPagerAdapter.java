package com.bfirestone.udacity.cookbook.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bfirestone.udacity.cookbook.Config;
import com.bfirestone.udacity.cookbook.R;
import com.bfirestone.udacity.cookbook.models.Step;
import com.bfirestone.udacity.cookbook.ui.fragments.RecipeStepDetailFragment;

import java.util.List;

public class StepsFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<Step> mSteps;

    public StepsFragmentPagerAdapter(Context context, List<Step> steps,
                                     FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mContext = context;
        this.mSteps = steps;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(Config.RECIPE_STEP_KEY, mSteps.get(position));
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return String.format(mContext.getString(R.string.step_num_text), position + 1);
    }

    @Override
    public int getCount() {
        return mSteps.size();
    }


}
