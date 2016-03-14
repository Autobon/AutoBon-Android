package cn.com.incardata.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.WorkItem_Data;

public class RadioFragmentGridAdapter extends BaseAdapter{
	private List<WorkItem_Data> mList;
	private Activity mActivity;
    private Map<Integer,String> workItemMap = new HashMap<Integer, String>();  //记录选中的工作项,其中key为id值,value为name
	private Map<Integer,Boolean> workItemStatus = new HashMap<Integer, Boolean>();  //value为工作项选中状态,true代表选中,false代表未选中

	public RadioFragmentGridAdapter(Activity mActivity,List<WorkItem_Data> mList){
		this.mList = mList;
		this.mActivity = mActivity;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
        final WorkItem_Data workItem_data = mList.get(position);
        workItemStatus.put(workItem_data.getId(),false);  //初始化各个按钮的状态

		View view=View.inflate(mActivity, R.layout.rg_tab_grid_item, null);
		final Button btn = (Button)view.findViewById(R.id.rg_btn);
		btn.setText(workItem_data.getName());

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
                //TODO
				boolean status = workItemStatus.get(workItem_data.getId()).booleanValue();
                if(status){
                    btn.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.corner_default_btn));
                    workItemMap.remove(workItem_data.getId());
                }else {
                    btn.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.corner_choice_btn));
                    workItemMap.put(workItem_data.getId(),workItem_data.getName());
                }
				workItemStatus.put(workItem_data.getId(),!status);  //重置状态(遵循覆盖原则)
                printWorkItemMap(workItemMap);
			}
		});
		return view;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

    /**
     * 打印选中工作项Map集合
     * @param workItemMap
     */
    private void printWorkItemMap(Map<Integer,String> workItemMap){
      	Set<Map.Entry<Integer,String>> keySets = workItemMap.entrySet();
		for(Map.Entry<Integer,String> entry : keySets){
			Log.i("test","key===>"+entry.getKey()+",value===>"+entry.getValue());
		}
        Log.i("test","===================================================");
    }
}
