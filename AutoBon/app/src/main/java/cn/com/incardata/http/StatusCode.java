package cn.com.incardata.http;

public class StatusCode {
	/**
	 * 
	 */
	public final static String STATUS_SUCCESS = "success";
	public final static String STATUS_FAILURE = "failure";

	/**
	 * 账号已认证
	 */
	public final static String VERIFIED = "VERIFIED";
	/**
	 * 账号未认证
	 */
	public final static String NOTVERIFIED = "NOTVERIFIED";
	/**
	 * 等待审核中
	 */
	public final static String IN_VERIFICATION = "IN_VERIFICATION";
	/**
	 * 认证未通过
	 */
	public final static String REJECTED = "REJECTED";
	/**
	 * 帐户已被禁用
	 */
	public final static String BANNED = "BANNED";
}
