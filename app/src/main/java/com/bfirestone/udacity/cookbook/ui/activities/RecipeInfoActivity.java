package com.bfirestone.udacity.cookbook.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bfirestone.udacity.cookbook.Config;
import com.bfirestone.udacity.cookbook.R;
import com.bfirestone.udacity.cookbook.adapters.RecipeAdapter;
import com.bfirestone.udacity.cookbook.models.Recipe;
import com.bfirestone.udacity.cookbook.ui.fragments.RecipeStepDetailFragment;
import com.bfirestone.udacity.cookbook.utils.SnackbarUtils;
import com.bfirestone.udacity.cookbook.utils.SpacingItemDecoration;
import com.bfirestone.udacity.cookbook.widget.CookBookWidgetService;
import com.orhanobut.logger.Logger;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeInfoActivity extends AppCompatActivity {

    @BindView(R.id.recipe_step_list)
    RecyclerView mRecyclerView;

    @BindView(android.R.id.content)
    View mParentLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbarView;

    @BindString(R.string.failed_to_load_recipe)
    String mFailedRecipeLoadText;

    private boolean isMultiPane;

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Config.RECIPE_KEY)) {
            mRecipe = bundle.getParcelable(Config.RECIPE_KEY);
        } else {
            SnackbarUtils.makeSnackBar(this, mParentLayout, mFailedRecipeLoadText, true);
            finish();
        }

        setContentView(R.layout.activity_recipe_info);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbarView);

        // Show the Up button in the action bar and set recipes name as title.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mRecipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        isMultiPane = getResources().getBoolean(R.bool.multiPaneMode);
        if (isMultiPane) {
            if (savedInstanceState == null && !mRecipe.getSteps().isEmpty()) {
                showStep(0);
            }
        }

        setupRecyclerView();
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

    private void setupRecyclerView() {
        mRecyclerView.addItemDecoration(new SpacingItemDecoration((int) getResources().getDimension(R.dimen.margin_medium)));
        mRecyclerView.setAdapter(new RecipeAdapter(mRecipe, this::showStep));
    }

    private void showStep(int position) {
        if (isMultiPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(Config.RECIPE_STEP_KEY, mRecipe.getSteps().get(position));
            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra(Config.RECIPE_KEY, mRecipe);
            intent.putExtra(Config.RECIPE_STEP_KEY, position);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_to_widget) {
            CookBookWidgetService.updateWidget(this, mRecipe);
            SnackbarUtils.makeSnackBar(this, mParentLayout, String.format(getString(R.string.added_to_widget), mRecipe.getName()), false);

            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
