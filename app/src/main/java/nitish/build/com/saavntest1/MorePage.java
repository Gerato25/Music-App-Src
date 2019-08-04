package nitish.build.com.saavntest1;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MorePage extends AppCompatActivity {
    AdView mAdView;
    ImageView btn_set_more;
    Button btn_buyMeCoffee;
    TextView tv_changeLog,tv_TelegramGroup,tv_Github,tv_Faqs,tv_getIntouch,tv_checkUpdates;
    ProgressDialog progressDialog;

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
                    String url = "https://p-y.tm/W8-4mek";
//                    if (amount.equals("25"))
//                        url = "https://p-y.tm/kHt-P4t";
//                    else if (amount.equals("55"))
//                        url = "https://p-y.tm/BdS-La9";
//                    else if (amount.equals("85"))
//                        url = "https://p-y.tm/cf4-xgQ";

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }
            });



            dialog.show();

        }
    }

    public class ViewDialog2 {
        String head,des,url;
        ViewDialog2(String head,String des,String url){
            this.head=head;
            this.des=des;
            this.url=url;
        }

        public void showDialog(Activity activity){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_update);

            TextView tv_head = dialog.findViewById(R.id.dialog_ud_head);
            TextView tv_des = dialog.findViewById(R.id.dialog_ud_des);
            Button btn_update = dialog.findViewById(R.id.dialog_ud_btn_ud);
            Button close = dialog.findViewById(R.id.ud_dialog_close);

            tv_des.setText(des);
            tv_head.setText(head);

            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //------Animation-----------//
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(500);
                    v.startAnimation(animation1);
                    //-------------------------//
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
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

            dialog.show();

        }
    }

    public class Updater extends AsyncTask<Void,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return DataHandlers.getContent("https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1yxVd1HRTBbO5ZjVHggjSEC3cLviRdJsMxojHODl6hSU&sheet=Sheet1");
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.contains("YES_THERE_IS")){
                Log.i("UPD_TEST","YES");
                try {
                    JSONObject obj = new JSONObject(s);
                    JSONArray jsonArray = obj.getJSONArray("Sheet1");
                    JSONObject mainObj = jsonArray.getJSONObject(0);
                    String url =mainObj.getString("URL");
                    String description = mainObj.getString("Description");
                    String version = mainObj.getString("Version");
                    String date = mainObj.getString("Date");

                    ViewDialog2 viewDialog=new ViewDialog2(version+"   "+date,description,url);
                    progressDialog.dismiss();
                    viewDialog.showDialog(MorePage.this);
                    SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_main), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref_main.edit();
                    editor.putBoolean(getResources().getString(R.string.pref_update),true).apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                progressDialog.dismiss();
                Toast.makeText(MorePage.this, "No Update Available", Toast.LENGTH_SHORT).show();
                Log.i("UPD_TEST","NO UPdate");
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_page);

        mAdView = findViewById(R.id.adView_More);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        tv_changeLog = findViewById(R.id.tv_mor_cl);
        tv_Faqs = findViewById(R.id.tv_faqs1);
        tv_getIntouch = findViewById(R.id.tv_getIntouch1);
        tv_Github = findViewById(R.id.tv_github1);
        tv_TelegramGroup = findViewById(R.id.tv_telegram1);
        btn_buyMeCoffee = findViewById(R.id.btn_buy_me_cofee);
        tv_checkUpdates = findViewById(R.id.tv_checkUpdates);

        progressDialog = new ProgressDialog(MorePage.this);
        progressDialog.setMessage("Checking..");
        progressDialog.setCancelable(false);
        progressDialog.dismiss();

        tv_checkUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//
                new Updater().execute();
            }
        });

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

        TextView tv_toGmail=findViewById(R.id.tv_toGmail1);
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
                intent.putExtra(Intent.EXTRA_SUBJECT, "Freemium Music App [v0.8b]")
                .putExtra(Intent.EXTRA_TEXT, "Required info:-\nIssue Description:\nAndroid OS version:\nDevice Name:");
                startActivity(Intent.createChooser(intent, "select Gmail"));
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
