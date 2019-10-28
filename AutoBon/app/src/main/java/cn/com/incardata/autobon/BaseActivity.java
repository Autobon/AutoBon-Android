package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import cn.com.incardata.permission.PermissionUtil;
import cn.com.incardata.view.CommonDialog;

/**
 * activity基类
 * Created by wanghao on 16/2/23.
 */
public class BaseActivity extends Activity{
    protected CommonDialog dialog;
    /**
     * 权限控制
     */
    protected PermissionUtil permissionUtil;

    protected void startActivity(Class<?> cls){
        Intent i = new Intent(this, cls);
        startActivity(i);
    }
    protected void startActivity(Class<?> cls, Bundle bundle){
        Intent i = new Intent(this, cls);
        i.putExtras(bundle);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionUtil != null) {
            //权限回调处理
            permissionUtil.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected BaseActivity getContext(){
        return this;
    }


    /**=== 封装对话框调用方法(默认提示：正在加载…) ===**/
    public void showDialog(){
        if (dialog == null) {
            dialog = new CommonDialog(this);
            dialog.setDisplay(Gravity.CENTER);
            return;
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void showDialog(String message){
        if (dialog == null) {
            dialog = new CommonDialog(this);
            dialog.setDisplay(Gravity.CENTER);
            return;
        }
        dialog.setMsg(message);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void cancelDialog(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
//            dialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelDialog();
        dialog = null;
    }

}
