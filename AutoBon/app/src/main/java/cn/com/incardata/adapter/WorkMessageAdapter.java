package cn.com.incardata.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.OrderConstructionShow;

/** 施工详情列表适配
 * Created by yang on 2016/12/2.
 */
public class WorkMessageAdapter extends BaseAdapter {
    private Activity activity;
    private OrderConstructionShow[] orderConstructionShows;

    public WorkMessageAdapter(Activity activity,OrderConstructionShow[] orderConstructionShows) {
        this.activity = activity;
        this.orderConstructionShows = orderConstructionShows;
    }

    @Override
    public int getCount() {
        return orderConstructionShows.length;
    }

    @Override
    public Object getItem(int i) {
        return orderConstructionShows[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null){
            holder = new Holder();
            view = LayoutInflater.from(activity).inflate(R.layout.work_message_item,viewGroup,false);
            holder.tech_name = (TextView) view.findViewById(R.id.tech_name);
            holder.work_message_item_list = (ListView) view.findViewById(R.id.work_message_item_list);
            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }
        OrderConstructionShow orderConstructionShow = orderConstructionShows[i];
        holder.tech_name.setText(orderConstructionShow.getTechName());
        WorkMessageItemAdapter workMessageItemAdapter = new WorkMessageItemAdapter(activity,orderConstructionShow.getProjectPosition());
        holder.work_message_item_list.setAdapter(workMessageItemAdapter);
        setListViewHeightBasedOnChildren(holder.work_message_item_list);


        return view;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
//            int desiredWidth= View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    class Holder{
        TextView tech_name;
        ListView work_message_item_list;
    }
}
