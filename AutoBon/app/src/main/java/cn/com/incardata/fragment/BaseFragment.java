package cn.com.incardata.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import cn.com.incardata.view.CommonDialog;

/**
 * Created by wanghao on 16/2/23.
 */
public class BaseFragment extends Fragment{
    protected CommonDialog dialog;

    protected void startActivity(Class<?> cls){
        Intent i = new Intent(getContext(), cls);
        startActivity(i);
    }
    protected void startActivity(Class<?> cls, Bundle bundle){
        Intent i = new Intent(getContext(), cls);
        i.putExtras(bundle);
        startActivity(i);
    }

    public Context getContext(){
        return this.getActivity();
    }


    /**=== 封装对话框调用方法(默认提示：正在加载…) ===**/
    public void showDialog(){
        if (dialog == null) {
            dialog = new CommonDialog(getContext());
            dialog.setDisplay(Gravity.CENTER);
            return;
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void showDialog(String message){
        if (dialog == null) {
            dialog = new CommonDialog(getContext());
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
    public void onDestroy() {
        super.onDestroy();
        cancelDialog();
        dialog = null;
    }
}
