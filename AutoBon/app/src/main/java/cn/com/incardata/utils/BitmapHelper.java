package cn.com.incardata.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;


/**
 * 图片处理
 *
 * @author wanghao
 */
public class BitmapHelper {
	private final static String TAG = "BitmapHelper";

	/** 通过传入位图,新的宽.高比进行位图的缩放操作 */
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int newWidth = w;
		int newHeight = h;


		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

		//释放原始图片占用的内存
		bitmap.recycle();
		return resizedBitmap;

	}
}
