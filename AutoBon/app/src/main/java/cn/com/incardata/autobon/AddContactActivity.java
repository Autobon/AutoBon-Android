package cn.com.incardata.autobon;

import android.app.Activity;
import android.os.Bundle;

import cn.com.incardata.view.CircleImageView;

/**
 * Created by Administrator on 2016/2/19.
 */
public class AddContactActivity extends Activity{
    private CircleImageView circle_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_activity);
        initView();
    }

    public void initView(){
        circle_image = (CircleImageView) findViewById(R.id.iv_circle);
    }
}
