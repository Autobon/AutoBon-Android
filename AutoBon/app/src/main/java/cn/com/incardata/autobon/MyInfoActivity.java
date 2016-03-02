package cn.com.incardata.autobon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import cn.com.incardata.utils.DecimalUtil;

/**
 * Created by zhangming on 2016/2/25.
 */
public class MyInfoActivity extends BaseActivity implements View.OnClickListener{
    private RatingBar mRatingbar;
    private TextView tv_rate,tv_logout;
    private LinearLayout my_ll_package,ll_my_package,ll_modify_pwd;
    private ImageView iv_back;
    private boolean isVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_info_activity);
        initView();
        initData();
        setListener();
    }

    public void initView(){
        mRatingbar = (RatingBar) findViewById(R.id.mratingbar);
        tv_rate = (TextView) findViewById(R.id.tv_rate);
        tv_logout = (TextView) findViewById(R.id.tv_logout);
        my_ll_package = (LinearLayout) findViewById(R.id.my_ll_package);
        ll_my_package = (LinearLayout) findViewById(R.id.ll_my_package);
        ll_modify_pwd = (LinearLayout) findViewById(R.id.ll_modify_pwd);
        iv_back = (ImageView) findViewById(R.id.iv_back);
    }

    private void initData(){
        float rating = DecimalUtil.FloatDecimal1(mRatingbar.getRating());
        tv_rate.setText(String.valueOf(rating));
    }

    private void setListener(){
        my_ll_package.setOnClickListener(this);
        ll_modify_pwd.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_ll_package:
                if(!isVisible){
                    ll_my_package.setVisibility(View.VISIBLE);
                    isVisible = true;
                }else{
                    ll_my_package.setVisibility(View.GONE);
                    isVisible = false;
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_logout:
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.ll_modify_pwd:
                Intent i = new Intent(this, ModifyPasswordActivity.class);
                startActivity(i);
                break;
        }
    }
}
