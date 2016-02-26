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

import cn.com.incardata.fragment.IndentMapFragment;
import cn.com.incardata.utils.StringUtil;

/**
 * Created by zhangming on 2016/2/24.
 * 改为使用Fragment来处理
 */
public class OrderReceiveActivity extends BaseActivity implements IndentMapFragment.OnFragmentInteractionListener,View.OnClickListener{
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private IndentMapFragment mFragment;

    private TextView tv_add_contact;
    private LinearLayout ll_add_contact,ll_tab_bottom;
    private TextView tv_username,tv_begin_work;
    private View bt_line_view;
    private ImageView iv_back;

    private static final int ADD_CONTACT_CODE = 1;  //添加联系人的请求码requestCode

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
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        transaction = fragmentManager.beginTransaction();
        mFragment = IndentMapFragment.newInstance("2月25日 14:35", "哈哈哈哈哈");
        transaction.replace(R.id.fragment_container, mFragment);
        transaction.commit();
    }

    private void initView(){
        tv_add_contact = (TextView) findViewById(R.id.tv_add_contact);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_begin_work = (TextView)findViewById(R.id.tv_begin_work);
        ll_add_contact = (LinearLayout) findViewById(R.id.ll_add_contact);
        ll_tab_bottom = (LinearLayout) findViewById(R.id.ll_tab_bottom);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        bt_line_view = findViewById(R.id.bt_line_view);
    }

    private void setListener(){
        iv_back.setOnClickListener(this);
        tv_add_contact.setOnClickListener(this);
        tv_begin_work.setOnClickListener(this);
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
            case R.id.tv_begin_work:
                super.startActivity(WorkSignInActivity.class);
                //finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CONTACT_CODE){  //添加联系人返回,更新界面
            switch (resultCode){
                case RESULT_OK:
                    String username = data.getExtras().getString("username");
                    if(StringUtil.isNotEmpty(username)){
                        tv_username.setText(username);
                        bt_line_view.setVisibility(View.VISIBLE);
                        ll_add_contact.setVisibility(View.VISIBLE);
                        ll_tab_bottom.setVisibility(View.GONE);
                        tv_begin_work.setVisibility(View.VISIBLE);
                        tv_begin_work.setOnClickListener(this);
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
