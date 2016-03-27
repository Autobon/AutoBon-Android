package cn.com.incardata.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.incardata.autobon.R;

/**
 * Created by zhangming on 2016/3/23.
 * 加载对话框
 */
public class CommonDialog extends Dialog{
    private Context context;
    private boolean cancelable;
    private AnimationDrawable animation;
    private ImageView image;
    private TextView message;
    private int color = Color.BLACK;

    public CommonDialog(Context context){
        super(context, R.style.TipsDialog);
        this.context = context;
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        // TODO 自动生成的构造函数存根
        super(context, cancelable, cancelListener);
        this.context = context;
        this.cancelable = cancelable;
    }

    public void setMsg(String msg){
        message.setText(msg);
    }

    /**
     * 显示Dialog
     * @param gravity --显示位置
     */
    public void setDisplay(int gravity){
        setDialogView(gravity);
        show();
    }

    public void setColor(int textColor) {
        color  = textColor;
        message.setTextColor(color);
    }

    private void setDialogView(int gravity) {
        setContentView(R.layout.common_dialog);
        image = (ImageView) findViewById(R.id.iv_progress);
        animation = (AnimationDrawable) image.getBackground();
        message = (TextView)findViewById(R.id.message);
        message.setTextColor(color);

        Window dialogWindow = this.getWindow();
        /**获取对话框参数设置对象，可修改对话框显示位置、大小、透明度等属性*/
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.alpha = 0.8f; //透明度（0~1）值越大越不透明

        dialogWindow.setAttributes(layoutParams);
        /**获取dialog Window对象可直接设置Gravity属性*/
        dialogWindow.setGravity(gravity);

        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (animation != null && !animation.isRunning()) {
            animation.start(); 	//启动帧动画
        }

    }

    @Override
    public void dismiss() {
        if(isShowing()){
            animation.stop();
            super.dismiss();
            Log.i("test", "myDialog dismiss");
        }
    }

}
