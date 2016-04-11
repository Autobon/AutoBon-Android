package cn.com.incardata.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 标准的基类Fragment
 * @author zhangming
 * @date 2016/03/14
 */
public abstract class BaseStandardFragment extends Fragment{
    protected List<String> paramList = new ArrayList<String>();
    protected OnFragmentInteractionListener mListener;

    /**
     * 通过工厂方法来创建Fragment实例
     * 同时给Fragment来提供参数来使用
     * 供外部调用,可以从Activity中传递参数到Fragment 
     * 通过泛型解决传入的fragment实例类型
     * @return BaseStandardFragment的实例.
     * @throws Exception
     */
    public static <T>T newInstance(Class<T> cls,String...params) throws Exception{
        T fragment = cls.newInstance();
        Bundle args = new Bundle();
        args.putStringArray("params", params);
        if(fragment instanceof BaseStandardFragment){
            BaseStandardFragment bsFragment = (BaseStandardFragment)fragment;
            bsFragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            for(String param : getArguments().getStringArray("params")){
                paramList.add(param);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return this.onFragmentCreateView(inflater, container, savedInstanceState);
    }

    // TODO: 提供此方法供子类的fragment重写,根据实际情况加载不同布局View
    public abstract View onFragmentCreateView(LayoutInflater inflater,
                                              ViewGroup container,Bundle savedInstanceState);

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     *onAttach中连接监听接口 确保Activity支持该接口
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * onDetach中注销接口
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * 注意:这个互动接口必须被含有BaseStandardFragment的Activity实现
     * 来实现Fragment与Activity直接的互通
     */
    public interface OnFragmentInteractionListener {
        // 根据实际需求更改
        public void onFragmentInteraction(Uri uri);
    }
}
