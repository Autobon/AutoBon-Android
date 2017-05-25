package cn.com.incardata.http;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;

public class NetTaskToken implements Runnable {
	private String strUrl;
	private NameValuePair[] params;
	private String param;
	private Handler handler;
	private Class<?> cls;
	private int httpMode;
	private int what;

	public NetTaskToken(String strUrl, String param, int httpMode, Class<?> cls, Handler handler, int what) {
		this.strUrl = strUrl;
		this.handler = handler;
		this.cls = cls;
		this.httpMode = httpMode;
		this.what = what;
		this.param = param;
	}

	public NetTaskToken(String strUrl, int httpMode, Class<?> cls, Handler handler, int what, NameValuePair... params) {
		this.strUrl = strUrl;
		this.handler = handler;
		this.cls = cls;
		this.httpMode = httpMode;
		this.what = what;
		this.params = params;
	}

	@Override
	public void run() {
		try {
			String json = null;
			switch (httpMode) {
				case Http.POST:
					if (param != null){
						json = HttpClientInCar.postHttpToken(strUrl, param);
						break;
					}
					if (params != null) {
						json = HttpClientInCar.postHttpToken(strUrl, params);
						break;
					}
					handler.sendEmptyMessage(what);
					return;
				case Http.GET:
					if (param != null){
						json = HttpClientInCar.getHttpToken(strUrl, param);
						break;
					}
					if (params != null){
						json = HttpClientInCar.getHttpToken(strUrl, params);
						break;
					}
					handler.sendEmptyMessage(what);
					return;
				case Http.PUT:
					if (param != null){
						json = HttpClientInCar.PutHttpToken(strUrl, param);
						break;
					}
					if (params != null){
						json = HttpClientInCar.PutHttpToken(strUrl, params);
						break;
					}
					handler.sendEmptyMessage(what);
					return;
				case Http.DEL:
					if (param != null){
						json = HttpClientInCar.DelHttpToken(strUrl, param);
						break;
					}
					if (params != null){
						json = HttpClientInCar.DelHttpToken(strUrl, params);
						break;
					}
					handler.sendEmptyMessage(what);
					return;
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
