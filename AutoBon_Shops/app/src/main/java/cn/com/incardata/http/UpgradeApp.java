package cn.com.incardata.http;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.com.incardata.utils.L;

public class UpgradeApp {
	private Context context;

	public UpgradeApp(Context context) {
		this.context = context;
	}

	public Uri downLoadFile(String httpUrl, String path, String json) {
		// TODO Auto-generated method stub
		StringBuffer fileN = new StringBuffer(path + "/CarBlue/80miles.apk");
		File tmpFile = new File(fileN.toString());
		if (!tmpFile.getParentFile().exists()) {
			tmpFile.mkdir();
		}
		File file = new File(fileN.toString());
		if (file.exists()) {
			file.delete();
		}

		HttpURLConnection conn = null;
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			httpUrl = httpUrl + "?filename=" + json;
			URL url = new URL(httpUrl);
			conn = (HttpURLConnection) url.openConnection();
			is = conn.getInputStream();
			fos = new FileOutputStream(file);
			byte[] buf = new byte[256];
			conn.connect();
			double count = 0;
			if (conn.getResponseCode() >= 400) {
				//					T.show(context, "连接超时");
				return null;
			} else {
				while (count <= 100) {
					if (is != null) {
						int numRead = is.read(buf);
						if (numRead <= 0) {
							break;
						} else {
							fos.write(buf, 0, numRead);
						}

					} else {
						break;
					}
				}
			}

			fos.close();
			is.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			conn.disconnect();
		}

		return Uri.fromFile(file);
	}

	//启动安装
	public void openFile(Uri result) {
		// TODO Auto-generated method stub
		L.e("OpenFile", result.toString());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(result,"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
