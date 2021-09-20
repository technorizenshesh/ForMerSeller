package com.my.formerseller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Preference {

public static final String APP_PREF = "KapsiePreferences";

public static SharedPreferences sp;
public static String KEY_USER_ID = "TutorIdid";
public static String KEY_check_status = "check_status";
public static String KEY_username = "username";
public static String KEY_Profile_image = "profile_image";
public static String KEY_Address_id = "address_id";
public static String KEY_location_id = "location_id";
public static String KEY_location_addreess = "addreess";
public static String KEY_tutor_category_id = "tutor_category_id";
public static String KEY_tutor_category_sub_id = "tutor_category_sub_id ";
public static String KEY_tutor_category_subJECT_id = "KEY_tutor_category_subJECT_id ";
public static String KEY_tutor_category_subJECT_Name = "KEY_tutor_category_subJECT_name ";
public static String KEY_USER_email = "email";
public static String KEY_CategoryId = "id";
public static String KEY_ReceiverId = "receiver_id";
public static String KEY_SenderId = "sender_id";
public static String KEY_student_Name = "student_name";
public static String KEY_amount = "amount";
public static String KEY_Address = "pic";
public static String KEYType_login = "social";
public static String KEY_DEsCriptionFinal = "DEsCriptionFinal";
public static String key_PlaceUser_address = "address_place";
public static String key_switch_shift_change = "shift_change";
public static String KEY_Ordertype= "Ordertype";
public static String KEY_OrderDay= "OrderDay";
public static String KEY_OrderTime= "OrderTime";
public static String KEY_ZipCode = "add";
public static String KEY_OrderiD= "name";

private Activity activity;
private Context context;

public Preference(Activity activity){
    this.activity = activity;
    this.context = activity.getApplicationContext();
}

//-----------------------------------


public boolean isNetworkAvailable()
{
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo ni = cm.getActiveNetworkInfo();
    return ni != null;
}

public static String get(Context context , String key) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    String userId = sp.getString(key, "0");
    return userId;
}
public static void save(Context context, String key, String value) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    SharedPreferences.Editor edit = sp.edit();
    edit.putString(key, value);
    edit.commit();
}

public static void saveInt(Context context, String key, int value) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    SharedPreferences.Editor edit = sp.edit();
    edit.putInt(key, value);
    edit.commit();
}
public static void saveFloat(Context context, String key, Float value) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    SharedPreferences.Editor edit = sp.edit();
    edit.putFloat(key, value);
    edit.commit();
}

public static int getInt(Context context , String key) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    int userId = sp.getInt(key,0);
    return userId;
}

public static Float getFloat(Context context , String key) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    Float userId = sp.getFloat(key,0);
    return userId;
}


public static boolean saveBool(Context context, String key, Boolean value) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    SharedPreferences.Editor edit = sp.edit();
    edit.putBoolean(key, value);
    edit.commit();
    return false;
}

public static Boolean getBool(Context context , String key) {
    sp = context.getSharedPreferences(APP_PREF, 0);
    return sp.getBoolean(key,false);
}

public static void clearPreference(Context context) {
    sp = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
    SharedPreferences.Editor edit = sp.edit();
    edit.clear();
    edit.apply();
}
}
