package cn.com.incardata.http;

public class NetURL {
	/** 基地址 */
	public final static String BASE_URL = "http://121.40.157.200:12345/api/mobile/";
	public final static String IP_PORT = "http://121.40.157.200:12345";
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
	public static final String COMMIT_CERTIFICATE = BASE_URL + "technician/certificate";
	/** 上传头像 */
	public static final String AVATAR = BASE_URL + "technician/avatar";
	/** 上传身份证照片 */
	public static final String ID_PHOTO = BASE_URL + "technician/idPhoto";

	/** 邀请技师(动态地址)*/
	public static final String INVITE_TECHNICIAN = BASE_URL + "technician/order";

	/** 查询技师 */
	public static final String SEARCH_TECHNICIAN = BASE_URL + "technician/search";
	/** 开始工作 */
	public static final String START_WORK = BASE_URL + "technician/construct/start";
	/** 签到*/
	public static final String SIGN_IN_URL = BASE_URL + "technician/order/signIn";

	/** 更新个推ID */
	public static final String PUSH_ID = BASE_URL + "technician/pushId";

	/** 报告实时位置 */
	public static final String REPORT_MY_ADDRESS = BASE_URL+ "technician/reportLocation";

	/** 我的信息*/
	public static final String MY_INFO_URL = BASE_URL + "technician";
	/** 修改银行卡信息 */
	public static final String MODIFY_BANK_CARD_INFO_URL = BASE_URL + "technician/changeBankCard";

	/** 获取订单详情(动态地址) */
	public static final String GET_ORDER_INFO = BASE_URL + "technician/order";

	public static String inviteTechnician(String orderId,String partnerId){
		return INVITE_TECHNICIAN+"/"+orderId+"/invite/"+partnerId;
	}

	public static String getOrderInfo(String orderId){
		return GET_ORDER_INFO+"/"+orderId;
	}
}
