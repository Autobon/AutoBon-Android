package cn.com.incardata.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;


public class NetTask implements Runnable{
	private Context context;
	private String strUrl;
	private String param;
	private Handler handler;
	private Class<?> cls;
	private int httpMode;
	private int what;

	public NetTask(Context context, String strUrl, String param, int httpMode, Class<?> cls,Handler handler, int what) {
		this.context = context;
		this.strUrl = strUrl;
		this.param = param;
		this.handler = handler;
		this.cls = cls;
		this.httpMode = httpMode;
		this.what = what;
	}

	@Override
	public void run() {
		try {
			String json = null;
			switch (httpMode) {
			case Http.POST:
				json = CustomHttpClient.PostFromWebByHttpClient(context, strUrl, param);
				break;
			case Http.GET:
				json = CustomHttpClient.getFromWebByHttpClient(context, strUrl, param);
				break;
			case Http.PUT:
				json = CustomHttpClient.PutHttpClient(context, strUrl, param);
				break;
			default:
				break;
			}
			Message msg = handler.obtainMessage(what);
			msg.obj = JSON.parseObject(json, cls);
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(what);
		}
	}
}


