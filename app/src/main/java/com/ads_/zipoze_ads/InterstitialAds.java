package com.ads_.zipoze_ads;


import static com.ads_.zipoze_ads.AD.MAX_TIME;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


public class InterstitialAds {
    public interface setOnClosed {
        void onClosed();
    }

    private static InterstitialAd mGoogleInterstitialAd = null;
    private static com.facebook.ads.InterstitialAd mFBInterstitialAd = null;
    private static InterstitialAdListener mInterstitialAdListener;
    private static Activity mActivity;
    public static setOnClosed mSetOnClosed;
    private static long CURR_TIME = 0;
    private static Handler mHandler = new Handler();
    private static Runnable mRunnable;

    public static void BackpressInterShow(Activity activity, setOnClosed setOnClosed) {

        if(AD.isbackpress) {
            mSetOnClosed = setOnClosed;
            mActivity = activity;

            if (AD.prirotyisFb) {
                if (AD.Facebookshow) {
                    loadFB(mSetOnClosed, true);
                } else if (AD.Admobshow) {
                    showGoogle(mSetOnClosed, true);
                } else if (AD.Qurekashow) {
                    mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
                }
                else {
                    setOnClosed.onClosed();
                }
            } else {
                if (AD.Admobshow) {
                    showGoogle(mSetOnClosed, true);
                } else if (AD.Facebookshow) {
                    loadFB(mSetOnClosed, true);
                } else if (AD.Qurekashow) {
                    mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
                }
                else {
                    setOnClosed.onClosed();
                }
            }
        }
        else {
            setOnClosed.onClosed();
        }
    }
    public static void loadGoogle() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(MyApplication.getInstance(), AD.Interad, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mGoogleInterstitialAd = interstitialAd;
                Log.i("InterstitialAds", "GG onAdLoaded");
                mGoogleInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d("InterstitialAds", "GG Ad was clicked.");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Set the ad reference to null so you don't show the ad a second time.
                        Log.d("InterstitialAds", "GG Ad dismissed fullscreen content.");
                        mGoogleInterstitialAd = null;
                        loadGoogle();
                        onClosed(mSetOnClosed);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        Log.e("InterstitialAds", "GG Ad failed to show fullscreen content.");
                        mGoogleInterstitialAd = null;
                    }

                    @Override
                    public void onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d("InterstitialAds", "GG Ad recorded an impression.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d("InterstitialAds", "GG Ad showed fullscreen content.");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.d("InterstitialAds", "GG Error : " + loadAdError.toString());
                mGoogleInterstitialAd = null;

            }
        });
    }

    private static void loadFB(setOnClosed mSetOnClosed, boolean isFirst) {
        mFBInterstitialAd = new com.facebook.ads.InterstitialAd(MyApplication.getInstance(), AD.fbinterad);
        mInterstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e("InterstitialAds", "FB Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e("InterstitialAds", "FB Interstitial ad dismissed.");
                onClosed(mSetOnClosed);
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                // Ad error callback

                Log.e("InterstitialAds", "FB Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("InterstitialAds", "FB Interstitial ad is loaded and ready to be displayed!");

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d("InterstitialAds", "FB Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d("InterstitialAds", "FB Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        mFBInterstitialAd.loadAd(mFBInterstitialAd.buildLoadAdConfig().withAdListener(mInterstitialAdListener).build());

        showFB(mSetOnClosed, isFirst);
    }
    private static void loadFB2(setOnClosed mSetOnClosed, boolean isFirst) {
        mFBInterstitialAd = new com.facebook.ads.InterstitialAd(MyApplication.getInstance(), AD.fbinterad2);
        mInterstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e("InterstitialAds", "FB Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e("InterstitialAds", "FB Interstitial ad dismissed.");
                onClosed(mSetOnClosed);

            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                // Ad error callback

                Log.e("InterstitialAds", "FB Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("InterstitialAds", "FB Interstitial ad is loaded and ready to be displayed!");

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d("InterstitialAds", "FB Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d("InterstitialAds", "FB Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        mFBInterstitialAd.loadAd(mFBInterstitialAd.buildLoadAdConfig().withAdListener(mInterstitialAdListener).build());

        showFB(mSetOnClosed, isFirst);
    }
    private static void loadFB3(setOnClosed mSetOnClosed, boolean isFirst) {
        mFBInterstitialAd = new com.facebook.ads.InterstitialAd(MyApplication.getInstance(), AD.fbinterad3);
        mInterstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e("InterstitialAds", "FB Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e("InterstitialAds", "FB Interstitial ad dismissed.");
                onClosed(mSetOnClosed);

            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                // Ad error callback

                Log.e("InterstitialAds", "FB Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("InterstitialAds", "FB Interstitial ad is loaded and ready to be displayed!");

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d("InterstitialAds", "FB Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d("InterstitialAds", "FB Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        mFBInterstitialAd.loadAd(mFBInterstitialAd.buildLoadAdConfig().withAdListener(mInterstitialAdListener).build());

        showFB(mSetOnClosed, isFirst);
    }
    private static void loadFB4(setOnClosed mSetOnClosed, boolean isFirst) {
        mFBInterstitialAd = new com.facebook.ads.InterstitialAd(MyApplication.getInstance(), AD.fbinterad4);
        mInterstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e("InterstitialAds", "FB Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e("InterstitialAds", "FB Interstitial ad dismissed.");
                onClosed(mSetOnClosed);

            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                // Ad error callback

                Log.e("InterstitialAds", "FB Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("InterstitialAds", "FB Interstitial ad is loaded and ready to be displayed!");

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d("InterstitialAds", "FB Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d("InterstitialAds", "FB Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        mFBInterstitialAd.loadAd(mFBInterstitialAd.buildLoadAdConfig().withAdListener(mInterstitialAdListener).build());

        showFB(mSetOnClosed, isFirst);
    }
    private static void loadFB5(setOnClosed mSetOnClosed, boolean isFirst) {
        mFBInterstitialAd = new com.facebook.ads.InterstitialAd(MyApplication.getInstance(), AD.fbinterad5);
        mInterstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e("InterstitialAds", "FB Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e("InterstitialAds", "FB Interstitial ad dismissed.");
                onClosed(mSetOnClosed);

            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                // Ad error callback

                Log.e("InterstitialAds", "FB Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("InterstitialAds", "FB Interstitial ad is loaded and ready to be displayed!");

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d("InterstitialAds", "FB Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d("InterstitialAds", "FB Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        mFBInterstitialAd.loadAd(mFBInterstitialAd.buildLoadAdConfig().withAdListener(mInterstitialAdListener).build());

        showFB(mSetOnClosed, isFirst);
    }


    public static void show(Activity activity, setOnClosed setOnClosed) {
        mSetOnClosed = setOnClosed;
        mActivity = activity;
        if (AD.prirotyisFb) {
            if (AD.Facebookshow) {
                loadFB(mSetOnClosed, true);
            } else if (AD.Admobshow) {
                showGoogle(mSetOnClosed, true);
            } else if (AD.Qurekashow) {
                mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
            }else{
                setOnClosed.onClosed();
            }
        } else {
            if (AD.Admobshow) {
                showGoogle(mSetOnClosed, true);
            } else if (AD.Facebookshow) {
                loadFB(mSetOnClosed, true);
            } else if (AD.Qurekashow) {
                mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
            }else{
                setOnClosed.onClosed();
            }
        }
    }

    public static void show2(Activity activity, setOnClosed setOnClosed) {
        mSetOnClosed = setOnClosed;
        mActivity = activity;
        if (AD.prirotyisFb) {
            if (AD.Facebookshow) {
                loadFB2(mSetOnClosed, true);
            } else if (AD.Admobshow) {
                showGoogle(mSetOnClosed, true);
            } else if (AD.Qurekashow) {
                mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
            }else{
                setOnClosed.onClosed();
            }

        } else {


            if (AD.Admobshow) {
                showGoogle(mSetOnClosed, true);
            } else if (AD.Facebookshow) {
                loadFB2(mSetOnClosed, true);
            } else if (AD.Qurekashow) {
                mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
            }else{
                setOnClosed.onClosed();
            }
        }
    }


    public static void show3(Activity activity, setOnClosed setOnClosed) {
        mSetOnClosed = setOnClosed;
        mActivity = activity;
        if (AD.prirotyisFb) {
            if (AD.Facebookshow) {
                loadFB3(mSetOnClosed, true);
            } else if (AD.Admobshow) {
                showGoogle(mSetOnClosed, true);
            } else if (AD.Qurekashow) {
                mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
            }else{
                setOnClosed.onClosed();
            }

        } else {


            if (AD.Admobshow) {
                showGoogle(mSetOnClosed, true);
            } else if (AD.Facebookshow) {
                loadFB3(mSetOnClosed, true);
            } else if (AD.Qurekashow) {
                mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
            }else{
                setOnClosed.onClosed();
            }
        }
    }

    public static void show4(Activity activity, setOnClosed setOnClosed) {
        mSetOnClosed = setOnClosed;
        mActivity = activity;
        if (AD.prirotyisFb) {
            if (AD.Facebookshow) {
                loadFB4(mSetOnClosed, true);
            } else if (AD.Admobshow) {
                showGoogle(mSetOnClosed, true);
            } else if (AD.Qurekashow) {
                mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
            }else{
                setOnClosed.onClosed();
            }

        } else {


            if (AD.Admobshow) {
                showGoogle(mSetOnClosed, true);
            } else if (AD.Facebookshow) {
                loadFB4(mSetOnClosed, true);
            } else if (AD.Qurekashow) {
                mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
            }else{
                setOnClosed.onClosed();
            }
        }
    }

    public static void show5(Activity activity, setOnClosed setOnClosed) {
        mSetOnClosed = setOnClosed;
        mActivity = activity;
        if (AD.prirotyisFb) {
            if (AD.Facebookshow) {
                loadFB5(mSetOnClosed, true);
            } else if (AD.Admobshow) {
                showGoogle(mSetOnClosed, true);
            } else if (AD.Qurekashow) {
                mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
            }else{
                setOnClosed.onClosed();
            }

        } else {


            if (AD.Admobshow) {
                showGoogle(mSetOnClosed, true);
            } else if (AD.Facebookshow) {
                loadFB5(mSetOnClosed, true);
            } else if (AD.Qurekashow) {
                mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
            }else{
                setOnClosed.onClosed();
            }
        }
    }

    private static void showFB(setOnClosed mSetOnClosed, boolean isFirst) {
       // Log.d("InterstitialAds", "showFB()......");
        CURR_TIME = 0;
        mRunnable = () -> {
            Log.d("InterstitialAds", "showFB()......1");
           // Log.e("maxfaxab2", String.valueOf(MAX_TIME));
            if (CURR_TIME > MAX_TIME) {
                Log.d("InterstitialAds", "showFB()......Timer Finish");
                if (isFirst)
                {
                    if (AD.Admobshow){
                        if (mGoogleInterstitialAd!=null)
                        {
                            showGoogle(mSetOnClosed, false);
                        } else if (AD.Qurekashow) {
                            mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
                        }
                        else onClosed(mSetOnClosed);
                    }
                    else if (AD.Qurekashow){
                        mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
                    }
                    else onClosed(mSetOnClosed);

                }
                else onClosed(mSetOnClosed);
                return;
            }

            if (mFBInterstitialAd == null) {
                Log.d("InterstitialAds", "showFB()......FB Null");
                if (isFirst)
                {
                    if (AD.Admobshow){
                        showGoogle(mSetOnClosed, false);
                    }
                    else if (AD.Qurekashow){
                        mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
                    }

                }
                else onClosed(mSetOnClosed);
                return;
            }

            if (mFBInterstitialAd.isAdLoaded()) {
                Log.d("InterstitialAds", "showFB()......Showwwwwwwww");
                mFBInterstitialAd.show();
                return;
            }
            CURR_TIME = CURR_TIME + 100;
            mHandler.postDelayed(mRunnable, CURR_TIME);
        };
        mHandler.postDelayed(mRunnable, CURR_TIME);
    }

    private static void showGoogle(setOnClosed mSetOnClosed, boolean isFirst) {
        CURR_TIME = 0;

        mRunnable = () -> {
            if (CURR_TIME > MAX_TIME) {
                if (mGoogleInterstitialAd == null) {
                    loadGoogle();
                    return;
                }
                if (isFirst)
                {
                    if (AD.Facebookshow){
                        if (mFBInterstitialAd.isAdLoaded()){
                            showFB(mSetOnClosed, false);
                        }
                        else if (AD.Qurekashow){
                            mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
                        }
                        else onClosed(mSetOnClosed);
                    }
                    else if (AD.Qurekashow){
                        mActivity.startActivityForResult(new Intent(mActivity, QurekaActivity.class), 523);
                    }
                    else onClosed(mSetOnClosed);

                }
                else onClosed(mSetOnClosed);
                return;

            }

            if (mGoogleInterstitialAd != null) {
                mGoogleInterstitialAd.show(mActivity);
                return;
            }
            CURR_TIME = CURR_TIME + 100;
            mHandler.postDelayed(mRunnable, CURR_TIME);
        };
        mHandler.postDelayed(mRunnable, CURR_TIME);
    }


    private static void onClosed(setOnClosed mSetOnClosed) {
        mFBInterstitialAd = null;
        Log.w("InterstitialAds", "onClosed ...1");
        if (mSetOnClosed != null) {
            Log.w("InterstitialAds", "onClosed ...2");
            mSetOnClosed.onClosed();
        }
    }

}
