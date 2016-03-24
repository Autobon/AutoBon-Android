package cn.com.incardata.http;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.utils.L;


public class CustomHttpClient {
	private static String TAG = "CustomHttpClient";
	protected static final String CHARSET_UTF8 = HTTP.UTF_8;
	protected static final String CHARSET_GB2312 = "GB2312";
	protected static HttpClient customerHttpClient;
	private static int ConnectionTimeOut = 10000;
	
	public CustomHttpClient() {
	}
	
	public static void setConnectionTimeout(int timeout,boolean flag)
	{
		if(flag)
		{
			ConnectionTimeOut = timeout;
		}else{
			ConnectionTimeOut = 10000;
		}
	}
	
	/**
	 * HttpClient post方法
	 * 
	 * @param url
	 * @return
	 */
	public static String PostStreamFromWebByHttpClient(Context context, String url,String filename) {
		try {
			InputStream is=null;
			InputStream input=null;
			File mfile=new File(filename);
			if(mfile.exists()){
				is=new FileInputStream(mfile);
				byte[] bytes = new byte[(int) mfile.length()];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					is.read(bytes);
				}
				is.close();
				byte[] updata = bytes;
			    input = new ByteArrayInputStream(updata);
			}else{
				return null;
			}
			InputStreamEntity ise=new InputStreamEntity(input, mfile.length());
			ise.setContentType("binary/octet-stream");
			ise.setChunked(false);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(ise);
			HttpClient client = getHttpClient(context);
			
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			//return (resEntity == null) ? null:resEntity.getContent().toString();
			return (resEntity == null) ? null : EntityUtils.toString(resEntity,
					CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			Log.w(TAG, e.getMessage());
			return null;
		} catch (ClientProtocolException e) {
			Log.w(TAG, e.getMessage());
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		}catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * HttpClient post方法
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	public static String PostFromWebByHttpClient(Context context, String url,
			NameValuePair... nameValuePairs) {
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (nameValuePairs != null) {
				for (int i = 0; i < nameValuePairs.length; i++) {
					params.add(nameValuePairs[i]);
				}
			}
			
	
			UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params,CHARSET_UTF8);
			urlEncoded.setContentType("binary/octet-stream");
			urlEncoded.setChunked(false);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(urlEncoded);
			
			HttpClient client = getHttpClient(context);
	
			
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			//return (resEntity == null) ? null:resEntity.getContent().toString();
			return (resEntity == null) ? null : EntityUtils.toString(resEntity,
					CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			Log.w(TAG, e.getMessage());
			return null;
		} catch (ClientProtocolException e) {
			Log.w(TAG, e.getMessage());
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		} 
	}
	
    protected static final String APPLICATION_JSON = "application/json";
    protected static final String CONTENT_TYPE_TEXT_JSON = "text/json";
	public static String PostFromWebByHttpClient(Context context, String url,
			String json) {
		try {
			HttpPost httpPost = new HttpPost(url); 
			//String encoderJson = URLEncoder.encode(json, HTTP.UTF_8);
			StringEntity s = new StringEntity(json, HTTP.UTF_8);
			//s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
			s.setContentType("application/json");
			httpPost.setEntity(s); 
			HttpClient client = getHttpClient(context);
			HttpResponse response = client.execute(httpPost);
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

	public static String getFromWebByHttpClient(Context context, String url,
			String json) throws Exception{
			L.d("getFromWebByHttpClient url = " + url);
		try {
			// HttpGet连接对象
			HttpGet httpRequest = new HttpGet(url + json);
			// 取得HttpClient对象
			HttpClient httpclient = getHttpClient(context);
			// 请求HttpClient，取得HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
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
		} 	
	}
	
	public static String getFromWebByHttpClient(Context context, String url,
			NameValuePair... nameValuePairs) throws Exception{
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(url);
			if (nameValuePairs != null && nameValuePairs.length > 0) {
				sb.append("?");
				for (int i = 0; i < nameValuePairs.length; i++) {
					if (i > 0) {
						sb.append("&");
					}
					sb.append(String.format("%s=%s",
							nameValuePairs[i].getName(),
							nameValuePairs[i].getValue()));
				}
			}
			L.d(CustomHttpClient.class,"getFromWebByHttpClient url = " + sb);
			// HttpGet连接对象
			HttpGet httpRequest = new HttpGet(sb.toString());
			// 取得HttpClient对象
			HttpClient httpclient = getHttpClient(context);
			// 请求HttpClient，取得HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// 请求成功
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("连接失败");
			}
			return EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
//			BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
//			String respone = "";
//            String line = null;
//            while ((line = br.readLine()) != null) {
//                    respone += line;
//            }
//            return respone;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("连接失败",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			L.e("IOException ");
			e.printStackTrace();
			throw new RuntimeException("连接失败",e);
		} 	
	}

	public static String PutHttpClient(Context context, String url,
			String json) {
		try {
			HttpPut httpPut = new HttpPut(url); 
			StringEntity s = new StringEntity(json, HTTP.UTF_8);
			s.setContentType("application/json");
			httpPut.setEntity(s); 
			HttpClient client = getHttpClient(context);
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
	
	/**
	 * 创建httpClient实例
	 * 
	 * @return
	 * @throws Exception
	 */
	public static synchronized HttpClient getHttpClient(Context context) {
		HttpClient customerHttpClient=null;
		HttpParams params = new BasicHttpParams();
		// 设置�?些基本参�?
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		HttpProtocolParams.setUseExpectContinue(params, true);
		HttpProtocolParams
				.setUserAgent(
						params,
						"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) ");
		// 超时设置
		/* 从连接池中取连接的超时时�? */
		ConnManagerParams.setTimeout(params, 1000);
		/* 连接超时 */
		if (!HttpUtils.isWifiDataEnable(context)) {
			ConnectionTimeOut = 15000;
		}
		HttpConnectionParams
				.setConnectionTimeout(params, ConnectionTimeOut);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(params, 10000);
		// 设置我们的HttpClient支持HTTP和HTTPS两种模式
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));

		// 使用线程安全的连接管理来创建HttpClient
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);
		customerHttpClient = new DefaultHttpClient(conMgr, params);
		return customerHttpClient;
	}
}
