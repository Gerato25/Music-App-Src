package nitish.build.com.saavntest1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.paytm.pg.merchant.CheckSumServiceHelper;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;



public class checksum extends AppCompatActivity  {

    String custid="", orderId="", mid="",str1,str2="TXN_SUCCESS";
    String android_id="FAILED",mobile="FAILED";
    PaytmPGService Service;
    String varifyurl;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_test);
        //initOrderId();


        Intent intent = getIntent();
        orderId = intent.getExtras().getString("orderid");
        custid = intent.getExtras().getString("custid");
        android_id = intent.getExtras().getString("android_id");
        mobile = intent.getExtras().getString("mobile");

        orderId="123564258";
        custid="541236851542";

        //production
        mid = "ohLFRd79849378749981";
        //staging
//        mid = "rxazcv89315285244163";

        varifyurl =  "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+orderId;





        Service = PaytmPGService.getProductionService();
//          Service = PaytmPGService.getStagingService();


         String checksum = "FAILED";
        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest sr = new StringRequest(Request.Method.POST,"http://www.jntuhspoorthi.com/nitishgadangi/generateChecksum.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("pipeline","prep: "+response);

                    String checksum =response;
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(checksum);
                        checksum = jsonObject.getString("CHECKSUMHASH");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    HashMap<String, String> paramMap = new HashMap<String, String>();
                    paramMap.put("MID", mid);
                    paramMap.put("ORDER_ID", orderId);
                    paramMap.put("CUST_ID", custid);
                    paramMap.put("CHANNEL_ID", "WAP");
                    paramMap.put("TXN_AMOUNT", "50.00");
                    paramMap.put("WEBSITE", "DEFAULT");
                    paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                    paramMap.put("CALLBACK_URL", varifyurl);
//                    paramMap.put("CHECKSUMHASH", checksum);
                    paramMap.put("CHECKSUMHASH", checksum);
                    PaytmOrder Order = new PaytmOrder(paramMap);

                    Service.initialize(Order, null);

                    Service.startPaymentTransaction(checksum.this, true, true, new PaytmPaymentTransactionCallback() {
                        @Override
                        public void onTransactionResponse(Bundle inResponse) {
                            Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                            Log.i("pipeline","tet: "+inResponse.toString());
                        }

                        @Override
                        public void networkNotAvailable() {
                            Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void clientAuthenticationFailed(String inErrorMessage) {
                            Toast.makeText(getApplicationContext(), "Authentication failed: Server error",  Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void someUIErrorOccurred(String inErrorMessage) {
                            Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                            Toast.makeText(getApplicationContext(), "Unable to load webpage " , Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onBackPressedCancelTransaction() {
                            Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                            Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("pipeline","prep: "+error);
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> paramMap = new HashMap<String, String>();
                    paramMap.put("MID", mid);
                    paramMap.put("ORDER_ID", orderId);
                    paramMap.put("CUST_ID", custid);
                    paramMap.put("CHANNEL_ID", "WAP");
                    paramMap.put("TXN_AMOUNT", "50.00");
                    paramMap.put("WEBSITE", "DEFAULT");
                    paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                    paramMap.put("CALLBACK_URL", varifyurl);

                    return paramMap;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(sr);

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("pipeline","tet: "+e.toString());
        }
        Log.i("pipeline","tet: "+checksum);
    }


}
