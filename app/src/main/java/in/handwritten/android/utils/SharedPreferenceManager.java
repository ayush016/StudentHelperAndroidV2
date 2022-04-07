package in.handwritten.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceManager {

    public static String SHARED_PREFERENCE = "handwritten_shared_pref_login";
    public static String SHARED_PREFERENCE_EXTRA = "handwritten_shared_pref_extra";
    public static String GOOGLE_ACCOUNT_DATA_EMAIL = "google_account_data_email";
    public static String GOOGLE_ACCOUNT_DATA_NAME = "google_account_data_name";
    public static String TOOLTIP_DISPLAY_COUNTER = "tooltip_display_counter";
    public static String GOOGLE_ACCOUNT_DATA_IMAGE = "google_account_data_image";
    public static String SERVER_WAKE_UP_UTC = "server_wake_up_utc";
    public static String ONE_TIME_DOCUMENT_GENERATED = "one_time_document_generated";
    public static String PLAY_STORE_POP_UP_TIME = "play_store_popup_time";
    public static String USER_WENT_TO_PLAY_STORE = "user_went_to_play_store";

    public static void setUserEmail(Context context, String email){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GOOGLE_ACCOUNT_DATA_EMAIL, email);
        editor.apply();
    }

    public static void setUserName(Context context, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GOOGLE_ACCOUNT_DATA_NAME, name);
        editor.apply();
    }

    public static String getUserEmail(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        return sharedPreferences.getString(GOOGLE_ACCOUNT_DATA_EMAIL,null);
    }

    public static String getUserName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        return sharedPreferences.getString(GOOGLE_ACCOUNT_DATA_NAME,null);
    }

    public static void setTooltipDisplayCounter(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_EXTRA,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TOOLTIP_DISPLAY_COUNTER, getTooltipDisplayCounter(context)+1);
        editor.apply();
    }

    public static int getTooltipDisplayCounter(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_EXTRA,MODE_PRIVATE);
        return sharedPreferences.getInt(TOOLTIP_DISPLAY_COUNTER,0);
    }

    public static void setGoogleAccountDataImage(Context context,String googleAccountDataImage) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GOOGLE_ACCOUNT_DATA_IMAGE, googleAccountDataImage);
        editor.apply();
    }

    public static String getGoogleAccountDataImage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        return sharedPreferences.getString(GOOGLE_ACCOUNT_DATA_IMAGE,null);
    }

    public static void clearSharePreferenceData(Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void setServerWakeUpUtc(Context context, long serverWakeUpUTC){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(GOOGLE_ACCOUNT_DATA_NAME, serverWakeUpUTC);
        editor.apply();
    }

    public static Long getServerWakeUpUtc(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        return sharedPreferences.getLong(GOOGLE_ACCOUNT_DATA_EMAIL,-1);
    }

    public static void setOneTimeDocumentGenerated(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ONE_TIME_DOCUMENT_GENERATED, true);
        editor.apply();
    }

    public static boolean isOneTimeDocumentGenerated(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        return sharedPreferences.getBoolean(ONE_TIME_DOCUMENT_GENERATED,false);
    }

    public static void setUserWentToPlayStore(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_WENT_TO_PLAY_STORE, true);
        editor.apply();
    }

    public static boolean isUserWentToPlayStore(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        return sharedPreferences.getBoolean(USER_WENT_TO_PLAY_STORE,false);
    }

    public static void setPlayStorePopUpTime(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(PLAY_STORE_POP_UP_TIME, System.currentTimeMillis());
        editor.apply();
    }

    public static Long getPlayStorePopUpTime(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        return sharedPreferences.getLong(PLAY_STORE_POP_UP_TIME,-1L);
    }
}
