package cn.com.incardata.autobon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.fragment.DropOrderDialogFragment;
import cn.com.incardata.fragment.ForceStartWorkDialogFragment;
import cn.com.incardata.fragment.IndentMapFragment;
import cn.com.incardata.getui.ActionType;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.BaseEntity;
import cn.com.incardata.http.response.DropOrderEntity;
import cn.com.incardata.http.response.MyInfo_Data;
import cn.com.incardata.http.response.OrderInfoEntity;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.http.response.StartWorkEntity;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;

/**接单开始工作
 * Created by zhangming on 2016/2/24.
 * 改为使用Fragment来处理
 */
public class OrderReceiveActivity extends BaseActivity implements
        IndentMapFragment.OnFragmentInteractionListener,
        View.OnClickListener,
        ForceStartWorkDialogFragment.OnForceListener,
        DropOrderDialogFragment.OnClickListener {
    /** 表示携带数据，不需要网络加载 */
    public static final String IsLocalData = "IsLocalData";

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private IndentMapFragment mFragment;
    private ForceStartWorkDialogFragment forceDialog;
    private DropOrderDialogFragment dropOderDialog;

    private TextView tv_add_contact;
    private LinearLayout ll_add_contact,ll_tab_bottom;
    private TextView tv_username;
    private View bt_line_view;
    private ImageView iv_back;
    private TextView accept_order;

    private static final int ADD_CONTACT_CODE = 1;  //添加联系人的请求码requestCode
    private OrderInfo_Data orderInfo;
    private boolean isLocalData = false;
    private boolean isFirst = true;//第一次加载

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_receive_activity);
        fragmentManager = getFragmentManager();
        init();
        initView();
        setListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isFirst) {
            if (isLocalData){
                orderInfo = getIntent().getParcelableExtra(AutoCon.ORDER_INFO);
                initializeData();
            }else {
                getDataFromServer(getIntent().getIntExtra(AutoCon.ORDER_ID, -1));
            }
            isFirst = false;
        }
    }

    /**放弃订单
     * @param v
     */
    public void onClickDropOrder(View v){
        //显示放弃订单对话框
        if (dropOderDialog == null){
            dropOderDialog = new DropOrderDialogFragment();
        }
        dropOderDialog.show(fragmentManager, "dropOderDialog");
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
        tv_add_contact = (TextView) findViewById(R.id.tv_add_contact);
        tv_username = (TextView) findViewById(R.id.tv_username);
        ll_add_contact = (LinearLayout) findViewById(R.id.ll_add_contact);
        ll_tab_bottom = (LinearLayout) findViewById(R.id.ll_tab_bottom);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        bt_line_view = findViewById(R.id.bt_line_view);
        accept_order = (TextView) findViewById(R.id.tv_accept_order);

        isLocalData = getIntent().getBooleanExtra(IsLocalData , false);
    }

    private void initializeData() {
        if (mFragment != null && orderInfo != null) {
            mFragment.setData(orderInfo);

            if (MyApplication.getInstance().getUserId() == orderInfo.getMainTech().getId()){
                MyInfo_Data tech = orderInfo.getSecondTech();
                if (tech != null){//有小伙伴
                    if (ActionType.SEND_INVITATION.equals(orderInfo.getStatus())){//待确认
                        showScheme(2);
                        tv_username.setText(tech.getName());
                        accept_order.setText(R.string.send_invitation);
                    }else if (ActionType.INVITATION_ACCEPTED.equals(orderInfo.getStatus())){//已接单
                        showScheme(3);
                        tv_username.setText(tech.getName());
                        accept_order.setText(R.string.receiver_text);
                    }
                }
            }else {
                MyInfo_Data tech = orderInfo.getMainTech();
                if (ActionType.SEND_INVITATION.equals(orderInfo.getStatus())){//待确认
                    Intent intent = new Intent(this , InvitationActivity.class);
                    intent.putExtra(AutoCon.ORDER_INFO, orderInfo);
                    startActivity(intent);
                    finish();
                }else if (ActionType.INVITATION_ACCEPTED.equals(orderInfo.getStatus())){//已接单
                    showScheme(3);
                    tv_username.setText(tech.getName());
                    accept_order.setText(R.string.receiver_text);
                }
            }
        }
    }

    private void showScheme(int type){
        switch (type){
            case 1://默认模式，暂不修改
                break;
            case 2://待确认
                bt_line_view.setVisibility(View.VISIBLE);
                ll_add_contact.setVisibility(View.VISIBLE);
                break;
            case 3://已接受
                bt_line_view.setVisibility(View.VISIBLE);
                ll_add_contact.setVisibility(View.VISIBLE);
                tv_add_contact.setVisibility(View.GONE);
                break;
        }
    }

    private void setListener(){
        iv_back.setOnClickListener(this);
        tv_add_contact.setOnClickListener(this);
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
                intent.putExtra(AutoCon.ORDER_ID, orderInfo.getId());
                startActivityForResult(intent,ADD_CONTACT_CODE);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.begin_work:
                //发起开始工作请求
                startWork(false);
                break;
        }
    }

    private void getDataFromServer(int orderId){
        Http.getInstance().getTaskToken(NetURL.getOrderInfo(orderId), OrderInfoEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if(entity == null){
                    T.show(getContext(), R.string.request_failed);
                    return;
                }
                OrderInfoEntity orderInfoEntity = (OrderInfoEntity) entity;
                if(orderInfoEntity.isResult()){
                    orderInfo = orderInfoEntity.getData();
                    initializeData();
//                    mFragment.setData(data.getPositionLon(), data.getPositionLat(), data.getPhoto(), data.getOrderTime(), data.getRemark(), data.getCreatorName());
//                    if(data.getSecondTech()!=null){  //有次技师信息
//                        String username = data.getSecondTech().getName();
//                        showTechnician(username);
//                    }
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
        showScheme(2);
        tv_username.setText(username);
        accept_order.setText(R.string.send_invitation);
    }

    private void startWork(boolean isForce){
        BasicNameValuePair bv_orderId = new BasicNameValuePair("orderId", String.valueOf(orderInfo.getId()));
        BasicNameValuePair ignoreInvitation = new BasicNameValuePair("ignoreInvitation", String.valueOf(isForce));

        Http.getInstance().postTaskToken(NetURL.START_WORK, StartWorkEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if(entity == null){
                    T.show(getContext(), R.string.start_work_failed);
                    return;
                }
                StartWorkEntity startWorkEntity = (StartWorkEntity) entity;
                if(startWorkEntity.isResult()){  //成功后跳转签到界面
                    if (orderInfo.getMainTech().getId() == MyApplication.getInstance().getUserId()){
                        orderInfo.setMainConstruct(startWorkEntity.getData());
                    }else {
                        orderInfo.setSecondConstruct(startWorkEntity.getData());
                    }

                    Intent intent = new Intent(getContext(), WorkSignInActivity.class);
                    intent.putExtra(AutoCon.ORDER_INFO, orderInfo);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else if ("INVITATION_NOT_FINISH".equals(startWorkEntity.getError())){//你邀请的小伙伴还未接受或拒绝邀请
                    //显示强制开始对话框
                    if (forceDialog == null){
                        forceDialog = new ForceStartWorkDialogFragment();
                    }
                    forceDialog.show(fragmentManager, "ForceDialog");
                }else {
                    T.show(getContext(), startWorkEntity.getMessage());
                }
            }
        },bv_orderId, ignoreInvitation);
    }

    @Override
    public void onForce() {
       startWork(true);
    }

    @Override
    public void onDropClick(View v) {
        if (orderInfo == null) {
            T.show(getContext(), getString(R.string.not_found_order_tips));
            return;
        }
        showDialog(getString(R.string.processing));
        Http.getInstance().postTaskToken(NetURL.getDropOrder(orderInfo.getId()), "", DropOrderEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null){
                    T.show(getContext(), R.string.operate_failed_agen);
                    return;
                }
                if (entity instanceof DropOrderEntity && ((DropOrderEntity) entity).isResult()){
                    MyApplication.isRefresh = true;
                    T.show(getContext(), getString(R.string.order_drop_successful));
                    finish();
                }else {
                    T.show(getContext(), ((BaseEntity)entity).getMessage());
                    return;
                }
            }
        });
    }
}
