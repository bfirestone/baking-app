package com.bfirestone.udacity.cookbook.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bfirestone.udacity.cookbook.R;
import com.bfirestone.udacity.cookbook.holders.IngredientsViewHolder;
import com.bfirestone.udacity.cookbook.holders.StepViewHolder;
import com.bfirestone.udacity.cookbook.models.Ingredient;
import com.bfirestone.udacity.cookbook.models.Recipe;
import com.bfirestone.udacity.cookbook.ui.Listeners;

import java.util.Iterator;
import java.util.Locale;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Recipe mRecipe;
    private Listeners.OnItemClickListener mOnItemClickListener;

    public RecipeAdapter(Recipe recipe, Listeners.OnItemClickListener onItemClickListener) {
        this.mRecipe = recipe;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new IngredientsViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_ingredient_list_item, parent, false));
        } else {
            return new StepViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_step_list_item, parent, false));
        }
    }

    // TODO: this would be nice to have as a listview or something similar rather than a string
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof IngredientsViewHolder) {
            IngredientsViewHolder viewHolder = (IngredientsViewHolder) holder;

            StringBuilder ingredientList = new StringBuilder();
            Iterator<Ingredient> iterator = mRecipe.getIngredients().iterator();
            while (iterator.hasNext()) {
                Ingredient ingredient = iterator.next();
                ingredientList.append(String.format(
                        Locale.getDefault(), "• %s %s - %s", ingredient.getQuantity(), ingredient.getMeasurement(), ingredient.getName()));
                if (iterator.hasNext()) {
                    ingredientList.append("\n");
                }
            }

            viewHolder.mIngredientsList.setText(ingredientList.toString());
        } else if (holder instanceof StepViewHolder) {
            String stepOrderText = String.format(Locale.getDefault(), "%d.", position);
            StepViewHolder viewHolder = (StepViewHolder) holder;
            viewHolder.mTvStepOrder.setText(stepOrderText);
            viewHolder.mTvStepName.setText(mRecipe.getSteps().get(position - 1).getShortDescription());

            holder.itemView.setOnClickListener(v -> {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(position - 1);
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return mRecipe.getSteps().size() + 1;
    }
}
