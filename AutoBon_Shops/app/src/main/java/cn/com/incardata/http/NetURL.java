package cn.com.incardata.http;

public class NetURL {
	/** 基地址 */
	public final static String BASE_URL = "http://121.40.157.200:12345/api/mobile/";
	public final static String IP_PORT = "http://121.40.157.200:12345";
	public static final String PUB = "http://121.40.157.200:12345/api/pub/";

	/** 发送验证短信 */
	public static final String VERIFY_SMS = PUB + "verifySms";
	/** 同步个推CID到后台 */
	public static final String PUSH_ID = "";
}
