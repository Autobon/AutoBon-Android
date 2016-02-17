package cn.com.incardata.http;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;

public class NetTaskToken implements Runnable {
	private String strUrl;
	private String param;
	private Handler handler;
	private Class<?> cls;
	private int httpMode;
	private int what;

	public NetTaskToken(String strUrl, String param, int httpMode, Class<?> cls,Handler handler, int what) {
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
				json = HttpClientInCar.postHttpToken(strUrl, param);
				break;
			case Http.GET:
				json = HttpClientInCar.getHttpToken(strUrl, param);
				break;
			case Http.PUT:
				json = HttpClientInCar.PutHttpToken(strUrl, param);
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
