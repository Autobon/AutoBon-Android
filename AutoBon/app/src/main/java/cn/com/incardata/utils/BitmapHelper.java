package cn.com.incardata.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 图片处理
 *
 * @author wanghao
 */
public class BitmapHelper {
	private final static String TAG = "BitmapHelper";

	public static Bitmap compressImage(Bitmap bitmap) {
		if(bitmap==null) return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024 > 200) { //循环判断如果压缩后图片是否大于200kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			options -= 10;//每次都减少10
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			if(options<10){   //压缩质量不能小于10%
				break;
			}
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

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

	/** 通过传入位图、比例进行位图的缩放操作 */
	public static Bitmap resizeImage(Bitmap bitmap, float scale) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

		//释放原始图片占用的内存
		bitmap.recycle();
		return resizedBitmap;
	}

	public static boolean saveBitmap(Uri bitmapUri, Bitmap bitmap, boolean isRecycle){
		File file = new File(bitmapUri.getPath());
		if (file.exists()){
			file.delete();
		}

		try {
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
			fos.flush();
			fos.close();
			if (isRecycle && !bitmap.isRecycled()){
				bitmap.recycle();
			}
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
