package cn.com.incardata.application;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.Locale;

import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.SharedPre;

/**
 * Created by wanghao on 16/2/16.
 */
public class MyApplication extends Application{
    private static MyApplication instance;
    private CookieStore cookieStore;

    public static synchronized MyApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initState();

    }

    private void initState(){
        //语言
//        int language = SharedPre.getInt(this, AutoCon.LANGUAGE, Language.DEFAULT);
//        if (language != Language.DEFAULT) {
//            switchLanguage(language);
//        }
    }

    public String getSDCardDirectory(){
        boolean isCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isCardExist) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    public synchronized void setCookieStore(String token){
        if (token == null){
            return;
        }
        cookieStore = new BasicCookieStore();
        BasicClientCookie clientCookie = new BasicClientCookie("token", token);
        clientCookie.setDomain("dev.incardata.com.cn");
        cookieStore.addCookie(clientCookie);
    }

    public synchronized CookieStore getCookieStore(){
        if (this.cookieStore == null) {
            String token = SharedPre.getString(this, AutoCon.TOKEN, "");
            setCookieStore(token);
        }
        return this.cookieStore;
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
        SharedPre.setSharedPreferences(this, AutoCon.LANGUAGE, language);
    }
}