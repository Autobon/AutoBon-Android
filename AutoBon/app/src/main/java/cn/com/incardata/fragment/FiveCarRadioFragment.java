package cn.com.incardata.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.com.incardata.adapter.RadioFragmentGridAdapter;
import cn.com.incardata.autobon.R;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.WorkItemEntity;
import cn.com.incardata.http.response.WorkItem_Data;
import cn.com.incardata.utils.AutoCon;
import cn.com.incardata.utils.T;

public class FiveCarRadioFragment extends BaseStandardFragment{
	private Activity mActivity;
	private GridView gv_tag;
	private View rootView;
	private RadioFragmentGridAdapter mAdapter;
	private List<WorkItem_Data> mList;

	//TODO 覆写抽象类BaseStandardFragment中的方法
	@Override
	public View onFragmentCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
	    if(null == rootView) {
            rootView = inflater.inflate(R.layout.car_radio_tab_fragment, container, false);
        }
        if(null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null) {
                parent.removeView(rootView);
            }
        }
		this.initView();
		this.initData();
		return rootView;
	}
	
	private void initView(){
        mList = new ArrayList<WorkItem_Data>();
		mActivity = getActivity();
		gv_tag = (GridView)rootView.findViewById(R.id.gv_tag);
		mAdapter = new RadioFragmentGridAdapter(mActivity,mList);
		gv_tag.setAdapter(mAdapter);
	}

	private void initData(){
		BasicNameValuePair bv_orderType = new BasicNameValuePair("orderType", String.valueOf(AutoCon.orderType));
		BasicNameValuePair bv_carSeat = new BasicNameValuePair("carSeat",String.valueOf(AutoCon.five_carSeat));  //五座车
		Http.getInstance().getTaskToken(NetURL.GET_WORK_ITEM, WorkItemEntity.class, new OnResult() {
			@Override
			public void onResult(Object entity) {
				if(entity == null){
					T.show(mActivity,mActivity.getString(R.string.request_failed));
					return;
				}
				WorkItemEntity workItemEntity = (WorkItemEntity) entity;
				List<WorkItem_Data> dataList =  workItemEntity.getData();  //获取五座车数据信息
				updateData(dataList);
			}
		},bv_orderType,bv_carSeat);
	}

	private void updateData(List<WorkItem_Data> dataList){
		mList.clear();
		mList.addAll(dataList);
		mAdapter.notifyDataSetChanged();
	}
}
