package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.RegisterEntity;
import cn.com.incardata.http.response.VerifySmsEntity;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;


/**
 * Created by Administrator on 2016/2/16.
 */
public class RegisterActivity extends Activity implements View.OnClickListener{
    private ImageView iv_back,iv_clear,iv_eye;
    private EditText et_phone,et_pwd,et_code;
    private Button register_btn,send_code_btn;
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
        et_code = (EditText) findViewById(R.id.et_code);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        register_btn = (Button) findViewById(R.id.register_btn);
        send_code_btn = (Button) findViewById(R.id.btn_send_code);
        tv_protocal = (TextView) findViewById(R.id.tv_protocal);
    }

    public void setListener(){
        iv_back.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
        iv_eye.setOnClickListener(this);
        register_btn.setOnClickListener(this);
        tv_protocal.setOnClickListener(this);
        send_code_btn.setOnClickListener(this);

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
                isFocus = !isFocus;
                if (isFocus) {
                    showOrHidenLoginPwd(true);
                    iv_eye.setImageResource(R.mipmap.eye_open);
                } else {
                    showOrHidenLoginPwd(false);
                    iv_eye.setImageResource(R.mipmap.eye_hidden);
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
            case R.id.btn_send_code: //倒计时发送验证码
                String phone = et_phone.getText().toString().trim();
                if(StringUtil.isEmpty(phone)){
                    T.show(context,context.getString(R.string.empty_phone));
                    return;
                }
                if(phone.length()!=11){
                    T.show(context,context.getString(R.string.error_phone));
                    return;
                }
                sendValidCode(phone);
                break;
        }
    }

    /**
     * 倒计时
     * @param time
     */
    private void countDownTimer(int time){
        new CountDownTimer(time*1000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                send_code_btn.setText("("+String.valueOf(millisUntilFinished/1000)+")"+context.getString(R.string.time_tips));
                send_code_btn.setClickable(false);
            }
            @Override
            public void onFinish() {
                send_code_btn.setText(context.getString(R.string.btn_text_send_code_repeat));
                send_code_btn.setClickable(true);
            }
        }.start();
    }

    private void sendValidCode(String phone){
        if(NetWorkHelper.isNetworkAvailable(context)) {
            BasicNameValuePair bv_phone = new BasicNameValuePair("phone",phone);
            Http.getInstance().getTaskToken(NetURL.VERIFY_SMS, VerifySmsEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null) {
                        T.show(context, context.getString(R.string.failed_code));
                        return;
                    }
                    VerifySmsEntity verifySmsEntity = (VerifySmsEntity) entity;
                    if(verifySmsEntity.isResult()){
                        countDownTimer(10); //验证码发送成功后,再倒计时60秒
                        T.show(context,context.getString(R.string.send_code_success));
                        return;
                    }
                }
            },bv_phone);
        }else{
            T.show(context,getString(R.string.no_network_tips));
            return;
        }
    }

    private void submitRegisterInfo(){
        String phone = et_phone.getText().toString().trim();
        String code = et_code.getText().toString().trim();
        String password = et_pwd.getText().toString().trim();
        if(StringUtil.isEmpty(phone)){
            T.show(context,context.getString(R.string.empty_phone));
            return;
        }
        if(phone.length()!=11){
            T.show(context,context.getString(R.string.error_phone));
            return;
        }
        if(StringUtil.isEmpty(password)){
            T.show(context,context.getString(R.string.empty_password));
            return;
        }
        if(StringUtil.isEmpty(code)){
            T.show(context,context.getString(R.string.empty_code));
            return;
        }
        if(code.length()!=6){  //验证码的长度不为6位,提示用户
            T.show(context,context.getResources().getString(R.string.code_length_tips));
            return;
        }

        if(password.length()<8){
            T.show(context,context.getString(R.string.password_length_tips));
            return;
        }
        if(!password.matches(".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]")){  //密码长度至少为8位,且为数字或字母组合
            T.show(context,context.getString(R.string.error_password));
            return;
        }

        List<BasicNameValuePair> mList = new ArrayList<BasicNameValuePair>();
        mList.add(new BasicNameValuePair("phone",phone));
        mList.add(new BasicNameValuePair("password",password));
        mList.add(new BasicNameValuePair("verifySms",code));

        if(NetWorkHelper.isNetworkAvailable(context)) {
            Http.getInstance().postTaskToken(NetURL.REGISTER, RegisterEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null) {
                        T.show(context, context.getString(R.string.register_failed_tips));
                        return;
                    }
                    RegisterEntity registerEntity = (RegisterEntity) entity;
                    if(registerEntity.isResult()){  //成功
                        T.show(context,context.getString(R.string.register_success_tips));
                        //TODO 注册成功后清空任务栈返回登录界面
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{  //失败
                        if("OCCUPIED_ID".equals(registerEntity.getError())){  //手机号已注册
                            T.show(context,context.getString(R.string.phone_has_register_tips));
                            return;
                        }else if("ILLEGAL_PARAM".equals(registerEntity.getError())){  //验证码错误
                            T.show(context,context.getString(R.string.valid_code_error_tips));
                            return;
                        }
                    }
                }
            },(BasicNameValuePair[]) mList.toArray(new BasicNameValuePair[mList.size()]));
        }else{
            T.show(context,getString(R.string.no_network_tips));
            return;
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
