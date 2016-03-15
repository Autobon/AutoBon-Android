package cn.com.incardata.autobon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.fragment.IndentMapFragment;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.OrderInfoEntity;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.http.response.StartWorkEntity;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.SharedPre;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;

/**
 * Created by zhangming on 2016/2/24.
 * 改为使用Fragment来处理
 */
public class OrderReceiveActivity extends BaseActivity implements IndentMapFragment.OnFragmentInteractionListener,View.OnClickListener{
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private IndentMapFragment mFragment;
    private Context context;

    private TextView tv_add_contact;
    private LinearLayout ll_add_contact,ll_tab_bottom;
    private TextView tv_username,tv_begin_work;
    private View bt_line_view;
    private ImageView iv_back;

    private static final int ADD_CONTACT_CODE = 1;  //添加联系人的请求码requestCode
    private OrderInfo_Data orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_receive_activity);
        fragmentManager = getFragmentManager();
        init();
        initView();
        setListener();

//        getDataFromServer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        transaction = fragmentManager.beginTransaction();
        mFragment = new IndentMapFragment();
        transaction.replace(R.id.fragment_container, mFragment);
        transaction.commit();
    }

    private void initView(){
        context = this;
        tv_add_contact = (TextView) findViewById(R.id.tv_add_contact);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_begin_work = (TextView)findViewById(R.id.tv_begin_work);
        ll_add_contact = (LinearLayout) findViewById(R.id.ll_add_contact);
        ll_tab_bottom = (LinearLayout) findViewById(R.id.ll_tab_bottom);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        bt_line_view = findViewById(R.id.bt_line_view);

        orderInfo = getIntent().getParcelableExtra("OrderInfo");
    }

    private void initializeData() {
        if (mFragment != null && orderInfo != null) {
            mFragment.setData(orderInfo.getPositionLon(), orderInfo.getPositionLat(), orderInfo.getPhoto(), orderInfo.getOrderTime(), orderInfo.getRemark(), orderInfo.getCreatorName());
        }
    }

    private void setListener(){
        iv_back.setOnClickListener(this);
        tv_add_contact.setOnClickListener(this);
        tv_begin_work.setOnClickListener(this);
        findViewById(R.id.begin_work).setOnClickListener(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.tv_add_contact:
                intent = new Intent(this,AddContactActivity.class);
                startActivityForResult(intent,ADD_CONTACT_CODE);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.begin_work:
            case R.id.tv_begin_work:
                //发起开始工作请求
                startWork();
                break;
        }
    }

    private void getDataFromServer(){
        Http.getInstance().getTaskToken(NetURL.getOrderInfo(String.valueOf(AutoCon.orderId)), OrderInfoEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if(entity == null){
                    T.show(context,context.getString(R.string.request_failed));
                    return;
                }
                OrderInfoEntity orderInfoEntity = (OrderInfoEntity) entity;
                if(orderInfoEntity.isResult()){
                    OrderInfo_Data data = orderInfoEntity.getData();
                    mFragment.setData(data.getPositionLon(), data.getPositionLat(), data.getPhoto(), data.getOrderTime(), data.getRemark(), data.getCreatorName());
                    if(data.getSecondTech()!=null){  //有次技师信息
                        String username = data.getSecondTech().getName();
                        showTechnician(username);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CONTACT_CODE){  //添加联系人返回,更新界面
            switch (resultCode){
                case RESULT_OK:
                    String username = data.getExtras().getString("username");  //技师姓名
                    if(StringUtil.isNotEmpty(username)){
                        showTechnician(username);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @param username 次技师姓名
     */
    public void showTechnician(String username){
        tv_username.setText(username);
        bt_line_view.setVisibility(View.VISIBLE);
        ll_add_contact.setVisibility(View.VISIBLE);
        ll_tab_bottom.setVisibility(View.GONE);
        tv_begin_work.setVisibility(View.VISIBLE);
    }

    private void startWork(){
        BasicNameValuePair bv_orderId = new BasicNameValuePair("orderId", String.valueOf(orderInfo.getId()));
        Http.getInstance().postTaskToken(NetURL.START_WORK, StartWorkEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if(entity == null){
                    T.show(context,context.getString(R.string.start_work_failed));
                    return;
                }
                StartWorkEntity startWorkEntity = (StartWorkEntity) entity;
                if(startWorkEntity.isResult()){  //成功后跳转签到界面
                    long startTimeStamp = startWorkEntity.getData().getStartTime(); //开始工作的时间戳
                    SharedPre.setSharedPreferences(context,AutoCon.START_WORK_TIMER,String.valueOf(startTimeStamp)); //保存

                    Intent intent = new Intent(context,WorkSignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    finish();
                }else{
                    T.show(context,startWorkEntity.getMessage());
                }
            }
        },bv_orderId);
    }

}
