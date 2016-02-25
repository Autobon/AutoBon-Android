package cn.com.incardata.autobon;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import cn.com.incardata.utils.DecimalUtil;

/**
 * Created by zhangming on 2016/2/25.
 */
public class MyInfoActivity extends Activity{
    private RatingBar mRatingbar;
    private TextView tv_rate;
    private LinearLayout ll_my_package;

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
        ll_my_package = (LinearLayout) findViewById(R.id.ll_my_package);
    }

    private void initData(){
        float rating = DecimalUtil.FloatDecimal1(mRatingbar.getRating());
        tv_rate.setText(String.valueOf(rating));
    }

    private void setListener(){

    }
}
