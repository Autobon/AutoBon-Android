package cn.com.incardata.autobon.application;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

import cn.com.incardata.autobon.util.AutoBonConstants;
import cn.com.incardata.autobon.util.SharedPre;

/**
 * Created by Administrator on 2016/2/17.
 */
public class MyApplication extends Application{
    private static MyApplication instance;

    public static synchronized MyApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        // TODO Auto-generated method stub
        super.onTrimMemory(level);
    }

    public void switchLanguage(int language){
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        switch (language) {
            case Language.SIMPLIFIED_CHINESE:
                configuration.locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case Language.ENGLISH:
                configuration.locale = Locale.ENGLISH;
                break;
            default:
                configuration.locale = Locale.getDefault();
                break;
        }
        resources.updateConfiguration(configuration, displayMetrics);
        SharedPre.setSharedPreferences(this, AutoBonConstants.LANGUAGE , language);
    }
}
