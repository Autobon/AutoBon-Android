package cn.com.incardata.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.AddContact_data_list;
import cn.com.incardata.http.response.InviteTechnicainEntity;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.CircleImageView;
import cn.com.incardata.view.PullToRefreshView;

/**
 * Created by zhangming on 2016/2/29.
 * 添加合作人适配器
 */
public class MyContactAdapter extends BaseAdapter{
    private Activity activity;
    private List<AddContact_data_list> mList;
    private String technicianName;
    private int technicianId;

    public MyContactAdapter(Activity activity, List<AddContact_data_list> mList){
        this.activity = activity;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.add_contact_item, parent, false);
            holder.circleImageView = (CircleImageView)convertView.findViewById(R.id.iv_circle);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.btn_submit = (Button) convertView.findViewById(R.id.btn_submit);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        final AddContact_data_list data = mList.get(position);
        if(data.getAvatar()!=null){
            String imageUrl = NetURL.IP_PORT+data.getAvatar();
            Log.i("test","imageUrl=======>"+imageUrl);
            ImageLoaderCache.getInstance().loader(imageUrl,holder.circleImageView);
        }
        holder.tv_username.setText(data.getName());
        holder.tv_phone.setText(data.getPhone());

        holder.btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGetPosition.getPosition(position);
            }
        });

        return convertView;
    }


    static class ViewHolder{
        CircleImageView circleImageView;
        TextView tv_username;
        TextView tv_phone;
        Button btn_submit;
    }

    private int orderId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

//    public View.OnClickListener onClickListener;
//
//    public void setOnClickListener(View.OnClickListener onClickListener){
//        this.onClickListener = onClickListener;
//    }
    private OnGetPosition onGetPosition;


    public OnGetPosition getOnGetPosition() {
        return onGetPosition;
    }

    public void setOnGetPosition(OnGetPosition onGetPosition) {
        this.onGetPosition = onGetPosition;
    }

    public interface OnGetPosition{
        void getPosition(int position);
    }
}
