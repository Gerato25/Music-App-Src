package nitish.build.com.saavntest1;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Settings_Alb extends AppCompatActivity {
    RadioGroup rg_kbps,rg_format;
    TextView tv_path,set_down_path;
    Button btn_save,btn_cancel;
    RadioButton rb_320,rb_160,rb_96,rb_m4a,rb_mp3;
    SharedPreferences pref_set;
    String d_kbps="320",d_format="m4a";
    String s_kbps="320",s_format="m4a";
    Boolean sved = false,s_aA_hidden,d_aA_hidden;
    CheckBox cb_hide_albumArt;
    Boolean is_fromAlb = false;


    @Override
    public void onBackPressed() {
        if (sved)
            Toast.makeText(getApplicationContext(), "Your Settings Saved.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Your settings wont be saved.", Toast.LENGTH_SHORT).show();
        if (is_fromAlb){
            Intent to_init = new Intent(getApplicationContext(),Search_Songs.class);
            to_init.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(to_init);
            overridePendingTransition(R.anim.slide_in_up  ,  R.anim.slide_out_up);
        }else {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_up  ,  R.anim.slide_out_up);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__alb);

        Intent fromAlb = getIntent();
        is_fromAlb =fromAlb.getBooleanExtra("fromAlb",false);

        rg_kbps = findViewById(R.id.rg_set_kbps);
        rg_format = findViewById(R.id.rg_set_format);
        tv_path = findViewById(R.id.set_down_path);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        rb_96 = findViewById(R.id.set_96);
        rb_160 = findViewById(R.id.set_160);
        rb_320 = findViewById(R.id.set_320);
        rb_m4a = findViewById(R.id.set_m4a);
        rb_mp3 = findViewById(R.id.set_mp3);

        pref_set = getApplicationContext().getSharedPreferences(getResources().getString(R.string.set_main),MODE_PRIVATE);
        d_format = pref_set.getString(getResources().getString(R.string.set_format),"m4a");
        d_kbps = pref_set.getString(getResources().getString(R.string.set_kbps),"320");
        d_aA_hidden = pref_set.getBoolean(getResources().getString(R.string.set_AlbumArt),false);
        cb_hide_albumArt = findViewById(R.id.cb_hide_albumArt);
        if (d_aA_hidden)
            cb_hide_albumArt.setChecked(true);


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

        set_down_path = findViewById(R.id.set_down_path);
        set_down_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//

                Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/FREEMIUM_DOWNLOADS/");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(selectedUri, "resource/folder");

                if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
                {
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please Install a File Manager.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        cb_hide_albumArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                    cb_hide_albumArt.setChecked(false);
                    new AlertDialog.Builder(Settings_Alb.this)
                            .setTitle("Sorry \uD83D\uDE14 !")
                            .setMessage("This feature is currently enabled only for devices with Android Oreo and Above.")
                            .setPositiveButton("Ok", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setCancelable(false)
                            .show();
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sved=true;
                s_kbps = findViewById(rg_kbps.getCheckedRadioButtonId()).getTag().toString();
                s_format = findViewById(rg_format.getCheckedRadioButtonId()).getTag().toString();
                if (cb_hide_albumArt.isChecked())
                    s_aA_hidden=true;
                else
                    s_aA_hidden = false;
                SharedPreferences.Editor edit_set = pref_set.edit();
                edit_set.putString(getResources().getString(R.string.set_kbps),s_kbps).apply();
                edit_set.putString(getResources().getString(R.string.set_format),s_format).apply();
                edit_set.putBoolean(getResources().getString(R.string.set_AlbumArt),s_aA_hidden).apply();
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
