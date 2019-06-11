package nitish.build.com.saavntest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Downloads_Page extends AppCompatActivity {
     ListView lv_queueList;
     ArrayAdapter<String> arrayAdapter;
    static ProgressBar pb_downPage;
    static TextView tv_info,tv_DownSong,tv_prog;
    static ArrayList<String> songList;
    AdView mAdView;


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Search_Songs.class));
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads__page);

        lv_queueList=findViewById(R.id.list_inQueue);
        tv_info=findViewById(R.id.tv_downInfo);


        mAdView = findViewById(R.id.adView_Downloads);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        songList=new ArrayList<>();



//        pb_downPage.setProgressTintList(ColorStateList.valueOf(Color.WHITE));
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                songList ){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

                /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.WHITE);

                return view;
            }
        };






    }

    public void btmSrch(View v){
        //------Animation-----------//
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        //-------------------------//
        startActivity(new Intent(getApplicationContext(),Search_Songs.class));
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
    }
    public void btmBrws(View v){
        //------Animation-----------//
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        //-------------------------//
        startActivity(new Intent(getApplicationContext(),SaavnWebView.class));
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
    }
    public void btmDown(View v){
        //------Animation-----------//
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        //-------------------------//
//        startActivity(new Intent(getApplicationContext(),Downloads_Page.class));
    }
    public void btmMore(View v){
        //------Animation-----------//
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        //-------------------------//
        startActivity(new Intent(getApplicationContext(),MorePage.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
