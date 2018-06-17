package com.example.neha.myapplication.Utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.globocom.geniogames_2018.Controller.MainApplication;
import com.globocom.geniogames_2018.Model.CountryCodeModel;
import com.globocom.geniogames_2018.Model.UserDetailsModel;
import com.globocom.geniogames_2018.R;
import com.globocom.geniogames_2018.Volley.MyVolleySingleton;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class AppUtilities {

    private static String countryCodeJsonArray = "[{\"name\":\"United Arab Emirates\",\"dial_code\":\"+971\",\"code\":\"AE\"},{\"name\":\"Sri Lanka\",\"dial_code\":\"+94\",\"code\":\"LK\"},{\"name\":\"Egypt\",\"dial_code\":\"+20\",\"code\":\"EG\"},{\"name\":\"Qatar\",\"dial_code\":\"+974\",\"code\":\"QA\"},{\"name\":\"Indonesia\",\"dial_code\":\"+62\",\"code\":\"ID\"},{\"name\":\"India\",\"dial_code\":\"+91\",\"code\":\"IN\"},{\"name\":\"Kuwait\",\"dial_code\":\"+965\",\"code\":\"KW\"}]";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static void sendAnalytics(Context mcontext, String screenName, String category, String action, String kpi, String url,
                                     String f_id, String f_name, String msisdn,

                                     int a_level, Object a_score, String a_submitPin,
                                     String a_pin_eventype, String a_sms_received, String a_pin_received) {

        SendAnalyticsInBackground sendAnalyticsInBackground = new SendAnalyticsInBackground(mcontext, screenName, category, action, kpi, url,
                f_id, f_name, msisdn,

                a_level, a_score, a_submitPin,
                a_pin_eventype, a_sms_received, a_pin_received);
        sendAnalyticsInBackground.execute();
    }


    public static void sendFirebaseAnalytics(Context context, String screenName, String category, String action, String kpi,
                                             String id, String name, String msisdn) {
        if (MainApplication.firebaseAnalytics == null) {
            MainApplication.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        Bundle bundle = new Bundle();
        bundle.putString("screenName", screenName);
        bundle.putString("category", category);
        bundle.putString("action", action);
        bundle.putString("kpi", kpi);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.VALUE, msisdn);
        bundle.putString(FirebaseAnalytics.Param.LEVEL, kpi);

        MainApplication.firebaseAnalytics.logEvent(kpi, bundle);
        MainApplication.firebaseAnalytics.setUserProperty("screenName",screenName);
        MainApplication.firebaseAnalytics.setUserProperty("valuen",msisdn);

        System.out.println("-analytics-sent----- Firebase");
    }

    public static void sendAppsFlyer(Context context, String screenName, String category, String action, String kpi,
                                     String msisdn, String submitPin, String pin_eventype, String sms_received,
                                     String pin_received, Object a_score, int a_level) {
        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.LEVEL, a_level);
        eventValue.put(AFInAppEventParameterName.SCORE, a_score);
        eventValue.put("screenName", screenName);
        eventValue.put("category", category);
        eventValue.put("action", action);
        eventValue.put("label", kpi);
        eventValue.put("MOBILE_NUMBER", msisdn);
        eventValue.put("APP_ID", AppSharedPrefSettings.getANDROID_ID(context));
        eventValue.put("UUID", AppSharedPrefSettings.getTrackingUuid(context));
        eventValue.put("REFERRER", AppSharedPrefSettings.getAppsflyerReferrer(context));

        if (!TextUtils.isEmpty(submitPin)) {
            eventValue.put("PIN_RECEIVED", submitPin);
        }
        if (!TextUtils.isEmpty(pin_eventype)) {
            eventValue.put("PIN_EVENT_TYPE", pin_eventype);
        }
        if (!TextUtils.isEmpty(sms_received)) {
            eventValue.put("SMS_RECEIVED", sms_received);
        }
        if (!TextUtils.isEmpty(pin_received)) {
            eventValue.put("PIN_RECEIVED", pin_received);
        }

        AppsFlyerLib.getInstance().trackEvent(context, kpi, eventValue);

        System.out.println("-analytics-sent----- AppsFlyerLib");
    }

    public static void sendInternalAnalytics(Context context, final String url, final String id) {
        if (!TextUtils.isEmpty(url)) {
            System.out.println("-analytics-sent----- InternalAnalytics");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("---internal-tracker-response-url-- " + url);
                            System.out.println("---internal-tracker-response-kpi-- " + id + "-> " + response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("---internal-tracker-error--- " + error.getMessage());
                }
            });
            MyVolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        }
    }

    public static Typeface applyTypeFace(Context context, String fontName) {
        Typeface typeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), fontName);
        return typeface;
    }


    public static class SendAnalyticsInBackground extends AsyncTask<Void, Void, Void> {
        Context context;
        String screenName, category, action, kpi, url, id, name, msisdn;
        String submitPin, pin_eventype, sms_received, pin_received;
        int a_level;
        Object a_score;

        SendAnalyticsInBackground(Context mcontext, String mscreenName, String mcategory, String maction, String mlabel, String murl,
                                  String mid, String mname, String mmsisdn,
                                  int aa_level, Object aa_score, String asubmitPin, String apin_eventype, String asms_received, String apin_received) {
            context = mcontext;
            screenName = mscreenName.toLowerCase();
            category = mcategory.toLowerCase();
            action = maction.toLowerCase();
            kpi = mlabel.toLowerCase();
            url = murl;


            id = mid;
            name = mname.toLowerCase();
            msisdn = mmsisdn;

            a_level = aa_level;
            a_score = aa_score;
            submitPin = asubmitPin;
            pin_eventype = apin_eventype;
            sms_received = asms_received.toLowerCase();
            pin_received = apin_received;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            sendFirebaseAnalytics(context, screenName, category, action, kpi, id, name, msisdn);
            sendAppsFlyer(context, screenName, category, action, kpi, msisdn, submitPin, pin_eventype,
                    sms_received, pin_received, a_score, a_level);
            sendInternalAnalytics(context, url, kpi);
            sendFacebook_AppLogger_Events(context, screenName, category, action, kpi, id, name, msisdn);
            return null;
        }
    }


    public static UserDetailsModel getUserModelData(Context context) {
        UserDetailsModel userDetailsModel = null;
        try {
            if (!TextUtils.isEmpty(AppSharedPrefSettings.getuserDetailsJSON(context))) {
                JSONObject jsonObject = new JSONObject(AppSharedPrefSettings.getuserDetailsJSON(context));
                if (jsonObject != null) {
                    userDetailsModel = new UserDetailsModel(jsonObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userDetailsModel;
    }

    public static void showAlertDialog(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setCancelable(false)
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });


        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }

    public static void showToastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public static String extractOTP(String sms, Integer length) {
        String[] nbs = sms.split("\\D+");
        if (nbs.length != 0) {
            for (String number : nbs) {
                if (number.matches("^[0-9]+$") && number.length() == length) {
                    return number;
                }
            }
        }
        return null;
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("---getipaddress--exception--- " + ex);
        } // for now eat exceptions
        return "";
    }

    public static boolean getRandPercent(int percent) {
        Random rand = new Random();
        return rand.nextInt(100) <= percent;
    }

    public static CountryCodeModel getCountryCodeModelByName(String cName) {
        CountryCodeModel countryCodeModel = null;
        try {
            JSONArray jsonArray = new JSONArray(countryCodeJsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                CountryCodeModel cModel = new CountryCodeModel(jsonArray.optJSONObject(i));
                if (cModel != null && !TextUtils.isEmpty(cModel.countryCode)
                        && cModel.countryCode.equalsIgnoreCase(cName)) {
                    return cModel;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countryCodeModel;

    }

    public static ArrayList<CountryCodeModel> getCountryCodeModelArrayByName() {
        ArrayList<CountryCodeModel> countryCodeModels = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(countryCodeJsonArray);


            for (int i = 0; i < jsonArray.length(); i++) {
                CountryCodeModel cModel = new CountryCodeModel(jsonArray.optJSONObject(i));
                if (cModel != null && !TextUtils.isEmpty(cModel.countryCode)) {
                    countryCodeModels.add(cModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(countryCodeModels, new Comparator<CountryCodeModel>() {
            @Override
            public int compare(CountryCodeModel s1, CountryCodeModel s2) {
                return s1.countryName.compareToIgnoreCase(s2.countryName);
            }
        });

        return countryCodeModels;

    }

    public static int getFlagMasterResID(String cCode) {
        switch (cCode.toLowerCase()) {
            //this should be sorted based on country name code.
            case "ad": //andorra
                return R.drawable.flag_andorra;
            case "ae": //united arab emirates
                return R.drawable.flag_uae;
            case "af": //afghanistan
                return R.drawable.flag_afghanistan;
            case "ag": //antigua & barbuda
                return R.drawable.flag_antigua_and_barbuda;
            case "ai": //anguilla // Caribbean Islands
                return R.drawable.flag_anguilla;
            case "al": //albania
                return R.drawable.flag_albania;
            case "am": //armenia
                return R.drawable.flag_armenia;
            case "ao": //angola
                return R.drawable.flag_angola;
            case "aq": //antarctica // custom
                return R.drawable.flag_antarctica;
            case "ar": //argentina
                return R.drawable.flag_argentina;
            case "at": //austria
                return R.drawable.flag_austria;
            case "au": //australia
                return R.drawable.flag_australia;
            case "aw": //aruba
                return R.drawable.flag_aruba;
            case "az": //azerbaijan
                return R.drawable.flag_azerbaijan;
            case "ba": //bosnia and herzegovina
                return R.drawable.flag_bosnia;
            case "bb": //barbados
                return R.drawable.flag_barbados;
            case "bd": //bangladesh
                return R.drawable.flag_bangladesh;
            case "be": //belgium
                return R.drawable.flag_belgium;
            case "bf": //burkina faso
                return R.drawable.flag_burkina_faso;
            case "bg": //bulgaria
                return R.drawable.flag_bulgaria;
            case "bh": //bahrain
                return R.drawable.flag_bahrain;
            case "bi": //burundi
                return R.drawable.flag_burundi;
            case "bj": //benin
                return R.drawable.flag_benin;
            case "bl": //saint barthélemy
                return R.drawable.flag_saint_barthelemy;// custom
            case "bm": //bermuda
                return R.drawable.flag_bermuda;
            case "bn": //brunei darussalam // custom
                return R.drawable.flag_brunei;
            case "bo": //bolivia, plurinational state of
                return R.drawable.flag_bolivia;
            case "br": //brazil
                return R.drawable.flag_brazil;
            case "bs": //bahamas
                return R.drawable.flag_bahamas;
            case "bt": //bhutan
                return R.drawable.flag_bhutan;
            case "bw": //botswana
                return R.drawable.flag_botswana;
            case "by": //belarus
                return R.drawable.flag_belarus;
            case "bz": //belize
                return R.drawable.flag_belize;
            case "ca": //canada
                return R.drawable.flag_canada;
            case "cc": //cocos (keeling) islands
                return R.drawable.flag_cocos;// custom
            case "cd": //congo, the democratic republic of the
                return R.drawable.flag_democratic_republic_of_the_congo;
            case "cf": //central african republic
                return R.drawable.flag_central_african_republic;
            case "cg": //congo
                return R.drawable.flag_republic_of_the_congo;
            case "ch": //switzerland
                return R.drawable.flag_switzerland;
            case "ci": //côte d\'ivoire
                return R.drawable.flag_cote_divoire;
            case "ck": //cook islands
                return R.drawable.flag_cook_islands;
            case "cl": //chile
                return R.drawable.flag_chile;
            case "cm": //cameroon
                return R.drawable.flag_cameroon;
            case "cn": //china
                return R.drawable.flag_china;
            case "co": //colombia
                return R.drawable.flag_colombia;
            case "cr": //costa rica
                return R.drawable.flag_costa_rica;
            case "cu": //cuba
                return R.drawable.flag_cuba;
            case "cv": //cape verde
                return R.drawable.flag_cape_verde;
            case "cx": //christmas island
                return R.drawable.flag_christmas_island;
            case "cy": //cyprus
                return R.drawable.flag_cyprus;
            case "cz": //czech republic
                return R.drawable.flag_czech_republic;
            case "de": //germany
                return R.drawable.flag_germany;
            case "dj": //djibouti
                return R.drawable.flag_djibouti;
            case "dk": //denmark
                return R.drawable.flag_denmark;
            case "dm": //dominica
                return R.drawable.flag_dominica;
            case "do": //dominican republic
                return R.drawable.flag_dominican_republic;
            case "dz": //algeria
                return R.drawable.flag_algeria;
            case "ec": //ecuador
                return R.drawable.flag_ecuador;
            case "ee": //estonia
                return R.drawable.flag_estonia;
            case "eg": //egypt
                return R.drawable.flag_egypt;
            case "er": //eritrea
                return R.drawable.flag_eritrea;
            case "es": //spain
                return R.drawable.flag_spain;
            case "et": //ethiopia
                return R.drawable.flag_ethiopia;
            case "fi": //finland
                return R.drawable.flag_finland;
            case "fj": //fiji
                return R.drawable.flag_fiji;
            case "fk": //falkland islands (malvinas)
                return R.drawable.flag_falkland_islands;
            case "fm": //micronesia, federated states of
                return R.drawable.flag_micronesia;
            case "fo": //faroe islands
                return R.drawable.flag_faroe_islands;
            case "fr": //france
                return R.drawable.flag_france;
            case "ga": //gabon
                return R.drawable.flag_gabon;
            case "gb": //united kingdom
                return R.drawable.flag_united_kingdom;
            case "gd": //grenada
                return R.drawable.flag_grenada;
            case "ge": //georgia
                return R.drawable.flag_georgia;
            case "gf": //guyane
                return R.drawable.flag_guyane;
            case "gh": //ghana
                return R.drawable.flag_ghana;
            case "gi": //gibraltar
                return R.drawable.flag_gibraltar;
            case "gl": //greenland
                return R.drawable.flag_greenland;
            case "gm": //gambia
                return R.drawable.flag_gambia;
            case "gn": //guinea
                return R.drawable.flag_guinea;
            case "gp": //guadeloupe
                return R.drawable.flag_guadeloupe;
            case "gq": //equatorial guinea
                return R.drawable.flag_equatorial_guinea;
            case "gr": //greece
                return R.drawable.flag_greece;
            case "gt": //guatemala
                return R.drawable.flag_guatemala;
            case "gw": //guinea-bissau
                return R.drawable.flag_guinea_bissau;
            case "gy": //guyana
                return R.drawable.flag_guyana;
            case "hk": //hong kong
                return R.drawable.flag_hong_kong;
            case "hn": //honduras
                return R.drawable.flag_honduras;
            case "hr": //croatia
                return R.drawable.flag_croatia;
            case "ht": //haiti
                return R.drawable.flag_haiti;
            case "hu": //hungary
                return R.drawable.flag_hungary;
            case "id": //indonesia
                return R.drawable.flag_indonesia;
            case "ie": //ireland
                return R.drawable.flag_ireland;
            case "il": //israel
                return R.drawable.flag_israel;
            case "im": //isle of man
                return R.drawable.flag_isleof_man; // custom
            case "is": //Iceland
                return R.drawable.flag_iceland;
            case "in": //india
                return R.drawable.flag_india;
            case "iq": //iraq
                return R.drawable.flag_iraq_new;
            case "ir": //iran, islamic republic of
                return R.drawable.flag_iran;
            case "it": //italy
                return R.drawable.flag_italy;
            case "jm": //jamaica
                return R.drawable.flag_jamaica;
            case "jo": //jordan
                return R.drawable.flag_jordan;
            case "jp": //japan
                return R.drawable.flag_japan;
            case "ke": //kenya
                return R.drawable.flag_kenya;
            case "kg": //kyrgyzstan
                return R.drawable.flag_kyrgyzstan;
            case "kh": //cambodia
                return R.drawable.flag_cambodia;
            case "ki": //kiribati
                return R.drawable.flag_kiribati;
            case "km": //comoros
                return R.drawable.flag_comoros;
            case "kn": //st kitts & nevis
                return R.drawable.flag_saint_kitts_and_nevis;
            case "kp": //north korea
                return R.drawable.flag_north_korea;
            case "kr": //south korea
                return R.drawable.flag_south_korea;
            case "kw": //kuwait
                return R.drawable.flag_kuwait;
            case "ky": //Cayman_Islands
                return R.drawable.flag_cayman_islands;
            case "kz": //kazakhstan
                return R.drawable.flag_kazakhstan;
            case "la": //lao people\'s democratic republic
                return R.drawable.flag_laos;
            case "lb": //lebanon
                return R.drawable.flag_lebanon;
            case "lc": //st lucia
                return R.drawable.flag_saint_lucia;
            case "li": //liechtenstein
                return R.drawable.flag_liechtenstein;
            case "lk": //sri lanka
                return R.drawable.flag_sri_lanka;
            case "lr": //liberia
                return R.drawable.flag_liberia;
            case "ls": //lesotho
                return R.drawable.flag_lesotho;
            case "lt": //lithuania
                return R.drawable.flag_lithuania;
            case "lu": //luxembourg
                return R.drawable.flag_luxembourg;
            case "lv": //latvia
                return R.drawable.flag_latvia;
            case "ly": //libya
                return R.drawable.flag_libya;
            case "ma": //morocco
                return R.drawable.flag_morocco;
            case "mc": //monaco
                return R.drawable.flag_monaco;
            case "md": //moldova, republic of
                return R.drawable.flag_moldova;
            case "me": //montenegro
                return R.drawable.flag_of_montenegro;// custom
            case "mg": //madagascar
                return R.drawable.flag_madagascar;
            case "mh": //marshall islands
                return R.drawable.flag_marshall_islands;
            case "mk": //macedonia, the former yugoslav republic of
                return R.drawable.flag_macedonia;
            case "ml": //mali
                return R.drawable.flag_mali;
            case "mm": //myanmar
                return R.drawable.flag_myanmar;
            case "mn": //mongolia
                return R.drawable.flag_mongolia;
            case "mo": //macao
                return R.drawable.flag_macao;
            case "mq": //martinique
                return R.drawable.flag_martinique;
            case "mr": //mauritania
                return R.drawable.flag_mauritania;
            case "ms": //montserrat
                return R.drawable.flag_montserrat;
            case "mt": //malta
                return R.drawable.flag_malta;
            case "mu": //mauritius
                return R.drawable.flag_mauritius;
            case "mv": //maldives
                return R.drawable.flag_maldives;
            case "mw": //malawi
                return R.drawable.flag_malawi;
            case "mx": //mexico
                return R.drawable.flag_mexico;
            case "my": //malaysia
                return R.drawable.flag_malaysia;
            case "mz": //mozambique
                return R.drawable.flag_mozambique;
            case "na": //namibia
                return R.drawable.flag_namibia;
            case "nc": //new caledonia
                return R.drawable.flag_new_caledonia;// custom
            case "ne": //niger
                return R.drawable.flag_niger;
            case "ng": //nigeria
                return R.drawable.flag_nigeria;
            case "ni": //nicaragua
                return R.drawable.flag_nicaragua;
            case "nl": //netherlands
                return R.drawable.flag_netherlands;
            case "no": //norway
                return R.drawable.flag_norway;
            case "np": //nepal
                return R.drawable.flag_nepal;
            case "nr": //nauru
                return R.drawable.flag_nauru;
            case "nu": //niue
                return R.drawable.flag_niue;
            case "nz": //new zealand
                return R.drawable.flag_new_zealand;
            case "om": //oman
                return R.drawable.flag_oman;
            case "pa": //panama
                return R.drawable.flag_panama;
            case "pe": //peru
                return R.drawable.flag_peru;
            case "pf": //french polynesia
                return R.drawable.flag_french_polynesia;
            case "pg": //papua new guinea
                return R.drawable.flag_papua_new_guinea;
            case "ph": //philippines
                return R.drawable.flag_philippines;
            case "pk": //pakistan
                return R.drawable.flag_pakistan;
            case "pl": //poland
                return R.drawable.flag_poland;
            case "pm": //saint pierre and miquelon
                return R.drawable.flag_saint_pierre;
            case "pn": //pitcairn
                return R.drawable.flag_pitcairn_islands;
            case "pr": //puerto rico
                return R.drawable.flag_puerto_rico;
            case "ps": //palestine
                return R.drawable.flag_palestine;
            case "pt": //portugal
                return R.drawable.flag_portugal;
            case "pw": //palau
                return R.drawable.flag_palau;
            case "py": //paraguay
                return R.drawable.flag_paraguay;
            case "qa": //qatar
                return R.drawable.flag_qatar;
            case "re": //la reunion
                return R.drawable.flag_martinique; // no exact flag found
            case "ro": //romania
                return R.drawable.flag_romania;
            case "rs": //serbia
                return R.drawable.flag_serbia; // custom
            case "ru": //russian federation
                return R.drawable.flag_russian_federation;
            case "rw": //rwanda
                return R.drawable.flag_rwanda;
            case "sa": //saudi arabia
                return R.drawable.flag_saudi_arabia;
            case "sb": //solomon islands
                return R.drawable.flag_soloman_islands;
            case "sc": //seychelles
                return R.drawable.flag_seychelles;
            case "sd": //sudan
                return R.drawable.flag_sudan;
            case "se": //sweden
                return R.drawable.flag_sweden;
            case "sg": //singapore
                return R.drawable.flag_singapore;
            case "sh": //saint helena, ascension and tristan da cunha
                return R.drawable.flag_saint_helena; // custom
            case "si": //slovenia
                return R.drawable.flag_slovenia;
            case "sk": //slovakia
                return R.drawable.flag_slovakia;
            case "sl": //sierra leone
                return R.drawable.flag_sierra_leone;
            case "sm": //san marino
                return R.drawable.flag_san_marino;
            case "sn": //senegal
                return R.drawable.flag_senegal;
            case "so": //somalia
                return R.drawable.flag_somalia;
            case "sr": //suriname
                return R.drawable.flag_suriname;
            case "st": //sao tome and principe
                return R.drawable.flag_sao_tome_and_principe;
            case "sv": //el salvador
                return R.drawable.flag_el_salvador;
            case "sx": //sint maarten
                return R.drawable.flag_sint_maarten;
            case "sy": //syrian arab republic
                return R.drawable.flag_syria;
            case "sz": //swaziland
                return R.drawable.flag_swaziland;
            case "tc": //turks & caicos islands
                return R.drawable.flag_turks_and_caicos_islands;
            case "td": //chad
                return R.drawable.flag_chad;
            case "tg": //togo
                return R.drawable.flag_togo;
            case "th": //thailand
                return R.drawable.flag_thailand;
            case "tj": //tajikistan
                return R.drawable.flag_tajikistan;
            case "tk": //tokelau
                return R.drawable.flag_tokelau; // custom
            case "tl": //timor-leste
                return R.drawable.flag_timor_leste;
            case "tm": //turkmenistan
                return R.drawable.flag_turkmenistan;
            case "tn": //tunisia
                return R.drawable.flag_tunisia;
            case "to": //tonga
                return R.drawable.flag_tonga;
            case "tr": //turkey
                return R.drawable.flag_turkey;
            case "tt": //trinidad & tobago
                return R.drawable.flag_trinidad_and_tobago;
            case "tv": //tuvalu
                return R.drawable.flag_tuvalu;
            case "tw": //taiwan, province of china
                return R.drawable.flag_taiwan;
            case "tz": //tanzania, united republic of
                return R.drawable.flag_tanzania;
            case "ua": //ukraine
                return R.drawable.flag_ukraine;
            case "ug": //uganda
                return R.drawable.flag_uganda;
            case "us": //united states
                return R.drawable.flag_united_states_of_america;
            case "uy": //uruguay
                return R.drawable.flag_uruguay;
            case "uz": //uzbekistan
                return R.drawable.flag_uzbekistan;
            case "va": //holy see (vatican city state)
                return R.drawable.flag_vatican_city;
            case "vc": //st vincent & the grenadines
                return R.drawable.flag_saint_vicent_and_the_grenadines;
            case "ve": //venezuela, bolivarian republic of
                return R.drawable.flag_venezuela;
            case "vg": //british virgin islands
                return R.drawable.flag_british_virgin_islands;
            case "vi": //us virgin islands
                return R.drawable.flag_us_virgin_islands;
            case "vn": //vietnam
                return R.drawable.flag_vietnam;
            case "vu": //vanuatu
                return R.drawable.flag_vanuatu;
            case "wf": //wallis and futuna
                return R.drawable.flag_wallis_and_futuna;
            case "ws": //samoa
                return R.drawable.flag_samoa;
            case "xk": //kosovo
                return R.drawable.flag_kosovo;
            case "ye": //yemen
                return R.drawable.flag_yemen;
            case "yt": //mayotte
                return R.drawable.flag_martinique; // no exact flag found
            case "za": //south africa
                return R.drawable.flag_south_africa;
            case "zm": //zambia
                return R.drawable.flag_zambia;
            case "zw": //zimbabwe
                return R.drawable.flag_zimbabwe;
            default:
                return R.drawable.flag_transparent;
        }
    }

    public static void sendFacebook_AppLogger_Events(Context context, String screenName, String category, String action,
                                                     String kpi, String id, String name, String msisdn) {
        try {
            if (MainApplication.appEventsLogger == null) {
                MainApplication.appEventsLogger = AppEventsLogger.newLogger(context);
            }
            Bundle bundle = new Bundle();
            bundle.putString("screenName", screenName);
            bundle.putString("category", category);
            bundle.putString("action", action);
            bundle.putString("kpi", kpi);
            bundle.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, id);
            bundle.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, name);
            bundle.putString(AppEventsConstants.EVENT_PARAM_CONTENT, msisdn);
            bundle.putString(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, kpi);
            bundle.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "USD");

            MainApplication.appEventsLogger.logEvent(
                    kpi,
                    1.0,
                    bundle
            );

            System.out.println("-analytics-sent----- Facebook_AppLogger_Events");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("-facebookEventLogger-exception---- " + e);
        }
    }

    public static Spannable applyTwoFontsForSingleText(String firstText, Typeface typefaceFirst, String secondText, Typeface typefaceSecond) {
        Spannable spannable = new SpannableString(firstText + secondText);
        spannable.setSpan(new CustomTypefaceSpan("", typefaceFirst), 0, firstText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("", typefaceSecond), firstText.length(), firstText.length() + secondText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static String userTrackingID(Context context) {
        String id = AppSharedPrefSettings.getuserTrackingId(context);
        if (TextUtils.isEmpty(id)) {
            id = UUID.randomUUID().toString();
            AppSharedPrefSettings.setTrackingUuid(context, id);
        }
        return id;
    }
}
