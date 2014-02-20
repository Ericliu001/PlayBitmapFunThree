package com.example.playbitmapfun.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

public class ImageResizer extends ImageWorker {
	
	protected int mImageWidth;
	protected int mImageHeight;
	
	
	public ImageResizer(Context context, int imageSize) {
		super(context);
		setImageSize(imageSize);
	}
	
	public void setImageSize(int size){
		setImageSize(size, size);
	}
	
	public void setImageSize(int width, int height){
		mImageWidth = width;
		mImageHeight = height;
	}

	@Override
	protected Bitmap processBitmap(Object data) {
		// TODO Auto-generated method stub
		return processBitmap(Integer.parseInt(String.valueOf(data)));
	}
	
	private Bitmap processBitmap(int resId){
		return decodeSampledBitmapFromResource(mResources, resId, mImageWidth, mImageHeight);
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {
		
		// first decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		
		// calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		Log.i("eric", "inSampleSize decided: " + options.inSampleSize);
		
		// decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
		
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
			int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float)height / (float)reqHeight );
			final int widthRatio = Math.round( (float)width / (float) reqWidth );
			
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			
			final float totalPixels = width * height;
			final float totalReqPixelsCap = reqHeight * reqWidth * 2;
			
			while (totalPixels /(inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize ++ ;
			}
		}
		
		
		return inSampleSize;
	}
}
