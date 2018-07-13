package cn.com.incardata.view;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.com.incardata.autobon.R;


/**
 * 选择拍照或者图库选取图片
 * create an instance of this fragment.
 * @author wanghao
 */
public class ImageChooseFragment extends DialogFragment {
    private OnFragmentInteractionListener mListener;
    private View rootView;

    public ImageChooseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragment_Cancelable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_image_choose, container, false);
            initView();
        }else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    private void initView() {
        rootView.findViewById(R.id.capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(CAPTURE, ImageChooseFragment.this);
                dismiss();
            }
        });
        rootView.findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(GALLERY, ImageChooseFragment.this);
                dismiss();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int type) {
        if (mListener != null) {
            mListener.onFragmentInteraction(type, this);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null){
            mListener.onDismiss();
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        /**选择回调
         * @param type
         */
        // TODO: Update argument type and name
        void onFragmentInteraction(int type, ImageChooseFragment mImageChoose);
        void onDismiss();
    }


    /**
     *拍照
     */
    public final static int CAPTURE= 1;
    /**
     *图库
     */
    public final static int GALLERY = 2;

    /**
     *拍照结果
     */
    public final static int CAPTURE_REQUEST = 30;
    /**
     *图库选择结果
     */
    public final static int GALLERY_REQUEST = 20;
    /**
     *裁剪结果
     */
    public final static int CROP_REQUEST = 12;


    /**拍照
     * @param requestCode 请求码
     * @param imageUri  图片存放位置
     */
    public void capture(int requestCode, Uri imageUri){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        getActivity().startActivityForResult(intent, requestCode);
    }

    /**
     * 裁剪
     * @param requestCode   请求码
     * @param imageUri      待裁剪的图片
     * @param imageCropUri  裁剪后的存放位置
     */
    public void crop(int requestCode, Uri imageUri, Uri imageCropUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, requestCode);
    }

}
