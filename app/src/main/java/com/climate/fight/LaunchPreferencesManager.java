package com.climate.fight;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Creadito con cariño por alexb el día 29/08/2019.
 */
public class LaunchPreferencesManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    PackageInfo pInfo;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "welcomepref";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LAST_LAUNCHED_VERSION = "LastOpenVersion";

    public LaunchPreferencesManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        try {
            pInfo = _context.getPackageManager().getPackageInfo(_context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.putInt(LAST_LAUNCHED_VERSION, pInfo.versionCode);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public boolean isFirstTimeUpdate(){
        return pref.getInt(LAST_LAUNCHED_VERSION, pInfo.versionCode - 1) < pInfo.versionCode;
    }

}
