package com.bfirestone.udacity.cookbook.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.bfirestone.udacity.cookbook.R;
import com.bfirestone.udacity.cookbook.utils.RecipeUtils;
import com.bfirestone.udacity.cookbook.models.Recipe;
import com.bfirestone.udacity.cookbook.ui.activities.MainActivity;

/**
 * Implementation of CookBook App Widget
 * TODO: Possibly refactor this to be a bit more generic
 */
public class CookBookWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {

        Recipe recipe = RecipeUtils.loadRecipe(context);
        if (recipe != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipes_widget);

            remoteViews.setTextViewText(R.id.recipe_widget_name_text, recipe.getName());
            remoteViews.setOnClickPendingIntent(R.id.recipe_widget_name_text, pendingIntent);

            Intent intent = new Intent(context, CookBookWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            remoteViews.setRemoteAdapter(R.id.recipe_widget_listview, intent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipe_widget_listview);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateCookBookWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {}

    @Override
    public void onDisabled(Context context) {}
}

