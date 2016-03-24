package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import cn.com.incardata.view.CommonDialog;

/**
 * activity基类
 * Created by wanghao on 16/2/23.
 */
public class BaseActivity extends Activity{
    protected CommonDialog dialog;

    protected void startActivity(Class<?> cls){
        Intent i = new Intent(this, cls);
        startActivity(i);
    }
    protected void startActivity(Class<?> cls, Bundle bundle){
        Intent i = new Intent(this, cls);
        i.putExtras(bundle);
        startActivity(i);
    }

    protected BaseActivity getContext(){
        return this;
    }


    /**=== 封装对话框调用方法 ===**/
    public void showDialog(String message){
        if (dialog == null) {
            dialog = new CommonDialog(this,message);
            dialog.setDisplay(Gravity.CENTER);
            return;
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void cancelDialog(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
