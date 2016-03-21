package cn.com.incardata.http;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
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
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.L;
import cn.com.incardata.utils.SharedPre;


/**
 * 专门针对《英卡》封装的http
 * @author wanghao
 */
public class HttpClientInCar extends CustomHttpClient {
	private static final String TAG = "HttpClientInCar";

	/**
	 * 登录
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	public static String postLoginHttpToken(Context context, String url, NameValuePair... nameValuePairs) throws Exception{
		HttpClient client = getHttpClient(context);
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (nameValuePairs != null) {
				for (int i = 0; i < nameValuePairs.length; i++) {
					params.add(nameValuePairs[i]);
				}
			}

			UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params,CHARSET_UTF8);
			urlEncoded.setContentType("application/x-www-form-urlencoded");
			urlEncoded.setChunked(false);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(urlEncoded);

			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			List<Cookie> cookies = ((AbstractHttpClient)client).getCookieStore().getCookies();
			for (Cookie cookie : cookies){
				if (AutoCon.AUTOKEN.equals(cookie.getName())){
					SharedPre.setSharedPreferences(context, AutoCon.AUTOKEN, AutoCon.AUTOKEN + "=\"" + cookie.getValue() + "\"");
					MyApplication.getInstance().setCookie(null);
					break;
				}
			}
			//SharedPre.setSharedPreferences(context, AutoCon.AUTOKEN,"autoken=\"technician:3lY/32kd4ziRd3O4Bbtx9Q==\"");
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
		}finally {
			client.getConnectionManager().shutdown();
		}
	}

	/**
	 * 以post键值对表单形式提交数据
	 * @param context
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	public static String PostFormByHttpClient(Context context, String url,NameValuePair... nameValuePairs) {
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (nameValuePairs != null) {
				for (int i = 0; i < nameValuePairs.length; i++) {
					params.add(nameValuePairs[i]);
				}
			}

			UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params,CHARSET_UTF8);
			urlEncoded.setContentType("application/x-www-form-urlencoded");
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

	public static String postHttpToken(String url, NameValuePair... nameValuePairs) {
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (nameValuePairs != null) {
				for (int i = 0; i < nameValuePairs.length; i++) {
					params.add(nameValuePairs[i]);
				}
			}

			UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params,CHARSET_UTF8);
			urlEncoded.setContentType("application/x-www-form-urlencoded");
			urlEncoded.setChunked(false);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(urlEncoded);
			DefaultHttpClient client = getDefaultHttpClient();

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

	public static String getHttpToken(String url, NameValuePair... nameValuePairs) throws Exception{
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
			DefaultHttpClient httpclient = getDefaultHttpClient();
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

	public static String PutHttpToken(String url, NameValuePair... nameValuePairs) throws Exception {
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (nameValuePairs != null) {
				for (int i = 0; i < nameValuePairs.length; i++) {
					params.add(nameValuePairs[i]);
				}
			}

			UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params, CHARSET_UTF8);
			urlEncoded.setContentType("application/x-www-form-urlencoded");
			urlEncoded.setChunked(false);
			HttpPut httpPut = new HttpPut(url);
			httpPut.setEntity(urlEncoded);

			DefaultHttpClient client = getDefaultHttpClient();

			HttpResponse response = client.execute(httpPut);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
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

	public static String uploadImage(String pathToOurFile, String urlServer) throws Exception{
		HttpClient httpclient = getDefaultHttpClient();
		try {
			//设置通信协议版本
			httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

			//File path= Environment.getExternalStorageDirectory(); //取得SD卡的路径

			//String pathToOurFile = path.getPath()+File.separator+"ak.txt"; //uploadfile
			//String urlServer = "http://192.168.1.88/test/upload.php";

			HttpPost httppost = new HttpPost(urlServer);
			File file = new File(pathToOurFile);

			FileBody fileBody = new FileBody(file);
			MultipartEntity mpEntity = new MultipartEntity(); //文件传输
			mpEntity.addPart("file", fileBody); // <input type="file" name="userfile" />  对应的


			httppost.setEntity(mpEntity);
			System.out.println("executing request " + httppost.getRequestLine());

			HttpResponse response = httpclient.execute(httppost);
			System.out.println(response.getStatusLine());//通信Ok

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			return (resEntity == null) ? null : EntityUtils.toString(resEntity,
					CHARSET_UTF8);

		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			httpclient.getConnectionManager().shutdown();
		}
		return null;
	}

	private static synchronized DefaultHttpClient getDefaultHttpClient(){
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpParams httpParameters = httpclient.getParams();

		httpclient.addRequestInterceptor(new HttpRequestInterceptor() {
			@Override
			public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
				httpRequest.setHeader("Cookie", MyApplication.getInstance().getCookie());
			}
		});

		HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
		HttpConnectionParams.setSoTimeout(httpParameters, 10000);

//		//请求超时
//		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); 
//		//读取超时
//		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
		return httpclient;
	}
}
