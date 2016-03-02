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

    public static String getAppRootDir() throws Throwable {
        if (isExistSDCard()){
            String path = Environment.getExternalStorageDirectory() + File.separator + appDir;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            return path;
        }
        throw new Throwable("No find SD card");
    }

    /**
     * 采集的图片临时目录
     * @return
     */
    public static String getGatherDir(){
        String path = null;
        try {
            path = getAppRootDir() + File.separator + "temp";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            return path;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    /**
     * 缓存目录
     * @return
     */
    public static File getCacheDir(){
        try {
            String path = getAppRootDir() + File.separator + "cache";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
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
