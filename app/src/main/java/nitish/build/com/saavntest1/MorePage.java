package nitish.build.com.saavntest1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MorePage extends AppCompatActivity {
    AdView mAdView;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Search_Songs.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_page);

        mAdView = findViewById(R.id.adView_More);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        TextView tv_toGmail=findViewById(R.id.tv_toGmail);
        tv_toGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "apps.nitish@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Freemium Music App");
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        Button btn_bck = findViewById(R.id.btn_morBak);
        btn_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void btmSrch(View v){
        startActivity(new Intent(getApplicationContext(),Search_Songs.class));
    }
    public void btmBrws(View v){
        startActivity(new Intent(getApplicationContext(),SaavnWebView.class));
    }
    public void btmDown(View v){
        startActivity(new Intent(getApplicationContext(),Downloads_Page.class));
    }
    public void btmMore(View v){
//        startActivity(new Intent(getApplicationContext(),MorePage.class));
    }
}
