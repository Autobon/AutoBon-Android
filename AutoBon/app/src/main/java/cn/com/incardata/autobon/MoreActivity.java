package cn.com.incardata.autobon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by zhangming on 2016/3/9.
 * 更多的入口
 */
public class MoreActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_back;
    private LinearLayout ll_qulify_cetification,ll_free_training,ll_service_center,ll_message_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_activity);
        initView();
        setListener();
    }

    private void initView(){
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_qulify_cetification = (LinearLayout) findViewById(R.id.ll_qulify_cetification);
        ll_free_training = (LinearLayout) findViewById(R.id.ll_free_training);
        ll_service_center = (LinearLayout) findViewById(R.id.ll_service_center);
        ll_message_notification = (LinearLayout) findViewById(R.id.ll_message_notification);
    }

    private void setListener(){
        iv_back.setOnClickListener(this);
        ll_qulify_cetification.setOnClickListener(this);
        ll_free_training.setOnClickListener(this);
        ll_service_center.setOnClickListener(this);
        ll_message_notification.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_qulify_cetification: //资格认证
                startActivity(QulifyCetificationActivity.class);
                break;
            case R.id.ll_free_training:  //免费培训
                startActivity(FreeTrainActivity.class);
                break;
            case R.id.ll_service_center:  //服务中心
                startActivity(ServiceCenterActivity.class);
                break;
            case R.id.ll_message_notification:  //消息通知
                startActivity(NotificationMessageActivity.class);
                break;
        }
    }
}
