package cn.com.incardata.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import cn.com.incardata.autobon.R;

/**
 * Created by yang on 2016/12/9.
 */
public class LeverPopupWindow extends PopupWindow {
    private Activity activity;
    private ImageView back;

    public LeverPopupWindow(Activity activity) {
        this.activity = activity;
    }

    public void init(){
        View view = LayoutInflater.from(activity).inflate(R.layout.lever_popupwindow,null,false);
        setContentView(view);
        back = (ImageView) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closePopupWindow();
            }
        });

        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth((int) (activity.getWindowManager().getDefaultDisplay().getWidth() * 0.8));
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight((int) (activity.getWindowManager().getDefaultDisplay().getHeight() * 0.2));
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
    public void closePopupWindow()
    {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha=1f;
        activity.getWindow().setAttributes(params);
        dismiss();
    }

}
