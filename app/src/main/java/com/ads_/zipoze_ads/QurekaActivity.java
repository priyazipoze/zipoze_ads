package com.ads_.zipoze_ads;

import static com.ads_.zipoze_ads.AD.qurekalink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

public class QurekaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qureka);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();

        customTabsIntent.intent.setPackage("com.android.chrome");
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.black));
        builder.setShowTitle(true);
        builder.addDefaultShareMenuItem();
        customTabsIntent.intent.setData(Uri.parse(qurekalink));
        startActivityForResult(customTabsIntent.intent,51,customTabsIntent.startAnimationBundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is same as what is passed  here it is 1
        if (requestCode == 51) {
            finish();
            InterstitialAds.mSetOnClosed.onClosed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        InterstitialAds.mSetOnClosed.onClosed();
    }

}