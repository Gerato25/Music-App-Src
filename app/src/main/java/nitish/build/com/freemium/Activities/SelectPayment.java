package nitish.build.com.freemium.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

import nitish.build.com.freemium.Handlers.DataHandlers;
import nitish.build.com.freemium.R;

public class SelectPayment extends AppCompatActivity {

    private BillingClient billingClient;

    String android_id="FAILED",mobile="FAILED",fin_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__payment);


        Intent intent = getIntent();
        android_id = intent.getExtras().getString("android_id");
        mobile = intent.getExtras().getString("mobile");

        fin_post = "https://script.google.com/macros/s/AKfycbxEZZ5oejQCAt2iw_Ck3dOZeSxOVoE0OvViPmKpy9_a7PYkAEg/exec?mobile=" +mobile+
                "&android_id=" +android_id+
                "&id=10Tj7i5utEXaBoJpo74eYdc2sH1jtGoEx-bBiVNwcpAo";



        billingClient = BillingClient.newBuilder(this).setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                        && purchases != null) {
                    for (Purchase purchase : purchases) {
                        handlePurchase(purchase);
                    }
                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                    // Handle an error caused by a user cancelling the purchase flow.
                    Toast.makeText(getApplicationContext(), "USER CANCELLED!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MorePage.class));
                } else {
                    if (billingResult.getResponseCode()==BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED){
                        Toast.makeText(getApplicationContext(), "Already Purchased! Contact Developer", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),MorePage.class));
                    }
                    Toast.makeText(getApplicationContext(), "Error!"+billingResult.getResponseCode()+ " Contact Developer", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),MorePage.class));
                }


            }
        }).enablePendingPurchases().build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.

                    List<String> skuList = new ArrayList<>();
                    skuList.add("buy_me_coffee");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult,
                                                                 List<SkuDetails> skuDetailsList) {
                                    // Process the result.

                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                        for (SkuDetails skuDetails : skuDetailsList) {
                                            String sku = skuDetails.getSku();
                                            String price = skuDetails.getPrice();
                                            if ("buy_me_coffee".equals(sku)) {
                                                Log.i("BILL_TEST",price+"1");
                                                Log.i("BILL_TEST",sku+"1");

                                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                        .setSkuDetails(skuDetails)
                                                        .build();
                                                billingClient.launchBillingFlow(SelectPayment.this,flowParams);

                                            }
                                        }
                                    }

                                }
                            });


                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Toast.makeText(getApplicationContext(), "Try again!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user.
            DataHandlers.getContent(fin_post);
            Toast.makeText(getApplicationContext(), "Transaction Success " , Toast.LENGTH_LONG).show();
            SharedPreferences pref_main = getApplicationContext().getSharedPreferences(getResources().getString(R.string.server_constants),MODE_PRIVATE);
            SharedPreferences.Editor editor = pref_main.edit();
            editor.putBoolean(getResources().getString(R.string.sc_thope),true).apply();
            Intent toRes = new Intent(getApplicationContext(),MorePage.class);
            startActivity(toRes);


            // Acknowledge the purchase if it hasn't already been acknowledged.
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                        Log.i("BILL_TEST",billingResult.toString()+"1");
                    }
                });
            }
        }
    }
}
