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
    public static Map<Integer,String> workItemMap = new HashMap<Integer, String>();  //记录选中的工作项,其中key为id值,value为name

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

		View view=View.inflate(mActivity, R.layout.rg_tab_grid_item, null);
		final Button btn = (Button)view.findViewById(R.id.rg_btn);
		btn.setText(workItem_data.getName());

		if(workItem_data.isFocus()){
			btn.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.corner_choice_btn));
			btn.setTextColor(mActivity.getResources().getColor(R.color.main_white));  //选中字体白色
			workItemMap.put(workItem_data.getId(),workItem_data.getName());
		}else {
			btn.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.corner_default_btn));
            btn.setTextColor(mActivity.getResources().getColor(R.color.gray_A3));
			workItemMap.remove(workItem_data.getId());
		}

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
                boolean status = workItem_data.isFocus();
                if(status){
                    btn.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.corner_default_btn));
					btn.setTextColor(mActivity.getResources().getColor(R.color.gray_A3));
                    workItemMap.remove(workItem_data.getId());
                }else {
                    btn.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.corner_choice_btn));
					btn.setTextColor(mActivity.getResources().getColor(R.color.main_white));  //选中字体白色
					workItemMap.put(workItem_data.getId(),workItem_data.getName());
                }
				workItem_data.setFocus(!status);
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
