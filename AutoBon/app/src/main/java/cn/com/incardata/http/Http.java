package cn.com.incardata.http;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class Http{
	public final static int POST = 1;
	public final static int GET = 2;
	public final static int PUT = 3;
	
	private static Http instance;
	private ExecutorService mExecutor;
	private static OnResult onResult;
	private static HashMap<Integer, OnResult> msgQueue = new HashMap<Integer, OnResult>();
	
	public Http() {
		mExecutor = Executors.newFixedThreadPool(3);
	}
	
	public static Http getInstance(){
		if (instance == null) {
			instance = new Http();
		}
		return instance;
	}
	
	protected static Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			onResult = msgQueue.get(what);
			onResult.onResult(msg.obj);
			msgQueue.remove(what);
		}
		
	};
	
	private void task(Context context, String strUrl, String param, int httpMode, Class<?> cls, int what){
		mExecutor.submit(new NetTask(context, strUrl, param, httpMode, cls, handler, what));
	}
	
	/**
	 * post方式加载数据
	 * @param context
	 * @param strUrl
	 * @param param
	 * @param cls
	 * @param onResult
	 */
	public void postTask(Context context, String strUrl, String param, Class<?> cls, OnResult onResult){
		msgQueue.put(onResult.hashCode(), onResult);
		task(context, strUrl, param, POST, cls, onResult.hashCode());
	}
	
	/**
	 * get方式加载数据
	 * @param context
	 * @param strUrl
	 * @param param
	 * @param cls
	 * @param onResult
	 */
	public void getTask(Context context, String strUrl, String param, Class<?> cls, OnResult onResult){
		msgQueue.put(onResult.hashCode(), onResult);
 		task(context, strUrl, "?" + param, GET, cls, onResult.hashCode());
	}
	
	/**
	 * put任务
	 * @param context
	 * @param strUrl
	 * @param param
	 * @param cls
	 * @param onResult
	 */
	public void putTask(Context context, String strUrl, String param, Class<?> cls, OnResult onResult){
		msgQueue.put(onResult.hashCode(), onResult);
		task(context, strUrl, param, PUT, cls, onResult.hashCode());
	}
	
	private void taskToken(String strUrl, String param, int httpMode, Class<?> cls, int what){
		mExecutor.submit(new NetTaskToken(strUrl, param, httpMode, cls, handler, what));
	}
	
	/**
	 * post方式加载数据带token
	 * @param context
	 * @param strUrl
	 * @param param
	 * @param cls
	 * @param onResult
	 */
	public void postTaskToken(String strUrl, String param, Class<?> cls, OnResult onResult){
		msgQueue.put(onResult.hashCode(), onResult);
		taskToken(strUrl, param, POST, cls, onResult.hashCode());
	}
	
	/**
	 * get方式加载数据带token
	 * @param context
	 * @param strUrl
	 * @param param
	 * @param cls
	 * @param onResult
	 */
	public void getTaskToken(String strUrl, String param, Class<?> cls, OnResult onResult){
		msgQueue.put(onResult.hashCode(), onResult);
 		taskToken(strUrl, "?" + param, GET, cls, onResult.hashCode());
	}
	
	/**
	 * put方式加载数据带token
	 * @param strUrl
	 * @param param
	 * @param cls
	 * @param onResult
	 */
	public void putTaskToken(String strUrl, String param, Class<?> cls, OnResult onResult){
		msgQueue.put(onResult.hashCode(), onResult);
 		taskToken(strUrl, param, PUT, cls, onResult.hashCode());
	}
	
	public void shutdown(){
		if (mExecutor != null) {
			mExecutor.shutdown();
		}
	}
	
	/**
	 * 释放资源
	 */
	public void release(){
		if (mExecutor != null) {
			mExecutor.shutdownNow();
		}
	}
}
