package cn.com.incardata.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	/**
	 * 携带token传送json的任务
	 * @param strUrl
	 * @param param
	 * @param httpMode
	 * @param cls
	 * @param what
	 */
	private void taskToken(String strUrl, String param, int httpMode, Class<?> cls, int what){
		mExecutor.submit(new NetTaskToken(strUrl, param, httpMode, cls, handler, what));
	}
	
	/**
	 * post方式加载数据带token（传送json）
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
	 * get方式加载数据带token（传送json）
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
	 * put方式加载数据带token（传送json）
	 * @param strUrl
	 * @param param
	 * @param cls
	 * @param onResult
	 */
	public void putTaskToken(String strUrl, String param, Class<?> cls, OnResult onResult){
		msgQueue.put(onResult.hashCode(), onResult);
 		taskToken(strUrl, param, PUT, cls, onResult.hashCode());
	}

	/**
	 * 携带token传送表单（NameValuePair）的任务
	 * @param strUrl
	 * @param httpMode
	 * @param cls
	 * @param what
	 * @param nameValuePairs
	 */
	private void taskToken(String strUrl, int httpMode, Class<?> cls, int what, NameValuePair... nameValuePairs){
		mExecutor.submit(new NetTaskToken(strUrl, httpMode, cls, handler, what, nameValuePairs));
	}

	/**
	 * post方式加载数据带token（传送表单NameValuePair）
	 * @param strUrl
	 * @param cls
	 * @param onResult
	 * @param nameValuePairs
	 */
	public void postTaskToken(String strUrl, Class<?> cls, OnResult onResult, NameValuePair... nameValuePairs){
		msgQueue.put(onResult.hashCode(), onResult);
		taskToken(strUrl, POST, cls, onResult.hashCode(), nameValuePairs);
	}

	/**
	 * get方式加载数据带token（传送表单NameValuePair)
	 * @param strUrl
	 * @param cls
	 * @param onResult
	 * @param nameValuePairs
	 */
	public void getTaskToken(String strUrl, Class<?> cls, OnResult onResult, NameValuePair... nameValuePairs){
		msgQueue.put(onResult.hashCode(), onResult);
		taskToken(strUrl, GET, cls, onResult.hashCode(), nameValuePairs);
	}

	/**
	 * put方式加载数据带token（传送表单NameValuePair)
	 * @param strUrl
	 * @param cls
	 * @param onResult
	 * @param nameValuePairs
	 */
	public void putTaskToken(String strUrl, Class<?> cls, OnResult onResult, NameValuePair... nameValuePairs){
		msgQueue.put(onResult.hashCode(), onResult);
		taskToken(strUrl, PUT, cls, onResult.hashCode(), nameValuePairs);
	}

	/**
	 * Executor.shutdown
	 */
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
