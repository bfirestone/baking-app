package com.bfirestone.udacity.cookbook.api;

public interface RecipesApiCallback<T> {
    void onResponse(T result);

    void onCancel();
}
