package nitish.build.com.saavntest1;

import android.content.SharedPreferences;
import android.graphics.RadialGradient;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Settings_Alb extends AppCompatActivity {
    RadioGroup rg_kbps,rg_format;
    TextView tv_path;
    Button btn_save,btn_cancel;
    RadioButton rb_320,rb_160,rb_96,rb_m4a,rb_mp3;
    SharedPreferences pref_set;
    String d_kbps="320",d_format="m4a";
    String s_kbps="320",s_format="m4a";
    Boolean sved = false;


    @Override
    public void onBackPressed() {
        if (sved)
            Toast.makeText(getApplicationContext(), "Your Settings Saved.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Your settings wont be saved.", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_up  ,  R.anim.slide_out_up);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__alb);

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


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sved=true;
                s_kbps = findViewById(rg_kbps.getCheckedRadioButtonId()).getTag().toString();
                s_format = findViewById(rg_format.getCheckedRadioButtonId()).getTag().toString();
                SharedPreferences.Editor edit_set = pref_set.edit();
                edit_set.putString(getResources().getString(R.string.set_kbps),s_kbps).apply();
                edit_set.putString(getResources().getString(R.string.set_format),s_format).apply();
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
