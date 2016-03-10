package cn.com.incardata.autobon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.MyInfoEntity;
import cn.com.incardata.http.response.MyInfo_Data;
import cn.com.incardata.utils.DecimalUtil;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.CircleImageView;

/**
 * Created by zhangming on 2016/2/25.
 */
public class MyInfoActivity extends BaseActivity implements View.OnClickListener{
    private Context context;
    private RatingBar mRatingbar;
    private TextView tv_rate,tv_logout,tv_cost,tv_good_rate,tv_login_username;
    private LinearLayout ll_my_package,ll_modify_pwd,ll_cost,ll_order_num;
    private ImageView iv_back;
    private CircleImageView iv_circle;
    private boolean isVisible = false;

    private String name;  //技师姓名
    private String bank;
    private String bankCardNumber;

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
        ll_my_package = (LinearLayout) findViewById(R.id.ll_my_package);
        ll_modify_pwd = (LinearLayout) findViewById(R.id.ll_modify_pwd);
        ll_cost = (LinearLayout) findViewById(R.id.ll_cost);
        ll_order_num = (LinearLayout) findViewById(R.id.ll_order_num);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_circle = (CircleImageView) findViewById(R.id.iv_circle);
        tv_good_rate = (TextView) findViewById(R.id.tv_good_rate);
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
    }

    private void getDataFromServer(){
        if(NetWorkHelper.isNetworkAvailable(context)) {
            Http.getInstance().getTaskToken(NetURL.MY_INFO_URL, MyInfoEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null) {
                        T.show(context, context.getString(R.string.get_info_failed));
                        return;
                    }
                    MyInfoEntity myInfoEntity = (MyInfoEntity) entity;
                    MyInfo_Data data = myInfoEntity.getData();

                    String avatar = data.getAvatar(); //技师头像url尾部
                    name = data.getName(); //技师姓名
                    int star = data.getStar();  //星级
                    bank = data.getBank(); //银行字典
                    bankCardNumber = data.getBankCardNo(); //银行卡号

                    if(StringUtil.isNotEmpty(avatar)){
                        ImageLoaderCache.getInstance().loader(NetURL.IP_PORT+avatar,iv_circle);
                    }
                    if(StringUtil.isNotEmpty(name)){
                        tv_login_username.setText(name);
                    }
                    mRatingbar.setRating(star);
                    tv_rate.setText(String.valueOf(star));
                    tv_good_rate.setText((star/5)*100+"%");
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
            case R.id.ll_cost:  //余额
                String rest_money = tv_cost.getText().toString().trim();
                Bundle bundle = new Bundle();
                bundle.putString("name",name);
                bundle.putString("rest_money",rest_money);  //余额信息
                bundle.putString("bank",bank);
                bundle.putString("bankCardNumber",bankCardNumber);
                startActivity(RestInfoActivity.class,bundle);
                break;
            case R.id.ll_order_num: //账单
                startActivity(BillActivity.class);
                break;
        }
    }
}
