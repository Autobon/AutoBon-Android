package cn.com.incardata.fragment;


import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.com.incardata.autobon.R;


/**
 * 强制开始工作对话框
 */
public class ForceStartWorkDialogFragment extends DialogFragment {
    private OnForceListener mListener;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_force_start_work_dialog, container, false);
        }
        if(null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null) {
                parent.removeView(rootView);
            }
        }
        initViews();
        return rootView;
    }

    private void initViews() {
        rootView.findViewById(R.id.continue_wait).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        rootView.findViewById(R.id.force_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onForce();
                dismiss();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnForceListener) {
            mListener = (OnForceListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnForceListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnForceListener {
        // TODO: Update argument type and name
        void onForce();
    }
}
