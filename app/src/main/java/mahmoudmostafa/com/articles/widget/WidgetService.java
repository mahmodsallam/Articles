package mahmoudmostafa.com.articles.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

/**
 * Created by Mahmoud Sallam on 2/25/2018.
 */

public class WidgetService extends RemoteViewsService {

    private ArrayList<String> newsPapers;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        int appWidgetId = intent.getIntExtra
                (
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID

                );



        return (new ListProvider(this.getApplicationContext(), intent, newsPapers  ));

    }
}
