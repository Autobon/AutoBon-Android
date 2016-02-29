package cn.com.incardata.http;

public class NetURL {
	/** 基地址 */
	public final static String BASE_URL = "http://121.40.157.200:51234/api/mobile/";

	/** 发送验证短信 */
	public static final String VERIFY_SMS = BASE_URL + "verifySms";
	/** 账户注册 */
	public static final String REGISTER = BASE_URL + "technician/register";
	/** 账户登录 */
	public static final String LOGIN = BASE_URL + "technician/login";
	/** 找回密码 */
	public static final String RESET_PASSWORD = BASE_URL + "technician/resetPassword";
	/** 更改密码 */
	public static final String CHANGE_PASSWORD = BASE_URL + "technician/changePassword";
	/** 查询技师 */
	public static final String SEARCH_TECHNICIAN = BASE_URL + "technician/search";

}
