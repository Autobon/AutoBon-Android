package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.CircleImageView;

/**
 * Created by Administrator on 2016/2/19.
 */
public class AddContactActivity extends Activity implements View.OnClickListener{
    private CircleImageView circle_image;
    private ImageView iv_back,iv_clear;
    private TextView tv_search,tv_username;
    private EditText et_phone;
    private Button btn_submit;
    private LinearLayout ll_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_activity);
        initView();
        setListener();
    }

    public void initView(){
        circle_image = (CircleImageView) findViewById(R.id.iv_circle);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_search = (TextView) findViewById(R.id.tv_search);
        tv_username = (TextView) findViewById(R.id.tv_username);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        ll_contact = (LinearLayout) findViewById(R.id.ll_contact);
    }

    public void setListener(){
        iv_back.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
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
            case R.id.tv_search:  //搜索
                String phone = et_phone.getText().toString().trim();
                if(StringUtil.isEmpty(phone)){
                    T.show(this,getString(R.string.empty_phone));
                    return;
                }
                if(phone.length()!=11){
                    T.show(this,getString(R.string.error_phone));
                    return;
                }
                //TODO 网络请求,根据手机号寻找联系人,显示订单数
                ll_contact.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_submit:  //确定
                Intent i=new Intent();
                String username = tv_username.getText().toString().trim();
                i.putExtra("username",username);
                this.setResult(RESULT_OK,i);
                finish();
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
