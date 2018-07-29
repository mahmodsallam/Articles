package mahmoudmostafa.com.articles.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import mahmoudmostafa.com.articles.R;
import mahmoudmostafa.com.articles.model.Article;
import mahmoudmostafa.com.articles.utils.Analytics;

public class DetailsActivity extends AppCompatActivity {

    ImageView back;
    TextView title, date, describtion;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        describtion = findViewById(R.id.details);

        Article article = getIntent().getParcelableExtra("article");

        Glide.with(this).load(article.getUrlToImage()).into(back);
        title.setText(article.getTitle());
        date.setText(article.getPublishedAt());
        describtion.setText(article.getDescription());

//google admob
        mAdView = findViewById(R.id.adView);
        //        mAdView.setAdSize(AdSize.BANNER);
//        mAdView.setAdUnitId(getString(R.string.banner_home_footer));

//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                // Check the LogCat to get your test device ID
//                .addTestDevice("C04B1BFFB0774708339BC273F8A43708")
//                .build();

        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);



        //google analytics
        try
        {
            Tracker t = ((Analytics) getApplication()).getTracker(
                    Analytics.TrackerName.APP_TRACKER);

            t.setScreenName("MyScreenName");

            t.send(new HitBuilders.AppViewBuilder().build());
        }
        catch(Exception  e)
        {
//            Toast.makeText(getApplicationContext(), "Error"+e.getMessage(), 1).show();
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
