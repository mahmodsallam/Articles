package mahmoudmostafa.com.articles.activities;

import android.app.VoiceInteractor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mahmoudmostafa.com.articles.R;
import mahmoudmostafa.com.articles.adapters.ArticlesAdapter;
import mahmoudmostafa.com.articles.constants.Constant;
import mahmoudmostafa.com.articles.model.Article;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //views in the layout
    RecyclerView articlesRecyclerView;
    ArticlesAdapter adapter;
    ArrayList<Article> articlesList;

    //the databse reference
    private DatabaseReference articlesDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //nav drawer working
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //finding views
        articlesRecyclerView = findViewById(R.id.articles_recycler);

        //related to firebase
        articlesDatabase = FirebaseDatabase.getInstance().getReference().child("article");/*child("-L6YPv3k6g--KWEhOSfT");*/

        //checking whether there is some thing in the on save instance state or not
        if (savedInstanceState != null) {
            articlesList = savedInstanceState.getParcelableArrayList("articlesList");
            adapter = new ArticlesAdapter(articlesList, this);
            articlesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            articlesRecyclerView.setItemAnimator(new DefaultItemAnimator());
            articlesRecyclerView.setAdapter(adapter);
        } else {
            articlesList = new ArrayList<>();
            articlesList = getArticlesList(Constant.EVERY_THING);
            adapter = new ArticlesAdapter(articlesList, this);
            articlesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            articlesRecyclerView.setItemAnimator(new DefaultItemAnimator());
            articlesRecyclerView.setAdapter(adapter);
        }

    }

    //salving and restoring the state while rotation
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("articlesList", articlesList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bookMarks) {

            //we have to retrieve data frm the fire base here

            articlesList.clear();
            articlesDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                        String author = postSnapshot.child("author").getValue().toString();
                        String desc = postSnapshot.child("description").getValue().toString();
                        String date = postSnapshot.child("publishedAt").getValue().toString();
                        String title = postSnapshot.child("title").getValue().toString();
                        String url = postSnapshot.child("url").getValue().toString();
                        String image = postSnapshot.child("urlToImage").getValue().toString();

                        articlesList.add(new Article(author, title, desc, url, image, date, ""));
                        adapter.notifyDataSetChanged();


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@SuppressWarnings("NullableProblems") MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.bbc_news) {
            articlesList.clear();
//            articlesList = getArticlesList(Constant.BBC_NEWS);
            //calling my async task
            new Task().execute() ;

        } else if (id == R.id.bbc_sport) {
            articlesList.clear();
            articlesList = getArticlesList(Constant.BBC_SPORT);

        } else if (id == R.id.cnn) {
            articlesList.clear();
            articlesList = getArticlesList(Constant.CNN);

        } else if (id == R.id.google) {
            articlesList.clear();
            articlesList = getArticlesList(Constant.GOOGLE);

        } else if (id == R.id.NAG) {
            articlesList.clear();
            articlesList = getArticlesList(Constant.NATIONAL_GEOGRAPHIC);

        } else if (id == R.id.sky_news) {
            articlesList.clear();
            articlesList = getArticlesList(Constant.SKY_NEWS);

        } else if (id == R.id.reddit) {
            articlesList.clear();
            articlesList = getArticlesList(Constant.REDDIT);
        } else if (id == R.id.cnb) {
            articlesList.clear();
            articlesList = getArticlesList(Constant.CNB);
        } else if (id == R.id.entertainment) {
            articlesList.clear();
            articlesList = getArticlesList(Constant.ENTERTAINMENT);
        } else if (id == R.id.NewScientist) {
            articlesList.clear();
            articlesList = getArticlesList(Constant.NEW_SCIENTIST);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //method to get articles list
    public ArrayList<Article> getArticlesList(String link) {


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
                        articlesList.add(new Article(author, title, description, url,
                                urlToImage, publishedAt, ""));

                        adapter.notifyDataSetChanged();
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        return articlesList;

    }




    //using an async task for showing some things
    class Task extends AsyncTask<String , Void , ArrayList<Article> >
    {

        @Override
        protected ArrayList<Article> doInBackground(String... strings) {

            //making a reques to pull data from the server
            StringRequest stringRequest = new StringRequest(Constant.BBC_NEWS,
                    new Response.Listener<String>() {
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
                            articlesList.add(new Article(author, title, description, url,
                                    urlToImage, publishedAt, ""));

                            adapter.notifyDataSetChanged();
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

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

            return  articlesList ;

        }

        @Override
        protected void onPostExecute(ArrayList<Article> articles) {
            adapter = new ArticlesAdapter(articles , getApplicationContext()) ;
            articlesRecyclerView.setAdapter(adapter);
            super.onPostExecute(articles);
        }
    }

}
