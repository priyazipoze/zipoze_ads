
package com.ads_.zipoze_ads;

import static com.ads_.zipoze_ads.AD.Nativead;
import static com.ads_.zipoze_ads.AD.fbnativead;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.nativead.NativeCustomFormatAd;

import java.util.ArrayList;
import java.util.List;

public class NativeAds {
    private Activity mActivity;
    private FrameLayout mLayout;

    private NativeCustomFormatAd nativeCustomFormatAd;
    private com.google.android.gms.ads.nativead.NativeAd nativeAd1;
    private final String TAG = "NativeAdActivity".getClass().getSimpleName();

    private NativeAd nativeAd;
    private com.facebook.ads.NativeAdLayout nativeAdLayout1;
    private LinearLayout ladView;

    public NativeAds(Activity mActivity, FrameLayout mLayout) {
        this.mActivity = mActivity;
        this.mLayout = mLayout;
    }


    public void show() {
        if (AD.prirotyisFb) {
            if (AD.Facebookshow) {
                loadFBNativeAd(true);
            } else if (AD.Admobshow) {
                loadGoogleNativeAd( true);
            }

        } else {
            if (AD.Admobshow) {
                loadGoogleNativeAd( true);
            } else if (AD.Facebookshow) {
                loadFBNativeAd(true);
            }
        }

    }


    public void loadFBNativeAd(boolean isFirst) {
        nativeAd = new NativeAd(mActivity, fbnativead);
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                if (isFirst)
                    if (AD.Admobshow){
                        loadGoogleNativeAd(false);

                    }

                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container


                inflateAd(nativeAd);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        };
        // Request an ad
        nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());
    }

    private void inflateAd(NativeAd nativeAd) {
        nativeAd.unregisterView();
        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        // Inflate the Ad view. The layout referenced should be the one you created in the last step.
        ladView = (LinearLayout) inflater.inflate(R.layout.custom_native_ad_layout, nativeAdLayout1, false);
//        nativeAdLayout1 = ladView.findViewById(R.id.native_ad_container);
        mLayout.addView(ladView);


        LinearLayout adChoicesContainer = ladView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(mActivity, nativeAd, nativeAdLayout1);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);
        // Create native UI using the ad metadata.
        com.facebook.ads.MediaView nativeAdIcon = ladView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = ladView.findViewById(R.id.native_ad_title);
        com.facebook.ads.MediaView nativeAdMedia = ladView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = ladView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = ladView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = ladView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = ladView.findViewById(R.id.native_ad_call_to_action);
        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                ladView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
    }


    private void populateNativeAdView(com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView((com.google.android.gms.ads.nativead.MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Updates the UI to say whether or not this ad has a video asset.
        if (nativeAd.getMediaContent() != null && nativeAd.getMediaContent().hasVideoContent()) {
            VideoController vc = nativeAd.getMediaContent().getVideoController();

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
//                    refresh.setEnabled(true);
//                    videoStatus.setText("Video status: Video playback has ended.");
                    super.onVideoEnd();
                }
            });
        } else {
//            videoStatus.setText("Video status: Ad does not contain a video asset.");
//            refresh.setEnabled(true);
        }
    }


    private void loadGoogleNativeAd(boolean isFirst) {

        MobileAds.initialize(mActivity,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus status) {
                    }
                });

        AdLoader.Builder builder = new AdLoader.Builder(mActivity, Nativead);


        builder.forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(com.google.android.gms.ads.nativead.NativeAd nativeAd) {

                boolean isDestroyed = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    isDestroyed = mActivity.isDestroyed();
                }
                if (isDestroyed || mActivity.isFinishing() || mActivity.isChangingConfigurations()) {
                    nativeAd.destroy();
                    return;
                }

                if (nativeAd1 != null) {
                    nativeAd1.destroy();
                }
                nativeAd1 = nativeAd;

                NativeAdView adView = (NativeAdView) mActivity.getLayoutInflater().inflate(R.layout.ad_unified, mLayout, false);
                populateNativeAdView(nativeAd, adView);
                mLayout.removeAllViews();
                mLayout.addView(adView);
            }
        });

        VideoOptions videoOptions =
                new VideoOptions.Builder().build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
//                                        refresh.setEnabled(true);
                        if (isFirst)
                            if (AD.Facebookshow){
                                loadFBNativeAd(false);
                            }

                    }
                })
                .build();

        adLoader.loadAd(new AdManagerAdRequest.Builder().build());

    }


}

