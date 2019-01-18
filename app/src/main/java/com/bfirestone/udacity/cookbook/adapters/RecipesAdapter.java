package com.bfirestone.udacity.cookbook.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bfirestone.udacity.cookbook.R;
import com.bfirestone.udacity.cookbook.holders.RecipeViewHolder;
import com.bfirestone.udacity.cookbook.models.Recipe;
import com.bfirestone.udacity.cookbook.ui.Listeners;
import com.bfirestone.udacity.cookbook.utils.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    private Context mContext;
    private List<Recipe> mRecipeList;
    private Listeners.OnItemClickListener mOnItemClickListener;

    public RecipesAdapter(Context context, List<Recipe> recipeList,
                          Listeners.OnItemClickListener onItemClickListener) {

        mContext = context;
        mRecipeList = recipeList;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, final int position) {
        holder.mTvRecipeName.setText(mRecipeList.get(position).getName());
        holder.mTvServings.setText(mContext.getString(R.string.servings, mRecipeList.get(position).getServings()));

        String recipeImage = mRecipeList.get(position).getImage();
        if (!recipeImage.isEmpty()) {
            GlideApp.with(mContext)
                    .load(recipeImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.icon_fork_knife_80)
                    .into(holder.mIvRecipe);
        }

        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        if (mRecipeList != null) {
            return mRecipeList.size();
        }
        return 0;
    }
}
