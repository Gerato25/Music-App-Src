package nitish.build.com.saavntest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
    static ArrayAdapter<String> arrayAdapter;
    static ProgressBar pb_downPage;
    static TextView tv_info,tv_DownSong,tv_prog;
    static ArrayList<String> songList;
    AdView mAdView;


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Search_Songs.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads__page);

        lv_queueList=findViewById(R.id.list_inQueue);
        pb_downPage=findViewById(R.id.pb_DownPage);
        tv_prog=findViewById(R.id.tv_curPro);
        tv_DownSong=findViewById(R.id.tv_curSongN);
        tv_info=findViewById(R.id.tv_downInfo);
        lv_queueList.setVisibility(View.GONE);

        mAdView = findViewById(R.id.adView_Downloads);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//        tv_DownSong.setText(Album_Song_List.nowDownS);
//        SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main),MODE_PRIVATE);
//        songList=new ArrayList<>();
//        try {
//            String songsL = pref_main.getString(getResources().getString(R.string.pref_song_list),ObjectSerializer.serialize(new ArrayList<>()));
//            songList =(ArrayList<String>)ObjectSerializer.deserialize(songsL);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (songList.size()>0){
//            lv_queueList.setVisibility(View.VISIBLE);
//             arrayAdapter = new ArrayAdapter<String>(
//                    this,
//                    android.R.layout.simple_list_item_1,
//                    songList ){
//
//                 @Override
//                 public View getView(int position, View convertView, ViewGroup parent) {
//                     View view =super.getView(position, convertView, parent);
//
//                     TextView textView=(TextView) view.findViewById(android.R.id.text1);
//
//                     /*YOUR CHOICE OF COLOR*/
//                     textView.setTextColor(Color.WHITE);
//
//                     return view;
//                 }
//             };
//
//            lv_queueList.setAdapter(arrayAdapter);
//        }
//        pb_downPage.setProgressTintList(ColorStateList.valueOf(Color.WHITE));


//        Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                tv_DownSong.setText(Album_Song_List.nowDownS);
//                try {
//                    String songsL = pref_main.getString(getResources().getString(R.string.pref_song_list),ObjectSerializer.serialize(new ArrayList<>()));
//                    songList =(ArrayList<String>)ObjectSerializer.deserialize(songsL);
//                    arrayAdapter.notifyDataSetChanged();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 0, 5000);



    }

    public void btmSrch(View v){
        startActivity(new Intent(getApplicationContext(),Search_Songs.class));
    }
    public void btmBrws(View v){
        startActivity(new Intent(getApplicationContext(),SaavnWebView.class));
    }
    public void btmDown(View v){
//        startActivity(new Intent(getApplicationContext(),Downloads_Page.class));
    }
    public void btmMore(View v){
        startActivity(new Intent(getApplicationContext(),MorePage.class));
    }
}
