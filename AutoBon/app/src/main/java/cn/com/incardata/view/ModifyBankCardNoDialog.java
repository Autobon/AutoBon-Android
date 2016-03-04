package cn.com.incardata.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.incardata.autobon.R;
import cn.com.incardata.autobon.R.style;

/**
 * Created by zhangming on 2016/3/3.
 * 修改银行卡对话框
 */
public class ModifyBankCardNoDialog extends Dialog{
    private Context context;
    private TextView content;
    private LinearLayout ll_dialog;

    public ModifyBankCardNoDialog(Context context) {
        this(context, style.TipsDialog);
        this.context = context;
    }


    public ModifyBankCardNoDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        init();
    }

    private void init(){
        setContentView(R.layout.modify_bank_number_dialog);

        content = (TextView) findViewById(R.id.tv_modify_bank_number);
        ll_dialog = (LinearLayout) findViewById(R.id.ll_dialog);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    public void setOnClickListener(View.OnClickListener listener){
        ll_dialog.setOnClickListener(listener);
    }

    public void setDialogAttribute(OnDialogBaseAttribute attribute){
        attribute.setOnDialogBaseAttribute();
    }

    public interface OnDialogBaseAttribute{
        /**
         * 设置分享对话框基本属性,宽度高度等
         */
        public void setOnDialogBaseAttribute();
    }
}
