package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.com.incardata.application.Language;
import cn.com.incardata.application.MyApplication;
import cn.com.incardata.utils.StringUtil;

public class LoginActivity extends Activity implements View.OnClickListener{
    private ImageView iv_eye,iv_clear;
    private EditText et_pwd,et_phone;
    private TextView tv_register,tv_language,tv_forget_pwd;
    private Button login_btn;
    private boolean isFocus;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initView();
        setListener();
    }

    public void initView(){
        context = this;
        iv_eye = (ImageView) findViewById(R.id.iv_eye);
        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_language = (TextView) findViewById(R.id.tv_language);
        tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
        login_btn =(Button) findViewById(R.id.login_btn);
    }

    public void setListener(){
        iv_eye.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_language.setOnClickListener(this);
        tv_forget_pwd.setOnClickListener(this);
        login_btn.setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.iv_eye:
                isFocus = !isFocus;
                if (isFocus) {
                    showOrHidenLoginPwd(true);
                    iv_eye.setImageResource(R.mipmap.eye_open);
                } else {
                    showOrHidenLoginPwd(false);
                    iv_eye.setImageResource(R.mipmap.eye_hidden);
                }
                break;
            case R.id.iv_clear: //清除
                et_phone.setText("");
                break;
            case R.id.tv_register: //注册
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_btn: //登陆
                login();
                break;
            case R.id.tv_language: //双语切换
                switchLang();
                break;
            case R.id.tv_forget_pwd: //忘记密码
                Intent i = new Intent(this,FindPasswordActivity.class);
                startActivity(i);
                break;
        }
    }

    /**
     * 语言切换
     */
    private void switchLang() {
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_language, null);
        final PopupWindow mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        view.findViewById(R.id.language_chinese).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().switchLanguage(Language.SIMPLIFIED_CHINESE);
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });
        view.findViewById(R.id.language_english).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().switchLanguage(Language.ENGLISH);
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAsDropDown(tv_language, 0, 0);
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
        /**
        new Thread(new Runnable() {
            @Override
            public void run() {
                BasicNameValuePair value01 = new BasicNameValuePair("phone","15972219148");
                BasicNameValuePair value02 = new BasicNameValuePair("password","12345678");
                String result = HttpClientInCar.PostFormByHttpClient(context,"http://121.40.157.200:51234/api/mobile/technician/login",value01,value02);
                Log.i("test","========================>"+result);
            }
        }).start();
         **/
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
