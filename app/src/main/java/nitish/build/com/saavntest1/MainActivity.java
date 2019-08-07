package nitish.build.com.saavntest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.core.app.ActivityCompat;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


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
//        Toast.makeText(getApplicationContext(), "Beta Release", Toast.LENGTH_SHORT).show();
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

        ImageView splash = findViewById(R.id.ic_splash);
        YoYo.with(Techniques.FadeIn)
                .duration(700)
                .playOn(splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(MainActivity.this,Search_Songs.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        }, 700);







    }





}
