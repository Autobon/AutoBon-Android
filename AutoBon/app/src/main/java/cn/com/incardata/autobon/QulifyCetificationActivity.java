package cn.com.incardata.autobon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by zhangming on 2016/3/9.
 * 资格认证
 */
public class QulifyCetificationActivity extends Activity implements View.OnClickListener{
    private LinearLayout ll_fill_layout;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qulify_cetification_activity);
        initView();
        setListener();
    }

    private void initView(){
        iv_back = (ImageView)findViewById(R.id.iv_back);
        ll_fill_layout = (LinearLayout) findViewById(R.id.ll_fill_layout);
        ll_fill_layout.removeAllViews();
        for(int i=0;i<10;i++){
            View view = getLayoutInflater().inflate(R.layout.qulify_cetification_item,null);
            ll_fill_layout.addView(view);
        }
    }

    private void setListener(){
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
