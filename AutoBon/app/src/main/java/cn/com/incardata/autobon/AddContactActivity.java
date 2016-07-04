package cn.com.incardata.autobon;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.MyContactAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AddContactEntity;
import cn.com.incardata.http.response.AddContact_data_list;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/**
 * Created by Administrator on 2016/2/19.
 */
public class AddContactActivity extends BaseActivity implements View.OnClickListener,
        PullToRefreshView.OnHeaderRefreshListener,PullToRefreshView.OnFooterRefreshListener{
    private Context context;
    private ImageView iv_back,iv_clear;
    private TextView tv_search;
    private EditText et_content;
    private ListView technician_list;
    private PullToRefreshView refreshView;

    private MyContactAdapter mAdapter;
    private List<AddContact_data_list> mList;

    private int total;
    private int curPage;
    private static final int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_activity);
        initView();
        setListener();

        mList = new ArrayList<AddContact_data_list>();
        mAdapter = new MyContactAdapter(AddContactActivity.this,mList);
        technician_list.setAdapter(mAdapter);
        mAdapter.setOrderId(getIntent().getIntExtra(AutoCon.ORDER_ID, -1));
    }

    public void initView(){
        context = this;
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        et_content = (EditText) findViewById(R.id.et_content);
        tv_search = (TextView) findViewById(R.id.tv_search);
        technician_list = (ListView) findViewById(R.id.technician_list);
        refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh);
        refreshView.setEnablePullTorefresh(false);
        refreshView.setVisibility(View.GONE);
    }

    public void setListener(){
        iv_back.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        refreshView.setOnFooterRefreshListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear:
                et_content.setText("");
                break;
            case R.id.tv_search:  //搜索
                String content = et_content.getText().toString().trim();
                if(StringUtil.isEmpty(content)){
                    T.show(this,getString(R.string.content_text_tips));
                    return;
                }
                total = -1;  //初始化总条目个数
                curPage = 1;  //初始化当前页
                mList.clear();  //清除上次的数据
                findContactByPage(content,1,pageSize,true);  //分页查询技师
                break;
        }
    }

    /**
     * 查询技师
     * @param page  当前第几页
     * @param pageSize 一页数量
     * @param isPullRefresh 是否上拉刷新
     */
    private void findContactByPage(final String content, final int page, final int pageSize, final boolean isPullRefresh){
        List<BasicNameValuePair> bpList = new ArrayList<BasicNameValuePair>();
        BasicNameValuePair param = new BasicNameValuePair("query",content);
        BasicNameValuePair two_param = new BasicNameValuePair("page",String.valueOf(page).trim());
        BasicNameValuePair three_param = new BasicNameValuePair("pageSize",String.valueOf(pageSize).trim());
        bpList.add(param);
        bpList.add(two_param);
        bpList.add(three_param);

        if(NetWorkHelper.isNetworkAvailable(context)) {
            Http.getInstance().getTaskToken(NetURL.SEARCH_TECHNICIAN, AddContactEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null) {
                        T.show(context, context.getString(R.string.add_contact_failed));
                        return;
                    }
                    AddContactEntity addContactEntity = (AddContactEntity) entity;
                    if (addContactEntity.isResult()) {
                        if (total == -1) {
                            total = addContactEntity.getData().getTotalElements(); //获取总条目数量(只在搜索时获取总条目个数,刷新时不必再次获取)
                        }
                        List<AddContact_data_list> dataList = addContactEntity.getData().getList();
                        if (total == 0) {
                            updateData(dataList,false);
                            refreshView.setVisibility(View.GONE);
                            T.show(context, context.getString(R.string.no_contact_tips));
                            return;
                        }
                        updateData(dataList, isPullRefresh);
                        refreshView.setVisibility(View.VISIBLE);
                    }
                }
            }, (BasicNameValuePair[]) bpList.toArray(new BasicNameValuePair[bpList.size()]));
        }else{
            T.show(this,getString(R.string.no_network_tips));
        }
        refreshView.onFooterRefreshComplete();  //不管网络是否连接,刷新完后隐藏脚布局
    }

    protected void updateData(List<AddContact_data_list> dataList,boolean isPullRefresh){
        if(mList.size() < total){
            mList.addAll(dataList);
            if(isPullRefresh){  //加载数据成功后,当前页指针加一
                curPage++;
            }
        }else if(mList.size()>0){  //排除条目数为0这种情况
            T.show(context,context.getString(R.string.has_load_all_label));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        String phone = et_content.getText().toString().trim();
        findContactByPage(phone,curPage+1,pageSize,true);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            //点击空白位置 隐藏软键盘
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}
