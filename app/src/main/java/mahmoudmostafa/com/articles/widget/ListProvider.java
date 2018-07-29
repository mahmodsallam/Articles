package mahmoudmostafa.com.articles.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mahmoudmostafa.com.articles.R;
import mahmoudmostafa.com.articles.constants.Constant;

/**
 * Created by Mahmoud Sallam on 2/25/2018.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<String> newsPaper;
    private Context context;
    private int appWidgetId;
    AppWidgetManager manager;

    public ListProvider(Context context, Intent intent, ArrayList<String> newsPaper ) {
        this.context = context;
        this.newsPaper = newsPaper;


        manager = (AppWidgetManager) context.getSystemService(Context.APPWIDGET_SERVICE);


        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);




    }


    @Override
    public void onCreate() {
        newsPaper = new ArrayList<>();

        newsPaper.add("BBC News");

        newsPaper = getArticlesList(Constant.BBC_NEWS);
    }


    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return newsPaper.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        String news = newsPaper.get(position);

        //fill the fields of the row
        remoteView.setTextViewText(R.id.newPaper, news);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    //method to get articles list
    public ArrayList<String> getArticlesList(String link) {

        StringRequest stringRequest = new StringRequest(link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject object = new JSONObject(response);
                    JSONArray articles = object.getJSONArray("articles");

                    for (int i = 0; i < articles.length(); i++) {

                        JSONObject article = articles.getJSONObject(i);
                        String author = article.getString("author");
                        String title = article.getString("title");
                        String description = article.getString("description");
                        String url = article.getString("url");
                        String urlToImage = article.getString("urlToImage");
                        String publishedAt = article.getString("publishedAt");
                        newsPaper.add(title.toString());

                        try{
                        manager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listView);}catch (Exception e ){}


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        return newsPaper;

    }


}
