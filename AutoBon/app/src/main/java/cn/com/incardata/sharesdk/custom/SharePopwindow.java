package cn.com.incardata.sharesdk.custom;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import cn.com.incardata.autobon.R;

/**
 * 分享窗口
 * @author wanghao
 */
public class SharePopwindow extends PopupWindow{
	private Activity activity;
	private OnClickSharePlatfornSelect listener;

	public SharePopwindow(Activity activity) {
		this.activity = activity;
	}

	public void init() {
		View view = LayoutInflater.from(activity).inflate(R.layout.share_popupwindow, null, false);
		//设置SelectPicPopupWindow的View  
		this.setContentView(view);  
		LinearLayout qq = (LinearLayout) view.findViewById(R.id.shareQQ);
		LinearLayout qZone = (LinearLayout) view.findViewById(R.id.shareQZone);
		LinearLayout wechat = (LinearLayout) view.findViewById(R.id.shareWechat);
		LinearLayout wechatMoment = (LinearLayout) view.findViewById(R.id.shareWechatMoment);
		LinearLayout sinaWeibo = (LinearLayout) view.findViewById(R.id.shareSinaWeibo);

		OnClickShare onClick = new OnClickShare();
		qq.setOnClickListener(onClick);
		qZone.setOnClickListener(onClick);
		wechat.setOnClickListener(onClick);
		wechatMoment.setOnClickListener(onClick);
		sinaWeibo.setOnClickListener(onClick);

		//设置SelectPicPopupWindow弹出窗体的宽  
		this.setWidth(LayoutParams.MATCH_PARENT);  
		//设置SelectPicPopupWindow弹出窗体的高  
		this.setHeight(LayoutParams.WRAP_CONTENT);  
		//设置SelectPicPopupWindow弹出窗体可点击  
		this.setFocusable(true);  
		//设置SelectPicPopupWindow弹出窗体动画效果  
		this.setAnimationStyle(R.style.SharePop);  
		//实例化一个ColorDrawable颜色为半透明  
		ColorDrawable dw = new ColorDrawable(0x000000);  
		//设置SelectPicPopupWindow弹出窗体的背景  
		this.setBackgroundDrawable(dw); 

		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				closePopupWindow();
			}
		});
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		WindowManager.LayoutParams params=activity.getWindow().getAttributes();  
		params.alpha=0.7f;
		activity.getWindow().setAttributes(params);
	};

	/** 
	 * 关闭窗口 
	 */  
	private void closePopupWindow()  
	{  
		WindowManager.LayoutParams params = activity.getWindow().getAttributes();  
		params.alpha=1f;  
		activity.getWindow().setAttributes(params);  
	}  

	/**
	 * @return the listener
	 */
	public OnClickSharePlatfornSelect getListener() {
		return listener;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(OnClickSharePlatfornSelect listener) {
		this.listener = listener;
	}

	public class OnClickShare implements OnClickListener{

		@Override
		public void onClick(View v) {
			dismiss();
			switch (v.getId()) {
			case R.id.shareQQ:
				listener.OnClick(v, SharePlatform.QQ);
				break;
			case R.id.shareQZone:
				listener.OnClick(v, SharePlatform.QZONE);
				break;
			case R.id.shareWechat:
				listener.OnClick(v, SharePlatform.WECHAT);
				break;
			case R.id.shareWechatMoment:
				listener.OnClick(v, SharePlatform.WECHAT_MOMENT);
				break;
			case R.id.shareSinaWeibo:
				listener.OnClick(v, SharePlatform.SINA_WEIBO);
				break;
			}
		}
	}


}
