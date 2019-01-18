package com.bfirestone.udacity.cookbook.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bfirestone.udacity.cookbook.Config;
import com.bfirestone.udacity.cookbook.CookBookApplication;
import com.bfirestone.udacity.cookbook.R;
import com.bfirestone.udacity.cookbook.adapters.RecipesAdapter;
import com.bfirestone.udacity.cookbook.api.RecipesApiCallback;
import com.bfirestone.udacity.cookbook.api.RecipesApiManager;
import com.bfirestone.udacity.cookbook.models.Recipe;
import com.bfirestone.udacity.cookbook.utils.NetworkChangeReceiver;
import com.bfirestone.udacity.cookbook.utils.RecipeUtils;
import com.bfirestone.udacity.cookbook.utils.SnackbarUtils;
import com.bfirestone.udacity.cookbook.utils.SpacingItemDecoration;
import com.bfirestone.udacity.cookbook.widget.CookBookWidgetService;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipesFragment extends Fragment {

    @BindView(R.id.recipes_recycler_view)
    RecyclerView mRecipesRecyclerView;

    @BindView(R.id.pull_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.noDataContainer)
    ConstraintLayout mNoDataContainer;

    private OnRecipeClickListener mClickListener;
    private Unbinder unbinder;
    private List<Recipe> mRecipeList;
    private CookBookApplication cookBookApplication;

    /**
     * Will load the recipes on launch or if the network state changes post launch
     */
    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mRecipeList == null) {
                loadRecipes();
            }
        }
    };

    public RecipesFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.recipes_fragment, container, false);
        unbinder = ButterKnife.bind(this, viewRoot);

        Logger.d("fetching recipes");
        mRefreshLayout.setOnRefreshListener(this::loadRecipes);
        Logger.d("loaded recipes");

        mNoDataContainer.setVisibility(View.VISIBLE);
        setupRecyclerView();

        // IdlingResource for Testing
        if (getActivity() != null) {
            cookBookApplication = (CookBookApplication) getActivity().getApplicationContext();
            cookBookApplication.setIdleState(false);


            if (savedInstanceState != null && savedInstanceState.containsKey(Config.SAVED_RECIPES_KEY)) {
                mRecipeList = savedInstanceState.getParcelableArrayList(Config.SAVED_RECIPES_KEY);

                mRecipesRecyclerView.setAdapter(
                        new RecipesAdapter(getActivity().getApplicationContext(), mRecipeList,
                        position -> mClickListener.onRecipeSelected(mRecipeList.get(position))));
                setDataVisibility();
            }
        }

        loadRecipes();

        return viewRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeClickListener) {
            mClickListener = (OnRecipeClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mClickListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Logger.d("onDestroyView");
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetworkChangeReceiver.NETWORK_CHANGE_ACTION);

        if (getActivity() != null) {
            getActivity().registerReceiver(networkChangeReceiver, intentFilter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (getActivity() != null) {
            getActivity().unregisterReceiver(networkChangeReceiver);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mRecipeList != null && !mRecipeList.isEmpty())
            outState.putParcelableArrayList(Config.SAVED_RECIPES_KEY, (ArrayList<? extends Parcelable>) mRecipeList);
    }

    private void setupRecyclerView() {
        mRecipesRecyclerView.setVisibility(View.GONE);
        mRecipesRecyclerView.setHasFixedSize(true);

        if (getActivity() != null ) {
            Context context = getActivity().getBaseContext();

            boolean multiPaneMode = getResources().getBoolean(R.bool.multiPaneMode);
            if (multiPaneMode) {
                mRecipesRecyclerView.setLayoutManager(new GridLayoutManager(
                        context.getApplicationContext(), 3));
            } else {
                mRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(
                        context.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            }

            mRecipesRecyclerView.addItemDecoration(new SpacingItemDecoration((int) getResources().getDimension(R.dimen.margin_medium)));
            mRecipesRecyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
        }
    }

    private void loadRecipes() {
        Logger.d("loading recipes");
        if (getActivity() != null ) {
            if (SnackbarUtils.isNetworkAvailable(getActivity().getApplicationContext())) {
                mRefreshLayout.setRefreshing(true);

                RecipesApiManager.getInstance().getRecipes(new RecipesApiCallback<List<Recipe>>() {
                    @Override
                    public void onResponse(final List<Recipe> result) {
                        if (result != null && getActivity() != null) {
//                            result.forEach(recipe -> Logger.d("recipe: " + recipe));

                            mRecipeList = result;

                            RecipesAdapter recipesAdapter = new RecipesAdapter(
                                    getActivity().getApplicationContext(),
                                    mRecipeList, position -> mClickListener.onRecipeSelected(mRecipeList.get(position)));

                            mRecipesRecyclerView.setAdapter(recipesAdapter);

                            if (RecipeUtils.loadRecipe(getActivity().getApplicationContext()) == null) {
                                CookBookWidgetService.updateWidget(getActivity(), mRecipeList.get(0));
                            }

                        } else {
                            Logger.e(getString(R.string.failed_to_load_data));
                            SnackbarUtils.makeSnackBar(getActivity(), getView(), getString(R.string.failed_to_load_data), true);
                        }

                        setDataVisibility();
                    }

                    @Override
                    public void onCancel() {
                        setDataVisibility();
                    }

                });
            } else {
                SnackbarUtils.makeSnackBar(getActivity(), getView(), getString(R.string.internet_offline), true);
            }
        }
    }


    /**
     * show/hide Recipes RecyclerView & NoDataContainer data state
     */
    private void setDataVisibility() {
        boolean loaded = mRecipeList != null && mRecipeList.size() > 0;
        Logger.d("loaded: " + loaded);
        mRefreshLayout.setRefreshing(false);

        mRecipesRecyclerView.setVisibility(loaded ? View.VISIBLE : View.GONE);
        mNoDataContainer.setVisibility(loaded ? View.GONE : View.VISIBLE);

        cookBookApplication.setIdleState(true);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRecipeClickListener {
        void onRecipeSelected(Recipe recipe);
    }
}
