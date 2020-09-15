package nitish.build.com.freemium.Activities;

//                           ____        _   _ _ _   _     _
//     /\                   |  _ \      | \ | (_) | (_)   | |
//    /  \   _ __  _ __  ___| |_) |_   _|  \| |_| |_ _ ___| |__
//   / /\ \ | '_ \| '_ \/ __|  _ <| | | | . ` | | __| / __| '_ \
//  / ____ \| |_) | |_) \__ \ |_) | |_| | |\  | | |_| \__ \ | | |
// /_/    \_\ .__/| .__/|___/____/ \__, |_| \_|_|\__|_|___/_| |_|
//          | |   | |               __/ |
//          |_|   |_|              |___/
//
//                 Freemium Music
//   Developed and Maintained by Nitish Gadangi

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tonyodev.fetch2okhttp.OkHttpDownloader;
import com.tonyodev.fetch2.DefaultFetchNotificationManager;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.DownloadNotification;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;
import com.tonyodev.fetch2core.Downloader;
import com.tonyodev.fetch2core.Extras;


import org.apache.commons.lang3.StringEscapeUtils;
import org.jaudiotagger.tag.images.Artwork;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nitish.build.com.freemium.Handlers.DataHandlers;
import nitish.build.com.freemium.R;

public class AlbumSongList extends AppCompatActivity {
    String albumID,jsonData,finUrl,downUrl="FAILED",downpath,fName,folderName="RandomAlbum",albumArtUrl="",
            chanelId="test1",dataType,dot=" • ",url_img="FAILED",prevAct="",tempSJson="",tempID,format="m4a",
            albumArt_fname="albumArt.jpg";
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
    static  InterstitialAd mInterstitialAd;
    Artwork artwork;
    static Fetch fetch;
    ImageView btn_settings;
    Intent toCancel;
    PendingIntent cIntent;
    ProgressDialog progressDialog;
    ListView song_list;
    static final String FETCH_NAMESPACE = "SongDownload";
    static BottomSheetLO bottomSheetLO;
    static  String d_dir,d_FN_code;
    static Boolean d_audTagging,d_subFolders;
    String banner2,inter2;
    static Boolean thopu;



    @Override
    public void onBackPressed() {

//        if (prevAct.equals("WEB_ACT"))
//            startActivity(new Intent(getApplicationContext(),SaavnWebView.class));
//        else if (prevAct.equals("SEARCH_ACT"))
//            startActivity(new Intent(getApplicationContext(),SearchSongs.class));
//        else
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

    }



    public class  MainTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();

        }
        @Override
        protected Void doInBackground(Void... voids) {
            if (dataType.equals("ALBUM"))
                jsonData= DataHandlers.getAlbumJson(albumID);
            else if (dataType.equals("PLAYLIST"))
                jsonData=DataHandlers.getPlaylistJson(albumID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONObject albumJson = new JSONObject(jsonData);

                if (dataType.equals("ALBUM")){
                    tv_artists.setText(StringEscapeUtils.unescapeXml(albumJson.getString("primary_artists")));
                    tv_ALPLname.setText(StringEscapeUtils.unescapeXml(albumJson.getString("title")));
                }
                else{
                    tv_artists.setText(albumJson.getString("follower_count")+" followers");
                    tv_ALPLname.setText(StringEscapeUtils.unescapeXml(albumJson.getString("listname")));
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

            Glide.with(AlbumSongList.this).load(url_img).into(img_album);


            CustomAdapter song_list_Adapter = new CustomAdapter();
            song_list.setAdapter(song_list_Adapter);

            progressDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressDialog.show();
        }


    }

    void showAds(Boolean show,String AD_UNIT_ID){
//        mAdView = findViewById(R.id.adView_search);
        mAdView = new AdView(this);
        if (show){
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(AD_UNIT_ID);
            AdRequest adRequest = new AdRequest.Builder().build();
            if(mAdView.getAdSize() != null || mAdView.getAdUnitId() != null)
                mAdView.loadAd(adRequest);
            LinearLayout linearLayout = findViewById(R.id.ad_layout_album);
            linearLayout.addView(mAdView);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album__song__list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkAccent));
        }

//        mAdView = findViewById(R.id.adView_Album);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        SharedPreferences sc_pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.server_constants),Context.MODE_PRIVATE);
        banner2 =sc_pref.getString(getResources().getString(R.string.sc_banner2),getResources().getString(R.string.banner2));
        inter2 =sc_pref.getString(getResources().getString(R.string.sc_inter2),getResources().getString(R.string.ad_inter2));
        thopu = sc_pref.getBoolean(getResources().getString(R.string.sc_thope),false);
        if (!thopu)
            showAds(true,banner2);

        SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main),MODE_PRIVATE);

        SharedPreferences pref_set = getApplicationContext().getSharedPreferences(getResources().getString(R.string.set_main),MODE_PRIVATE);
        d_dir = pref_set.getString(getResources().getString(R.string.set_Directory),Environment.getExternalStorageDirectory()+ "/FREEMIUM_DOWNLOADS/");
        d_audTagging = pref_set.getBoolean(getResources().getString(R.string.set_audioTagging),true);
        d_subFolders = pref_set.getBoolean(getResources().getString(R.string.set_subFolders),true);
        d_FN_code = pref_set.getString(getResources().getString(R.string.set_defFN_code),"SAK");

        progressDialog = new ProgressDialog(AlbumSongList.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(inter2);
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
                    if (mInterstitialAd.isLoaded() && (!thopu)) {
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
        btn_settings=findViewById(R.id.btn_settings);
        song_list =findViewById(R.id.song_list);


        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//
                Intent in_toSet = new Intent(getApplicationContext(), SettingsAlbum.class);
                        in_toSet.putExtra("fromAlb",true);
                startActivity(in_toSet);
                overridePendingTransition(R.anim.slide_in_down,  R.anim.slide_out_down);
            }
        });






        if (dataType.equals("FAILED")){
            new AlertDialog.Builder(AlbumSongList.this)
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


        new MainTask().execute();




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            SharedPreferences temp_preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.set_main),MODE_PRIVATE);
            if (temp_preferences.getBoolean(getResources().getString(R.string.set_AlbumArt),true)){
                albumArt_fname = ".albumArt.jpg";
            }
        }



        SharedPreferences set_pref=getApplicationContext().getSharedPreferences(getResources().getString(R.string.set_main),MODE_PRIVATE);
        String d_kbps = set_pref.getString(getResources().getString(R.string.set_kbps),"320");
        format = set_pref.getString(getResources().getString(R.string.set_format),"m4a");
        if(d_kbps.equals("320"))
            rad_320.setChecked(true);
        else if(d_kbps.equals("160"))
            rad_128.setChecked(true);
        else if(d_kbps.equals("96"))
            rad_96.setChecked(true);
        else
            rad_320.setChecked(true);






        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);

        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .setDownloadConcurrentLimit(1)
                .setAutoRetryMaxAttempts(3)
                .setHttpDownloader(new OkHttpDownloader(Downloader.FileDownloaderType.PARALLEL))
                .setNamespace(FETCH_NAMESPACE)
                .setNotificationManager(new DefaultFetchNotificationManager(this) {
                    @NotNull
                    @Override
                    public Fetch getFetchInstanceForNamespace(@NotNull String namespace) {
                        return fetch;
                    }

                    @Override
                    public boolean shouldCancelNotification(@NotNull DownloadNotification downloadNotification) {
//                        return super.shouldCancelNotification(downloadNotification);
                        if (downloadNotification.isPaused())
                            return false;
                        else if (downloadNotification.isCancelled())
                            return true;
                        return false;
                    }

                    @NotNull
                    @Override
                    public NotificationCompat.Builder getNotificationBuilder(int notificationId, int groupId) {
                        Intent onClIntent = new Intent(getApplicationContext(), DownloadsPage.class);
                        PendingIntent pOnclick = PendingIntent.getActivity(getApplicationContext(),1,onClIntent,0);
                        return super.getNotificationBuilder(notificationId, groupId)
                                .setOnlyAlertOnce(true)
                                .setSmallIcon(R.drawable.ic_notif)
                                .setContentIntent(pOnclick);
                    }
                })
                .build();

        fetch = Fetch.Impl.getInstance(fetchConfiguration);



        File directory = new File(Environment.getExternalStorageDirectory()+ "/FREEMIUM_DOWNLOADS/");
        if (!directory.exists())
            if(directory.mkdirs())
                Log.i("DIRDONE1","1DONE");
            else
                Log.i("DIRDONE1","FAILED");

//        toCancel = new Intent(getApplicationContext(),NotificationRecive.class);
//        toCancel.putExtra("action","cancel");
//        toCancel.putExtra("notifID",download.getId());
//        cIntent = PendingIntent.getBroadcast(getApplicationContext(),1,toCancel,PendingIntent.FLAG_CANCEL_CURRENT);
//        Intent onClIntent = new Intent(getApplicationContext(),DownloadsPage.class);
//        PendingIntent pOnclick = PendingIntent.getActivity(getApplicationContext(),1,onClIntent,0);
        createNotificationChannel();
        mBuilder = new NotificationCompat.Builder(getApplicationContext(),chanelId)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle("Download Name")
                .setContentText("Description")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                .setProgress(100,0,false)
//                .setContentIntent(pOnclick);
//        mBuilder.addAction(R.drawable.ic_cancel_black_24dp,"Cancel",cIntent);
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());




        song_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
                //-------------------------//
                bottomSheetLO = new BottomSheetLO(position,songArr,albumArt_fname,format,kbpsGroup.getCheckedRadioButtonId());
                bottomSheetLO.show(getSupportFragmentManager(),"AlbumBottomSheet");

//                if (false){
//                JSONObject songJsn = null;
//                tempID = null;
//                String kbps = findViewById(kbpsGroup.getCheckedRadioButtonId()).getTag().toString();
//
//                try {
//                    songJsn = songArr.getJSONObject(position);
//                    tempSJson = songJsn.toString();
//
//                    SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main), MODE_PRIVATE);
//                    SharedPreferences.Editor editor = pref_main.edit();
//                    tempID = songJsn.getString("id");
//                    editor.putString(getResources().getString(R.string.pref_JsonFromID) + tempID, tempSJson).apply();
//                    int tempCount = pref_main.getInt(getResources().getString(R.string.pref_counter), 0);
//                    tempCount = tempCount + 1;
//                    if (tempCount >= 5) {
//                        tempCount = 0;
//                        if (mInterstitialAd.isLoaded()) {
//                            mInterstitialAd.show();
//                        }
//                    }
//                    editor.putInt(getResources().getString(R.string.pref_counter), tempCount).apply();
//
//                    folderName = StringEscapeUtils.unescapeXml(DataHandlers.getAlbumName(songJsn));
//                    fName = StringEscapeUtils.unescapeXml(songJsn.getString("song")) + "_" + kbps + "." + format;
////                    notificationID=DataHandlers.generateNotificationID(songJsn.getString("id"));
//                    downUrl = DataHandlers.getDownloadLink(songJsn, kbps);
//                    albumArtUrl = songJsn.getString("image").replace("150x150", "500x500");
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                downpath = DataHandlers.makeDir(folderName);
//
//                if (!(new File(downpath + "/" + albumArt_fname)).exists()) {
//                    PRDownloader.download(albumArtUrl, downpath, albumArt_fname)
//                            .build()
//                            .setOnStartOrResumeListener(new OnStartOrResumeListener() {
//                                @Override
//                                public void onStartOrResume() {
//
//                                }
//                            })
//                            .start(new OnDownloadListener() {
//                                @Override
//                                public void onDownloadComplete() {
//
//                                }
//
//                                @Override
//                                public void onError(Error error) {
//
//                                }
//                            });
//                }
//
//                if ((new File(downpath + "/" + fName)).exists()) {
//                    new AlertDialog.Builder(AlbumSongList.this)
//                            .setTitle("Already Downloaded.!")
//                            .setMessage("Do you want to download \"" + fName.replace(".m4a", "") + "\" again..")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    if (!downUrl.equals("FAILED")) {
//                                        Request request = new Request(downUrl, downpath + "/" + fName);
//                                        request.setPriority(Priority.HIGH);
//                                        request.setNetworkType(NetworkType.ALL);
//                                        request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");
//                                        if (!tempID.equals(null)) {
//                                            Map<String, String> map = new HashMap<String, String>();
//                                            map.put("ids", tempID);
//                                            request.setExtras(new Extras(map));
//                                        }
//
//                                        fetch.enqueue(request, updatedRequest -> {
//
//
//                                            //Request was successfully enqueued for download.
//                                            Toast.makeText(getApplicationContext(), "Added to Download Queue", Toast.LENGTH_SHORT).show();
//                                        }, error -> {
//                                            //An error occurred enqueuing the request.
//                                            Toast.makeText(getApplicationContext(), "Error! Unable to download song", Toast.LENGTH_SHORT).show();
//                                        });
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), "Sorry! You can't download this song..", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            })
//                            .setNegativeButton("No", null)
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .setCancelable(false)
//                            .show();
//
//
//                } else {
//                    if (!downUrl.equals("FAILED")) {
//                        final Request request = new Request(downUrl, downpath + "/" + fName);
//                        request.setPriority(Priority.HIGH);
//                        request.setNetworkType(NetworkType.ALL);
//                        request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");
//                        if (!tempID.equals(null)) {
//                            Map<String, String> map = new HashMap<String, String>();
//                            map.put("ids", tempID);
//                            request.setExtras(new Extras(map));
//                        }
//
//
//                        fetch.enqueue(request, updatedRequest -> {
//
//                            //Request was successfully enqueued for download.
//
//                            Toast.makeText(getApplicationContext(), "Added to Download Queue", Toast.LENGTH_SHORT).show();
//                        }, error -> {
//
//                            //An error occurred enqueuing the request.
//                            Toast.makeText(getApplicationContext(), "Error! Unable to download song", Toast.LENGTH_SHORT).show();
//                        });
//
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Sorry! You can't download this song..", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            }

            }
        });

        FetchListener fetchListener = new FetchListener() {
            @Override
            public void onAdded(@NotNull Download download) {
                Log.i("NOTIF_OBS","ADDED");
                String fil_name = download.getFile();
                fil_name=fil_name.substring(fil_name.lastIndexOf("/")+1);
                mBuilder.setContentTitle(fil_name).setOnlyAlertOnce(true);
                mBuilder.setContentText("processing..");
                mBuilder.setOngoing(false);
                notificationManagerCompat.notify(download.getId(),mBuilder.build());
            }

            @Override
            public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

            }

            @Override
            public void onError(@NotNull Download download, @NotNull com.tonyodev.fetch2.Error error, @Nullable Throwable throwable) {

            }

            @Override
            public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {
                Log.i("NOTIF_OBS","STARTED");
                notificationManagerCompat.cancel(download.getId());
//                Intent toNotif = new Intent(getApplicationContext(),NotificationRecive.class);
//                toNotif.putExtra("action","pause");
//                toNotif.putExtra("notifID",download.getId());
//                PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(),1,toNotif, PendingIntent.FLAG_ONE_SHOT);

//                Intent toCancel = new Intent(getApplicationContext(),NotificationRecive.class);
//                toCancel.putExtra("action","cancel");
//                toCancel.putExtra("notifID",download.getId());
//                PendingIntent cIntent = PendingIntent.getBroadcast(getApplicationContext(),1,toCancel,PendingIntent.FLAG_CANCEL_CURRENT);

//                String fil_name = download.getFile();
//                fil_name=fil_name.substring(fil_name.lastIndexOf("/")+1);

//                toCancel.putExtra("notifID",download.getId());
//                cIntent = PendingIntent.getBroadcast(getApplicationContext(),1,toCancel,PendingIntent.FLAG_ONE_SHOT);

//                mBuilder.setContentTitle(fil_name).setOnlyAlertOnce(true);
//                mBuilder.setContentText("Starting..");
//                mBuilder.setProgress(0,0,true);
//                mBuilder.addAction(R.drawable.ic_pause_circle_filled_black_24dp,"Pause",pIntent);
//                mBuilder.addAction(R.drawable.ic_cancel_black_24dp,"Cancel",cIntent);
//                mBuilder.setOngoing(true);
//                notificationManagerCompat.notify(download.getId(),mBuilder.build());

            }

            @Override
            public void onWaitingNetwork(@NotNull Download download) {
                Log.i("NOTIF_OBS","STARTED");
            }

            @Override
            public void onQueued(@NotNull Download download, boolean waitingOnNetwork) {

            }

            @Override
            public void onCompleted(@NotNull Download download) {
//                notificationManagerCompat.cancel(download.getId());
                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)&&d_audTagging){
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    Extras extras= download.getExtras();
                    Map<String,String> map=extras.getMap();
                    String id=map.get("ids");
                    Log.i("STAG_A",id);
                    SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main), MODE_PRIVATE);
                    String sjson= pref_main.getString(getResources().getString(R.string.pref_JsonFromID)+id,null);
                    SharedPreferences pref_main1 = getApplicationContext().getSharedPreferences(getResources().getString(R.string.set_main), MODE_PRIVATE);
                    Boolean remove_aA = pref_main1.getBoolean(getResources().getString(R.string.set_del_aA),false);
                    if (!sjson.equals(null)) {
                        try {
                            String fName1 = download.getFile();
                            fName1=fName1.substring(fName1.lastIndexOf("/")+1);
                            String albumArt_fname1 = albumArt_fname.replace(".jpg","_"+fName1).replace(format,"jpg");
                            DataHandlers.setTags2(download.getFile(),sjson,albumArt_fname1.replace(" ","_"));
                            if (remove_aA){
                                File img_art = new File(download.getFile().substring(0,download.getFile().lastIndexOf("/"))+ "/" + albumArt_fname1.replace(" ","_"));
                                if (img_art.exists()){
                                    img_art.delete();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


//                String filname = download.getFile();
//                filname=filname.substring(filname.lastIndexOf("/")+1);
//                mBuilder.setContentTitle(filname);
//                mBuilder.setProgress(0,0,false);
//                mBuilder.setOngoing(false);
//                mBuilder.setContentText("Done");
//                notificationManagerCompat.notify(download.getId(),mBuilder.build());
            }


            @Override
            public void onProgress(@NotNull Download download, long etaInMilliSeconds, long downloadedBytesPerSecond) {
//                String filname = download.getFile();
//                filname=filname.substring(filname.lastIndexOf("/")+1);
//                mBuilder.setContentTitle(filname);
//
//                totMB=((float)(download.getTotal()))/1048576;
//                curMB=((float)(download.getDownloaded()))/1048576;
//
//                mBuilder.setContentText(f.format(curMB)+"MB/"+f.format(totMB)+"MB");
//                mBuilder.setProgress((int)download.getTotal(),(int)download.getDownloaded(),false);

//                notificationManagerCompat.notify(download.getId(),mBuilder.build());

            }

            @Override
            public void onPaused(@NotNull Download download) {

            }

            @Override
            public void onResumed(@NotNull Download download) {

            }

            @Override
            public void onCancelled(@NotNull Download download) {
                Log.i("resu1","200");
                try {
                    notificationManagerCompat.cancel(download.getId());
                    File f = new File(download.getFile());
                    if (f.exists())
                        f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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

    public static class BottomSheetLO extends BottomSheetDialogFragment{
        String tempID,tempSJson,folderName,fName,downUrl="FAILED",albumArtUrl,downpath;
        String albumArt_fname,format;
        int kbpsGroup;
        JSONArray songArr;
        int position;

        public BottomSheetLO(int position1,JSONArray songArr1,String albumArt_fname1,String format1,int kbpsGroup1) {
            this.albumArt_fname=albumArt_fname1;
            this.songArr=songArr1;
            this.format=format1;
            this.position=position1;
            this.kbpsGroup=kbpsGroup1;
        }

        @Override
        public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        }

        @androidx.annotation.Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.album_bottom_sheet,container,false);


            rootView.findViewById(R.id.btn_view_lyrics).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //------Animation-----------//
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(1000);
                    v.startAnimation(animation1);
                    //-------------------------//



                    JSONObject songJsn = null;
                    tempID = null;
                    String kbps = getActivity().findViewById(kbpsGroup).getTag().toString();

                    try {
                        songJsn = songArr.getJSONObject(position);
                        tempSJson = songJsn.toString();

                        tempID = songJsn.getString("id");

                        String hasLyrics=songJsn.getString("has_lyrics");

                        folderName = StringEscapeUtils.unescapeXml(DataHandlers.getAlbumName(songJsn));
                        fName = StringEscapeUtils.unescapeXml(songJsn.getString("song"))+".lrc";
                        if (fName.equals("FAILED"))
                            fName = StringEscapeUtils.unescapeXml(songJsn.getString("song")) + ".txt";

                        downpath = DataHandlers.makeDir2(d_dir,folderName,d_subFolders);
                        if (downpath.equals("FAILED"))
                            downpath = DataHandlers.makeDir(folderName);

                        if (hasLyrics.equals("true")){
                            Intent intent = new Intent(getContext(), ViewLyrics.class);
                            intent.putExtra("ID",tempID);
                            intent.putExtra("FNAME",fName);
                            intent.putExtra("DIR",downpath);
                            intent.putExtra("SNAME",StringEscapeUtils.unescapeXml(songJsn.getString("song")));
                            startActivity(intent);
                            bottomSheetLO.dismiss();
                        }else{
                            ((TextView) v).setText("No Lyrics Available");
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });


            rootView.findViewById(R.id.btn_sht_down).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //------Animation-----------//
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(1000);
                    v.startAnimation(animation1);
                    //-------------------------//



                    JSONObject songJsn = null;
                    tempID = null;
                    String kbps = getActivity().findViewById(kbpsGroup).getTag().toString();

                    try {
                        songJsn = songArr.getJSONObject(position);
                        tempSJson = songJsn.toString();

                        SharedPreferences pref_main = rootView.getContext().getSharedPreferences(getResources().getString(R.string.pref_main), MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref_main.edit();
                        tempID = songJsn.getString("id");
                        editor.putString(getResources().getString(R.string.pref_JsonFromID) + tempID, tempSJson).apply();
                        int tempCount = pref_main.getInt(getResources().getString(R.string.pref_counter), 0);
                        tempCount = tempCount + 1;
                        if (tempCount >= 5) {
                            tempCount = 0;
                            if (mInterstitialAd.isLoaded() && (!thopu)) {
                                mInterstitialAd.show();
                            }
                        }
                        editor.putInt(getResources().getString(R.string.pref_counter), tempCount).apply();

                        folderName = StringEscapeUtils.unescapeXml(DataHandlers.getAlbumName(songJsn));
                        fName = DataHandlers.getFileName(StringEscapeUtils.unescapeXml(songJsn.getString("song")),folderName,kbps,format,d_FN_code);
                        if (fName.equals("FAILED"))
                            fName = StringEscapeUtils.unescapeXml(songJsn.getString("song")) + "_" + kbps + "." + format;
//                    notificationID=DataHandlers.generateNotificationID(songJsn.getString("id"));
                        downUrl = DataHandlers.getDownloadLink(songJsn, kbps);
                        albumArtUrl = songJsn.getString("image").replace("150x150", "500x500");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    downpath = DataHandlers.makeDir(folderName);
                    downpath = DataHandlers.makeDir2(d_dir,folderName,d_subFolders);
                    if (downpath.equals("FAILED"))
                        downpath = DataHandlers.makeDir(folderName);

                    albumArt_fname = albumArt_fname.replace(".jpg","_"+fName).replace(format,"jpg");
                    albumArt_fname = albumArt_fname.replace(" ","_");

                    if (!(new File(downpath + "/" + albumArt_fname)).exists()) {
                        PRDownloader.download(albumArtUrl, downpath, albumArt_fname)
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

                    if ((new File(downpath + "/" + fName)).exists()) {
                        new AlertDialog.Builder(rootView.getContext())
                                .setTitle("Already Downloaded.!")
                                .setMessage("Do you want to download \"" + fName.replace(".m4a", "") + "\" again..")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!downUrl.equals("FAILED")) {
                                            final Request request = new Request(downUrl, downpath + "/" + fName);
                                            request.setPriority(Priority.HIGH);
                                            request.setNetworkType(NetworkType.ALL);
                                            request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");
                                            if (!tempID.equals(null)) {
                                                Map<String, String> map = new HashMap<String, String>();
                                                map.put("ids", tempID);
                                                request.setExtras(new Extras(map));
                                            }

                                            fetch.enqueue(request, updatedRequest -> {


                                                //Request was successfully enqueued for download.
                                                Toast.makeText(rootView.getContext(), "Added to Download Queue", Toast.LENGTH_SHORT).show();
                                            }, error -> {
                                                //An error occurred enqueuing the request.
                                                Toast.makeText(rootView.getContext(), "Error! Unable to download song", Toast.LENGTH_SHORT).show();
                                            });
                                        } else {
                                            Toast.makeText(rootView.getContext(), "Sorry! You can't download this song..", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("No", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setCancelable(false)
                                .show();


                    } else {
                        if (!downUrl.equals("FAILED")) {
                            final Request request = new Request(downUrl, downpath + "/" + fName);
                            request.setPriority(Priority.HIGH);
                            request.setNetworkType(NetworkType.ALL);
                            request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");
                            if (!tempID.equals(null)) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("ids", tempID);
                                request.setExtras(new Extras(map));
                            }


                            fetch.enqueue(request, updatedRequest -> {

                                //Request was successfully enqueued for download.

                                Toast.makeText(rootView.getContext(), "Added to Download Queue", Toast.LENGTH_SHORT).show();
                            }, error -> {

                                //An error occurred enqueuing the request.
                                Toast.makeText(rootView.getContext(), "Error! Unable to download song", Toast.LENGTH_SHORT).show();
                            });

                        } else {
                            Toast.makeText(rootView.getContext(), "Sorry! You can't download this song..", Toast.LENGTH_SHORT).show();
                        }
                    }


                    bottomSheetLO.dismiss();
                }
            });

            rootView.findViewById(R.id.btn_sht_playnow).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //------Animation-----------//
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(1000);
                    v.startAnimation(animation1);
                    //-------------------------//
                    bottomSheetLO.dismiss();
                    Toast.makeText(rootView.getContext(), "This feature is still under-construction", Toast.LENGTH_SHORT).show();

                }
            });

            rootView.findViewById(R.id.btn_sht_queue).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //------Animation-----------//
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(1000);
                    v.startAnimation(animation1);
                    //-------------------------//
                    bottomSheetLO.dismiss();
                    Toast.makeText(rootView.getContext(), "This feature is still under-construction", Toast.LENGTH_SHORT).show();

                }
            });

            rootView.findViewById(R.id.btn_sht_song_info).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //------Animation-----------//
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(1000);
                    v.startAnimation(animation1);
                    //-------------------------//
                    bottomSheetLO.dismiss();

                }
            });

            JSONObject songJsn = null;
            try {
                songJsn=songArr.getJSONObject(position);

                ImageView img_art= rootView.findViewById(R.id.dia_si_art1);
                TextView tv_song =  rootView.findViewById(R.id.dia_si_song1);
                TextView tv_album =  rootView.findViewById(R.id.dia_si_album1);
                TextView tv_music =  rootView.findViewById(R.id.dia_si_music1);
                TextView tv_singers =  rootView.findViewById(R.id.dia_si_singers1);
                TextView tv_lang =  rootView.findViewById(R.id.dia_si_lang1);
                TextView tv_year =  rootView.findViewById(R.id.dia_si_year1);
                TextView tv_copy = rootView.findViewById(R.id.dia_si_copyrights);

                Glide.with(getActivity()).load(songJsn.getString("image").replace("150x150","250x250")).into(img_art);

                tv_song.setText(StringEscapeUtils.unescapeXml(songJsn.getString("song")));
                tv_album.setText("Album : "+StringEscapeUtils.unescapeXml(songJsn.getString("album")));
                if (songJsn.getString("music").equals("")){
                    tv_music.setVisibility(View.GONE);
                }else
                    tv_music.setText("Music : "+StringEscapeUtils.unescapeXml(songJsn.getString("music")));

                tv_singers.setText("Singers : "+StringEscapeUtils.unescapeXml(songJsn.getString("singers")));
                tv_lang.setText(songJsn.getString("language"));
                tv_year.setText(songJsn.getString("year"));

                if (songJsn.getString("copyright_text").equals("")){
                    tv_copy.setVisibility(View.GONE);
                }else
                    tv_copy.setText((songJsn.getString("copyright_text").replace("\\u2117","©").replace("&copy;","©")));

            }catch (Exception e){
                e.printStackTrace();
            }

            return rootView;
        }
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

            TextView songName= convertView.findViewById(R.id.cus_songName_frag);
            TextView artists = convertView.findViewById(R.id.cus_artist_frag);
            TextView duration = convertView.findViewById(R.id.cus_duration);


            try {
                songObj =songArr.getJSONObject(position);
                songName.setText(StringEscapeUtils.unescapeXml(songObj.getString("song")));
                artists.setText(StringEscapeUtils.unescapeXml(songObj.getString("album")+dot+
                        songObj.getString("primary_artists")));
                duration.setText(DataHandlers.getSongDuration(songObj));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }
    }

    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name ="Download Client";
            String description = "Notifies when ever there is any on-going download";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(chanelId,name,importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
