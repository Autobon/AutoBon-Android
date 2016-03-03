package cn.com.incardata.http;

public class NetURL {
	/** 基地址 */
	public final static String BASE_URL = "http://121.40.157.200:51234/api/mobile/";
	public final static String IP_PORT = "http://121.40.157.200:51234";
	public final static String DOMAIN = "121.40.157.200";

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

	/** 提交认证 */
	public static final String COMMIT_CERTIFICATE = BASE_URL + "technician/commitCertificate";
	/** 上传头像 */
	public static final String AVATAR = BASE_URL + "technician/avatar";
	/** 上传身份证照片 */
	public static final String ID_PHOTO = BASE_URL + "technician/idPhoto";
	/** 查询技师 */

	/** 认证进度 */
	public static final String AUTHORIZATION_PROGRESS = BASE_URL + "technician/getCertificate";
	/** 查询技师 */
	public static final String SEARCH_TECHNICIAN = BASE_URL + "technician/search";
	/** 签到*/
	public static final String SIGN_IN_URL = BASE_URL + "construction/signIn";

	/** 更新个推ID */
	public static final String PUSH_ID = BASE_URL + "technician/pushId";
}
