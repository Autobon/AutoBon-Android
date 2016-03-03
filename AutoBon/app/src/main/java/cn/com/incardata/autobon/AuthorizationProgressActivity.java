package cn.com.incardata.autobon;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
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
    private ImageView iv_back,iv_circle,iv_card_photo;
    private LinearLayout ll_failed_reason;
    private Button btn_change_info;
    private Context context;
    private TextView tv_status,tv_username,tv_id_number,tv_bank_number;

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
        iv_circle = (ImageView) findViewById(R.id.iv_circle);
        iv_card_photo = (ImageView) findViewById(R.id.iv_card_photo);
        ll_failed_reason = (LinearLayout)findViewById(R.id.ll_failed_reason);
        btn_change_info = (Button) findViewById(R.id.btn_change_info);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_id_number = (TextView) findViewById(R.id.tv_id_number);
        tv_bank_number = (TextView) findViewById(R.id.tv_bank_number);

        iv_back.setOnClickListener(this);
        btn_change_info.setOnClickListener(this);
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
                    String avatar = apData.getAvatar(); //技师头像地址URL
                    String name = apData.getName();
                    String idNo = apData.getIdNo();  //身份证号
                    String skill = apData.getSkill();  //技能项
                    String bankCardNo = apData.getBankCardNo(); //银行卡号
                    String bank = apData.getBank();  //银行
                    String idPhoto = apData.getIdPhoto();  //身份证图像地址URL

                    if("REJECTED".equals(status)){  //审核失败
                        tv_status.setText(R.string.authorize_progress_failed_text);
                        ll_failed_reason.setVisibility(View.VISIBLE);
                        btn_change_info.setVisibility(View.VISIBLE);
                    }
                    tv_username.setText(name);
                    ImageLoaderCache.getInstance().loader(NetURL.IP_PORT+avatar,iv_circle);
                    ImageLoaderCache.getInstance().loader(NetURL.IP_PORT+idPhoto,iv_card_photo);
                    tv_id_number.setText(idNo);
                    tv_bank_number.setText(bankCardNo);
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
            case R.id.btn_change_info: //更改认证信息
                break;
        }
    }
}
