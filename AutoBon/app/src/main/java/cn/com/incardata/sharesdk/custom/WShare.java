package cn.com.incardata.sharesdk.custom;

import android.content.Context;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 自定义分享平台
 * @author wanghao
 */
public class WShare {
	private Context context; 
	public WShare(Context context) {
		this.context = context;
		ShareSDK.initSDK(context);
	}

	/**
	 * 分享到QQ好友
	 * @param title 
	 * @param titleUrl
	 * @param content
	 * @param imageUrl
	 * @return false 未找到客户端
	 */
	public boolean shareQQ(String title, String titleUrl, String content, String imageUrl){
		ShareSDK.initSDK(context);
		Platform qq = ShareSDK.getPlatform(QQ.NAME);
		if (!qq.isClientValid()) {
			return false;
		}

		ShareParams sp = new ShareParams();
		sp.setTitle(title);
		sp.setTitleUrl(titleUrl);
		sp.setText(content);
		sp.setImageUrl(imageUrl);

		qq.share(sp);
		return true;
	}



	/**
	 * 分享到QQ空间
	 * @param title
	 * @param titleUrl
	 * @param content
	 * @param imageUrl
	 * @param site
	 * @param siteUrl
	 * @return false 未找到客户端
	 */
	public boolean shareQZone(String title, String titleUrl, String content, String imageUrl, String site, String siteUrl){
		ShareSDK.initSDK(context);
		Platform qzone = ShareSDK.getPlatform(QZone.NAME);
		if (!qzone.isClientValid()) {
			return false;
		}

		ShareParams sp = new ShareParams();
		sp.setTitle(title);
		sp.setTitleUrl(titleUrl);
		sp.setText(content);
		sp.setImageUrl(imageUrl);
		sp.setSite(site);
		sp.setSiteUrl(siteUrl);

		qzone.share(sp);
		return true;
	}



	/**
	 * 分享到微信好友
	 * @param title
	 * @param titleUrl
	 * @param content
	 * @param imageUrl
	 * @param url
	 * @return false 未找到客户端
	 */
	public boolean shareWechat(String title, String titleUrl, String content, String imageUrl, String url){
		ShareSDK.initSDK(context);
		Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
		if (!wechat.isClientValid()) {
			return false;
		}

		ShareParams sp = new ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);
		sp.setTitle(title);
		sp.setTitleUrl(titleUrl);
		sp.setText(content);
		sp.setImageUrl(imageUrl);
		sp.setUrl(url);

		wechat.share(sp);
		return true;
	}

	/**
	 * 分享到微信朋友圈
	 * @param title
	 * @param titleUrl
	 * @param content
	 * @param imageUrl
	 * @param url
	 * @return false 未找到客户端
	 */
	public boolean shareWechatMoment(String title, String titleUrl, String content, String imageUrl, String url){
		ShareSDK.initSDK(context);
		Platform moment = ShareSDK.getPlatform(WechatMoments.NAME);
		if (!moment.isClientValid()) {
			return false;
		}

		ShareParams sp = new ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);
		sp.setTitle(title);
		sp.setTitleUrl(titleUrl);
		sp.setText(content);
		sp.setImageUrl(imageUrl);
		sp.setUrl(url);

		moment.share(sp);
		return true;
	}
	
	
}
