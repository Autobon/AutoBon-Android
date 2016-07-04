package cn.com.incardata.http;

public class NetURL {
	/** 基地址 */
//	public final static String BASE_URL = "http://121.40.157.200:12345/api/mobile/";
//	public final static String IP_PORT = "http://121.40.157.200:12345";
//	public static final String PUB = "http://121.40.157.200:12345/api/pub/";

	public final static String BASE_URL = "http://hpecar.com:8012/api/mobile/";
	public final static String IP_PORT = "http://hpecar.com:8012";
	public static final String PUB = "http://hpecar.com:8012/api/pub/";

	/** 发送验证短信 */
	public static final String VERIFY_SMS = PUB + "verifySms";
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
	public static final String SIGN_IN_URL = BASE_URL + "technician/construct/signIn";

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
	/** 抢单 */
	public static final String TAKEUP = BASE_URL + "technician/order/takeup";
	/** 未完成订单列表 */
	public final static String UNFINISHED_ORDER_LIST = BASE_URL + "technician/order/listUnfinished";
	/** 已完成订单列表－主责任人 */
	public final static String FINISHED_ORDER_LIST_MAIN = BASE_URL + "technician/order/listMain";
	/** 已完成订单列表－次责任人 */
	public final static String FINISHED_ORDER_LIST_SECOND = BASE_URL + "technician/order/listSecond";

	/**上传施工图片*/
	public static final String UPLOAD_WORK_PHOTO = BASE_URL + "technician/construct/uploadPhoto";
	/** 提交施工前图片地址 **/
	public static final String SUBMIT_BEFORE_WORK_PHOTO_URL = BASE_URL + "technician/construct/beforePhoto";

	/** 获取订单工作项 **/
	public static final String GET_WORK_ITEM = PUB + "technician/workItems";

	/** 完成施工 **/
	public static final String WORK_FINISH_URL = BASE_URL + "technician/construct/finish";
	/** 接受或拒绝邀请 **/
	public static final String INVITATION = BASE_URL + "technician/order/";

	/** 账单 **/
	public static final String BILL_URL = BASE_URL + "technician/bill";
	/** 通知消息列表 **/
	public static final String MESSAGE_LIST = BASE_URL + "technician/message";
	/** 可抢订单 **/
	public static final String LIST_NEW = BASE_URL + "technician/order/listNew";

	public static String inviteTechnician(String orderId,String partnerId){
		return INVITE_TECHNICIAN+"/"+orderId+"/invite/"+partnerId;
	}

	public static String getOrderInfo(int orderId){
		return GET_ORDER_INFO+"/"+orderId;
	}

	public static String getInvitation(int orderId){
		return INVITATION + orderId + "/invitation";
	}

	/** 拉取帐单下的订单列表 **/
	public static String getBillOrderInfo(int billId){
		return BILL_URL+"/"+billId+"/order";
	}
	/** 放弃订单 **/
	public static String getDropOrder(int orderId){
		return BASE_URL + "technician/order/" + orderId + "/cancel";
	}
}
