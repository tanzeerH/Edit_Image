package com.tanzeer.editimage.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.util.Log;

public class ImageUtils {
	private final static String t = "ImageUtils";
	private static ImageUtils imageUtils;

	public static ImageUtils getInstance() {
		if (imageUtils == null) {
			imageUtils = new ImageUtils();
		}
		return imageUtils;
	}

	// Rotate image according to orientation start
	public float rotationForImage(Context context, String path) {
		try {
			ExifInterface exif = new ExifInterface(path);
			int rotation = (int) exifOrientationToDegrees(exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL));
			return rotation;
		} catch (IOException e) {
			Log.e(t, "rotationForImage Error checking exif", e);
		}
		return 0f;
	}

	private float exifOrientationToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}

	public List<Face> detectFaces(Bitmap bitmap) {
		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			FaceDetector detector = new FaceDetector(width, height,
					CommonConstants.MAX_FACES);
			Face[] faces = new Face[CommonConstants.MAX_FACES];
			Bitmap bitmap565 = Bitmap.createBitmap(width, height,
					Config.RGB_565);
			Paint ditherPaint = new Paint();
			Paint drawPaint = new Paint();
			ditherPaint.setDither(true);
			drawPaint.setColor(Color.RED);
			drawPaint.setStyle(Paint.Style.STROKE);
			drawPaint.setStrokeWidth(2);
			Canvas canvas = new Canvas();
			canvas.setBitmap(bitmap565);
			canvas.drawBitmap(bitmap, 0, 0, ditherPaint);
			int facesFound = detector.findFaces(bitmap565, faces);
			Log.i("FaceDetector", "Number of faces found: " + facesFound);
			List<Face> listFaces = new ArrayList<FaceDetector.Face>();

			if (facesFound > 0) {
				for (int index = 0; index < facesFound; ++index) {
					listFaces.add(faces[index]);
				}
			}
			if (bitmap565 != bitmap){
				bitmap565.recycle();
				bitmap565 = null;
			}
			detector = null;
			return listFaces;
		} catch (Exception exception) {
			Log.e(t, exception.toString());
		}

		return null;

	}

	/*public Bitmap drawDefaultMustache(Context context, Bitmap orgBitmap) {
		int width = orgBitmap.getWidth();
		int height = orgBitmap.getHeight();
		FaceDetector detector = new FaceDetector(width, height,
				CommonConstants.MAX_FACES);
		Face[] faces = new Face[CommonConstants.MAX_FACES];
		Bitmap bitmap565 = Bitmap.createBitmap(width, height, Config.RGB_565);
		Paint ditherPaint = new Paint();
		Paint drawPaint = new Paint();
		ditherPaint.setDither(true);
		drawPaint.setColor(Color.RED);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeWidth(2);
		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap565);
		canvas.drawBitmap(orgBitmap, 0, 0, ditherPaint);

		int facesFound = detector.findFaces(bitmap565, faces);
		Log.i("FaceDetector", "Number of faces found: " + facesFound);

		PointF midPoint = new PointF();
		float eyeDistance = 0.0f;
		float confidence = 0.0f;

		if (facesFound > 0) {
			for (int index = 0; index < facesFound; ++index) {
				faces[index].getMidPoint(midPoint);
				eyeDistance = faces[index].eyesDistance();
				confidence = faces[index].confidence();
				Log.i("FaceDetector", "Confidence: " + confidence
						+ ", Eye distance: " + eyeDistance + ", Mid Point: ("
						+ midPoint.x + ", " + midPoint.y + ")");
				int w = (int) (eyeDistance * 2);
				int h = (int) (eyeDistance * 2);
				Bitmap bitmap = rescaleBitmapAspectRatio(
						getMustache(context,
								"celebrity/mustache/butcher_2x.png"), w, h);
				canvas.drawBitmap(bitmap, (int) midPoint.x - eyeDistance,
						(int) midPoint.y + eyeDistance / 2, ditherPaint);
				canvas.drawRect((int) midPoint.x - eyeDistance,
						(int) midPoint.y - eyeDistance, (int) midPoint.x
								+ eyeDistance, (int) midPoint.y + eyeDistance,
						drawPaint);

			}
		}
		return bitmap565;
	}*/

	/*public Bitmap getMustache(Context context, String path) {
		Bitmap bitmap = null;
		// String path = "celebrity/mustache/butcher_2x.png" ;//+
		// stache.getBaseName() + "_2x.png";
		try {
			bitmap = BitmapFactory.decodeStream(context.getAssets().open(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}*/

/*	public Drawable[] getMustacheDrawables(Context context, String path,
			String baseName) {
		List<Drawable> drawables = new ArrayList<Drawable>();
		try {
			path = path + CommonConstants.MUSTACHE_FOLDER;
			String[] strings = context.getAssets().list(path );
			String s = "_2x" + CommonConstants.MUSTACHE_FILE_EXT;
			for (int i = 0; i < strings.length; i++) {
				if (strings[i].startsWith(baseName) && strings[i].endsWith(s)) {
					String fullPath = path + "/" + strings[i];
					Bitmap bitmap = BitmapFactory.decodeStream(context
							.getAssets().open(fullPath));
					drawables.add(new BitmapDrawable(bitmap));
				}
			}
			Drawable[] drawables2 = new Drawable[drawables.size()];
			drawables2 = drawables.toArray(drawables2);
			return drawables2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			drawables.clear();
			drawables = null;
		}
		return null;
	}	*/
	public Drawable[] getMustacheDrawables(Context context, String path,
			String baseName,int w,int h) {
		List<Drawable> drawables = new ArrayList<Drawable>();
		try {
			path = path + CommonConstants.MUSTACHE_FOLDER;
			String[] strings = context.getAssets().list(path );
			String s = "_2x" + CommonConstants.MUSTACHE_FILE_EXT;
			for (int i = 0; i < strings.length; i++) {
				if (strings[i].startsWith(baseName) && strings[i].endsWith(s)) {
					String fullPath = path + "/" + strings[i];
					Bitmap bitmap = decodeStreamByWidthHeight(context
							.getAssets().open(fullPath),w,h);
					/*float scaleWidth;
					float scaleHeight;*/
					// calculating scale factor
					/*scaleWidth = (float) w / bitmap.getWidth();
					scaleHeight = (float) h / bitmap.getHeight();
					bitmap = rescaleBitmapAspectRatio(bitmap, Math.min(scaleWidth, scaleHeight));*/
					drawables.add(new BitmapDrawable(bitmap));
				}
			}
			Drawable[] drawables2 = new Drawable[drawables.size()];
			drawables2 = drawables.toArray(drawables2);
			return drawables2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	
	public float getScaleFactor(int imgW, int imgH, int srcW, int scrH) {
		float scaleWidth;
		float scaleHeight;
		// calculating scale factor
		scaleWidth = (float) srcW / imgW;
		scaleHeight = (float) scrH / imgH;
		Log.d(t, "scaleWidth: " + scaleWidth +" ;scaleHeight:" + scaleHeight);
		return Math.min(scaleWidth, scaleHeight);

	}

	public Bitmap loadBitmapFromFile(String path) {
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		return bitmap;
	}

	// Rotate image according to orientation end
	public Bitmap rescaleBitmapAspectRatio(Bitmap orgBitmap, float scaleFactor) {
		Bitmap scaledBimap = null;
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleFactor, scaleFactor);
		try {
			// recreate the new Bitmap
			scaledBimap = Bitmap.createBitmap(orgBitmap, 0, 0,
					orgBitmap.getWidth(), orgBitmap.getHeight(), matrix, true);

			return scaledBimap;
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}

		return scaledBimap;
	}
	public  Drawable rescaleDrawableAspectRatio(Drawable orgBitmap,
			int srcW, int scrH) {
		Bitmap b = ((BitmapDrawable) orgBitmap).getBitmap();
		Bitmap bitmapResized = rescaleBitmapAspectRatio(b, getScaleFactor(b.getWidth(), b.getHeight(), srcW, scrH));
		return new BitmapDrawable(bitmapResized);
	}
	public Bitmap decodeFileByWidthHeight(String pathName, int expectedWidth,
			int expectedHeight) {
		Bitmap bitmap = null;
		// decode image with checking out-of-memory
		boolean work = true;
		int insample = 0;
		BitmapFactory.Options options = new BitmapFactory.Options();
		while (work) {
			try {
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}
				options.inSampleSize = insample;
				bitmap = BitmapFactory.decodeFile(pathName, options);
				if (bitmap.getWidth() <= expectedWidth
						|| bitmap.getWidth() <= expectedHeight
						|| bitmap.getHeight() <= expectedWidth
						|| bitmap.getHeight() <= expectedHeight) {
					work = false;
				} else {
					insample++;
				}
			} catch (Exception e) {
				e.printStackTrace();
				work = false;
			} catch (Error e) {
				Log.e(t,
						"decodeFileByWidth: OutOfMemory with options.inSampleSize: "
								+ insample);
				insample++;
			}
		}
		if (bitmap != null) {
			Log.i(t, "decodeFileByWidth W:" + bitmap.getWidth() + ", H:"
					+ bitmap.getHeight() + " with inSampleSize: " + insample);
		} else {
			Log.e(t, "decodeFileByWidth failed with inSampleSize: " + insample);
		}
		return bitmap;
	}
	public Bitmap decodeStreamByWidthHeight(InputStream is, int expectedWidth,
			int expectedHeight) {
		Bitmap bitmap = null;
		// decode image with checking out-of-memory
		boolean work = true;
		int insample = 0;
		BitmapFactory.Options options = new BitmapFactory.Options();
		while (work) {
			try {
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}
				options.inSampleSize = insample;
				
				bitmap = BitmapFactory.decodeStream(is,null,options); //BitmapFactory.decodeFile(pathName, options);
				if (bitmap.getWidth() <= expectedWidth
						|| bitmap.getWidth() <= expectedHeight
						|| bitmap.getHeight() <= expectedWidth
						|| bitmap.getHeight() <= expectedHeight) {
					work = false;
				} else {
					insample++;
				}
			} catch (Exception e) {
				e.printStackTrace();
				work = false;
			} catch (Error e) {
				Log.e(t,
						"decodeFileByWidth: OutOfMemory with options.inSampleSize: "
								+ insample);
				insample++;
			}
		}
		if (bitmap != null) {
			Log.i(t, "decodeFileByWidth W:" + bitmap.getWidth() + ", H:"
					+ bitmap.getHeight() + " with inSampleSize: " + insample);
		} else {
			Log.e(t, "decodeFileByWidth failed with inSampleSize: " + insample);
		}
		return bitmap;
	}
	public Bitmap rescaleBitmapAspectRatio(Bitmap orgBitmap, int width,
			int height) {
		Bitmap scaledBimap = null;

		float scaleWidth;
		float scaleHeight;
		// calculating scale factor
		scaleWidth = (float) width / orgBitmap.getWidth();
		scaleHeight = (float) height / orgBitmap.getHeight();
		float scaleFactor = Math.min(scaleWidth, scaleHeight);
		// re-scale bitmap
		// int newWidth = Math.round(orgBitmap.getWidth() * scaleFactor);
		// int newHeight = Math.round(orgBitmap.getHeight() * scaleFactor);
		// scaledBimap = Bitmap.createScaledBitmap(
		// orgBitmap, newWidth, newHeight, true);
		//
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleFactor, scaleFactor);

		try {
			// recreate the new Bitmap
			scaledBimap = Bitmap.createBitmap(orgBitmap, 0, 0,
					orgBitmap.getWidth(), orgBitmap.getHeight(), matrix, true);

			return scaledBimap;
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}

		return scaledBimap;
	}
	public Bitmap decodeResourceByWidthHeight(Resources resources,int resID, int expectedWidth,
			int expectedHeight) {
		Bitmap bitmap = null;
		// decode image with checking out-of-memory
		boolean work = true;
		int insample = 0;
		BitmapFactory.Options options = new BitmapFactory.Options();
		while (work) {
			try {
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}
				options.inSampleSize = insample;
				
				bitmap = BitmapFactory.decodeResource(resources,resID,options); //BitmapFactory.decodeFile(pathName, options);
				if (bitmap.getWidth() <= expectedWidth
						|| bitmap.getWidth() <= expectedHeight
						|| bitmap.getHeight() <= expectedWidth
						|| bitmap.getHeight() <= expectedHeight) {
					work = false;
				} else {
					insample++;
				}
			} catch (Exception e) {
				e.printStackTrace();
				work = false;
			} catch (Error e) {
				Log.e(t,
						"decodeFileByWidth: OutOfMemory with options.inSampleSize: "
								+ insample);
				insample++;
			}
		}
		if (bitmap != null) {
			Log.i(t, "decodeFileByWidth W:" + bitmap.getWidth() + ", H:"
					+ bitmap.getHeight() + " with inSampleSize: " + insample);
		} else {
			Log.e(t, "decodeFileByWidth failed with inSampleSize: " + insample);
		}
		return bitmap;
	}
}
