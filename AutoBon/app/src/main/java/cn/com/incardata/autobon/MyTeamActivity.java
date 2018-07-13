package cn.com.incardata.autobon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.incardata.adapter.MyTeamAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.ListNewEntity;
import cn.com.incardata.http.response.TeamListData;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/**
 * 我的团队列表界面
 * <p>Created by wangyang on 2018/6/26.</p>
 */
public class MyTeamActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, View.OnClickListener  {
    private PullToRefreshView refresh;                                      //上下拉View
    private ListView mListView;                                             //我的团队列表
    private int page = 1;                                                   //当前是第几页
    private int totalPages;                                                 //总共多少页
    private boolean isRefresh = false;                                      //是否是刷新

    private MyTeamAdapter adapter;                                          //我的团队列表适配
    private List<TeamListData> list;                                        //团队列表集合


    Map<String, String> params = new HashMap<>();
    public Map<String, String> getParams() {
        params.put("page", String.valueOf(page));
        params.put("pageSize", "20");
        return params;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        list = new ArrayList<>();
        refresh = (PullToRefreshView) findViewById(R.id.pull);
        mListView = (ListView) findViewById(R.id.my_team_list);

        adapter = new MyTeamAdapter(this,list);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getContext(), TeamTechnicianActivity.class);
                intent.putExtra("team", list.get(position));
                startActivity(intent);
            }
        });

        refresh.setOnHeaderRefreshListener(this);

        getTeamList(getParams());

        findViewById(R.id.back).setOnClickListener(this);
    }

    /**
     * 获取我的团队列表数据
     * @param param
     */
    private void getTeamList(Map<String, String> param) {
//        List<BasicNameValuePair> paramList = new ArrayList<>();
//        for (Map.Entry parama : param.entrySet()) {
//            paramList.add(new BasicNameValuePair(parama.getKey().toString(), parama.getValue().toString()));
//        }
        Http.getInstance().getTaskToken(NetURL.MY_TEAM_LIST, null,ListNewEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                refresh.loadedCompleted();
                if (entity == null) {
                    isRefresh = false;
                    T.show(getContext(), R.string.request_failed);
                    return;
                }
                if (entity instanceof ListNewEntity) {
                    ListNewEntity listNew = (ListNewEntity) entity;
                    if (listNew.isStatus()) {
                        List<TeamListData> lists = JSON.parseArray(listNew.getMessage().toString(),TeamListData.class);
                        if (isRefresh) {
                            list.clear();
                        }
                        if (lists == null || lists.size() == 0) {
                            T.show(getContext(), "暂无团队");
                        }
                        list.addAll(lists);
                        adapter.notifyDataSetInvalidated();
                    } else {
                        T.show(getContext(), listNew.getMessage().toString());
                        return;
                    }
                    isRefresh = false;
                }
            }
        });
    }


    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        page = 1;
        isRefresh = true;
        getTeamList(getParams());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
