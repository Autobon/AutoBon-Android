package cn.com.incardata.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.incardata.adapter.OrderFinishedAdapter;
import cn.com.incardata.autobon.OrderInfoActivity;
import cn.com.incardata.autobon.R;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.ListUnFinishOrder;
import cn.com.incardata.http.response.ListUnfinishedEntity;
import cn.com.incardata.http.response.ListUnfinishedOrderEntity;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.OrderInfoEntity;
import cn.com.incardata.http.response.OrderInfo_Data;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/**
 * 我的订单－主/次责任人
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyOrderFragment.OnMyOrderFragmentListener} interface
 * to handle interaction events.
 * Use the {@link MyOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrderFragment extends BaseFragment {

    private OnMyOrderFragmentListener mListener;
    private View rootView;
    private boolean isMainResponsible;
    private String url;

    private PullToRefreshView mPull;
    private ListView mListView;
    private OrderFinishedAdapter mAdapter;
    private ArrayList<OrderInfo> mList;

    private int page = 1;//当前是第几页
    private int page1 = 1;
    private int pageSize = 10;
    private int totalPages;//总共多少页
    private boolean isRefresh = false;

    OrderInfo orderInfo;

    Map<String,String> mainParams = new HashMap<>();
    Map<String,String> ciParams = new HashMap<>();

    public Map<String, String> getMainParams() {
        mainParams.put("page", String.valueOf(page));
        mainParams.put("pageSize", String.valueOf(pageSize));
        mainParams.put("status", "3");
        return mainParams;
    }

    public Map<String, String> getCiParams() {
        ciParams.put("page", String.valueOf(page1));
        ciParams.put("pageSize", String.valueOf(pageSize));
        return ciParams;
    }

    public MyOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrderFragment newInstance(boolean isMainResponsible) {
        MyOrderFragment fragment = new MyOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isMainResponsible", isMainResponsible);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.isMainResponsible = getArguments().getBoolean("isMainResponsible");
        }

//        if (isMainResponsible){
//            url = NetURL.FINISHED_ORDER_LIST_MAIN;
//        }else {
//            url = NetURL.FINISHED_ORDER_LIST_SECOND;
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_order, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        initView();
        return rootView;
    }

    private void initView() {
        mPull = (PullToRefreshView) rootView.findViewById(R.id.order_pull);
        mListView = (ListView) rootView.findViewById(R.id.finished_order_list);

        mList = new ArrayList<OrderInfo>();
        mAdapter = new OrderFinishedAdapter(getActivity(), isMainResponsible, mList);
        mListView.setAdapter(mAdapter);

        mPull.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {

                if (isMainResponsible) {
                    page = 1;
                    isRefresh = true;
                    getpageList(getMainParams());
                } else {
                    page1 = 1;
                    isRefresh = true;
                    getpageListCi(getCiParams(), true);
                }

            }
        });
        mPull.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {

                if (isMainResponsible) {
                    if (page >= totalPages) {
                        T.show(getActivity(), R.string.has_load_all_label);
                        mPull.loadedCompleted();
                        return;
                    }
                    page++;
                    getpageList(getMainParams());
                } else {
                    if (page1 >= totalPages) {
                        T.show(getActivity(), R.string.has_load_all_label);
                        mPull.loadedCompleted();
                        return;
                    }
                    page1++;
                    getpageListCi(getCiParams(), true);
                }

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                getOrderInfo(mList.get(position).getId());
                Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
                intent.putExtra("orderId", mList.get(position).getId());
                startActivity(intent);
            }
        });
        if (isMainResponsible) {
            page = 1;
            getpageList(getMainParams());
        } else {
            page1 = 1;
            getpageListCi(getCiParams(), false);
        }

    }


    /**
     * 查询主责任人数据
     * @param param
     */
    public void queryMainData(Map<String,String> param){
        if (mainParams != null){
            mainParams.clear();
        }
        isRefresh = true;
        page = 1;
        mainParams.putAll(param);
        getpageList(getMainParams());
    }
    /**
     * 查询次责任人数据
     * @param param
     */
    public void queryCiData(Map<String,String> param){
        if (ciParams != null){
            ciParams.clear();
        }
        page1 = 1;
        isRefresh = true;
        ciParams.putAll(param);
        getpageListCi(getCiParams(), true);
    }

    private void startActivity(Class<?> cls, OrderInfo orderInfo) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtra(AutoCon.ORDER_INFO, orderInfo);
//        intent.putExtra("isMain", isMainResponsible);
        startActivity(intent);
    }


    //获取订单详情
    private void getOrderInfo(int orderID) {
        showDialog();
        Http.getInstance().getTaskToken(NetURL.getOrderInfoV2(orderID), "", OrderInfoEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                if (entity == null) {
                    T.show(getContext(), R.string.loading_data_failure);
                    return;
                }
                if (entity instanceof OrderInfoEntity) {
                    if (((OrderInfoEntity) entity).isStatus()) {
                        orderInfo = JSON.parseObject(((OrderInfoEntity) entity).getMessage().toString(), OrderInfo.class);
                        Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
                        intent.putExtra(AutoCon.ORDER_INFO, orderInfo);
                        startActivity(intent);
                    } else {
                        T.show(getContext(), R.string.loading_data_failure);
                        return;
                    }
                }
            }
        });
    }

    /**
     * 获取主责任李彪数据
     * @param param
     */
    private void getpageList(Map<String,String> param) {
        List<BasicNameValuePair> paramList = new ArrayList<>();
        for (Map.Entry parama : param.entrySet()) {
            paramList.add(new BasicNameValuePair(parama.getKey().toString(),parama.getValue().toString()));
        }
        showDialog();
        Http.getInstance().getTaskToken(NetURL.ORDER_LIST, ListUnfinishedOrderEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                cancelDialog();
                mPull.loadedCompleted();
                if (entity == null) {
                    T.show(getActivity(), R.string.loading_data_failure);
                    isRefresh = false;
                    return;
                }
                if (entity instanceof ListUnfinishedOrderEntity) {
                    ListUnfinishedOrderEntity list = (ListUnfinishedOrderEntity) entity;
                    if (list.isStatus()) {
                        ListUnFinishOrder listUnFinishOrder = JSON.parseObject(list.getMessage().toString(), ListUnFinishOrder.class);
                        totalPages = listUnFinishOrder.getTotalPages();
                        if (isRefresh) {
                            mList.clear();
                        }
                        if (listUnFinishOrder.getTotalElements() == 0) {
                            T.show(getContext(), getString(R.string.no_order));
                        }
                        for (OrderInfo orderInfo : listUnFinishOrder.getContent()) {
                            mList.add(orderInfo);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        T.show(getActivity(), list.getMessage().toString());
                    }
                    isRefresh = false;
                }
            }
        }, (BasicNameValuePair[]) paramList.toArray(new BasicNameValuePair[paramList.size()]));
    }

    /**
     * 获取次责任人列表数据
     * @param param
     * @param isHead
     */
    private void getpageListCi(Map<String,String> param, final boolean isHead) {
        List<BasicNameValuePair> paramList = new ArrayList<>();
        for (Map.Entry parama : param.entrySet()) {
            paramList.add(new BasicNameValuePair(parama.getKey().toString(),parama.getValue().toString()));
        }
        Http.getInstance().getTaskToken(NetURL.ORDER_LISTCI, ListUnfinishedOrderEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                mPull.loadedCompleted();
                if (entity == null) {
                    T.show(getActivity(), R.string.loading_data_failure);
                    isRefresh = false;
                    return;
                }
                if (entity instanceof ListUnfinishedOrderEntity) {
                    ListUnfinishedOrderEntity list = (ListUnfinishedOrderEntity) entity;
                    if (list.isStatus()) {
                        ListUnFinishOrder listUnFinishOrder = JSON.parseObject(list.getMessage().toString(), ListUnFinishOrder.class);
                        totalPages = listUnFinishOrder.getTotalPages();
                        if (isRefresh) {
                            mList.clear();
                        }

                        if (listUnFinishOrder.getTotalElements() == 0) {
                            if (isHead) {
                                T.show(getContext(), getString(R.string.no_order));
                            }
                        }
                        for (OrderInfo orderInfo : listUnFinishOrder.getContent()) {
                            mList.add(orderInfo);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        T.show(getActivity(), R.string.loading_data_failure);
                    }
                    isRefresh = false;
                }
            }
        },  (BasicNameValuePair[]) paramList.toArray(new BasicNameValuePair[paramList.size()]));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnMyOrderFragmentListener) {
            mListener = (OnMyOrderFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMyOrderFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMyOrderFragmentListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
