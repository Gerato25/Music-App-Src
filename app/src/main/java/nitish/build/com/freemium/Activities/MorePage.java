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

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.noties.markwon.Markwon;
import nitish.build.com.freemium.Handlers.DataHandlers;
import nitish.build.com.freemium.R;

public class MorePage extends AppCompatActivity {
    AdView mAdView;
    ImageView btn_set_more;
    Button btn_buyMeCoffee;
    TextView tv_changeLog,tv_TelegramGroup,tv_Github,tv_Faqs,tv_getIntouch,tv_checkUpdates;
    ProgressDialog progressDialog;
    String banner4;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), SearchSongs.class));
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
            SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.server_constants),MODE_PRIVATE);
            String sc_amount=pref_main.getString(getResources().getString(R.string.sc_amount),"50.00");
            String sc_pay_url=pref_main.getString(getResources().getString(R.string.sc_pay_link),"http://www.jntuhspoorthi.com/nitishgadangi/generateChecksum.php");

            TextView tv_pay50 = dialog.findViewById(R.id.dialog_tv_pay50);
            tv_pay50.setText("pay ₹"+sc_amount.replace(".00","")+" in next step");

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
//                    String amount = dialog.findViewById(rg_dialog.getCheckedRadioButtonId()).getTag().toString();
//                    String url = "https://p-y.tm/W8-4mek";
//
////                    try {
////                        String out=DataHandlers.getContent("https://script.google.com/macros/s/AKfycbxEZZ5oejQCAt2iw_Ck3dOZeSxOVoE0" +
////                                "OvViPmKpy9_a7PYkAEg/exec?mobile=898989898989&android_id=xxxxxxxxxxxxxxxxxxx&id=10Tj7i5utEXaBoJpo74eY" +
////                                "dc2sH1jtGoEx-bBiVNwcpAo");
////                        dialog.dismiss();
////                        Toast.makeText(MorePage.this, out, Toast.LENGTH_SHORT).show();
////
////                    }catch (Exception e){
////                        e.printStackTrace();
////                    }
//
//                    Random random = new Random();
//                    String i_custID = System.currentTimeMillis()+"";
//                    String i_orderID = System.currentTimeMillis()+"@"+String.format("%04d", random.nextInt(10000));
//                    String unique_id = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//
//                    EditText et = dialog.findViewById(R.id.et_payment_dialog);
//                    String num = et.getText().toString();
//
////                    SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.server_constants),MODE_PRIVATE);
////                    String sc_amount=pref_main.getString(getResources().getString(R.string.sc_amount),"50.00");
////                    String sc_pay_url=pref_main.getString(getResources().getString(R.string.sc_pay_link),"http://www.jntuhspoorthi.com/nitishgadangi/generateChecksum.php");
//
//
//                    if (num.length()>1){
//                    Intent intent=new Intent(getApplicationContext(),PaytmGoogleInapp.class);
//                    intent.putExtra("orderid",num+"_"+i_orderID);
//                    intent.putExtra("custid",num+"@"+i_custID);
//                    intent.putExtra("android_id",unique_id);
//                    intent.putExtra("mobile",num+"");
//                    intent.putExtra("amount",sc_amount+"");
//                    intent.putExtra("pay_url",sc_pay_url+"");
//
//                    dialog.dismiss();
//                    startActivity(intent);
//
////                        Intent intent2 = new Intent(getApplicationContext(),SelectPayment.class);
////                        intent2.putExtra("android_id",unique_id);
////                        intent2.putExtra("mobile",num+"");
////                        startActivity(intent2);
//                    }else {
//                        Toast.makeText(MorePage.this, "enter correct mobile number", Toast.LENGTH_SHORT).show();
//                    }

                    String url = "https://nitishgadangi.github.io/?buy_me_coffee";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }
            });

            dialog.findViewById(R.id.tv_coffee_already).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //------Animation-----------//
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(500);
                    v.startAnimation(animation1);
                    //-------------------------//

                    String unique_id = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "appsbynitish@gmail.com" });
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Freemium Music App [v1.5] Stable")
                            .putExtra(Intent.EXTRA_TEXT, "PaymentHash:"+unique_id+"\n\nRequired info:-\nPlease Enter the Mobile number used for payment:");
                    startActivity(Intent.createChooser(intent, "select Gmail"));
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

    public class ViewDialog3 {
        String head,subhead;

        ViewDialog3(String head,String subhead){
            this.head=head;
            this.subhead=subhead;
        }

        public void showDialog(Activity activity){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_more_info);


            Button close = dialog.findViewById(R.id.dia_mor_close);

            TextView tv_head = dialog.findViewById(R.id.dia_mor_head);
            TextView tv_subH = dialog.findViewById(R.id.dia_mor_subHead);

            tv_head.setText(head);

            final Markwon markwon = Markwon.create(getApplicationContext());
            markwon.setMarkdown(tv_subH, subhead);

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


    public class ThanksDialog {


        public void showDialog(Activity activity){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.more_thank_you);

            dialog.findViewById(R.id.more_TU_gmail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String unique_id = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "appsbynitish@gmail.com" });
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Freemium Music App [v1.5] Stable")
                            .putExtra(Intent.EXTRA_TEXT, "PaymentHash:"+unique_id+"\n\nRequired info:-\nPlease Enter the Mobile number used for payment:");
                    startActivity(Intent.createChooser(intent, "select Gmail"));
                }
            });

            dialog.findViewById(R.id.more_TU_rate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = getResources().getString(R.string.playstore_link);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            dialog.findViewById(R.id.thank_you_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            progressDialog.setMessage("checking...");
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

            if(s.contains("YES_OPENAPP1")){
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


    public class Faqs extends AsyncTask<Void,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Fetching...");
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
                Log.i("UPD_TEST","YES");
                try {
                    JSONObject obj = new JSONObject(s);
                    JSONArray jsonArray = obj.getJSONArray("Sheet1");
                    JSONObject mainObj = jsonArray.getJSONObject(0);
                    String faqs = mainObj.getString("faqs");

                    ViewDialog3 viewDialog= new ViewDialog3("FAQ's",faqs);
                    progressDialog.dismiss();
                    viewDialog.showDialog(MorePage.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MorePage.this, "Error: Unable to fetch faqs.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    void showAds(Boolean show,String AD_UNIT_ID){
//        mAdView = findViewById(R.id.adView_search);
        mAdView = new AdView(this);
        if (show){
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(AD_UNIT_ID);
            AdRequest adRequest = new AdRequest.Builder().build();
            if(mAdView.getAdSize() != null || mAdView.getAdUnitId() != null)
                mAdView.loadAd(adRequest);
            LinearLayout linearLayout = findViewById(R.id.ad_layout_more);
            linearLayout.addView(mAdView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_page);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkAccent));
        }

//        mAdView = findViewById(R.id.adView_More);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        SharedPreferences sc_pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.server_constants),Context.MODE_PRIVATE);
        banner4 =sc_pref.getString(getResources().getString(R.string.sc_banner4),getResources().getString(R.string.banner4));
        Boolean thopu = sc_pref.getBoolean(getResources().getString(R.string.sc_thope),false);
        if (!thopu)
            showAds(true,banner4);

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


        findViewById(R.id.img_icon_morepage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MorePage.this, "icon designed by apkfolks.com", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.img_icon_morepage).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MorePage.this, "Version Code 4", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

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

        if (thopu)
            btn_buyMeCoffee.setBackgroundResource(R.drawable.tq_koo_fi);
        btn_buyMeCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//
                SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.server_constants),MODE_PRIVATE);
                Boolean thopu = pref_main.getBoolean(getResources().getString(R.string.sc_thope),false);
                if (thopu){
//                    ThanksDialog thanksDialog = new ThanksDialog();
//                    thanksDialog.showDialog(MorePage.this);
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(MorePage.this);
                }else{
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(MorePage.this);
                }

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
//                String url = "https://github.com/NitishGadangi/Freemium-App/blob/master/change_log.md";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
                String tempStr = "*First stable release*\n" +
                        "* Improved all UI elements to make it smooth.\n" +
                        "* New updated downloads tab to browse all the downloaded stuff.\n" +
                        "* Automatic search results is enabled.\n" +
                        "* Updated the Change Storage feature in settings.\n" +
                        "* Add Auto delete albumart option in settings.\n" +
                        "* Now you can buy me a coffee ☕ and enjoy complete Ad-Free experience.";

                ViewDialog3 viewDialog3=new ViewDialog3("Change Log",tempStr);
                viewDialog3.showDialog(MorePage.this);
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



                new Faqs().execute();

//                String url = "https://github.com/NitishGadangi/Freemium-App/blob/master/FAQ's.md";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
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

                startActivity(new Intent(getApplicationContext(), SettingsAlbum.class));
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

                String device_name = android.os.Build.MANUFACTURER+"_"+android.os.Build.MODEL;
                String os_version= android.os.Build.VERSION.RELEASE;

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "appsbynitish@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Freemium Music App [v1.5] Stable")
                .putExtra(Intent.EXTRA_TEXT, "Android OS version:"+os_version+"\nDevice Name:"+device_name+"\nIssue Description:\n");
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

        findViewById(R.id.tv_toPlaystore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);
                //-------------------------//
                String url = getResources().getString(R.string.playstore_link);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

    }

    public void btmSrch(View v){
        //------Animation-----------//
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        v.startAnimation(animation1);
        //-------------------------//
        startActivity(new Intent(getApplicationContext(), SearchSongs.class));
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
        startActivity(new Intent(getApplicationContext(), DownloadsPage.class));
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
