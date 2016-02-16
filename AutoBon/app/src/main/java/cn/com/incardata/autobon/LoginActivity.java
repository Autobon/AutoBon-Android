package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends Activity implements View.OnClickListener{
    private ImageView iv_eye,iv_clear;
    private EditText et_pwd,et_phone;
    private TextView tv_register;
    private Button login_btn;
    private boolean isFocus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initView();
    }

    public void initView(){
        iv_eye = (ImageView) findViewById(R.id.iv_eye);
        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        tv_register = (TextView) findViewById(R.id.tv_register);
        login_btn =(Button) findViewById(R.id.login_btn);

        iv_eye.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_eye:
                if (isFocus) {
                    showOrHidenLoginPwd(true);
                    iv_eye.setImageResource(R.mipmap.eye_hidden);
                    isFocus = false;
                } else {
                    showOrHidenLoginPwd(false);
                    iv_eye.setImageResource(R.mipmap.eye_open);
                    isFocus = true;
                }
                break;
            case R.id.iv_clear: //清除
                et_phone.setText("");
                break;
            case R.id.tv_register: //注册
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.login_btn: //登陆
                login();
                break;
        }
    }

    /**
     * @param isShowPwd
     * 是否显示密码
     */
    private void showOrHidenLoginPwd(boolean isShowPwd) {
        if (isShowPwd) {
            et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    private void login(){

    }
}
