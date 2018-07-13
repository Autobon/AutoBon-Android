package cn.com.incardata.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.com.incardata.autobon.R;


/**
 * 条件筛选悬浮框
 * Created by yang on 2017/1/17.
 */
public class SelectPopupWindow extends PopupWindow implements View.OnClickListener {
    private TextView[] textViews = new TextView[10];
    private TextView tv_cancel,tv_ok;
    private Activity context;
    private int checkId = -1;
    private OnCheckedListener listener;

    /**
     * 车长
     */
    public static final int CARLENGTH = 1;
    /**
     * 厢型
     */
    public static final int CARCAMIONNETTE = 2;
    /**
     * 状态
     */
    public static final int CARSTATUS = 3;
    /**
     * 类型
     */
    public static final int CARTYPE = 4;


    public SelectPopupWindow(Activity context) {
        this.context = context;
    }


    public void init(){
        View view = LayoutInflater.from(context).inflate(R.layout.garage_select_popup_wondow_fleet,null,false);
        this.setContentView(view);

        textViews[0] = (TextView) view.findViewById(R.id.tv1);
        textViews[1] = (TextView) view.findViewById(R.id.tv2);
        textViews[2] = (TextView) view.findViewById(R.id.tv3);
        textViews[3] = (TextView) view.findViewById(R.id.tv4);
        textViews[4] = (TextView) view.findViewById(R.id.tv5);
        textViews[5] = (TextView) view.findViewById(R.id.tv6);
        textViews[6] = (TextView) view.findViewById(R.id.tv7);
        textViews[7] = (TextView) view.findViewById(R.id.tv8);
        textViews[8] = (TextView) view.findViewById(R.id.tv9);
        textViews[9] = (TextView) view.findViewById(R.id.tv10);

        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok = (TextView) view.findViewById(R.id.tv_ok);

        for (TextView textView : textViews){
            textView.setOnClickListener(this);
        }
        tv_ok.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                closePopupWindow();
            }
        });

        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.SharePop);

//        实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
//        设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }

    public void setData(List<String> list){
        checkId = -1;
        for (int i = 0; i < textViews.length; i++){
            textViews[i].setTextColor(context.getResources().getColor(R.color.darkgray));
            if (i < list.size()){
                textViews[i].setVisibility(View.VISIBLE);
                textViews[i].setText(list.get(i));
            }else {
                textViews[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv1:
                showColor(0);
                break;
            case R.id.tv2:
                showColor(1);
                break;
            case R.id.tv3:
                showColor(2);
                break;
            case R.id.tv4:
                showColor(3);
                break;
            case R.id.tv5:
                showColor(4);
                break;
            case R.id.tv6:
                showColor(5);
                break;
            case R.id.tv7:
                showColor(6);
                break;
            case R.id.tv8:
                showColor(7);
                break;
            case R.id.tv9:
                showColor(8);
                break;
            case R.id.tv10:
                showColor(9);
                break;
            case R.id.tv_cancel:
                closePopupWindow();
                break;
            case R.id.tv_ok:
                if (listener != null){
                    listener.onChecked(checkId);
                }
                closePopupWindow();
                break;
        }
    }

    public void showColor(int id){
        checkId = id;
        for (int i = 0; i < textViews.length; i++){
            if (i == id){
                textViews[i].setTextColor(context.getResources().getColor(R.color.main_orange));
            }else {
                textViews[i].setTextColor(context.getResources().getColor(R.color.darkgray));
            }
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        this.showAsDropDown(parent);
//        super.showAtLocation(parent, gravity, x, y);
        WindowManager.LayoutParams params=context.getWindow().getAttributes();
        params.alpha=0.7f;
        context.getWindow().setAttributes(params);
    };


    /**
     * 关闭窗口
     */
    public void closePopupWindow() {
        WindowManager.LayoutParams params = context.getWindow().getAttributes();
        params.alpha = 1f;
        context.getWindow().setAttributes(params);
        dismiss();
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
