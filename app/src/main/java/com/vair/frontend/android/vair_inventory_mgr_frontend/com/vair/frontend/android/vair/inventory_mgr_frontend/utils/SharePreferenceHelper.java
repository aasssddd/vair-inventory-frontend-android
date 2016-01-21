package com.vair.frontend.android.vair_inventory_mgr_frontend.com.vair.frontend.android.vair.inventory_mgr_frontend.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by william.chen on 2016/1/21.
 */
public class SharePreferenceHelper {

    private final static String APP_IDENTIFIER = "VAIR_INVENTORY_MGR";

    private SharedPreferences preferences;

    public static SharePreferenceHelper getInstance(Context context) {
        return new SharePreferenceHelper(context.getSharedPreferences(APP_IDENTIFIER, Context.MODE_PRIVATE));
    }

    private SharePreferenceHelper(SharedPreferences pref) {
        this.preferences = pref;
    }

    public void Save(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String GetValue(String key) {
        return preferences.getString(key, "");
    }

}
