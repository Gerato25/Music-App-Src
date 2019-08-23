package nitish.build.com.saavntest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import androidx.core.app.ActivityCompat;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    String finUrl,resName,resID,str_ptrn,str_NamePtrn,chanelId="test_chnl";
    TextView tv_Album_ID,tv_Album_Name;
    EditText et_url;
    int notificationID = 100;



//    String finString="failed";

    //https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=10Tj7i5utEXaBoJpo74eYdc2sH1jtGoEx-bBiVNwcpAo&sheet=Sheet1
    //https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1f-u1MlaVpJKKL1JFIV-g6unzs5YyvBIGA1fpiph3umU&sheet=Sheet1

    class InfoUpdate extends AsyncTask<Void,Void,String[]>{
        @Override
        protected String[] doInBackground(Void... voids) {
            Log.i("Thop_Stuff","1:started");
            String thopulu = DataHandlers.getContent("https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=10Tj7i5utEXaBoJpo74eYdc2sH1jtGoEx-bBiVNwcpAo&sheet=Sheet1");
//            Log.i("Thop_Stuff","1:"+thopulu);

           //String serverID="1FZQ5RujUUWoPvU7byPsq-oCrLsUE_bghZa6vYBKzbS0"; //production
            String serverID="1f-u1MlaVpJKKL1JFIV-g6unzs5YyvBIGA1fpiph3umU"; //Staging

            String serverValues = DataHandlers.getContent("https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id="+serverID+"&sheet=Sheet1");
//            Log.i("Thop_Stuff","2:"+serverValues);

            String unique_id = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            String[] values = new String[12];
            values[0]="FAILED";
            if (thopulu.contains(unique_id)){
                values[0] = "true";
            }else
                values[0]= "false";
            try{
                JSONObject tempObj = new JSONObject(serverValues);
                JSONArray tempArr = tempObj.getJSONArray("Sheet1");
                tempObj=tempArr.getJSONObject(0);
                values[1]=tempObj.getString("app_id");
                values[2]=tempObj.getString("banner1");//search
                values[3]=tempObj.getString("inter1");
                values[4]=tempObj.getString("amount");
                values[5]=tempObj.getString("pay_link");
                values[6]=tempObj.getString("inter2");//albumInter
                values[7]=tempObj.getString("banner2");//album
                values[8]=tempObj.getString("banner3");//down
                values[9]=tempObj.getString("banner4");//more
                values[10]=tempObj.getString("banner5");//bowse
                values[11]=tempObj.getString("banner6");//settings
            }catch (Exception e){
                e.printStackTrace();
                values[0]="FAILED";
            }

            return values;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if (!strings[0].equals("FAILED")){
                SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.server_constants),MODE_PRIVATE);
                SharedPreferences.Editor editor=pref_main.edit();
                if (strings[0].equals("true"))
                    editor.putBoolean(getResources().getString(R.string.sc_thope),true).apply();
                else
                    editor.putBoolean(getResources().getString(R.string.sc_thope),false).apply();
                editor.putString(getResources().getString(R.string.sc_app_id),strings[1]).apply();
                editor.putString(getResources().getString(R.string.sc_banner1),strings[2]).apply();
                editor.putString(getResources().getString(R.string.sc_inter1),strings[3]).apply();
                editor.putString(getResources().getString(R.string.sc_amount),strings[4].replace("i","")).apply();
                editor.putString(getResources().getString(R.string.sc_pay_link),strings[5]).apply();
                editor.putString(getResources().getString(R.string.sc_inter2),strings[6]).apply();
                editor.putString(getResources().getString(R.string.sc_banner2),strings[7]).apply();
                editor.putString(getResources().getString(R.string.sc_banner3),strings[8]).apply();
                editor.putString(getResources().getString(R.string.sc_banner4),strings[9]).apply();
                editor.putString(getResources().getString(R.string.sc_banner5),strings[10]).apply();
                editor.putString(getResources().getString(R.string.sc_banner6),strings[11]).apply();
//                Log.i("Thop_Stuff","2:"+strings[0]+strings[1]+strings[2]+strings[3]+strings[4]+strings[5]);
            }
        }
    }

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkAccent));
        }

        new InfoUpdate().execute();

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
