package cn.com.incardata.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by wanghao on 16/2/29.
 */
public class SDCardUtils {
    private final static String appDir = "Autobon";//应用根目录

    public static boolean isExistSDCard(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public SDCardUtils() {
    }

    /**
     * 采集的图片临时目录
     * @return
     */
    public static String getGatherDir(){
        if (isExistSDCard()){
            String path = Environment.getExternalStorageDirectory() + File.separator + appDir + File.separator + "temp";
            if (!existFile(path)) {
                File file = new File(path);
                file.mkdirs();
            }
            return path;
        }else {
            return null;
        }
    }

    /**
     * 缓存目录
     * @return
     */
    public static String getCacheDir(){
        if (isExistSDCard()){
            return Environment.getExternalStorageDirectory() + File.separator + "cache";
        }else {
            return null;
        }
    }

    public static boolean existFile(String path){
        File file = new File(path);
        if (file.exists()){
            return true;
        }else {
            return false;
        }

    }
}
