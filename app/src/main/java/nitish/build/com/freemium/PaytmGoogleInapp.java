package nitish.build.com.freemium;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class PaytmGoogleInapp extends AppCompatActivity {

    String custid="", orderId="";
    String android_id="FAILED",mobile="FAILED";
    String sc_amount,sc_pay_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_google_inapp);

        Intent intent = getIntent();
        orderId = intent.getExtras().getString("orderid");
        custid = intent.getExtras().getString("custid");
        android_id = intent.getExtras().getString("android_id");
        mobile = intent.getExtras().getString("mobile");
        sc_amount = intent.getExtras().getString("amount");
        sc_pay_url = intent.getExtras().getString("pay_url");

        findViewById(R.id.img_pay_paytm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//

//                Intent intent=new Intent(getApplicationContext(),checksum.class);
//                intent.putExtra("orderid",orderId);
//                intent.putExtra("custid",custid);
//                intent.putExtra("android_id",android_id);
//                intent.putExtra("mobile",mobile);
//                intent.putExtra("amount",sc_amount+"");
//                intent.putExtra("pay_url",sc_pay_url+"");
//                startActivity(intent);


            }
        });

        findViewById(R.id.img_pay_gpay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                v.startAnimation(animation1);
                //-------------------------//

                        Intent intent2 = new Intent(getApplicationContext(),Select_Payment.class);
                        intent2.putExtra("android_id",android_id);
                        intent2.putExtra("mobile",mobile);
                        startActivity(intent2);


            }
        });

    }
}
