package nitish.build.com.saavntest1;

import android.Manifest;
import android.app.AlertDialog;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;


import java.io.File;
import java.io.IOException;




public class MainActivity extends AppCompatActivity {
    String finUrl,resName,resID,str_ptrn,str_NamePtrn,chanelId="test_chnl";
    TextView tv_Album_ID,tv_Album_Name;
    EditText et_url;
    int notificationID = 100;



//    String finString="failed";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main),MODE_PRIVATE);
        SharedPreferences.Editor editor=pref_main.edit();
        editor.clear();

        editor.commit();
        Toast.makeText(getApplicationContext(), "Beta Release", Toast.LENGTH_SHORT).show();
//        //-----------Permission part------------------//
//        String permission1 = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
//        String permission2 = android.Manifest.permission.READ_EXTERNAL_STORAGE;
//        if (getApplicationContext().checkCallingOrSelfPermission(permission1)== PackageManager.PERMISSION_DENIED){
//            ActivityCompat.requestPermissions(MainActivity.this,
//                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                1);
//        }
//        if (getApplicationContext().checkCallingOrSelfPermission(permission2)== PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    1);
//        }
//        //---------------Done Permission--------------//

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(MainActivity.this,Search_Songs.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, 1000);







    }





}
