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
                    getpageList(1);
                } else {
                    page1 = 1;
                    isRefresh = true;
                    getpageListCi(1, true);
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
                    getpageList(++page);
                } else {
                    if (page1 >= totalPages) {
                        T.show(getActivity(), R.string.has_load_all_label);
                        mPull.loadedCompleted();
                        return;
                    }
                    getpageListCi(++page1, true);
                }

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getOrderInfo(mList.get(position).getId());
            }
        });
        if (isMainResponsible) {
            getpageList(1);
        } else {
            getpageListCi(1, false);
        }

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

    private void getpageList(int page) {
//        String param = "status=" + status + "page=" + page + "pageSize=" + pageSize;
        Http.getInstance().getTaskToken(NetURL.ORDER_LIST, ListUnfinishedOrderEntity.class, new OnResult() {
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
                            T.show(getContext(), getString(R.string.no_order));
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
        }, new BasicNameValuePair("status", String.valueOf(3)), new BasicNameValuePair("page", String.valueOf(page)), new BasicNameValuePair("pageSize", String.valueOf(pageSize)));
    }

    private void getpageListCi(int page, final boolean isHead) {
//        String param = "status=" + status + "page=" + page + "pageSize=" + pageSize;
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
        }, new BasicNameValuePair("page", String.valueOf(page)), new BasicNameValuePair("pageSize", String.valueOf(pageSize)));
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
