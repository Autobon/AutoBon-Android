package cn.com.incardata.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.MapViewLayoutParams;

import cn.com.incardata.autobon.R;

/**
 * 放弃订单dialog
 * @author wanghao
 */
public class DropOrderDialogFragment extends DialogFragment implements View.OnClickListener{
    private OnClickListener mListener;
    private View rootView;
    private TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragment);
    }

    public void setData(boolean isCancel){
        if (isCancel){
            title.setText("确认要放弃此单吗");
        }else {
            title.setText("确认要放弃此单吗");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_drop_order_dialog, container, false);
            initViews();
        }
        if(null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    private void initViews() {
        title = (TextView) rootView.findViewById(R.id.title);
        rootView.findViewById(R.id.cancel).setOnClickListener(this);
        rootView.findViewById(R.id.ok).setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnClickListener) {
            mListener = (OnClickListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnClickListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                dismiss();
                break;
            case R.id.ok:
                if (mListener != null){
                    mListener.onDropClick(v);
                }
                dismiss();
                break;
        }
    }

    public interface OnClickListener {
        // TODO: Update argument type and name
        void onDropClick(View v);
    }
}
