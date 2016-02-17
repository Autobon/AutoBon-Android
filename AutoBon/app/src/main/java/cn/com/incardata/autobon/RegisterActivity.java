package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.incardata.autobon.util.StringUtil;

/**
 * Created by Administrator on 2016/2/16.
 */
public class RegisterActivity extends Activity implements View.OnClickListener{
    private ImageView iv_back,iv_clear,iv_eye;
    private EditText et_phone,et_pwd;
    private Button register_btn;
    private boolean isFocus;
    private TextView tv_protocal;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        initView();
        setListener();
    }

    public void initView(){
        context = this;
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        iv_eye = (ImageView) findViewById(R.id.iv_eye);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        register_btn = (Button) findViewById(R.id.register_btn);
        tv_protocal = (TextView) findViewById(R.id.tv_protocal);
    }

    public void setListener(){
        iv_back.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
        iv_eye.setOnClickListener(this);
        register_btn.setOnClickListener(this);
        tv_protocal.setOnClickListener(this);

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                if(StringUtil.isEmpty(text)){  //文本空,隐藏删除图片
                    iv_clear.setVisibility(View.INVISIBLE);
                }else{
                    iv_clear.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear:
                et_phone.setText("");
                break;
            case R.id.iv_eye:
                if (isFocus) {
                    showOrHidenLoginPwd(true);
                    iv_eye.setImageResource(R.mipmap.eye_open);
                    isFocus = false;
                } else {
                    showOrHidenLoginPwd(false);
                    iv_eye.setImageResource(R.mipmap.eye_hidden);
                    isFocus = true;
                }
                break;
            case R.id.register_btn:
                submitRegisterInfo();
                break;
            case R.id.tv_protocal: //服务协议
                Intent intent = new Intent();
                intent.setClass(context,ServiceProtocalActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void submitRegisterInfo(){
        //TODO 注册成功后清空任务栈返回登录界面
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
