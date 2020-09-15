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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.codekidlabs.storagechooser.StorageChooser;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.File;

import nitish.build.com.freemium.R;


public class SettingsAlbum extends AppCompatActivity {
    RadioGroup rg_kbps,rg_format;
    TextView tv_path,set_down_path,tv_cur_fn_format,set_change_dir;
    Button btn_save,btn_cancel;
    RadioButton rb_320,rb_160,rb_96,rb_m4a,rb_mp3;
    SharedPreferences pref_set;
    String d_kbps="320",d_format="m4a",d_dir,d_FN_code;
    String s_kbps="320",s_format="m4a",s_dir,s_FN_code;
    Boolean sved = false,s_aA_hidden,s_audTagging,s_subFolders;
    Boolean d_aA_hidden,d_audTagging,d_subFolders,d_del_aA;
    Switch cb_hide_albumArt,switch_sub_folders,switch_audio_tag,fn_pref_con_kbps,fn_pref_con_AlbumName,cb_delete_albumArt;
    Boolean is_fromAlb = false;
    StorageChooser chooser;

    AdView mAdView;
    String banner6;




    @Override
    public void onBackPressed() {
        if (sved)
//            Snackbar.make(getCurrentFocus(),"Your Settings Saved.",Snackbar.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Your Settings Saved.", Toast.LENGTH_SHORT).show();
        else
//            Snackbar.make(findViewById(android.R.id.content),"Your settings wont be saved.",Snackbar.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Your settings wont be saved.", Toast.LENGTH_SHORT).show();
        if (is_fromAlb){
            Intent to_init = new Intent(getApplicationContext(), SearchSongs.class);
            to_init.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(to_init);
            overridePendingTransition(R.anim.slide_in_up  ,  R.anim.slide_out_up);
        }else {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_up  ,  R.anim.slide_out_up);
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("HERE","IAMHR");
        switch(requestCode) {
            case 9999:
                try {
                    Uri uri = data.getData();
                    File file = new File(uri.getPath());//create path from uri
                    final String[] split = file.getPath().split(":");//split the path.
                    String filePath = "";
                    if (split.length > 1)
                        filePath = split[1];
                    s_dir = Environment.getExternalStorageDirectory() + "/" + filePath + "/FREEMIUM_DOWNLOADS/";
                    set_down_path.setText("/" + filePath + "/FREEMIUM_DOWNLOADS/");
                }catch (Exception e){
                    e.printStackTrace();
                    new AlertDialog.Builder(SettingsAlbum.this)
                            .setTitle("Try again \uD83D\uDE14 !")
                            .setMessage("If this happens again.This feature might not be working in your device. Please report the bug to resolve the error.")
                            .setPositiveButton("Ok", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                break;
        }
    }

    void showAds(Boolean show,String AD_UNIT_ID){
//        mAdView = findViewById(R.id.adView_search);
        mAdView = new AdView(this);
        if (show){
            mAdView.setAdSize(AdSize.SMART_BANNER);
            mAdView.setAdUnitId(AD_UNIT_ID);
            AdRequest adRequest = new AdRequest.Builder().build();
            if(mAdView.getAdSize() != null || mAdView.getAdUnitId() != null)
                mAdView.loadAd(adRequest);
            LinearLayout linearLayout = findViewById(R.id.ad_layout_settings);
            linearLayout.addView(mAdView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__alb);

//        mAdView = findViewById(R.id.adView_Settings);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        SharedPreferences sc_pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.server_constants),Context.MODE_PRIVATE);
        banner6 =sc_pref.getString(getResources().getString(R.string.sc_banner6),getResources().getString(R.string.banner6));
        Boolean thopu = sc_pref.getBoolean(getResources().getString(R.string.sc_thope),false);
        if (!thopu)
            showAds(true,banner6);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkAccent));
        }

        Intent fromAlb = getIntent();
        is_fromAlb =fromAlb.getBooleanExtra("fromAlb",false);

        rg_kbps = findViewById(R.id.rg_set_kbps);
        rg_format = findViewById(R.id.rg_set_format);
        set_down_path = findViewById(R.id.set_down_path);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        rb_96 = findViewById(R.id.set_96);
        rb_160 = findViewById(R.id.set_160);
        rb_320 = findViewById(R.id.set_320);
        rb_m4a = findViewById(R.id.set_m4a);
        rb_mp3 = findViewById(R.id.set_mp3);
        tv_cur_fn_format=findViewById(R.id.cur_fn_format);
        switch_audio_tag = findViewById(R.id.switch_audio_tag);
        switch_sub_folders = findViewById(R.id.switch_sub_folders);
        fn_pref_con_AlbumName = findViewById(R.id.fn_pref_con_AlbumName);
        fn_pref_con_kbps = findViewById(R.id.fn_pref_con_kbps);
        set_change_dir = findViewById(R.id.set_change_dir);
        cb_hide_albumArt = findViewById(R.id.cb_hide_albumArt);
        cb_delete_albumArt = findViewById(R.id.cb_delete_albumArt);

        pref_set = getApplicationContext().getSharedPreferences(getResources().getString(R.string.set_main),MODE_PRIVATE);
        d_format = pref_set.getString(getResources().getString(R.string.set_format),"m4a");
        d_kbps = pref_set.getString(getResources().getString(R.string.set_kbps),"320");
        d_aA_hidden = pref_set.getBoolean(getResources().getString(R.string.set_AlbumArt),true);

        d_dir = pref_set.getString(getResources().getString(R.string.set_Directory),Environment.getExternalStorageDirectory()+ "/FREEMIUM_DOWNLOADS/");
        d_audTagging = pref_set.getBoolean(getResources().getString(R.string.set_audioTagging),true);
        d_subFolders = pref_set.getBoolean(getResources().getString(R.string.set_subFolders),true);
        d_FN_code = pref_set.getString(getResources().getString(R.string.set_defFN_code),"SAK");

        d_del_aA = pref_set.getBoolean(getResources().getString(R.string.set_del_aA),false);

        Log.i("Test111", "Def Path: " + d_dir);

        set_down_path.setText(d_dir.replace(Environment.getExternalStorageDirectory().getAbsolutePath()," "));
        s_dir=d_dir;

        if (d_FN_code.equals("S")){
            tv_cur_fn_format.setText("SongName.format");
            fn_pref_con_kbps.setChecked(false);
            fn_pref_con_AlbumName.setChecked(false);
        }else if (d_FN_code.equals("SA")){
            tv_cur_fn_format.setText("SongName[AlbumName].format");
            fn_pref_con_kbps.setChecked(false);
            fn_pref_con_AlbumName.setChecked(true);
        }else if (d_FN_code.equals("SAK")){
            tv_cur_fn_format.setText("SongName[AlbumName]_kbps.format");
            fn_pref_con_kbps.setChecked(true);
            fn_pref_con_AlbumName.setChecked(true);
        }else if (d_FN_code.equals("SK")){
            tv_cur_fn_format.setText("SongName_kbps.format");
            fn_pref_con_kbps.setChecked(true);
            fn_pref_con_AlbumName.setChecked(false);
        }

        switch_sub_folders.setChecked(d_subFolders);
        switch_audio_tag.setChecked(d_audTagging);
        cb_hide_albumArt.setChecked(d_aA_hidden);
        cb_delete_albumArt.setChecked(d_del_aA);


//        if (d_subFolders)
//            switch_sub_folders.setChecked(true);
//        else
//            switch_sub_folders.setChecked(false);
//
//        if (d_audTagging)
//            switch_audio_tag.setChecked(true);
//        else
//            switch_audio_tag.setChecked(false);
//
//        if (d_aA_hidden)
//            cb_hide_albumArt.setChecked(true);

        if (d_kbps.equals("320"))
            rb_320.setChecked(true);
        else if (d_kbps.equals("160"))
            rb_160.setChecked(true);
        else if (d_kbps.equals("96"))
            rb_96.setChecked(true);
        else
            rb_320.setChecked(true);

        if (d_format.equals("m4a"))
            rb_m4a.setChecked(true);
        else if (d_format.equals("mp3"))
            rb_mp3.setChecked(true);
        else
            rb_m4a.setChecked(true);

        StorageChooser.Theme theme = new StorageChooser.Theme(SettingsAlbum.this);
        theme.setScheme(theme.getDefaultDarkScheme());
        chooser = new StorageChooser.Builder()
                .withActivity(SettingsAlbum.this)
                .withFragmentManager(getFragmentManager())
                .withMemoryBar(true).allowAddFolder(true).disableMultiSelect()
                .allowCustomPath(true).setTheme(theme)
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .build();




        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
            @Override
            public void onSelect(String path) {
                Log.e("SELECTED_PATH", path);
                s_dir = path+ "/FREEMIUM_DOWNLOADS/";
                set_down_path.setText(s_dir.replace(Environment.getExternalStorageDirectory().getAbsolutePath(),"").replace("/storage/",""));
            }
        });



        set_change_dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//

//                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//                i.addCategory(Intent.CATEGORY_DEFAULT);
//                startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);


                chooser.show();

            }
        });

        set_down_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//

                Uri selectedUri = Uri.parse(s_dir);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(selectedUri, "resource/folder");

                if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
                {
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please Install a Third-Party File Manager.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        cb_hide_albumArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                    cb_hide_albumArt.setChecked(false);
                    new AlertDialog.Builder(SettingsAlbum.this)
                            .setTitle("Sorry \uD83D\uDE14 !")
                            .setMessage("This feature is currently enabled only for devices with Android Oreo and Above.")
                            .setPositiveButton("Ok", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        fn_pref_con_AlbumName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean t_1=fn_pref_con_AlbumName.isChecked(),t_2=fn_pref_con_kbps.isChecked();
                if (t_1&&t_2)
                    tv_cur_fn_format.setText("SongName[AlbumName]_kbps.format");
                else if (t_1&&(!t_2))
                    tv_cur_fn_format.setText("SongName[AlbumName].format");
                else if ((!t_1)&&(t_2))
                    tv_cur_fn_format.setText("SongName_kbps.format");
                else if ((!t_1)&&(!t_2))
                    tv_cur_fn_format.setText("SongName.format");

            }
        });
        fn_pref_con_kbps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean t_1=fn_pref_con_AlbumName.isChecked(),t_2=fn_pref_con_kbps.isChecked();
                if (t_1&&t_2)
                    tv_cur_fn_format.setText("SongName[AlbumName]_kbps.format");
                else if (t_1&&(!t_2))
                    tv_cur_fn_format.setText("SongName[AlbumName].format");
                else if ((!t_1)&&(t_2))
                    tv_cur_fn_format.setText("SongName_kbps.format");
                else if ((!t_1)&&(!t_2))
                    tv_cur_fn_format.setText("SongName.format");

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sved=true;
                s_kbps = findViewById(rg_kbps.getCheckedRadioButtonId()).getTag().toString();
                s_format = findViewById(rg_format.getCheckedRadioButtonId()).getTag().toString();
                s_audTagging = switch_audio_tag.isChecked();
                s_subFolders = switch_sub_folders.isChecked();
                s_aA_hidden=cb_hide_albumArt.isChecked();
                Boolean t_1=fn_pref_con_AlbumName.isChecked(),t_2=fn_pref_con_kbps.isChecked();
                if (t_1&&t_2)
                    s_FN_code="SAK";
                else if (t_1&&(!t_2))
                    s_FN_code="SA";
                else if ((!t_1)&&(t_2))
                    s_FN_code="SK";
                else if ((!t_1)&&(!t_2))
                    s_FN_code="S";

//                if (cb_hide_albumArt.isChecked())
//                    s_aA_hidden=true;
//                else
//                    s_aA_hidden = false;
                SharedPreferences.Editor edit_set = pref_set.edit();
                edit_set.putString(getResources().getString(R.string.set_kbps),s_kbps).apply();
                edit_set.putString(getResources().getString(R.string.set_format),s_format).apply();
                edit_set.putBoolean(getResources().getString(R.string.set_AlbumArt),s_aA_hidden).apply();

                edit_set.putBoolean(getResources().getString(R.string.set_audioTagging),s_audTagging).apply();
                edit_set.putBoolean(getResources().getString(R.string.set_subFolders),s_subFolders).apply();
                edit_set.putString(getResources().getString(R.string.set_Directory),s_dir).apply();
                edit_set.putString(getResources().getString(R.string.set_defFN_code),s_FN_code).apply();

                edit_set.putBoolean(getResources().getString(R.string.set_del_aA),cb_delete_albumArt.isChecked()).apply();
                onBackPressed();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sved=false;
                onBackPressed();
            }
        });



    }
}
