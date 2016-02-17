package cn.com.incardata.http;

import com.alibaba.fastjson.JSON;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.response.LoginEntity;
import cn.com.incardata.utils.L;


/**
 * 专门针对《英卡》封装的http
 * @author wanghao
 */
public class HttpClientInCar extends CustomHttpClient {
	private static final String TAG = "HttpClientInCar";

	/**
	 * 登录
	 * @param url
	 * @param json
	 * @return
	 */
	public static String postLoginHttpToken(String url, String json) throws Exception{
		HttpPost httpPost = new HttpPost(url); 
		DefaultHttpClient httpclient = getDefaultHttpClient();
		try {
			StringEntity s = new StringEntity(json, HTTP.UTF_8);
			s.setContentType(APPLICATION_JSON);
			httpPost.setEntity(s); 
			HttpResponse response = httpclient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			String result = null;
			if (resEntity != null) {
				result = EntityUtils.toString(resEntity,CHARSET_UTF8);
				LoginEntity loginEntity = JSON.parseObject(result, LoginEntity.class);
				if (StatusCode.STATUS_SUCCESS.equals(loginEntity.getStatus())) {
					List<Cookie> token = httpclient.getCookieStore().getCookies();
					//提取token
					MyApplication.getInstance().setCookieStore("");
				}
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			L.e(TAG, e.getMessage());
			return null;
		} catch (ClientProtocolException e) {
			L.e(TAG, e.getMessage());
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		}finally{
			httpPost.abort();
		}
	}

//	/**
//	 * 注册
//	 * @param url
//	 * @param json
//	 * @return
//	 */
//	public static AddUserEntity postRegisterHttpToken(String url, String json) throws Exception{
//		HttpPost httpPost = new HttpPost(url);
//		DefaultHttpClient httpclient = getDefaultHttpClient();
//		try {
//			StringEntity s = new StringEntity(json, HTTP.UTF_8);
//			s.setContentType(APPLICATION_JSON);
//			httpPost.setEntity(s);
//			HttpResponse response = httpclient.execute(httpPost);
//			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//				throw new RuntimeException("请求失败");
//			}
//			HttpEntity resEntity = response.getEntity();
//			String result = null;
//			if (resEntity != null) {
//				result = EntityUtils.toString(resEntity,CHARSET_UTF8);
//				AddUserEntity register = JSON.parseObject(result, AddUserEntity.class);
//				if (register.getStatus().equals(StatusCode.STATUS_SUCCESS)) {
//					MyApplication.getInstance().setCookieStore(httpclient.getCookieStore());
//				}
//				return register;
//			}
//			return null;
//		} catch (UnsupportedEncodingException e) {
//			L.e(TAG, e.getMessage());
//			return null;
//		} catch (ClientProtocolException e) {
//			L.e(TAG, e.getMessage());
//			return null;
//		} catch (IOException e) {
//			throw new RuntimeException("连接失败", e);
//		}finally{
//			httpPost.abort();
//		}
//	}
	
	public static String postHttpToken(String url, String json) throws Exception{
		HttpPost httpPost = new HttpPost(url); 
		DefaultHttpClient httpclient = getDefaultHttpClient();
		try {
			StringEntity s = new StringEntity(json, HTTP.UTF_8);
			s.setContentType(APPLICATION_JSON);
			httpPost.setEntity(s); 
			HttpResponse response = httpclient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			return (resEntity == null) ? null : EntityUtils.toString(resEntity,
					CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			L.e(TAG, e.getMessage());
			return null;
		} catch (ClientProtocolException e) {
			L.e(TAG, e.getMessage());
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		}finally{
			httpPost.abort();
		}
	}

	public static String getHttpToken(String url, String json) throws Exception{
		L.d("getFromWebByHttpClient url = " + url);
		// HttpGet连接对象
		HttpGet httpGet = new HttpGet(url + json);
		// 取得HttpClient对象
		DefaultHttpClient httpclient = getDefaultHttpClient();
		try {
			// 请求HttpClient，取得HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpGet);
			// 请求成功
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("连接失败");
			}
			return EntityUtils.toString(httpResponse.getEntity());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("连接失败",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
			throw new RuntimeException("连接失败",e);
		}finally{
			httpGet.abort();
		} 	
	}

	public static String PutHttpToken(String url,String json) throws Exception{
		try {
			HttpPut httpPut = new HttpPut(url); 
			StringEntity s = new StringEntity(json, HTTP.UTF_8);
			s.setContentType("application/json");
			httpPut.setEntity(s); 
			DefaultHttpClient client = getDefaultHttpClient();
			HttpResponse response = client.execute(httpPut);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			return (resEntity == null) ? null : EntityUtils.toString(resEntity,
					CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			L.e(TAG, e.getMessage());
			return "";
		} catch (ClientProtocolException e) {
			L.e(TAG, e.getMessage());
			return "";
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		} 
	}
	
	private static synchronized DefaultHttpClient getDefaultHttpClient(){
//		BasicHttpParams params = new BasicHttpParams(); 
//		HttpConnectionParams.setConnectionTimeout(params, 10000); //设置连接超时
//		 HttpConnectionParams.setSoTimeout(params, 10000); //设置请求超时
		 
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpParams httpParameters = httpclient.getParams();
//		BasicCookieStore cookieStore = new BasicCookieStore();
//		BasicClientCookie clientCookie = new BasicClientCookie("token", "vWfvmf1nPssdPBWOSuXJSg%3D%3D");
//		clientCookie.setDomain("dev.incardata.com.cn");
//		cookieStore.addCookie(clientCookie);

		CookieStore token = MyApplication.getInstance().getCookieStore();
		httpclient.setCookieStore(token);
		
		HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
		HttpConnectionParams.setSoTimeout(httpParameters, 10000);
		
//		//请求超时
//		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); 
//		//读取超时
//		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
		return httpclient;
	}
}
