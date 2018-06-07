
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using admob;
public class AdScript : MonoBehaviour {

	// Use this for initialization
	void Start () {
        initAdmob();
        Admob.Instance().showBannerRelative(AdSize.SmartBanner, AdPosition.BOTTOM_CENTER, 0);
	}
	
	// Update is called once per frame
	void Update () {
			}

    Admob ad;
    void initAdmob()
    {

        ad = Admob.Instance();
        ad.bannerEventHandler += onBannerEvent;
        ad.interstitialEventHandler += onInterstitialEvent;
        ad.rewardedVideoEventHandler += onRewardedVideoEvent;
        ad.nativeBannerEventHandler += onNativeBannerEvent;
        ad.initAdmob("ca-app-pub-3940256099942544/6300978111", "ca-app-pub-3940256099942544/6300978111");//all id are admob test id,change those to your
        //ad.setTesting(true);//show test ad
        //ad.setNonPersonalized(true);//if want load NonPersonalized only,set true
        // ad.setIsDesignedForFamilies(true);//if is Is Designed For Families set true
        //ad.setGender(AdmobGender.MALE);
        //string[] keywords = { "game", "crash", "male game" };
        //  ad.setKeywords(keywords);//set keywords for ad
        Debug.Log("admob inited -------------");

    }

    void onInterstitialEvent(string eventName, string msg)
    {
        Debug.Log("handler onAdmobEvent---" + eventName + "   " + msg);
        if (eventName == AdmobEvent.onAdLoaded)
        {
            Admob.Instance().showInterstitial();
        }
    }
    void onBannerEvent(string eventName, string msg)
    {
        Debug.Log("handler onAdmobBannerEvent---" + eventName + "   " + msg);
    }
    void onRewardedVideoEvent(string eventName, string msg)
    {
        Debug.Log("handler onRewardedVideoEvent---" + eventName + "  rewarded: " + msg);
    }
    void onNativeBannerEvent(string eventName, string msg)
    {
        Debug.Log("handler onAdmobNativeBannerEvent---" + eventName + "   " + msg);
    }

  
}
