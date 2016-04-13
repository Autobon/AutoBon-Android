package cn.com.incardata.customfun;

/**
 * 处理相机或图库选取的图片
 * Created by wanghao on 16/2/29.
 */
public class GatherImage {
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
    public final static int CAPTURE_REQUEST = 10;
    /**
     *图库选择结果
     */
    public final static int GALLERY_REQUEST = 11;
    /**
     *裁剪结果
     */
    public final static int CROP_REQUEST = 12;
    /**
     *身份证拍照结果
     */
    public final static int CAPTURE_ID_REQUEST = 13;
    /**
     *身份证图库选择
     */
    public final static int GALLERY_ID_REQUEST = 14;
}
