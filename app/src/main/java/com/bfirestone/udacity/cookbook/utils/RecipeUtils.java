package com.bfirestone.udacity.cookbook.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.bfirestone.udacity.cookbook.R;
import com.bfirestone.udacity.cookbook.models.Recipe;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;


public class RecipeUtils {
    public static final String SHARED_PREFS_KEY = "recipe";

    public static void saveRecipe(Context context, Recipe recipe) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Recipe> jsonAdapter = moshi.adapter(Recipe.class);
        String recipeJson = jsonAdapter.toJson(recipe);

        SharedPreferences.Editor prefs = context.getSharedPreferences(SHARED_PREFS_KEY,
                Context.MODE_PRIVATE).edit();

        prefs.putString(context.getString(R.string.widget_recipe_key), recipeJson);
        prefs.apply();
    }

    public static Recipe loadRecipe(Context context) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Recipe> jsonAdapter = moshi.adapter(Recipe.class);

        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        String recipeJson = prefs.getString(context.getString(R.string.widget_recipe_key), "");

        try {
            if (recipeJson != null) {
                return jsonAdapter.fromJson(recipeJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
