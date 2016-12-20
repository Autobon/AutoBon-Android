package cn.com.incardata.view.wheel.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.List;

import cn.com.incardata.adapter.ArrayWheelAdapter;
import cn.com.incardata.autobon.R;



/**
 * 滚轮选择，屏幕底部出现
 * Created by wanghao on 16/5/10.
 */
public class WheelPopupWindow extends PopupWindow{
    private Activity activity;
    private WheelView wheelView;
    private ArrayWheelAdapter mAdapter;
    private OnCheckedListener listener;

    public WheelPopupWindow(Activity activity) {
        this.activity = activity;
    }

    public void init(){
        View view = LayoutInflater.from(activity).inflate(R.layout.wheel_popupwindow, null, false);
        //设置SelectPicPopupWindow的View
        this.setContentView(view);

        wheelView = (WheelView) view.findViewById(R.id.wheel);
        mAdapter = new ArrayWheelAdapter(activity, R.layout.wheel_item, R.id.wheel_item);
        wheelView.setViewAdapter(mAdapter);
        wheelView.setVisibleItems(7);
        wheelView.setCurrentItem(0);

        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.SharePop);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                if (listener != null){
                    listener.onChecked(wheelView.getCurrentItem());
                }
                closePopupWindow();
            }
        });
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        WindowManager.LayoutParams params=activity.getWindow().getAttributes();
        params.alpha=0.7f;
        activity.getWindow().setAttributes(params);
    };

    /**
     * 关闭窗口
     */
    public void closePopupWindow()
    {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha=1f;
        activity.getWindow().setAttributes(params);
        dismiss();
    }

    /**
     * @param list
     * @param curIndex 显示指定位置
     */
    public void setData(List<String> list, int curIndex){
        setData(list);
        wheelView.setCurrentItem(curIndex);
    }

    public void setData(List<String> list){
        mAdapter.updateData(list);
    }

    /**
     * @return the listener
     */
    public OnCheckedListener getListener() {
        return listener;
    }

    /**
     * @param listener the listener to set
     */
    public void setListener(OnCheckedListener listener) {
        this.listener = listener;
    }

    public interface OnCheckedListener{
        /**
         * popupwindow消失时选中的内容
         */
        void onChecked(int index);
    }
}
