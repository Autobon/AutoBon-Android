package cn.com.incardata.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.com.incardata.autobon.R;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.response.AddContact_data_list;
import cn.com.incardata.view.CircleImageView;

/**
 * Created by zhangming on 2016/2/29.
 * 添加合作人适配器
 */
public class MyContactAdapter extends BaseAdapter{
    private Activity activity;
    private List<AddContact_data_list> mList;
    private String technicianName;

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
        if(getCount() > 0){
            AddContact_data_list data = mList.get(position);
            if(data.getAvatar()!=null){
                String imageUrl = NetURL.IP_URL+data.getAvatar();
                Log.i("test","imageUrl=======>"+imageUrl);
                if(getHttpBitmap(imageUrl)!=null){
                    holder.circleImageView.setImageBitmap(getHttpBitmap(imageUrl));
                }
            }
            holder.tv_username.setText(data.getName());
            holder.tv_phone.setText(data.getPhone());
            technicianName = data.getName();
        }
        holder.btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTechnician(technicianName); //添加技师
            }
        });

        return convertView;
    }

    public void addTechnician(String technicianName){
        Intent i=new Intent();
        i.putExtra("username",technicianName);
        activity.setResult(activity.RESULT_OK,i);
        activity.finish();
    }

    /**
     * 获取网落图片资源
     * @param imageUrl
     * @return
     */
    public static Bitmap getHttpBitmap(String imageUrl){
        Bitmap bitmap=null;
        try{
            URL myFileURL = new URL(imageUrl);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();

            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    static class ViewHolder{
        CircleImageView circleImageView;
        TextView tv_username;
        TextView tv_phone;
        Button btn_submit;
    }
}
