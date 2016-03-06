package cn.com.incardata.application;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.baidu.mapapi.SDKInitializer;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.SharedPre;

/**
 * Created by wanghao on 16/2/16.
 */
public class MyApplication extends Application{
    private static MyApplication instance;
    private CookieStore cookieStore;
    private static HashMap<Integer, String> skillMap;

    public static synchronized MyApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //在使用百度地图SDK各组件之前初始化context信息,传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        //初始化ImageLoaderCache
        ImageLoaderCache.getInstance().init(getApplicationContext());

        if (skillMap == null){
            skillMap = new HashMap<Integer, String>();
            skillMap.put(1, "隔热层");
            skillMap.put(1, "隐形车衣");
            skillMap.put(1, "车身改色");
            skillMap.put(1, "美容清洁");
        }
        //initState();
    }

    private void initState(){
        //语言
        int language = SharedPre.getInt(this, AutoCon.LANGUAGE, Language.DEFAULT);
        if (language != Language.DEFAULT) {
            switchLanguage(language);
        }
    }

    /**
     * 获取技能项id
     * @param skillName
     * @return
     */
    public int getSkill(String skillName) {
        Iterator iter = skillMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            String val = (String) entry.getValue();

            if (val.equals(skillName)){
                return key;
            }
        }
        return -1;
    }

    /**
     * 获取技能项名字
     * @param skillId
     * @return
     */
    public String getSkill(int skillId){
        Iterator iter = skillMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            String val = (String) entry.getValue();

            if (key == skillId){
                return val;
            }
        }
        return null;
    }

    public synchronized void setCookieStore(String autoken){
        if (autoken == null){
            return;
        }
        cookieStore = new BasicCookieStore();
        BasicClientCookie clientCookie = new BasicClientCookie(AutoCon.AUTOKEN, autoken);
        clientCookie.setDomain(NetURL.DOMAIN);
        cookieStore.addCookie(clientCookie);
    }

    public synchronized CookieStore getCookieStore(){
        if (this.cookieStore == null) {
            String autoken = SharedPre.getString(this, AutoCon.AUTOKEN, "");
            setCookieStore(autoken);
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