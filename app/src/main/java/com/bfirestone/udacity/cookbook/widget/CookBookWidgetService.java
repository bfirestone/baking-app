package com.bfirestone.udacity.cookbook.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.bfirestone.udacity.cookbook.utils.RecipeUtils;
import com.bfirestone.udacity.cookbook.models.Recipe;

public class CookBookWidgetService extends RemoteViewsService {

    public static void updateWidget(Context context, Recipe recipe) {
        RecipeUtils.saveRecipe(context, recipe);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, CookBookWidget.class));
        CookBookWidget.updateCookBookWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        return new CookBookRemoteViewsFactory(getApplicationContext());
    }

}