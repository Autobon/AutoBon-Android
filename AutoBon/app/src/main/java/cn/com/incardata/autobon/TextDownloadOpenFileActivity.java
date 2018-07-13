package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.utils.SDCardUtils;
import cn.com.incardata.utils.T;

/**
 * Created by yang on 2017/6/22.
 */
public class TextDownloadOpenFileActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_download;

    private final String[][] MIME_MapTable = {
//{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-JavaScript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_download_open_file);
        initView();
    }

    private void downloadFile(String fileName) {
        String fileUrl = null;
        if (SDCardUtils.isExistSDCard()) {
            try {
                fileUrl = SDCardUtils.getAppRootDir() + File.separator + fileName;
                File file = new File(fileUrl);

                if (file.exists()) {
                    T.show(getContext(), "文件存在");
                    openFile(file);
                    return;
                } else {
                    T.show(this, "文件不存在");
                    file.createNewFile();
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            T.show(this, getString(R.string.uninstalled_sdcard));
            return;
        }

        new AsyncTask<String, String, Boolean>() {
            File newFile;

            @Override
            protected Boolean doInBackground(String... strings) {
                newFile = new File(strings[1]);
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(strings[0]);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(15_000);
                    conn.setReadTimeout(30_000);
                    // 设置是否从httpUrlConnection读入，默认情况下是true;
                    conn.setDoInput(true);
                    // 防止屏蔽程序抓取而返回403错误
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    conn.addRequestProperty("Cookie", "autoken=\"staff:ssEoVBwJ3rSYnidORQUvhQ==@QADUVK\"");
                    conn.setRequestMethod("GET");
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        InputStream is = conn.getInputStream();
                        FileOutputStream fos = new FileOutputStream(newFile);
                        byte[] buy = readInputStream(is);
                        fos.write(buy);
                        if (fos != null){
                            fos.close();
                        }
                        if (is != null){
                            is.close();
                        }
                        return buy.length > 0;
                    }else {
                        return false;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }catch (Exception e){
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean){
                    openFile(newFile);
                }else {
                    T.show(TextDownloadOpenFileActivity.this,"下载失败");
                }
            }
        }.execute("http://10.0.12.182:12345/api/web/admin/study/download?path=/uploads/study/20170622175610HPB68C.pptx", fileUrl);
    }


//    /**
//     * 下载app更新文件
//     * <p>Created by wanghao on 2017/5/8.</p>
//     */
//    public void loadAppFile() {
//
//        String appFileStr;
//        try {
//            appFileStr = SDCardUtils.getAppRootDir() + File.separator + "file" + File.separator + "2017062210352642YQF6.docx";
//            File appFile = new File(appFileStr);
//            if (appFile.getParentFile().exists()) {
//                if(appFile.exists()){
//                    T.show(this,"文件已存在");
//                    openFile(appFile);
//                    return;
//                }else {
//                    T.show(this,"文件不存在");
//                    appFile.mkdir();
//                }
//            }else {
//                T.show(this,"父文件不存在");
//                appFile.mkdirs();
//            }
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//            T.show(this, "你没有安装SD卡");
//            return;
//        }
//
//        new AsyncTask<String, Integer, Boolean>() {
//            File file = null;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected Boolean doInBackground(String... params) {
////                int fileSize = Integer.valueOf(params[1]);
//                file = new File(params[1]);
//
//                HttpURLConnection urlConnection = null;
//                FileOutputStream fos = null;
//                InputStream is = null;
//                int countByte = 0;
//                try {
//                    URL url = new URL(params[0]);
//                    urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.setConnectTimeout(15_000);
//                    urlConnection.setReadTimeout(30_000);
//                    // 设置是否从httpUrlConnection读入，默认情况下是true;
//                    urlConnection.setDoInput(true);
////                    防止屏蔽程序抓取而返回403错误
//                    urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//                    urlConnection.addRequestProperty("Cookie", "autoken=\"staff:ssEoVBwJ3rSYnidORQUvhQ==@7JRJFX\"");
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.connect();
//                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                        is = urlConnection.getInputStream();
//                        byte[] buf = readInputStream(is);
//                        fos = new FileOutputStream(file);
//                        fos.write(buf);
//                        if (fos != null){
//                            fos.close();
//                        }
//                        if (is != null){
//                            is.close();
//                        }
//                        return file != null;
//                    } else {
//                        return false;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return false;
//                } finally {
//                    urlConnection.disconnect();
//                }
//            }
//
//            @Override
//            protected void onPostExecute(Boolean aBoolean) {
//                super.onPostExecute(aBoolean);
//                if (aBoolean && file != null) {
////                    Intent intent = new Intent();
////                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    intent.setAction(Intent.ACTION_VIEW);
////                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                    openFile(file);
//                } else {
//                    if (file.exists()){
//                        file.delete();
//                    }
//                    T.show(TextDownloadOpenFileActivity.this, "下载");
//                }
//            }
//
//        }.execute("http://10.0.12.182:12345/api/web/admin/study/download?path=/uploads/study/2017062210352642YQF6.docx",
//                appFileStr, MyApplication.getInstance().getCookie());
//    }

//    public static Intent getWordFileIntent(File file) {
//        Intent intent = null;
//        try {
//            intent = new Intent("android.intent.action.VIEW");
//            intent.addCategory("android.intent.category.DEFAULT");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Uri uri = Uri.fromFile(file);
//            intent.setDataAndType(uri, "application/msword");
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return intent;
//    }
//

    /**
     * 判断Intent 是否存在 防止崩溃
     *
     * @param context
     * @param intent
     * @return
     */
    private boolean isIntentAvailable(Activity context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    /**
     * 打开文件
     *
     * @param file
     */
    private void openFile(File file) {

        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
//获取文件file的MIME类型
            String type = getMIMEType(file);
//设置intent的data和Type属性。
            intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
            if (isIntentAvailable(this, intent)) {
                //跳转
                startActivity(intent); //这里最好try一下，有可能会报错。 //比如说你的MIME类型是打开邮箱，但是你手机里面没装邮箱客户端，就会报错。
            } else {
                T.show(getContext(), "未安装相关应用");
            }
        } catch (Exception e) {
            e.printStackTrace();
            T.show(getContext(), "未安装相关应用");
        }


    }

    /**
     * 检测是否安装了某个软件
     *
     * @param pkgName  "com.bill99.kuaishua"
     * @param mContext
     * @return
     */
    public static boolean isPkgInstalled(String pkgName, Activity mContext) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
//获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
/* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
//在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    private void initView() {
        btn_download = (Button) findViewById(R.id.btn_download);

        btn_download.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
                downloadFile("20170622175610HPB68C.pptx");
                break;
        }
    }
}
