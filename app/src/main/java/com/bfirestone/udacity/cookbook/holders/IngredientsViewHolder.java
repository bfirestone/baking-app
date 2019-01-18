package com.bfirestone.udacity.cookbook.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bfirestone.udacity.cookbook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsViewHolder extends RecyclerView.ViewHolder {
//    @BindView(R.id.tv_ingredient_quantity)
//    TextView mTvIngredientQuantity;
//
//    @BindView(R.id.tv_ingredient_measurement)
//    TextView mTvIngredientMeasurement;
//
//    @BindView(R.id.tv_ingredient_name)
//    TextView mTvIngredientName;
//
//    @BindView(R.id.lv_ingredient_list)
//    public ListView mIngredientListView;

    @BindView(R.id.ingredient_list_text)
    public TextView mIngredientsList;

    public IngredientsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
