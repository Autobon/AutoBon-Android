package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AuthorizationProgressEntity;
import cn.com.incardata.http.response.AuthorizationProgress_Data;
import cn.com.incardata.utils.T;

/**
 * 认证进度
 * @author wanghao
 */
public class AuthorizationProgressActivity extends Activity implements View.OnClickListener{
    private ImageView iv_back;
    private LinearLayout ll_failed_reason;
    private Button btn_change_info;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization_progress);
        initView();
        getDataFromServer();
    }

    private void initView(){
        context = this;
        iv_back = (ImageView)findViewById(R.id.iv_back);
        ll_failed_reason = (LinearLayout)findViewById(R.id.ll_failed_reason);
        btn_change_info = (Button) findViewById(R.id.btn_change_info);
    }

    private void getDataFromServer(){
        Http.getInstance().postTaskToken(NetURL.AUTHORIZATION_PROGRESS, AuthorizationProgressEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if(entity == null){
                    T.show(context,context.getString(R.string.get_info_failed));
                    return;
                }
                AuthorizationProgressEntity apEntity = (AuthorizationProgressEntity) entity;
                AuthorizationProgress_Data apData = apEntity.getData();
                if(apData!=null){
                    String status = apData.getStatus(); //审核状态
                    String avatar = apData.getAvatar(); //技师头像
                    String name = apData.getName();
                    String idNo = apData.getIdNo();  //身份证号
                    String skill = apData.getSkill();  //技能项
                    String bankCardNo = apData.getBankCardNo(); //银行卡号
                    String bank = apData.getBank();  //银行
                    String idPhoto = apData.getIdPhoto();  //身份证图像地址URL
                }
            }
        });
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
