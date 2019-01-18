package com.bfirestone.udacity.cookbook.api;

import com.bfirestone.udacity.cookbook.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface RecipesApiService {

    @GET("android-baking-app-json")
    Call<List<Recipe>> getRecipes();

}
