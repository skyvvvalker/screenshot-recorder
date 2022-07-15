package apk.screenrec;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

class AppUtils {
    static String CONFIG_LastState = "LastState";
    static String CONFIG_JpegQuality = "JpegQuality";
    static String CONFIG_ImageSize = "ImageSize";
    static String CONFIG_Threshold = "Threshold";
    static String CONFIG_Interval = "Interval";
    static String CONFIG_NoForeground = "NoForeground";
    static String CONFIG_AutoDelete = "AutoDelete";

    static int DefaultJpegQuality = 80;
    static int DefaultImageSize = 512;
    static int DefaultThreshold = 20;
    static int DefaultInterval = 10;
    static int DefaultAutoDelDays = 90;
    static boolean DefaultNoForeground = false;

    private static final SharedPreferences sharedPreferences = App.getInstance().getSharedPreferences(BuildConfig.APPLICATION_ID, AppCompatActivity.MODE_PRIVATE);

    static Boolean GetConfigBool(String s, Boolean def) {
        return sharedPreferences.getBoolean(s, def);
    }

    static Integer GetConfigInt(String s, Integer def) {
        return sharedPreferences.getInt(s, def);
    }

    @SuppressLint("ApplySharedPref")
    static void SetConfigBool(String s, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(s, value);
        editor.commit();
    }

    @SuppressLint("ApplySharedPref")
    static void SetConfigInt(String s, Integer value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(s, value);
        editor.commit();
    }
}