package cn.com.incardata.autobon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;

import cn.com.incardata.http.ImageLoaderCache;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.view.ZoomImageView;

/**查看放大图片(传递当前显示位置POSITION及地址数组IMAGE_URL字段)
 * Created by yang on 2016/6/14.
 */
public class EnlargementActivity extends BaseActivity{
    public final static String IMAGE_URL = "IMAGE_URL";
    public final static String POSITION = "POSITION";
    private ViewPager viewPager;
    private RelativeLayout rl1;
    private TextView indicator;
    private String[] imgUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enlargement_image);

        bindView();
        overridePendingTransition(R.anim.anim_image_enter, R.anim.anim_image_quit);
    }

//

    public void bindView() {
        //传递当前显示位置POSITION及地址数组IMAGE_URL字段
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        imgUrl = bundle.getStringArray("IMAGE_URL");
        if (imgUrl == null){
            imgUrl = new String[]{"intent data error"};
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        rl1 = (RelativeLayout) findViewById(R.id.rl1);
        indicator = (TextView) findViewById(R.id.indicator);

        int position = intent.getExtras().getInt("POSITION", 0);
        indicator.setText((position + 1) + "/" + imgUrl.length);
        //设置Adapter
        viewPager.setAdapter(new MyAdapter());
        //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
        viewPager.setCurrentItem(position);
        //设置监听，主要是设置点点的背景
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            indicator.setText((position + 1) + "/" + imgUrl.length);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    });
    }


    public class MyAdapter extends PagerAdapter {
        private LinkedList<View> mViewCache = null;

        public MyAdapter() {
            mViewCache = new LinkedList<>();
        }

        @Override
        public int getCount() {
            return imgUrl == null ? 0 : imgUrl.length;
        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            mViewCache.add((View) object);
        }


        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(ViewGroup container, int i) {
            ZoomImageView convertView = null;
            if (mViewCache.size() == 0){
                convertView = new ZoomImageView(EnlargementActivity.this);
                convertView.setScaleType(ImageView.ScaleType.MATRIX);
                convertView.setOnClickListener(onClickPageListener);
            }else {
                convertView = (ZoomImageView) mViewCache.removeFirst();
            }
            ImageLoaderCache.getInstance().loader(NetURL.IP_PORT + imgUrl[i], convertView, R.mipmap.load_image_failed);
            container.addView(convertView);
            return convertView;
        }
    }

    private View.OnClickListener onClickPageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            overridePendingTransition(R.anim.anim_image_enter, R.anim.anim_image_quit);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

