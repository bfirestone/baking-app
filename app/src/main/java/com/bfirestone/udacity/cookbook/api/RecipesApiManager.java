package com.bfirestone.udacity.cookbook.api;

import android.support.annotation.NonNull;

import com.bfirestone.udacity.cookbook.Config;
import com.bfirestone.udacity.cookbook.models.Recipe;
import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public final class RecipesApiManager implements Serializable {

    private static volatile RecipesApiManager sharedInstance = new RecipesApiManager();
    private RecipesApiService recipesApiService;

    private RecipesApiManager() {
        if (sharedInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        // setup client to follow redirect
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .followRedirects(true)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.RECIPES_API_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        recipesApiService = retrofit.create(RecipesApiService.class);
    }

    public static RecipesApiManager getInstance() {
        if (sharedInstance == null) {
            synchronized (RecipesApiManager.class) {
                if (sharedInstance == null) sharedInstance = new RecipesApiManager();
            }
        }

        return sharedInstance;
    }

    public void getRecipes(final RecipesApiCallback<List<Recipe>> recipesApiCallback) {
        recipesApiService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call,
                                   @NonNull Response<List<Recipe>> response) {

                recipesApiCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<Recipe>> call,
                    @NonNull Throwable exception) {

                if (call.isCanceled()) {
                    Logger.e("Request was cancelled");
                    recipesApiCallback.onCancel();
                } else {
                    Logger.e(exception.getMessage());
                    recipesApiCallback.onResponse(null);
                }
            }
        });
    }

}

