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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codekidlabs.storagechooser.StorageChooser;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.tonyodev.fetch2.FetchListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import nitish.build.com.freemium.Handlers.DataHandlers;
import nitish.build.com.freemium.R;

public class DownloadsPage extends AppCompatActivity {
    private ListView lv_queueList;
    private AdView mAdView;
    private ImageView btn_set_dp,btn_bak_dp;
    private Button btn_folder_dp;
    private SharedPreferences settings_pref;
    private String def_down_dir;
    private TextView tv_head_dir;
    private int listSize;
    private RecyclerView rv_showfiles;
    private RecyclerView.LayoutManager layoutManager;
    private String cur_down_path;
    private Button btn_back_files;

    private String banner3;



    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), SearchSongs.class));
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
    }

    void showAds(Boolean show,String AD_UNIT_ID){
        mAdView = new AdView(this);
        if (show){
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(AD_UNIT_ID);
            AdRequest adRequest = new AdRequest.Builder().build();
            if(mAdView.getAdSize() != null || mAdView.getAdUnitId() != null)
                mAdView.loadAd(adRequest);
            LinearLayout linearLayout = findViewById(R.id.ad_layout_downs);
            linearLayout.addView(mAdView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads__page);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkAccent));
        }

        lv_queueList=findViewById(R.id.list_inQueue);

        btn_back_files = findViewById(R.id.btn_back_files);

        rv_showfiles= findViewById(R.id.rv_showfiles);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_showfiles.setLayoutManager(layoutManager);
        
        SharedPreferences sc_pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.server_constants), Context.MODE_PRIVATE);
        banner3 =sc_pref.getString(getResources().getString(R.string.sc_banner3),getResources().getString(R.string.banner3));
        Boolean thopu = sc_pref.getBoolean(getResources().getString(R.string.sc_thope),false);
        if (!thopu)
            showAds(true,banner3);

        btn_set_dp = findViewById(R.id.btn_set_dp);
        btn_set_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateView(v, 500);
                startActivity(new Intent(getApplicationContext(), SettingsAlbum.class));
                overridePendingTransition(R.anim.slide_in_down,  R.anim.slide_out_down);
            }
        });
        btn_bak_dp = findViewById(R.id.btn_bak_dp);
        btn_bak_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateView(v, 500);
                btn_back_files.callOnClick();
            }
        });

        settings_pref=getApplicationContext().getSharedPreferences(getResources().getString(R.string.set_main),MODE_PRIVATE);
        def_down_dir= settings_pref.getString(getResources().getString(R.string.set_Directory),Environment.getExternalStorageDirectory() + "/FREEMIUM_DOWNLOADS/");

        tv_head_dir= findViewById(R.id.tv_head_dir);
        tv_head_dir.setText(def_down_dir.replace(Environment.getExternalStorageDirectory().getAbsolutePath(),"").replace("/storage/",""));

        btn_folder_dp =findViewById(R.id.btn_folder_dp);
        btn_folder_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateView(v, 500);

                Uri selectedUri = Uri.parse(def_down_dir);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(selectedUri, "resource/folder");

                if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
                {
                    startActivity(intent);
                }
                else
                {
                    Snackbar.make(findViewById(android.R.id.content),"Please install ant third-party file browser",Snackbar.LENGTH_LONG).show();
                }


            }
        });
        cur_down_path=def_down_dir;
        String mainJsonArr = DataHandlers.getFullDirectory(def_down_dir);
        if (!mainJsonArr.equals("FAILED")){
            try {
                JSONArray fullArr = new JSONArray(mainJsonArr);
                listSize = fullArr.length();
                RecAdapter recAdapter = new RecAdapter(fullArr,listSize);
                rv_showfiles.setAdapter(recAdapter);
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        btn_back_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);

                if (!cur_down_path.equals(def_down_dir)){
                    String newpath = cur_down_path.substring(0,cur_down_path.lastIndexOf("/"));
                    String mainJsonArr =DataHandlers.getFullDirectory(newpath);
                    if (!mainJsonArr.equals("FAILED")){
                        try {
                            cur_down_path=newpath;
                            JSONArray fullArr = new JSONArray(mainJsonArr);
                            RecAdapter recAdapter = new RecAdapter(fullArr,fullArr.length());
                            rv_showfiles.setAdapter(recAdapter);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else
                    Toast.makeText(DownloadsPage.this, "You are already in home directory..", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_home_files).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);
                
                if (!cur_down_path.equals(def_down_dir)) {
                    String mainJsonArr = DataHandlers.getFullDirectory(def_down_dir);
                    if (!mainJsonArr.equals("FAILED")) {
                        try {
                            cur_down_path = def_down_dir;
                            JSONArray fullArr = new JSONArray(mainJsonArr);
                            RecAdapter recAdapter = new RecAdapter(fullArr, fullArr.length());
                            rv_showfiles.setAdapter(recAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else
                    Toast.makeText(DownloadsPage.this, "You are already in home directory..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void animateView(View view, int duration) {
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(duration);
        view.startAnimation(animation1);
    }


    class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder>{
        JSONArray fullDirArr;
        int rvSize;
        RecAdapter(JSONArray fromMain,int size){
            this.fullDirArr=fromMain;
            this.rvSize=size;

            if (size==0){
                findViewById(R.id.empty_box_downPage).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_empty_box_downPage).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_noDowns).setVisibility(View.VISIBLE);
            }else {
                findViewById(R.id.empty_box_downPage).setVisibility(View.GONE);
                findViewById(R.id.tv_empty_box_downPage).setVisibility(View.GONE);
                findViewById(R.id.tv_noDowns).setVisibility(View.GONE);
            }
            tv_head_dir.setText(cur_down_path.replace("/storage/","").replace("emulated/0","").replace("emulated/1",""));
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_download_list,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            try{
                JSONObject tempObj = fullDirArr.getJSONObject(position);
                String absPath = tempObj.getString("abs_path");
                String fileType= tempObj.getString("file_type");
                holder.useless.setVisibility(View.GONE);
                holder.tv_head.setText(tempObj.getString("file_name"));
                holder.tv_subhead.setText(tempObj.getString("last_modified"));

                if (fileType.equals("FOLDER"))
                    holder.img_art.setImageResource(R.drawable.music_folder);
                else if (fileType.equals("MP3")) {
                    holder.img_art.setPadding(5,5,5,5);
                    holder.img_art.setImageResource(R.drawable.file_mp);
                }
                else if (fileType.equals("M4A")) {
                    holder.img_art.setPadding(5,5,5,5);
                    holder.img_art.setImageResource(R.drawable.file_ma);
                }

                holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                        animation1.setDuration(1000);
                        v.startAnimation(animation1);

                        if (fileType.equals("FOLDER")){
                            String mainJsonArr =DataHandlers.getFullDirectory(absPath);
                            if (!mainJsonArr.equals("FAILED")){
                                try {
                                    cur_down_path=absPath;
                                    JSONArray fullArr = new JSONArray(mainJsonArr);
                                    RecAdapter recAdapter = new RecAdapter(fullArr,fullArr.length());
                                    rv_showfiles.setAdapter(recAdapter);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }else {
                            Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                            File file = new File(absPath);
                            Uri uri = FileProvider.getUriForFile(getApplicationContext(), "nitish.build.com.freemium.fileprovider", file);
                            viewIntent.setDataAndType(uri,"audio/*");
                            viewIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(viewIntent, null));
                        }

                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return rvSize;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView tv_head,tv_subhead,useless;
            ImageView img_art;
            ConstraintLayout parentLayout;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_head = itemView.findViewById(R.id.cus_songName_frag);
                tv_subhead = itemView.findViewById(R.id.cus_artist_frag);
                img_art = itemView.findViewById(R.id.cus_img_frag69);
                useless = itemView.findViewById(R.id.cus_duration);
                parentLayout = itemView.findViewById(R.id.cus_download_layout);
            }
        }

    }



    public void btmSrch(View v){
        
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        
        startActivity(new Intent(getApplicationContext(), SearchSongs.class));
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
    }
    public void btmBrws(View v){

        animateView(v, 1000);
        
        startActivity(new Intent(getApplicationContext(),SaavnWebView.class));
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
    }
    public void btmDown(View v){
        animateView(v, 1000);
    }
    public void btmMore(View v){
        animateView(v, 1000);
        
        startActivity(new Intent(getApplicationContext(),MorePage.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
