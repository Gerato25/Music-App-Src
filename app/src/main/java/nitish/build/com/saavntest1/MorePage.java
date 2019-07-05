package nitish.build.com.saavntest1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

public class MorePage extends AppCompatActivity {
    AdView mAdView;
    ImageView btn_set_more;
    Button btn_buyMeCoffee;
    TextView tv_changeLog,tv_TelegramGroup,tv_Github,tv_Faqs,tv_getIntouch;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Search_Songs.class));
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
    }

    public class ViewDialog {

        public void showDialog(Activity activity){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog_coffee);

            ImageView btn_close = dialog.findViewById(R.id.btn_bak_dialog);
            btn_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //------Animation-----------//
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(500);
                    v.startAnimation(animation1);
                    //-------------------------//
                    dialog.dismiss();
                }
            });

            RadioGroup rg_dialog = dialog.findViewById(R.id.rg_dialog);
            Button btn_proceed = dialog.findViewById(R.id.btn_proceed_dialog);
            btn_proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //------Animation-----------//
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(500);
                    v.startAnimation(animation1);
                    //-------------------------//
                    String amount = dialog.findViewById(rg_dialog.getCheckedRadioButtonId()).getTag().toString();
                    //25 --  https://p-y.tm/kHt-P4t
                    //55 --  https://p-y.tm/BdS-La9
                    //85 --  https://p-y.tm/cf4-xgQ
                    String url = "https://p-y.tm/BdS-La9";
                    if (amount.equals("25"))
                        url = "https://p-y.tm/kHt-P4t";
                    else if (amount.equals("55"))
                        url = "https://p-y.tm/BdS-La9";
                    else if (amount.equals("85"))
                        url = "https://p-y.tm/cf4-xgQ";

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }
            });



            dialog.show();

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_page);

//        mAdView = findViewById(R.id.adView_More);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        tv_changeLog = findViewById(R.id.tv_mor_cl);
        tv_Faqs = findViewById(R.id.tv_faqs);
        tv_getIntouch = findViewById(R.id.tv_getIntouch);
        tv_Github = findViewById(R.id.tv_github);
        tv_TelegramGroup = findViewById(R.id.tv_telegram);
        btn_buyMeCoffee = findViewById(R.id.btn_buy_me_cofee);

        btn_buyMeCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//
                ViewDialog alert = new ViewDialog();
                alert.showDialog(MorePage.this);
            }
        });

        tv_changeLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//
                String url = "https://github.com/NitishGadangi/Freemium-App/blob/master/change_log.md";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        tv_TelegramGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//
                String url = "https://t.me/joinchat/HH4B2xFVtt6_2hbJl_qKQA";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        tv_Github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//
                String url = "https://github.com/NitishGadangi/Freemium-App";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        tv_getIntouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//
                String url = "https://nitishgadangi.github.io/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        tv_Faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//
                String url = "https://github.com/NitishGadangi/Freemium-App/blob/master/FAQ's.md";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        btn_set_more = findViewById(R.id.btn_set_more);
        btn_set_more.setOnClickListener(new View.OnClickListener() {
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

        TextView tv_toGmail=findViewById(R.id.tv_toGmail);
        tv_toGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);
                //-------------------------//
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
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);
                //-------------------------//
                onBackPressed();
            }
        });

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
        startActivity(new Intent(getApplicationContext(),Downloads_Page.class));
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
    }
    public void btmMore(View v){
        //------Animation-----------//
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        //-------------------------//
//        startActivity(new Intent(getApplicationContext(),MorePage.class));
    }
}
