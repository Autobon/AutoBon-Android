package cn.com.incardata.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.incardata.application.MyApplication;
import cn.com.incardata.autobon.R;
import cn.com.incardata.autobon.WorkFinishActivity;
import cn.com.incardata.http.response.ConstructionPosition;
import cn.com.incardata.http.response.GetOrderProjectItem;
import cn.com.incardata.http.response.ProductData;
import cn.com.incardata.http.response.Technician;
import cn.com.incardata.http.response.WorkFinish;

/**
 * 添加合伙人列表
 * Created by yang on 2016/11/2.
 */
public class ListViewAdapter extends BaseAdapter {
    private List<Technician> users;
    //    private List<GetOrderProjectItem> items;
    private Activity context;
    private List<ProductData> items;
    private int check;
    private GridViewAdapter adapter1;

    private int workItemId;


    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    private HashMap<Integer, GridViewAdapter> mMapGridView;

    public ListViewAdapter(List<Technician> users, List<ProductData> items, Activity context,int workItemId) {
        this.users = users;
        this.items = items;
        this.context = context;
        this.workItemId = workItemId;
        mMapGridView = new HashMap<>(5);
//        for (Technician technician : users){
//            for (ConstructionPosition constructionPosition : items){
//                if (constructionPosition.getTechnicianId() != technician.getId()){
//                    constructionPosition.setTechnicianId(-1);
//                }
//            }
//        }
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
            holder.ciName = (TextView) view.findViewById(R.id.ciName);
            holder.ciGridview = (GridView) view.findViewById(R.id.ciGridview);
            holder.delect_tech = (ImageView) view.findViewById(R.id.delect_tech);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (users.get(position).getId() == MyApplication.getInstance().getLoginUserId()) {
            holder.delect_tech.setVisibility(View.GONE);
        }
        holder.delect_tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = users.get(position).getId();
//                adapter1.setUserId(users.get(position).getId());

//                for (ConstructionPosition constructionPosition : items){
//                    if (constructionPosition.getTechnicianId() == users.get(position).getId()){
//                        constructionPosition.setTechnicianId(-1);
//                        constructionPosition.setCheck(false);
//                    }
//                }
                users.remove(position);

                notifyDataSetChanged();
//                adapter1.notifyDataSetChanged();
                onGetHeight.getHeight();
            }
        });
//        if (holder.delect_tech.isFocused()){
//            check = position;
//            WorkFinishActivity activity = new WorkFinishActivity();
//            activity.setListViewHeightBasedOnChildren(activity.getListview_workItem());
//        }
        holder.ciName.setText(users.get(position).getName());
//        if (mMapGridView.containsKey(position)) {
//            holder.ciGridview.setAdapter(mMapGridView.get(position));
//        } else {
        if (workItemId == 4){
            holder.ciGridview.setNumColumns(2);
        }else {
            holder.ciGridview.setNumColumns(4);
        }
        GridViewAdapter adapter = new GridViewAdapter(context, items, users.get(position),workItemId);
        adapter1 = adapter;

        holder.ciGridview.setAdapter(adapter);
        adapter.setOnRefreshGridViewListener(new GridViewAdapter.OnRefreshGridViewListener() {
            @Override
            public void onRefresh() {
                if (mMapGridView != null && !mMapGridView.isEmpty()) {
                    Iterator iter = mMapGridView.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        GridViewAdapter a = (GridViewAdapter) entry.getValue();
                        a.notifyDataSetChanged();
                    }
                }
            }
        });
        mMapGridView.put(position, adapter);
//        }

        return view;
    }

    class ViewHolder {
        private TextView ciName;
        private GridView ciGridview;
        private ImageView delect_tech;
        GridViewAdapter adapter;
    }
//    private View.OnClickListener onClickListener;
//
//    public void setOnClickListener(View.OnClickListener onClickListener){
//        this.onClickListener = onClickListener;
//    }

    OnGetHeight onGetHeight;

    public void setOnGetHeight(OnGetHeight onGetHeight) {
        this.onGetHeight = onGetHeight;
    }

    public interface OnGetHeight {
        public void getHeight();
    }
}
