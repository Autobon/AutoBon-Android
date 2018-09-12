package cn.com.incardata.autobon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.incardata.adapter.TeamTechnicianAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.ListNewEntity;
import cn.com.incardata.http.response.TeamListData;
import cn.com.incardata.http.response.TeamTechnicianData;
import cn.com.incardata.http.response.TeamTechnicianListData;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/**
 * 团队技师列表
 * <p>Created by wangyang on 2018/6/27.</p>
 */
public class TeamTechnicianActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, View.OnClickListener {
    private PullToRefreshView refresh;                                      //上下拉View
    private ListView mListView;                                             //我的团队列表
    private int page = 1;                                                   //当前是第几页
    private int totalPages;                                                 //总共多少页
    private boolean isRefresh = false;                                      //是否是刷新

    private TeamTechnicianAdapter adapter;                                  //团队技师列表适配
    private List<TeamTechnicianData> list;                                  //团队技师列表集合

    private TeamListData team;                                              //团队信息


    Map<String, String> params = new HashMap<>();
    public Map<String, String> getParams() {
        params.put("page", String.valueOf(page));
        params.put("pageSize", "20");
        return params;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_technicain);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        list = new ArrayList<>();
        refresh = (PullToRefreshView) findViewById(R.id.pull);
        mListView = (ListView) findViewById(R.id.team_technician_list);

        if (getIntent() != null){
            team = getIntent().getParcelableExtra("team");
            if (team == null){
                T.show(getContext(),"数据加载错误，请返回重新加载");
                return;
            }
        }

        adapter = new TeamTechnicianAdapter(this,list);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getContext(),TeamTechnicianOrderListActivity.class);
                intent.putExtra("technician",list.get(position));
                startActivity(intent);
            }
        });

        refresh.setOnHeaderRefreshListener(this);
        refresh.setOnFooterRefreshListener(this);

        getTeamTechnicianList(getParams());

        findViewById(R.id.back).setOnClickListener(this);
    }

    /**
     * 获取团队技师列表数据
     * @param param
     */
    private void getTeamTechnicianList(Map<String, String> param) {
        List<BasicNameValuePair> paramList = new ArrayList<>();
        for (Map.Entry parama : param.entrySet()) {
            paramList.add(new BasicNameValuePair(parama.getKey().toString(), parama.getValue().toString()));
        }
        Http.getInstance().getTaskToken(NetURL.getTeanTechnicianlist(team.getId()), ListNewEntity.class, new OnResult() {
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
                        if (listNew.getMessage() == null){
                            T.show(getContext(), "暂无技师");
                            return;
                        }
                        TeamTechnicianListData data = JSON.parseObject(listNew.getMessage().toString(), TeamTechnicianListData.class);
                        totalPages = data.getTotalPages();
                        if (isRefresh) {
                            list.clear();
                        }
                        if (data.getTotalElements() == 0) {
                            T.show(getContext(), "暂无技师");
                        }
                        list.addAll(data.getContent());
                        adapter.notifyDataSetInvalidated();
                    } else {
                        T.show(getContext(), listNew.getMessage().toString());
                        return;
                    }
                    isRefresh = false;
                }
            }
        }, (BasicNameValuePair[]) paramList.toArray(new BasicNameValuePair[paramList.size()]));
    }


    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        page = 1;
        isRefresh = true;
        getTeamTechnicianList(getParams());
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (page >= totalPages) {
            T.show(getContext(), R.string.has_load_all_label);
            refresh.loadedCompleted();
            return;
        }
        ++page;
        isRefresh = false;
        getTeamTechnicianList(getParams());
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
