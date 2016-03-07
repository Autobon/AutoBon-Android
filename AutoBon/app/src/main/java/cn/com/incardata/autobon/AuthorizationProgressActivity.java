package cn.com.incardata.autobon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.MyInfoEntity;
import cn.com.incardata.http.response.MyInfo_Data;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;

/**
 * 认证进度
 * @author wanghao
 */
public class AuthorizationProgressActivity extends BaseActivity implements View.OnClickListener{
    private final static int requestCode = 0x11;
    private ImageView iv_back,iv_circle,iv_card_photo;
    private LinearLayout ll_failed_reason;
    private Button btn_change_info;
    private Context context;
    private TextView tv_status,tv_username,tv_id_number,tv_bank_number,tv_bank,tv_skill;

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
        tv_bank = (TextView) findViewById(R.id.tv_bank);
        tv_skill = (TextView) findViewById(R.id.tv_skill);

        iv_back.setOnClickListener(this);
        btn_change_info.setOnClickListener(this);
    }

    /**
     * 此处获取认证进度信息返回的就是我的信息的字段
     */
    private void getDataFromServer(){
        Http.getInstance().getTaskToken(NetURL.MY_INFO_URL, MyInfoEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if(entity == null){
                    T.show(context,context.getString(R.string.get_info_failed));
                    return;
                }
                MyInfoEntity apEntity = (MyInfoEntity) entity;
                MyInfo_Data apData = apEntity.getData();
                if(apData!=null){
                    String status = apData.getStatus(); //审核状态
                    String avatar = apData.getAvatar(); //技师头像地址URL
                    String name = apData.getName();
                    String idNo = apData.getIdNo();  //身份证号
                    String skill = apData.getSkill();  //技能项
                    String bankCardNo = apData.getBankCardNo(); //银行卡号
                    String bank = apData.getBank();  //银行
                    String idPhoto = apData.getIdPhoto();  //身份证图像地址URL
                    //String verifyMsg = apData.getVerifyMsg();  //原因

                    StringBuilder sb = new StringBuilder();
                    MyApplication myApplication = MyApplication.getInstance();
                    if(StringUtil.isNotEmpty(skill) && skill.contains(",")){
                        String[] skillArray = skill.split(",");
                        for(int i=0;i<skillArray.length;i++){
                            sb.append(myApplication.getSkill(Integer.parseInt(skillArray[i])));
                            if(i<skillArray.length-1){
                                sb.append(",");
                            }
                        }
                    }
                    tv_skill.setText(sb.toString());

                    if("IN_VERIFICATION".equals(status)){ //等待审核
                        tv_status.setText(context.getString(R.string.authorize_IN_VERIFICATION));
                    }else if("REJECTED".equals(status)){  //审核失败
                        ll_failed_reason.setVisibility(View.VISIBLE);
                        btn_change_info.setVisibility(View.VISIBLE);
                        tv_status.setText(context.getString(R.string.authorize_REJECTED));
                    }
                    tv_username.setText(name);
                    ImageLoaderCache.getInstance().loader(NetURL.IP_PORT+avatar,iv_circle);
                    ImageLoaderCache.getInstance().loader(NetURL.IP_PORT+idPhoto,iv_card_photo);
                    tv_id_number.setText(idNo);
                    tv_bank_number.setText(bankCardNo);
                    tv_bank.setText(bank);
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
                Intent intent = new Intent(getContext(), AuthorizeActivity.class);
                intent.putExtra("isAgain", true);
                startActivityForResult(intent, requestCode);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
