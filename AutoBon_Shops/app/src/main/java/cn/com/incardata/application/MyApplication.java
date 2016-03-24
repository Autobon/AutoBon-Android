package cn.com.incardata.application;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.baidu.mapapi.SDKInitializer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.SharedPre;

/**
 * Created by wanghao on 16/3/24.
 */
public class MyApplication extends Application{

    private static MyApplication instance;
    private String cookie;
    private static HashMap<Integer, String> skillMap;
    private int userId;
    /**
     * 主页是否在前台运行（表示是否可以接收广播）
     */
    private static boolean isMainForego;
    /**
     * 跳过通知新订单
     */
    private static boolean isSkipNewOrder;

    public static boolean isSkipNewOrder() {
        return isSkipNewOrder;
    }

    public static void setIsSkipNewOrder(boolean isSkipNewOrder) {
        MyApplication.isSkipNewOrder = isSkipNewOrder;
    }

    public static boolean isMainForego() {
        return isMainForego;
    }

    public static void setMainForego(boolean mainForego) {
        isMainForego = mainForego;
    }

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
            skillMap.put(2, "隐形车衣");
            skillMap.put(3, "车身改色");
            skillMap.put(4, "美容清洁");
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

    public synchronized void setCookie(String autoken){
        this.cookie = autoken;
    }

    public synchronized String getCookie(){
        if (this.cookie == null) {
            String autoken = SharedPre.getString(this, AutoCon.AUTOKEN, "");
            setCookie(autoken);
        }
        return this.cookie;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
