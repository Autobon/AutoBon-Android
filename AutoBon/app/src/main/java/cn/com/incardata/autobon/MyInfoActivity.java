package cn.com.incardata.autobon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.MyMessage;
import cn.com.incardata.http.response.MyMessageData;
import cn.com.incardata.http.response.MyMessageEntity;
import cn.com.incardata.utils.DecimalUtil;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.CircleImageView;

/** 我的信息
 * Created by zhangming on 2016/2/25.
 */
public class MyInfoActivity extends BaseActivity implements View.OnClickListener{
    private Context context;
    private RatingBar mRatingbar;
    private TextView tv_rate,tv_logout,tv_cost,tv_login_username,tv_order_num,tv_my_order_num,tv_standard_ommission;
    private LinearLayout ll_modify_pwd,ll_cost,ll_order_num;
    private LinearLayout collection_shop;                 //收藏商户
    private ImageView iv_back;
    private CircleImageView iv_circle;

    private String name;  //技师姓名
    private int id;  //技师ID
    private String bank;
    private String bankCardNumber;
    private String bankAddress;
    private MyMessage myMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_info_activity);
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDataFromServer();  //从服务器上拉取我的信息的数据
    }

    public void initView(){
        context = this;
        mRatingbar = (RatingBar) findViewById(R.id.mratingbar);
        tv_rate = (TextView) findViewById(R.id.tv_rate);
        tv_logout = (TextView) findViewById(R.id.tv_logout);
        tv_cost = (TextView) findViewById(R.id.tv_cost);
        tv_order_num = (TextView) findViewById(R.id.order_num);
        tv_my_order_num = (TextView) findViewById(R.id.tv_my_order_num);
        tv_standard_ommission = (TextView) findViewById(R.id.tv_standard_ommission);
        ll_modify_pwd = (LinearLayout) findViewById(R.id.ll_modify_pwd);
        ll_cost = (LinearLayout) findViewById(R.id.ll_cost);
        ll_order_num = (LinearLayout) findViewById(R.id.ll_order_num);
        collection_shop = (LinearLayout) findViewById(R.id.collection_shop);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_circle = (CircleImageView) findViewById(R.id.iv_circle);
        tv_login_username = (TextView)findViewById(R.id.tv_login_username);
    }

    private void initData(){
        float rating = DecimalUtil.FloatDecimal1(mRatingbar.getRating());
        tv_rate.setText(String.valueOf(rating));
    }

    private void setListener(){
        ll_modify_pwd.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        ll_cost.setOnClickListener(this);
        ll_order_num.setOnClickListener(this);
        tv_standard_ommission.setOnClickListener(this);
        collection_shop.setOnClickListener(this);

        findViewById(R.id.my_info_layout).setOnClickListener(this);
        findViewById(R.id.my_order_layout).setOnClickListener(this);
        findViewById(R.id.my_team_layout).setOnClickListener(this);
        findViewById(R.id.notification_list_layout).setOnClickListener(this);
        findViewById(R.id.server_conter_layout).setOnClickListener(this);
        findViewById(R.id.study_garden_layout).setOnClickListener(this);
    }

    /**
     * 此处获取认证进度信息返回的就是我的信息的字段
     */
//    private void getDataFromServer(){
//        Http.getInstance().getTaskToken(NetURL.MY_INFO_URLV2, MyMessageEntity.class, new OnResult() {
//            @Override
//            public void onResult(Object entity) {
//                if(entity == null){
//                    T.show(context,context.getString(R.string.get_info_failed));
//                    return;
//                }
//                MyMessageEntity myMessageEntity = (MyMessageEntity) entity;
////                if (myMessageEntity.isStatus()){
////
////                }
//                MyMessageData myMessageData = JSON.parseObject(myMessageEntity.getMessage().toString(),MyMessageData.class);
//                myMessage = myMessageData.getTechnician();
//                if(myMessage!=null){
//                    String status = myMessage.getStatus(); //审核状态
//                    String avatar = myMessage.getAvatar(); //技师头像地址URL
//                    String name = myMessage.getName();
//                    String idNo = myMessage.getIdNo();  //身份证号
//                    String resume = myMessage.getResume();
//                    String reference = myMessage.getReference();
//                    String bankCardNo = myMessage.getBankCardNo(); //银行卡号
//                    String bank = myMessage.getBank();  //银行
//                    String bankAdress = myMessage.getBankAddress();
//                    String idPhoto = myMessage.getIdPhoto();  //身份证图像地址URL
////                    String verifyMsg = myMessage.getVerifyMsg();  //原因
//
//
//                    if("IN_VERIFICATION".equals(status)){ //等待审核
//                        tv_status.setText(context.getString(R.string.authorize_IN_VERIFICATION));
//                    }else if("REJECTED".equals(status)){  //审核失败
//                        ll_failed_reason.setVisibility(View.VISIBLE);
//                        btn_change_info.setVisibility(View.VISIBLE);
//                        failedReason.setText(myMessage.getVerifyMsg());
//                        tv_status.setText(context.getString(R.string.authorize_REJECTED));
//                    }
//                    tv_username.setText(name);
//                    if (TextUtils.isEmpty(reference)){
//                        tv_reference.setText("无");
//                    }else {
//                        tv_reference.setText(reference);
//                    }
//                    ImageLoaderCache.getInstance().loader(NetURL.IP_PORT+avatar,iv_circle);
//                    ImageLoaderCache.getInstance().loader(NetURL.IP_PORT+idPhoto,iv_card_photo);
//                    tv_id_number.setText(idNo);
//                    tv_bank_number.setText(bankCardNo);
//                    tv_bank.setText(bank);
//                    tv_bank_address.setText(bankAdress);
//                    if (TextUtils.isEmpty(resume)){
//                        tv_resume.setText("无");
//                    }else {
//                        tv_resume.setText(resume);
//                    }
//
//                }else {
//                    T.show(getContext(),getString(R.string.dataUploadFailed));
//                }
//            }
//        });
//    }

//    private MyInfo_Data myInfo;
    private void getDataFromServer(){
        if(NetWorkHelper.isNetworkAvailable(context)) {
            Http.getInstance().getTaskToken(NetURL.MY_INFO_URLV2, MyMessageEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null) {
                        T.show(context, context.getString(R.string.get_info_failed));
                        return;
                    }
                    MyMessageEntity myInfoEntity = (MyMessageEntity) entity;

                    if (myInfoEntity.isStatus()){
                        MyMessageData myMessageData = JSON.parseObject(myInfoEntity.getMessage().toString(),MyMessageData.class);
                        myMessage = myMessageData.getTechnician();
                        String avatar = myMessage.getAvatar(); //技师头像url尾部
                        name = myMessage.getName(); //技师姓名
                        id = myMessage.getId();
                        String star = myMessageData.getStarRate();  //星级
                        bank = myMessage.getBank(); //银行字典
                        bankCardNumber = myMessage.getBankCardNo(); //银行卡号
                        bankAddress = myMessage.getBankAddress();//开户行地址

                        if(StringUtil.isNotEmpty(myMessageData.getTotalOrders())){
                            try{
                                int totalOrders = Integer.parseInt(myMessageData.getTotalOrders());  //订单数
                                tv_order_num.setText(String.valueOf(totalOrders));
                            }catch (NumberFormatException e){
                                tv_order_num.setText("0");
                            }
                        }

                        if(StringUtil.isNotEmpty(myMessageData.getBalance())){
                            try{
                                double balance = Double.parseDouble(myMessageData.getBalance());  //余额
                                tv_cost.setText(String.valueOf(balance));
                            }catch (NumberFormatException e){
                                tv_cost.setText("0");
                            }
                        }

                        if(StringUtil.isNotEmpty(myMessageData.getUnpaidOrders())){
                            try{
                                int unpadOrders = Integer.parseInt(myMessageData.getUnpaidOrders());  //账单数
                                tv_my_order_num.setText(String.valueOf(unpadOrders));
                            }catch (NumberFormatException e){
                                tv_my_order_num.setText("0");
                            }
                        }
                        if(StringUtil.isNotEmpty(avatar)){
                            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + avatar,iv_circle);
                        }
                        if(StringUtil.isNotEmpty(name)){
                            tv_login_username.setText(name);
                        }
                        try {
                            float starNum = Float.parseFloat(star);
                            mRatingbar.setRating((int)Math.floor(starNum));  //取整设置星级
                            tv_rate.setText(String.valueOf(DecimalUtil.FloatDecimal1(starNum)));  //设置保留一位小数
                        }catch (Exception e){
                            mRatingbar.setRating(0);
                            tv_rate.setText("0");
                        }
                    }
                }
            });
        }else{
            T.show(this,getString(R.string.no_network_tips));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
                startActivity(ModifyPasswordActivity.class);
                break;
            case R.id.tv_standard_ommission:
                startActivity(StandardOmmissionActivity.class);
                break;
            case R.id.ll_cost:  //余额
                String rest_money = tv_cost.getText().toString().trim();
                Bundle bundle = new Bundle();
                bundle.putString("name",name);
                bundle.putInt("id",id);
                bundle.putString("rest_money",rest_money);  //余额信息
                bundle.putString("bank",bank);
                bundle.putString("bankCardNumber",bankCardNumber);
                bundle.putString("bankAddress",bankAddress);
                bundle.putString("bankAddress",bankAddress);
                startActivity(RestInfoActivity.class,bundle);
                break;
            case R.id.ll_order_num: //账单
                startActivity(BillActivity.class);
                break;
            case R.id.my_info_layout:
                Intent intent1 = new Intent(getContext(), AuthorizationProgressActivity.class);
//                intent1.putExtra("isAgain", 2);
//                intent1.putExtra("myMessage",myMessage);
//                intent1.putExtra("name", myInfo.getName());
//                intent1.putExtra("headUrl", myInfo.getAvatar());
//                intent1.putExtra("idNumber", myInfo.getIdNo());
//                intent1.putExtra("skillArray", myInfo.getSkill());
//                intent1.putExtra("idUrl", myInfo.getIdPhoto());
//                intent1.putExtra("bankName", myInfo.getBank());
//                intent1.putExtra("bankNo", myInfo.getBankCardNo());
                intent1.putExtra("isInfo",true);
                startActivity(intent1);
                break;
            case R.id.my_order_layout:
                startActivity(MyOrderActivity.class);
                break;
            case R.id.my_team_layout:
                startActivity(MyTeamActivity.class);
                break;
            case R.id.notification_list_layout:
                startActivity(NotificationMessageActivity.class);
                break;
            case R.id.server_conter_layout:
                startActivity(ServiceCenterActivity.class);
                break;
            case R.id.study_garden_layout:
                startActivity(StudyGardenActivity.class);
                break;
            case R.id.collection_shop:
                startActivity(CollectionShopActivity.class);
                break;
        }
    }
}
