package cn.com.incardata.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 图片处理
 *
 * @author wanghao
 */
public class BitmapHelper {
	private final static String TAG = "BitmapHelper";

	/**质量压缩（循环压缩至指定大小）
	 * @param bitmap
	 * @return
	 */
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

		BitmapFactory.Options option = new BitmapFactory.Options();
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmapTemp = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		bitmap.recycle();
		try {
			baos.close();
			isBm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmapTemp;
	}

	/**质量压缩（指定大小）
	 * @param bitmap
	 * @return
	 */
	public static Bitmap compressImage(Bitmap bitmap, int quality) {
		if(bitmap==null) return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public static Bitmap resizeImage(Context context, Uri uri, float scale) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;//不加载到内存
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		try {
			InputStream in = context.getContentResolver().openInputStream(uri);
			BitmapFactory.decodeStream(in, null, options);
			in.close();
			int originalWidth = options.outWidth;
			int originalHeight = options.outHeight;
			if ((originalWidth == -1) || (originalHeight == -1)) return null;

			int inSampleSize = 1;//计算缩放比（最长边不超过800px）
			if (originalWidth > originalHeight && originalHeight > 800){
				inSampleSize = originalWidth / 800;
			}else if (originalHeight > originalWidth && originalHeight > 800){
				inSampleSize = originalHeight / 800;
			}
			if (inSampleSize <= 0) inSampleSize = 4;
			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			in = context.getContentResolver().openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();
			return ThumbnailUtils.extractThumbnail(bitmap, originalWidth / inSampleSize, originalHeight / inSampleSize, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//			return Bitmap.createScaledBitmap(bitmap, originalWidth / inSampleSize, originalHeight / inSampleSize, true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Bitmap createImageThumbnail(String filePath){
		Bitmap bitmap = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);

		opts.inSampleSize = computeSampleSize(opts, -1, 128*128);
		opts.inJustDecodeBounds = false;

		try {
			bitmap = BitmapFactory.decodeFile(filePath, opts);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return bitmap;
	}

	/**计算缩放比例方法
	 * @param op
	 * @param reqWidth
	 * @param reqheight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options op, int reqWidth, int reqheight) {
		int originalWidth = op.outWidth;
		int originalHeight = op.outHeight;
		int inSampleSize = 1;
		if (originalWidth > reqWidth || originalHeight > reqheight) {
			int halfWidth = originalWidth / 2;
			int halfHeight = originalHeight / 2;
			while ((halfWidth / inSampleSize >= reqWidth)
					&&(halfHeight / inSampleSize >= reqheight)) {
				inSampleSize *= 2;

			}
		}
		return inSampleSize;
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 :(int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 保存图片到SD卡
	 * @param bitmapUri
	 * @param bitmap
	 * @return
	 */
	public static boolean saveBitmap(Uri bitmapUri, Bitmap bitmap){
		File file = new File(bitmapUri.getPath());
		if (file.exists()){
			file.delete();
		}else{
			file.getParentFile().mkdirs();
		}

		try {
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			if (!bitmap.isRecycled()){
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
