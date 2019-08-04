package nitish.build.com.saavntest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codekidlabs.storagechooser.StorageChooser;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2core.FetchObserver;
import com.tonyodev.fetch2core.Reason;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class Downloads_Page extends AppCompatActivity {
     ListView lv_queueList;
     ArrayAdapter<String> arrayAdapter;
    static ProgressBar pb_downPage;
    static TextView tv_info,tv_DownSong,tv_prog;
    static ArrayList<String> songList;
    AdView mAdView;
    ImageView btn_set_dp,btn_bak_dp;
    Button btn_folder_dp;
    static FetchListener fetchListener1;
    StorageChooser chooser;
    SharedPreferences settings_pref;
    String def_down_dir;
    TextView tv_head_dir;
    int listSize;
    RecyclerView rv_showfiles;
    RecyclerView.LayoutManager layoutManager;
    String cur_down_path;
    Button btn_back_files;



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

        rv_showfiles= findViewById(R.id.rv_showfiles);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_showfiles.setLayoutManager(layoutManager);

        mAdView = findViewById(R.id.adView_Downloads);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        btn_set_dp = findViewById(R.id.btn_set_dp);
        btn_set_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//

                startActivity(new Intent(getApplicationContext(),Settings_Alb.class));
                overridePendingTransition(R.anim.slide_in_down,  R.anim.slide_out_down);
            }
        });
        btn_bak_dp = findViewById(R.id.btn_bak_dp);
        btn_bak_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//

                onBackPressed();
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
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//

//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                Uri uri = Uri.parse("file://"+Environment.getExternalStorageDirectory() + "/FREEMIUM_DOWNLOADS/"); //  directory path
//                intent.setDataAndType(uri, "*/*");
//                startActivity(Intent.createChooser(intent, "Open folder"));


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
//                    Toast.makeText(getApplicationContext(), "Please Install a File Manager.", Toast.LENGTH_SHORT).show();
                }


            }
        });
        cur_down_path=def_down_dir;
        String mainJsonArr =DataHandlers.getFullDirectory(def_down_dir);
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

        btn_back_files = findViewById(R.id.btn_back_files);
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
                }
            }
        });

        findViewById(R.id.btn_home_files).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);

                String mainJsonArr =DataHandlers.getFullDirectory(def_down_dir);
                if (!mainJsonArr.equals("FAILED")){
                    try {
                        cur_down_path=def_down_dir;
                        JSONArray fullArr = new JSONArray(mainJsonArr);
                        RecAdapter recAdapter = new RecAdapter(fullArr,fullArr.length());
                        rv_showfiles.setAdapter(recAdapter);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

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
            }else {
                findViewById(R.id.empty_box_downPage).setVisibility(View.GONE);
                findViewById(R.id.tv_empty_box_downPage).setVisibility(View.GONE);
            }
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
//                            viewIntent.setDataAndType(Uri.fromFile(file), "audio/*");
                            Uri uri = FileProvider.getUriForFile(getApplicationContext(), "nitish.build.com.saavntest1.fileprovider", file);
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
