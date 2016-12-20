package cn.com.incardata.autobon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.MyMessage;
import cn.com.incardata.http.response.MyMessageData;
import cn.com.incardata.http.response.MyMessageEntity;
import cn.com.incardata.utils.T;

/**
 * 认证进度
 *
 * @author wanghao
 */
public class AuthorizationProgressActivity extends BaseActivity implements View.OnClickListener {
    private final static int requestCode = 0x11;
    private ImageView iv_back, iv_circle, iv_card_photo;
    private LinearLayout ll_failed_reason;
    private TextView failedReason;
    private Button btn_change_info;
    private Context context;
    private RatingBar[] ratingBars = new RatingBar[4];
    private TextView[] workYears = new TextView[4];
    private TextView tv_status, tv_username, tv_reference, tv_id_number, tv_bank_number, tv_bank, tv_bank_address, tv_resume;
    private TextView title;
    private boolean isInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization_progress);
        initView();
        getDataFromServer();
    }

    private void initView() {
        isInfo = getIntent().getBooleanExtra("isInfo", false);
        context = this;
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_circle = (ImageView) findViewById(R.id.iv_circle);
        iv_card_photo = (ImageView) findViewById(R.id.iv_card_photo);
        ll_failed_reason = (LinearLayout) findViewById(R.id.ll_failed_reason);
        failedReason = (TextView) findViewById(R.id.failed_reason);
        btn_change_info = (Button) findViewById(R.id.btn_change_info);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_reference = (TextView) findViewById(R.id.tv_reference);
        tv_id_number = (TextView) findViewById(R.id.tv_id_number);
        tv_bank_number = (TextView) findViewById(R.id.tv_bank_number);
        tv_bank = (TextView) findViewById(R.id.tv_bank);
        tv_bank_address = (TextView) findViewById(R.id.tv_bank_address);
        tv_resume = (TextView) findViewById(R.id.tv_resume);
        ratingBars[0] = (RatingBar) findViewById(R.id.ge_ratingBar);
        ratingBars[1] = (RatingBar) findViewById(R.id.yin_ratingBar);
        ratingBars[2] = (RatingBar) findViewById(R.id.che_ratingBar);
        ratingBars[3] = (RatingBar) findViewById(R.id.mei_ratingBar);
        workYears[0] = (TextView) findViewById(R.id.ge_workYear);
        workYears[1] = (TextView) findViewById(R.id.yin_workYear);
        workYears[2] = (TextView) findViewById(R.id.che_workYear);
        workYears[3] = (TextView) findViewById(R.id.mei_workYear);
        title = (TextView) findViewById(R.id.title);
//        tv_skill = (TextView) findViewById(R.id.tv_skill);
        if (isInfo) {
            title.setText(R.string.userMessage);
        }

        iv_back.setOnClickListener(this);
        btn_change_info.setOnClickListener(this);
        iv_card_photo.setOnClickListener(this);
    }

    private MyMessage myMessage;

    /**
     * 此处获取认证进度信息返回的就是我的信息的字段
     */
    private void getDataFromServer() {
        Http.getInstance().getTaskToken(NetURL.MY_INFO_URLV2, MyMessageEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if (entity == null) {
                    T.show(context, context.getString(R.string.get_info_failed));
                    return;
                }
                MyMessageEntity myMessageEntity = (MyMessageEntity) entity;
//                if (myMessageEntity.isStatus()){
//
//                }
                MyMessageData myMessageData = JSON.parseObject(myMessageEntity.getMessage().toString(), MyMessageData.class);
                myMessage = myMessageData.getTechnician();
                if (myMessage != null) {
                    String status = myMessage.getStatus(); //审核状态
                    String avatar = myMessage.getAvatar(); //技师头像地址URL
                    String name = myMessage.getName();
                    String idNo = myMessage.getIdNo();  //身份证号
                    String resume = myMessage.getResume();
                    String reference = myMessage.getReference();
                    String bankCardNo = myMessage.getBankCardNo(); //银行卡号
                    String bank = myMessage.getBank();  //银行
                    String bankAdress = myMessage.getBankAddress();
                    String idPhoto = myMessage.getIdPhoto();  //身份证图像地址URL
//                    String verifyMsg = myMessage.getVerifyMsg();  //原因


                    if ("IN_VERIFICATION".equals(status)) { //等待审核
                        tv_status.setText(context.getString(R.string.authorize_IN_VERIFICATION));
                    } else if ("REJECTED".equals(status)) {  //审核失败
                        ll_failed_reason.setVisibility(View.VISIBLE);
                        btn_change_info.setVisibility(View.VISIBLE);
                        failedReason.setText(myMessage.getVerifyMsg());
                        tv_status.setText(context.getString(R.string.authorize_REJECTED));
                    } else if ("VERIFIED".equals(status)) {
                        tv_status.setText("已通过");
                        if(isInfo){
                            btn_change_info.setVisibility(View.VISIBLE);
                        }
                    }
                    tv_username.setText(name);
                    if (TextUtils.isEmpty(reference)) {
                        tv_reference.setText(R.string.no);
                    } else {
                        tv_reference.setText(reference);
                    }
                    ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + avatar, iv_circle);
                    ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + idPhoto, iv_card_photo);
                    tv_id_number.setText(idNo);
                    tv_bank_number.setText(bankCardNo);
                    tv_bank.setText(bank);
                    tv_bank_address.setText(bankAdress);
                    if (TextUtils.isEmpty(resume)) {
                        tv_resume.setText(R.string.no);
                    } else {
                        tv_resume.setText(resume);
                    }
                    ratingBars[0].setRating(myMessage.getFilmLevel());
                    ratingBars[1].setRating(myMessage.getCarCoverLevel());
                    ratingBars[2].setRating(myMessage.getColorModifyLevel());
                    ratingBars[3].setRating(myMessage.getBeautyLevel());
                    workYears[0].setText(myMessage.getFilmWorkingSeniority() + "年");
                    workYears[1].setText(myMessage.getCarCoverWorkingSeniority() + "年");
                    workYears[2].setText(myMessage.getColorModifyWorkingSeniority() + "年");
                    workYears[3].setText(myMessage.getBeautyWorkingSeniority() + "年");
                } else {
                    T.show(getContext(), getString(R.string.dataUploadFailed));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_change_info: //更改认证信息
                if (myMessage == null) {
                    T.show(getContext(), R.string.info_load_failure);
                    return;
                }
                Intent intent = new Intent(getContext(), AuthorizeActivity.class);
                intent.putExtra("isAgain", true);

                intent.putExtra("isInfo",isInfo);

//                intent.putExtra("name", myMessage.getName());
//                intent.putExtra("headUrl", myMessage.getAvatar());
//                intent.putExtra("idNumber", myMessage.getIdNo());
//                intent.putExtra("skillArray", myMessage.getSkill());
//                intent.putExtra("idUrl", myMessage.getIdPhoto());
//                intent.putExtra("bankName", myMessage.getBank());
//                intent.putExtra("bankNo", myMessage.getBankCardNo());
                startActivity(intent);
                finish();
                break;
            case R.id.iv_card_photo:
                String[] urls = new String[]{myMessage.getIdPhoto()};
                openImage(0,urls);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    /** 查看图片
     * @param position
     * @param urls
     */
    private void openImage(int position, String... urls){
        Bundle bundle = new Bundle();
        bundle.putStringArray(EnlargementActivity.IMAGE_URL, urls);
        bundle.putInt(EnlargementActivity.POSITION, position);
        startActivity(EnlargementActivity.class, bundle);
    }
}
