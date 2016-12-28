package cn.com.incardata.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.response.ConstructionPosition;
import cn.com.incardata.http.response.GetOrderProjectItem;
import cn.com.incardata.http.response.Technician;

/**
 * 施工完成工作项适配
 * Created by yang on 2016/11/2.
 */
public class GridViewAdapter extends BaseAdapter {
    private ConstructionPosition[] list;
    private Activity context;
    private Technician user;
    private int checkId;
    private ConstructionPosition[] constructionPositions;


    public GridViewAdapter() {
    }

    public GridViewAdapter(Activity context, ConstructionPosition[] list, Technician user) {
        this.context = context;
        this.list = list;
        this.user = user;
    }

    public void setCheck(int checkId) {
//        this.checkId = checkId;
//        constructionPositions = list.get(checkId).getConstructionPositions();
//        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final ConstructionPosition item = list[position];
        View view1 = View.inflate(context, R.layout.rg_tab_grid_item, null);
        final Button btn = (Button) view1.findViewById(R.id.rg_btn);
        btn.setText(item.getName());
        if (item.getTechnicianId() == -1) {
            btn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.corner_default_btn));
            btn.setTextColor(context.getResources().getColor(R.color.gray_A3));
            btn.setEnabled(true);
        } else {
            if (item.getTechnicianId() == user.getId()) {
                btn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.corner_choice_btn));
                btn.setTextColor(context.getResources().getColor(R.color.main_white));
                btn.setEnabled(true);
            } else {
                btn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.corner_no_btn));
                btn.setTextColor(context.getResources().getColor(R.color.darkgray));
                btn.setEnabled(false);
            }
        }
//        if (item.isCheck()){
//            btn.setBackgroundColor(Color.RED);
//            btn.setTextColor(Color.WHITE);
//            btn.setEnabled(false);
//        }else {
//            btn.setBackgroundColor(Color.WHITE);
//            btn.setTextColor(Color.RED);
//            btn.setEnabled(true);
//        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (item.getTechnicianId() == -1) {
                    btn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.corner_choice_btn));
                    btn.setTextColor(context.getResources().getColor(R.color.main_white));
                    item.setTechnicianId(user.getId());
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                } else {
                    if (item.getTechnicianId() == user.getId()) {
                        btn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.corner_default_btn));
                        btn.setTextColor(context.getResources().getColor(R.color.gray_A3));
                        item.setTechnicianId(-1);
                        if (mListener != null) {
                            mListener.onRefresh();
                        }
                    }
                }


//                if (item.isCheck()){
//                    btn.setBackgroundColor(Color.WHITE);
//                    btn.setTextColor(Color.RED);
//                    item.setCheck(false);
//                    notifyDataSetChanged();
//                }else {
//                    btn.setBackgroundColor(Color.RED);
//                    btn.setTextColor(Color.WHITE);
//                    item.setCheck(true);
//                    notifyDataSetChanged();
//                }
            }
        });

        return view1;
    }

    public void setOnRefreshGridViewListener(OnRefreshGridViewListener mListener) {
        this.mListener = mListener;
    }

    private OnRefreshGridViewListener mListener;


    public interface OnRefreshGridViewListener {
        void onRefresh();
    }

    public void setUserId(int userId) {
        for (ConstructionPosition constructionPosition : list) {
//            if (constructionPosition.getTechnicianId() == userId){
            constructionPosition.setTechnicianId(-1);
//                btn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.corner_default_btn));
//                btn.setTextColor(context.getResources().getColor(R.color.gray_A3));
//                item.setTechnicianId(-1);

//            }
        }
        if (mListener != null) {
            mListener.onRefresh();
        }
    }
}
