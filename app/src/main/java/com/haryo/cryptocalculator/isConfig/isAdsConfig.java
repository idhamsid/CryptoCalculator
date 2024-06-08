package com.haryo.cryptocalculator.isConfig;

import static com.haryo.cryptocalculator.isConfig.Settings.BANNER;
import static com.haryo.cryptocalculator.isConfig.Settings.COUNTER;
import static com.haryo.cryptocalculator.isConfig.Settings.INTER;
import static com.haryo.cryptocalculator.isConfig.Settings.INTERVAL;
import static com.haryo.cryptocalculator.isConfig.Settings.MAX_BANNER;
import static com.haryo.cryptocalculator.isConfig.Settings.MAX_INTERST;
import static com.haryo.cryptocalculator.isConfig.Settings.MAX_NATIV;
import static com.haryo.cryptocalculator.isConfig.Settings.NATIV;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdRevenueListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.adapters.google.BuildConfig;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinPrivacySettings;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkUtils;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.haryo.cryptocalculator.R;

import java.util.Map;

public class isAdsConfig {
    public static void initAds(Activity context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.d("MyApp", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status.getDescription(), status.getLatency()));
                    AppLovinPrivacySettings.setHasUserConsent(true, context);
                    AppLovinPrivacySettings.setIsAgeRestrictedUser(false, context);
                }
            }
        });


        AppLovinSdk.getInstance(context).setMediationProvider(AppLovinMediationProvider.MAX);
        AppLovinSdk.getInstance(context).initializeSdk(config -> {

        });
        AppLovinSdk sdk = AppLovinSdk.getInstance(context);
        sdk.getSettings().setMuted(!sdk.getSettings().isMuted());
        if(BuildConfig.DEBUG) AppLovinSdk.getInstance( context).showMediationDebugger();
    }

    private static IsAdsListener isListener;

    public interface IsAdsListener {
        void onClose();

        void onShow();

        void onNotShow();
    }

    public static void setIsAdsListener(IsAdsListener isAdsListener) {
        isListener = isAdsListener;
    }


    public static InterstitialAd mInterstitialAd;
    public static MaxInterstitialAd interstitialAd;

    public static void loadInters(Activity activity, Boolean show) {
        AdRequest request = new AdRequest.Builder()
                .build();
        InterstitialAd.load(activity, INTER, request,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
        interstitialAd = new MaxInterstitialAd(MAX_INTERST, activity);
        interstitialAd.loadAd();
    }

    public static void showInterst(Activity activity,Boolean showLoadDialog,Integer lamaLoad) {
        if (COUNTER >= INTERVAL) {
            if(!checkConnectivity(activity)){
                isListener.onNotShow();
                return;
            }
            if (showLoadDialog) {
                showDialog(activity);
                dialog.show();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (mInterstitialAd != null) {
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                isListener.onClose();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                                Log.e("adslog", "onAdFailedToShowFullScreenContent: admob ");
                                backupShowInters(activity,showLoadDialog);
                                if (showLoadDialog) {
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                isListener.onShow();
                                if (showLoadDialog) {
                                    dialog.dismiss();
                                }
                            }
                        });
                        mInterstitialAd.show(activity);
                    } else {
                        Log.e("adslog", "showInterst: admob null");
                        backupShowInters(activity,showLoadDialog);
                    }
                    loadInters(activity, false);
                    COUNTER = 0;
                }
            },lamaLoad*1000);

        } else{
            isListener.onNotShow();
            COUNTER++;
        }
    }

    private static void backupShowInters(Activity activity,Boolean showLoadDialog) {
        if (interstitialAd.isReady()) {
            interstitialAd.setListener(new MaxAdListener() {
                @Override
                public void onAdLoaded(MaxAd ad) {

                }

                @Override
                public void onAdDisplayed(MaxAd ad) {
                    Log.i("adslog", "onAdDisplayed: ");
                    isListener.onShow();
                    if (showLoadDialog) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onAdHidden(MaxAd ad) {
                    Log.i("adslog", "onAdHidden: ");
                    isListener.onClose();
                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                    Log.i("adslog", "onAdLoadFailed: ");
                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    Log.i("adslog", "onAdDisplayFailed: ");
                    isListener.onNotShow();
                    if (showLoadDialog) {
                        dialog.dismiss();
                    }
                }
            });
            interstitialAd.showAd();
            interstitialAd.loadAd();
        }
    }

    private static RewardListener rListener;

    public interface RewardListener {
        void onRewarded();
    }

    public void setIsAdsListener(RewardListener listener) {
        rListener = listener;
    }


    public static NativeAd nativeAd;
    public static MaxAd nativeAdMax;
    public static MaxNativeAdView nativeAdView;
    public static MaxNativeAdLoader nativeAdLoader;

    public static void callNative(Activity activity, RelativeLayout layNative, int layoutAdmobNative,
                                  int layoutMaxNative) {
        AdLoader.Builder builder = new AdLoader.Builder(activity, NATIV);
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull NativeAd nativeAds) {
                Log.i("adslog", "onNativeAdLoaded: " + NATIV);
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                if (nativeAdMax != null) {
                    nativeAdLoader.destroy(nativeAdMax);
                }
                nativeAd = nativeAds;
                NativeAdView adView = (NativeAdView) activity.getLayoutInflater()
                        .inflate(layoutAdmobNative, null);
                populateNativeAdView(nativeAds, adView);
                layNative.removeAllViews();
                layNative.addView(adView);
            }

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdRequest request = new AdRequest.Builder()
                .build();
        AdLoader adLoader = builder.withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        backupNative(activity, layNative,
                                layoutMaxNative);
                    }
                })
                .build();
        adLoader.loadAd(request);
    }

    public static void backupNative(Activity activity, RelativeLayout layNative,
                                    int layoutMaxNative) {
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(layoutMaxNative)
                .setTitleTextViewId(R.id.title_text_view)
                .setBodyTextViewId(R.id.body_text_view)
                .setAdvertiserTextViewId(R.id.advertiser_textView)
                .setIconImageViewId(R.id.icon_image_view)
                .setMediaContentViewGroupId(R.id.media_view_container)
                .setOptionsContentViewGroupId(R.id.ad_options_view)
                .setCallToActionButtonId(R.id.cta_button)
                .build();
        nativeAdView = new MaxNativeAdView(binder, activity);
        nativeAdLoader = new MaxNativeAdLoader(MAX_NATIV, activity);
        nativeAdLoader.setRevenueListener(new MaxAdRevenueListener() {
            @Override
            public void onAdRevenuePaid(MaxAd ad) {

            }
        });
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                isListener.onShow();
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                // Cleanup any pre-existing native ad to prevent memory leaks.
                if (nativeAdMax != null) {
                    nativeAdLoader.destroy(nativeAdMax);
                }

                // Save ad for cleanup.
                nativeAdMax = ad;

                // Add ad view to view.
                layNative.removeAllViews();
                layNative.addView(nativeAdView);
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                isListener.onNotShow();
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {

            }
        });
        nativeAdLoader.loadAd(nativeAdView);
    }

    private static void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
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
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
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
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.GONE);
        }
        adView.setNativeAd(nativeAd);
    }
    public static AdView adViewAdmob;
    public static MaxAdView adViewMax;

    public static void callBanner(Activity activity, RelativeLayout layAds){
        AdRequest request = new AdRequest.Builder().build();
        adViewAdmob = new AdView(activity);
        adViewAdmob.setAdUnitId(BANNER);
        layAds.addView(adViewAdmob);
        AdSize adSize = getAdSize(activity);
        adViewAdmob.setAdSize(adSize);
        adViewAdmob.loadAd(request);
        adViewAdmob.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (adViewMax != null) {
                    adViewMax.destroy();
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                adViewMax = new MaxAdView(MAX_BANNER, activity);
                adViewMax.setListener(new MaxAdViewAdListener() {
                    @Override
                    public void onAdExpanded(MaxAd ad) {

                    }

                    @Override
                    public void onAdCollapsed(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoaded(MaxAd ad) {

                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {

                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
                        AdRequest request = new AdRequest.Builder().build();
                        adViewAdmob = new AdView(activity);
                        adViewAdmob.setAdUnitId(BANNER);
                        layAds.addView(adViewAdmob);
                        AdSize adSize = getAdSize(activity);
                        adViewAdmob.setAdSize(adSize);
                        adViewAdmob.loadAd(request);
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                    }
                });
                final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
                final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
                adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
                layAds.addView(adViewMax);
                adViewMax.loadAd();
            }
        });
    }

    private static Dialog dialog;
    private static void showDialog(Activity activity) {
        dialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.load_activity);
    }
    public static void clearBanner(RelativeLayout adsLay){
        adsLay.removeAllViews();
    }
    private static AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }
    public static boolean checkConnectivity(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnected() && info.isAvailable();
    }
}
