package cn.com.incardata.autobon;

import android.content.Context;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.ChangePasswordEntity;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;

/**
 * Created by zhangming on 2016/2/26.
 */
public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener{
    private EditText et_pwd;
    private EditText et_new_pwd;
    private Button submit_pwd_btn;
    private Context context;
    private ImageView iv_eye,iv_new_eye,iv_back;
    private boolean isOldFocus;
    private boolean isNewFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_newpassword);
        initView();
    }

    public void initView(){
        context = this;
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
        submit_pwd_btn = (Button)findViewById(R.id.submit_pwd_btn);
        iv_eye = (ImageView) findViewById(R.id.iv_eye);
        iv_new_eye = (ImageView) findViewById(R.id.iv_new_eye);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        iv_eye.setOnClickListener(this);
        iv_new_eye.setOnClickListener(this);
        submit_pwd_btn.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void submitRegisterInfo(){
        String old_pwd = et_pwd.getText().toString().trim();
        String new_pwd = et_new_pwd.getText().toString().trim();
        if(StringUtil.isEmpty(old_pwd)){
            T.show(this,getString(R.string.old_empty_tips));
            return;
        }
        if(StringUtil.isEmpty(new_pwd)){
            T.show(this,getString(R.string.new_empty_tips));
            return;
        }

        if(old_pwd.length()<8){
            T.show(this,getString(R.string.old_length_tips));
            return;
        }
        if(new_pwd.length()<8){
            T.show(this,getString(R.string.new_length_tips));
            return;
        }
        if(!old_pwd.matches(".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]")){
            T.show(this,getString(R.string.error_password));
            return;
        }
        if(!new_pwd.matches(".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]")){  //密码长度至少为8位,且为数字或字母组合
            T.show(context,context.getString(R.string.error_password));
            return;
        }

        BasicNameValuePair oldPassword = new BasicNameValuePair("oldPassword",old_pwd);
        BasicNameValuePair newPassword = new BasicNameValuePair("newPassword",new_pwd);

        if(NetWorkHelper.isNetworkAvailable(context)) {
            Http.getInstance().postTaskToken(NetURL.CHANGE_PASSWORD, ChangePasswordEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null) {
                        T.show(context, context.getString(R.string.modify_pwd_failed));
                        return;
                    }
                    ChangePasswordEntity changePasswordEntity = (ChangePasswordEntity) entity;
                    if (changePasswordEntity.isResult()) {
                        T.show(context, context.getString(R.string.modify_pwd_success));
                        finish();
                        return;
                    } else {
                        if("ILLEGAL_PARAM".equals(changePasswordEntity.getError())){
                            T.show(context,context.getString(R.string.old_pwd_error_tips));
                            return;
                        }
                    }
                }
            }, oldPassword, newPassword);
        }else{
            T.show(this,getString(R.string.no_network_tips));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_eye:
                isOldFocus = !isOldFocus;
                if (isOldFocus) {
                    showOrHidenLoginPwd(et_pwd,true);
                    iv_eye.setImageResource(R.mipmap.eye_open);
                } else {
                    showOrHidenLoginPwd(et_pwd,false);
                    iv_eye.setImageResource(R.mipmap.eye_hidden);
                }
                break;
            case R.id.iv_new_eye:
                isNewFocus = !isNewFocus;
                if (isNewFocus) {
                    showOrHidenLoginPwd(et_new_pwd,true);
                    iv_new_eye.setImageResource(R.mipmap.eye_open);
                } else {
                    showOrHidenLoginPwd(et_new_pwd,false);
                    iv_new_eye.setImageResource(R.mipmap.eye_hidden);
                }
                break;
            case R.id.submit_pwd_btn:
                submitRegisterInfo();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * @param isShowPwd
     * 是否显示密码
     */
    private void showOrHidenLoginPwd(EditText editText,boolean isShowPwd) {
        if (isShowPwd) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            //点击空白位置 隐藏软键盘
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}
