package nitish.build.com.saavntest1;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;
import com.tonyodev.fetch2core.Extras;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Album_Song_List extends AppCompatActivity {
    String albumID,jsonData,finUrl,downUrl,downpath,fName,folderName="RandomAlbum",albumArtUrl="",
            chanelId="test1",dataType,dot=" • ",url_img="FAILED",prevAct="",tempSJson="",tempID;
    static String nowDownS="";
    JSONArray songArr ;
    JSONObject songObj;
    int listSize=1,notificationID=100;
    Float totMB,curMB;

    TextView tv_ALPLname,tv_artists,tv_TypeTot,tv_DownCount;
    ImageView img_album;
    DecimalFormat f = new DecimalFormat("##.00");
    RadioGroup kbpsGroup;
    RadioButton rad_320,rad_128,rad_96;
    NotificationCompat.Builder mBuilder;
    NotificationManagerCompat notificationManagerCompat;
    Button btn_toDownPage;
    AdView mAdView;
    InterstitialAd mInterstitialAd;
    Artwork artwork;
    Fetch fetch;


    @Override
    public void onBackPressed() {

        if (prevAct.equals("WEB_ACT"))
            startActivity(new Intent(getApplicationContext(),SaavnWebView.class));
        else if (prevAct.equals("SEARCH_ACT"))
            startActivity(new Intent(getApplicationContext(),Search_Songs.class));
        else
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album__song__list);

        mAdView = findViewById(R.id.adView_Album);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main),MODE_PRIVATE);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            @Override
            public void onAdLoaded() {
                int tempCount=pref_main.getInt(getResources().getString(R.string.pref_counter),0);
                if (tempCount>=5){
                    tempCount=0;
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                    //Toast.makeText(getApplicationContext(), "Add...", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences.Editor editor=pref_main.edit();
                editor.putInt(getResources().getString(R.string.pref_counter),tempCount).apply();
            }
        });




        Button btn_back=findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent fromHoome = getIntent();
        albumID = fromHoome.getStringExtra("TYPE_ID");
        dataType=fromHoome.getStringExtra("TYPE");
        prevAct=fromHoome.getStringExtra("PREV_ACT");


        tv_ALPLname=findViewById(R.id.tv_ALPLname);
        tv_artists=findViewById(R.id.tv_artists);
        tv_TypeTot=findViewById(R.id.tv_typetot);
        img_album=findViewById(R.id.img_album);
        kbpsGroup=findViewById(R.id.radioGroup);
        rad_96=findViewById(R.id.radio_96);
        rad_128=findViewById(R.id.radio_160);
        rad_320=findViewById(R.id.radio_320);
        tv_DownCount=findViewById(R.id.tv_totDowns);
        btn_toDownPage=findViewById(R.id.btn_down_alb);

//        SharedPreferences pre_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main),MODE_PRIVATE);
//        int inQ=pre_main.getInt(getResources().getString(R.string.pref_inqueue),0);
//        tv_DownCount.setText(inQ+"");

        btn_toDownPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),Downloads_Page.class));
            }
        });






        if (dataType.equals("FAILED")){
            new AlertDialog.Builder(Album_Song_List.this)
                    .setTitle("No Album or Playlist Found")
                    .setMessage("Please use another way to find the Song you want to downoad..!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();
            jsonData=getResources().getString(R.string.FailedJson);
            dataType="ALBUM";
        }


        if (dataType.equals("SONG"))
            dataType="ALBUM";

        if (dataType.equals("ALBUM"))
            jsonData=DataHandlers.getAlbumJson(albumID);
        else if (dataType.equals("PLAYLIST"))
            jsonData=DataHandlers.getPlaylistJson(albumID);



        try {
            JSONObject albumJson = new JSONObject(jsonData);

            if (dataType.equals("ALBUM")){
                tv_artists.setText(albumJson.getString("primary_artists"));
                tv_ALPLname.setText(albumJson.getString("title"));
            }
            else{
                tv_artists.setText(albumJson.getString("follower_count")+" followers");
                tv_ALPLname.setText(albumJson.getString("listname"));
            }
            url_img=albumJson.getString("image");
            if(url_img.length()<=5)
                url_img="FAILED";
            songArr = new JSONArray(albumJson.getString("songs"));

            listSize = songArr.length();
            if (albumJson.getString("title").equals("FAILED"))
                listSize=0;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        tv_TypeTot.setText(dataType+dot+listSize+" Songs");


        PRDownloader.download(url_img, DataHandlers.makeDir(".cache"), tv_ALPLname.getText()+".jpg")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        //Toast.makeText(Album_Song_List.this, "START_IMG", Toast.LENGTH_SHORT).show();
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        File imgFile = new  File(DataHandlers.makeDir(".cache")+"/"+tv_ALPLname.getText()+".jpg");

                        if(imgFile.exists()){
                            //Toast.makeText(Album_Song_List.this, "SET", Toast.LENGTH_SHORT).show();
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            img_album.setImageBitmap(myBitmap);
                        }
                    }

                    @Override
                    public void onError(Error error) {

                    }
                });




        ListView song_list =findViewById(R.id.song_list);
        CustomAdapter song_list_Adapter = new CustomAdapter();
        song_list.setAdapter(song_list_Adapter);

        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);

        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .setDownloadConcurrentLimit(1)
                .build();

        fetch = Fetch.Impl.getInstance(fetchConfiguration);



        File directory = new File(Environment.getExternalStorageDirectory()+ "/FREEMIUM_DOWNLOADS/");
        if (!directory.exists())
            if(directory.mkdirs())
                Log.i("DIRDONE1","1DONE");
            else
                Log.i("DIRDONE1","FAILED");

        createNotificationChannel();
        mBuilder = new NotificationCompat.Builder(getApplicationContext(),chanelId)
                .setSmallIcon(R.drawable.ic_main_app)
                .setContentTitle("Download Name")
                .setContentText("progress")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setProgress(100,0,false);
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());




        song_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject songJsn=null;
                tempID=null;
                String kbps = findViewById(kbpsGroup.getCheckedRadioButtonId()).getTag().toString();

                try {
                    songJsn = songArr.getJSONObject(position);
                    tempSJson=songJsn.toString();

                    SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main),MODE_PRIVATE);
                    SharedPreferences.Editor editor=pref_main.edit();
                    tempID=songJsn.getString("id");
                    editor.putString(getResources().getString(R.string.pref_JsonFromID)+tempID,tempSJson).apply();
                    int tempCount=pref_main.getInt(getResources().getString(R.string.pref_counter),0);
                    tempCount=tempCount+1;
                    if (tempCount>=5){
                        tempCount=0;
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                    }
                    editor.putInt(getResources().getString(R.string.pref_counter),tempCount).apply();

                    folderName = DataHandlers.getAlbumName(songJsn);
                    fName = songJsn.getString("song") + "_" + kbps + ".m4a";
//                    notificationID=DataHandlers.generateNotificationID(songJsn.getString("id"));
                    downUrl = DataHandlers.getDownloadLink(songJsn, kbps);
                    albumArtUrl = songJsn.getString("image").replace("150x150","500x500");


                } catch (Exception e) {
                    e.printStackTrace();
                }
                downpath = DataHandlers.makeDir(folderName);

                if (!(new File(downpath + "/albumArt.jpg")).exists()) {
                    PRDownloader.download(albumArtUrl, downpath, "albumArt.jpg")
                            .build()
                            .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                @Override
                                public void onStartOrResume() {

                                }
                            })
                            .start(new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {

                                }

                                @Override
                                public void onError(Error error) {

                                }
                            });
                }

                if((new File(downpath + "/" + fName)).exists()){
                    new AlertDialog.Builder(Album_Song_List.this)
                            .setTitle("Already Downloaded.!")
                            .setMessage("Do you want to download \""+fName.replace(".m4a","")+"\" again..")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final Request request = new Request(downUrl, downpath + "/" + fName);
                                    request.setPriority(Priority.HIGH);
                                    request.setNetworkType(NetworkType.ALL);
                                    request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");
                                    if (!tempID.equals(null)) {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("ids",tempID);
                                        Log.i("STAG_B",tempID);
                                        request.setExtras(new Extras(map));
                                    }

                                    fetch.enqueue(request, updatedRequest -> {
//                                        SharedPreferences pre_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main),MODE_PRIVATE);
//                                        SharedPreferences.Editor edito=pre_main.edit();
//                                        int inQ=pre_main.getInt(getResources().getString(R.string.pref_inqueue),0);
//                                        inQ=inQ+1;
//                                        tv_DownCount.setText(inQ);
//                                        edito.putInt(getResources().getString(R.string.pref_inqueue),inQ).apply();
                                        //Request was successfully enqueued for download.
                                        Toast.makeText(getApplicationContext(), "Added to Download Queue", Toast.LENGTH_SHORT).show();
                                    }, error -> {
                                        //An error occurred enqueuing the request.
                                        Toast.makeText(getApplicationContext(), "Error! Unable to download song", Toast.LENGTH_SHORT).show();
                                    });
                                }
                            })
                            .setNegativeButton("No",null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setCancelable(false)
                            .show();

                }else{
                    final Request request = new Request(downUrl, downpath + "/" + fName);
                    request.setPriority(Priority.HIGH);
                    request.setNetworkType(NetworkType.ALL);
                    request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");
                    if (!tempID.equals(null)) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("ids",tempID);
                        Log.i("STAG_B",tempID);
                        request.setExtras(new Extras(map));
                    }

                    fetch.enqueue(request, updatedRequest -> {
//                        SharedPreferences pre_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main),MODE_PRIVATE);
//                        SharedPreferences.Editor edito=pre_main.edit();
//                        int inQ=pre_main.getInt(getResources().getString(R.string.pref_inqueue),0);
//                        inQ=inQ+1;
//                        tv_DownCount.setText(inQ);
//                        edito.putInt(getResources().getString(R.string.pref_inqueue),inQ).apply();

                        //Request was successfully enqueued for download.

                        Toast.makeText(getApplicationContext(), "Added to Download Queue", Toast.LENGTH_SHORT).show();
                    }, error -> {

                        //An error occurred enqueuing the request.
                        Toast.makeText(getApplicationContext(), "Error! Unable to download song", Toast.LENGTH_SHORT).show();
                    });

                }



            }
        });

        FetchListener fetchListener = new FetchListener() {
            @Override
            public void onAdded(@NotNull Download download) {

            }

            @Override
            public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

            }

            @Override
            public void onError(@NotNull Download download, @NotNull com.tonyodev.fetch2.Error error, @Nullable Throwable throwable) {

            }

            @Override
            public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {

                String filname = download.getFile();
                filname=filname.substring(filname.lastIndexOf("/")+1);
                filname.replace(".m4a","");
                Log.i("FTCH","st: "+filname);
                mBuilder.setContentTitle(filname).setOnlyAlertOnce(true);
                mBuilder.setContentText("Starting..");
                mBuilder.setProgress(0,0,true);
                notificationManagerCompat.notify(download.getId(),mBuilder.build());

            }

            @Override
            public void onWaitingNetwork(@NotNull Download download) {

            }

            @Override
            public void onQueued(@NotNull Download download, boolean waitingOnNetwork) {

            }

            @Override
            public void onCompleted(@NotNull Download download) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    Extras extras= download.getExtras();
                    Map<String,String> map=extras.getMap();
                    String id=map.get("ids");
                    Log.i("STAG_A",id);
                    SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main), MODE_PRIVATE);
                    String sjson= pref_main.getString(getResources().getString(R.string.pref_JsonFromID)+id,null);
                    if (!sjson.equals(null)) {
                        try {
                            DataHandlers.setTags2(download.getFile(),sjson);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                mBuilder.setContentText("Done");
                mBuilder.setProgress(0,0,false);
                notificationManagerCompat.notify(download.getId(),mBuilder.build());
//                        SharedPreferences pre_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main),MODE_PRIVATE);
//                        SharedPreferences.Editor edito=pre_main.edit();
//                        int inQ=pre_main.getInt(getResources().getString(R.string.pref_inqueue),0);
//                        inQ=inQ-1;
//                        tv_DownCount.setText(inQ+"");
//                        edito.putInt(getResources().getString(R.string.pref_inqueue),inQ).apply();
            }


            @Override
            public void onProgress(@NotNull Download download, long etaInMilliSeconds, long downloadedBytesPerSecond) {
                Log.i("FTCH",download.getProgress()+"--"+etaInMilliSeconds);

                totMB=((float)(download.getTotal()))/1048576;
                curMB=((float)(download.getDownloaded()))/1048576;

                mBuilder.setContentText(f.format(curMB)+"MB/"+f.format(totMB)+"MB");
                mBuilder.setProgress((int)download.getTotal(),(int)download.getDownloaded(),false);

                notificationManagerCompat.notify(download.getId(),mBuilder.build());

            }

            @Override
            public void onPaused(@NotNull Download download) {

            }

            @Override
            public void onResumed(@NotNull Download download) {

            }

            @Override
            public void onCancelled(@NotNull Download download) {

            }

            @Override
            public void onRemoved(@NotNull Download download) {

            }

            @Override
            public void onDeleted(@NotNull Download download) {

            }
        };

        fetch.addListener(fetchListener);



    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return (listSize);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.custom_download_list,null);

            TextView songName= convertView.findViewById(R.id.cus_songName);
            TextView artists = convertView.findViewById(R.id.cus_artist);
            TextView duration = convertView.findViewById(R.id.cus_duration);


            try {
                songObj =songArr.getJSONObject(position);
                songName.setText(songObj.getString("song"));
                artists.setText(songObj.getString("album")+dot+
                        songObj.getString("primary_artists"));
                duration.setText(DataHandlers.getSongDuration(songObj));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }
    }

    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name ="Download Notif";
            String description = "include all download notif";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(chanelId,name,importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
