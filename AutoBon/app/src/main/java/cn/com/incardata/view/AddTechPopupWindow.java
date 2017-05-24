package cn.com.incardata.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.MyContactAdapter;
import cn.com.incardata.autobon.R;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.NetWorkHelper;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AddContactEntity;
import cn.com.incardata.http.response.AddContact_data;
import cn.com.incardata.http.response.AddContact_data_list;
import cn.com.incardata.utils.StringUtil;
import cn.com.incardata.utils.T;

/** 添加合伙人弹出界面
 * Created by yang on 2016/11/30.
 */
public class AddTechPopupWindow extends PopupWindow implements View.OnClickListener,
        PullToRefreshView.OnFooterRefreshListener,PullToRefreshView.OnHeaderRefreshListener {
    protected Activity activity;
    private TextView tv_search;
    private EditText et_content;
    private ListView technician_list;
    private ImageView iv_clear,iv_back;
    private PullToRefreshView refreshView;
    private MyContactAdapter mAdapter;
    private List<AddContact_data_list> mList;
    private Context context;
    private int checkId;

    private int total = -1;
    private int curPage;
    private static final int pageSize = 20;
    private OnCheckedListener listener;
    private AddContact_data_list addContact_data_list;

    public AddTechPopupWindow(Activity activity) {
        this.activity = activity;
    }

    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    public void init(){
        View view = LayoutInflater.from(activity).inflate(R.layout.add_contact_activity, null, false);
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        mList = new ArrayList<AddContact_data_list>();
        mAdapter = new MyContactAdapter(activity,mList);
        mAdapter.setOnGetPosition(new MyContactAdapter.OnGetPosition() {
            @Override
            public void getPosition(int position) {
                checkId = position;
                addContact_data_list = mList.get(position);
                if (listener != null){
                    listener.onChecked(addContact_data_list);
                }
                closePopupWindow();
            }
        });
        iv_clear = (ImageView) view.findViewById(R.id.iv_clear);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        et_content = (EditText) view.findViewById(R.id.et_content);
        tv_search = (TextView) view.findViewById(R.id.tv_search);
        technician_list = (ListView) view.findViewById(R.id.technician_list);
        technician_list.setAdapter(mAdapter);
        refreshView = (PullToRefreshView) view.findViewById(R.id.pull_refresh);
        refreshView.setEnablePullTorefresh(false);
        refreshView.setVisibility(View.GONE);
        iv_clear.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        refreshView.setOnFooterRefreshListener(this);

        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth((int) (activity.getWindowManager().getDefaultDisplay().getWidth() * 0.9));
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight((int) (activity.getWindowManager().getDefaultDisplay().getHeight() * 0.9));
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.SharePop);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                closePopupWindow();
            }
        });

        findContactByPage("",1,pageSize,true);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        WindowManager.LayoutParams params=activity.getWindow().getAttributes();
        params.alpha=0.7f;
        activity.getWindow().setAttributes(params);
    };

    /**
     * 关闭窗口
     */
    public void closePopupWindow()
    {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha=1f;
        activity.getWindow().setAttributes(params);
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_clear:
                et_content.setText("");
                break;
            case R.id.tv_search:  //搜索
                String content = et_content.getText().toString().trim();

                total = -1;  //初始化总条目个数
                curPage = 1;  //初始化当前页
                mList.clear();  //清除上次的数据
                findContactByPage(content,1,pageSize,true);  //分页查询技师
                break;
            case R.id.iv_back:
                closePopupWindow();
                break;
        }

    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        String phone = et_content.getText().toString().trim();
        findContactByPage(phone,curPage+1,pageSize,true);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {

    }

    /**
     * 查询技师
     * @param page  当前第几页
     * @param pageSize 一页数量
     * @param isPullRefresh 是否上拉刷新
     */
    private void findContactByPage(final String content, final int page, final int pageSize, final boolean isPullRefresh){
        List<BasicNameValuePair> bpList = new ArrayList<BasicNameValuePair>();
        if (!TextUtils.isEmpty(content)){
            BasicNameValuePair param = new BasicNameValuePair("query",content);
            bpList.add(param);
        }
        BasicNameValuePair two_param = new BasicNameValuePair("page",String.valueOf(page).trim());
        BasicNameValuePair three_param = new BasicNameValuePair("pageSize",String.valueOf(pageSize).trim());

        bpList.add(two_param);
        bpList.add(three_param);

        if(NetWorkHelper.isNetworkAvailable(activity)) {
            Http.getInstance().getTaskToken(NetURL.SEARCH_TECHNICIANV2, AddContactEntity.class, new OnResult() {
                @Override
                public void onResult(Object entity) {
                    if (entity == null) {
                        T.show(activity, activity.getString(R.string.request_failed));
                        return;
                    }
                    AddContactEntity addContactEntity = (AddContactEntity) entity;
                    if (addContactEntity.isStatus()) {
                        AddContact_data addContact_data = JSON.parseObject(addContactEntity.getMessage().toString(),AddContact_data.class);
                        if (total == -1) {
                            total = addContact_data.getTotalElements(); //获取总条目数量(只在搜索时获取总条目个数,刷新时不必再次获取)
                        }
                        List<AddContact_data_list> dataList = addContact_data.getList();
                        if (total == 0) {
                            updateData(dataList,false);
                            refreshView.setVisibility(View.GONE);
                            T.show(activity, activity.getString(R.string.no_contact_tips));
                            return;
                        }
                        updateData(dataList, isPullRefresh);
                        refreshView.setVisibility(View.VISIBLE);
                    }
                }
            }, (BasicNameValuePair[]) bpList.toArray(new BasicNameValuePair[bpList.size()]));
        }else{
            T.show(activity,activity.getString(R.string.no_network_tips));
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
            T.show(activity,activity.getString(R.string.has_load_all_label));
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * @return the listener
     */
    public OnCheckedListener getListener() {
        return listener;
    }

    /**
     * @param listener the listener to set
     */
    public void setListener(OnCheckedListener listener) {
        this.listener = listener;
    }

    public interface OnCheckedListener{
        /**
         * popupwindow消失时选中的内容
         */
        void onChecked(AddContact_data_list addContact_data_list);
    }

}
