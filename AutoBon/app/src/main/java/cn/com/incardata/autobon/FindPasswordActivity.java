package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;

/**
 * Created by Administrator on 2016/2/17.
 */
public class FindPasswordActivity extends Activity implements View.OnClickListener{
    private ImageView iv_back,iv_clear;
    private EditText et_phone,et_code;
    private Timer timer;
    private Button btn_check,next_btn;
    private Context context;
    private static int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_password_activity);
        initView();
        setListener();
    }

    public void initView(){
        context = this;
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        btn_check = (Button) findViewById(R.id.btn_check);
    }

    public void setListener(){
        iv_back.setOnClickListener(this);
        btn_check.setOnClickListener(this);
        next_btn.setOnClickListener(this);

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

        et_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){  //失去焦点
                    String code = et_code.getText().toString().trim();
                    if(code.length()!=6){  //验证码的长度不为6位,提示用户
                        T.show(context,context.getResources().getString(R.string.code_length_tips));
                    }
                }
            }
        });
    }

    public void openTimerTask(){
        count = 60;  //默认时间1分钟
        btn_check.setText(getString(R.string.text_get_code));

        TimerTask task = new MyTimerTask();
        timer = new Timer();
        timer.schedule(task,0,1000);  //执行定时任务
    }

    /**
     * 关闭定时器
     */
    private void closeTimerTask(){
        timer.cancel();
    }

    /**
     * 自定义定时器
     * @author Administrator
     */
    class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            if(count>0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn_check.setFocusable(false);
                        btn_check.setClickable(false);

                        String str = btn_check.getText().toString().trim();
                        if(StringUtil.isNotEmpty(str)){
                            btn_check.setText("("+count+")"+context.getResources().getString(R.string.second_text));
                            count--;
                        }
                    }
                });
            }else{
                closeTimerTask();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn_check.setText(context.getResources().getString(R.string.repeat_get_text));
                        btn_check.setTextSize(15);
                        btn_check.setFocusable(true);
                        btn_check.setClickable(true);
                    }
                });
            }
        }
    }

    /**
     * 发送验证码
     */
    public void sendValidCode(){
        //TODO
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_check:
                openTimerTask();
                sendValidCode();
                break;
            case R.id.next_btn: //下一步
                Intent intent = new Intent();
                intent.setClass(context,ResetPasswordActivity.class);
                startActivity(intent);
                break;
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
