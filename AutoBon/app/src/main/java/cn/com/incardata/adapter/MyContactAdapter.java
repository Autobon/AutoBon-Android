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
    public View getView(int position, View convertView, ViewGroup parent) {
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
                technicianName = data.getName();
                technicianId = data.getId();
                addTechnician(); //添加技师
            }
        });

        return convertView;
    }

    public void addTechnician(){
        //TODO 发送合作邀请
        Http.getInstance().postTaskToken(NetURL.inviteTechnician(String.valueOf(orderId), String.valueOf(technicianId)), InviteTechnicainEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                if(entity == null){
                    T.show(activity,activity.getString(R.string.invite_contact_failed));
                    return;
                }
                InviteTechnicainEntity inviteTechnicainEntity = (InviteTechnicainEntity) entity;
                if(inviteTechnicainEntity.isResult()){
                    Log.i("test","添加合作技师成功");
                    Intent i=new Intent();
                    i.putExtra("username",technicianName);
                    i.putExtra("technicianId",technicianId);
                    activity.setResult(activity.RESULT_OK,i);
                    activity.finish();
                }else{
                    T.show(activity,inviteTechnicainEntity.getMessage());
                }
            }
        }, new BasicNameValuePair("orderId", String.valueOf(orderId)), new BasicNameValuePair("partnerId", String.valueOf(technicianId)));
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
}
