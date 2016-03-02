package cn.com.incardata.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
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
        if(getCount() > 0){
            AddContact_data_list data = mList.get(position);
            if(data.getAvatar()!=null){
                String imageUrl = NetURL.IP_URL+data.getAvatar();
                Log.i("test","imageUrl=======>"+imageUrl);
                //TechnicianPhotoAsyncTask task = new TechnicianPhotoAsyncTask(imageUrl,holder.circleImageView);
                //task.execute();
            }
            holder.tv_username.setText(data.getName());
            holder.tv_phone.setText(data.getPhone());
            technicianName = data.getName();
            technicianId = data.getId();
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
        i.putExtra("technicianId",technicianId);
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
            HttpGet httpGet = new HttpGet(imageUrl);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                InputStream in = entity.getContent();
                bitmap = BitmapFactory.decodeStream(in);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    class TechnicianPhotoAsyncTask extends AsyncTask<Void,Void,Bitmap>{
        private String imageUrl;
        private CircleImageView image;

        public TechnicianPhotoAsyncTask(String imageUrl,CircleImageView image){
            this.imageUrl = imageUrl;
            this.image = image;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = getHttpBitmap(this.imageUrl);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            image.setImageBitmap(bitmap);
        }
    }

    static class ViewHolder{
        CircleImageView circleImageView;
        TextView tv_username;
        TextView tv_phone;
        Button btn_submit;
    }
}
