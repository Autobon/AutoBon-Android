package cn.com.incardata.autobon;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.CollectionShopAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.BaseEntity;
import cn.com.incardata.http.response.BaseEntityTwo;
import cn.com.incardata.http.response.CollectionShopEntity;
import cn.com.incardata.http.response.CollectionShop_Data;
import cn.com.incardata.http.response.ListNewEntity;
import cn.com.incardata.http.response.ListNew_Data;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/**
 * 收藏商户界面
 * Created by yang on 2017/5/19.
 */
public class CollectionShopActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {
    private PullToRefreshView refresh;
    private ListView mListView;
    private List<CollectionShop_Data> list;
    private CollectionShopAdapter adapter;

    private int page = 1;//当前是第几页
    private int totalPages;//总共多少页
    private boolean isRefresh = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_shop);
        initView();
    }


    private void initView(){
        list = new ArrayList<>();
        refresh = (PullToRefreshView) findViewById(R.id.pull);
        mListView = (ListView) findViewById(R.id.collection_shop_list);

        adapter = new CollectionShopAdapter(getContext(),list);
        mListView.setAdapter(adapter);

        adapter.setListeren(new CollectionShopAdapter.ClickListeren() {
            @Override
            public void onClick(int position) {
                deleteCollectionShop(position);
            }
        });

        refresh.setOnHeaderRefreshListener(this);
        refresh.setOnFooterRefreshListener(this);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getCollectionShopList(1);
    }


    /**
     * 获取收藏技师列表
     * @param page   页数
     */
    private void getCollectionShopList(int page){
        Http.getInstance().getTaskToken(NetURL.YETCOLLECTIONSHOP, "page=" + page + "&pageSize=20", CollectionShopEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                refresh.loadedCompleted();
                if (entity == null) {
                    isRefresh = false;
                    T.show(getContext(),R.string.request_failed);
                    return;
                }
                if (entity instanceof CollectionShopEntity){
                    CollectionShopEntity collectionShopEntity = (CollectionShopEntity) entity;
//                    if (listNew.isStatus()){
//                        ListNew_Data listNew_data = JSON.parseObject(listNew.getMessage().toString(),ListNew_Data.class);
                        totalPages = collectionShopEntity.getTotalPages();
                        if (isRefresh){
                            list.clear();
                        }
                        if (collectionShopEntity.getTotalElements() == 0){
                            T.show(getContext(),"暂无收藏商户");
                        }
                        list.addAll(collectionShopEntity.getList());
                        adapter.notifyDataSetInvalidated();
//                    }else {
//                        T.show(getContext(),listNew.getMessage().toString());
//                        return;
//                    }
                    isRefresh = false;
                }
            }
        });
    }

    /**
     * 移除收藏的技师
     * @param position  被移除的技师位置
     */
    private void deleteCollectionShop(final int position){
        showDialog();
        Http.getInstance().delTaskToken(NetURL.deleteCollectionShop(list.get(position).getCooperator().getId()), "", BaseEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(getContext(),R.string.request_failed);
                    return;

                }
                if (entity instanceof BaseEntity){
                    BaseEntity entity1 = (BaseEntity) entity;
                    if (entity1.isResult()){
                        T.show(getContext(),"移除商户成功");
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }else {
                        T.show(getContext(),entity1.getMessage());
                    }
                }

            }
        });
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (page >= totalPages){
            T.show(getContext(), R.string.has_load_all_label);
            refresh.loadedCompleted();
            return;
        }
        getCollectionShopList(++page);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        page = 1;
        isRefresh = true;
        getCollectionShopList(1);
    }
}
