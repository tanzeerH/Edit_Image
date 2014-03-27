/**
 * PhotoSorterView.java
 * 
 * (c) Luke Hutchison (luke.hutch@mit.edu)
 * 
 * TODO: Add OpenGL acceleration.
 * 
 * Released under the Apache License v2.
 */
package com.tanzeer.editimage.views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.metalev.multitouch.controller.MultiTouchController;
import org.metalev.multitouch.controller.MultiTouchController.MultiTouchObjectCanvas;
import org.metalev.multitouch.controller.MultiTouchController.PointInfo;
import org.metalev.multitouch.controller.MultiTouchController.PositionAndScale;
import org.metalev.multitouch.controller.MultiTouchEntity;
import org.metalev.multitouch.controller.ImageEntity;

import com.tanzeer.editimage.R;
import com.tanzeer.editimage.utils.CommonConstants;




import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PictureView extends ImageView implements
		MultiTouchObjectCanvas<MultiTouchEntity> {

//	private final static String t = PictureView.class.getName();
	
	private ArrayList<MultiTouchEntity> imageEntities = new ArrayList<MultiTouchEntity>();
	private Uri outputPictireUri = null;
	// --

	private MultiTouchController<MultiTouchEntity> multiTouchController = new MultiTouchController<MultiTouchEntity>(
			this);

	// --

	private PointInfo currTouchPoint = new PointInfo();

	private boolean mShowDebugInfo = false;

	private static final int UI_MODE_ROTATE = 1, UI_MODE_ANISOTROPIC_SCALE = 2;

	private int mUIMode = UI_MODE_ROTATE;

	// --

	private Paint mLinePaintTouchPointCircle = new Paint();

	private static final float SCREEN_MARGIN = 0;

	private int displayWidth, displayHeight;

	// ---------------------------------------------------------------------------------------------------

	public PictureView(Context context) {
		this(context, null);
	}

	public PictureView(Context context, AttributeSet attrs) {
		this(context, attrs, 0, null);
	}

	public PictureView(Context context, AttributeSet attrs, int defStyle,
			ArrayList<Drawable> images) {
		super(context, attrs, defStyle);
		init(context, images);

	}

	private void init(Context context, ArrayList<Drawable> images) {
		Resources res = context.getResources();

		/*if (images != null) {
			for (int i = 0; i < images.size(); i++) {
				imageEntities.add(new ImageEntity(images.get(i), res));
			}
		}*/

		mLinePaintTouchPointCircle.setColor(Color.YELLOW);
		mLinePaintTouchPointCircle.setStrokeWidth(5);
		mLinePaintTouchPointCircle.setStyle(Style.STROKE);
		mLinePaintTouchPointCircle.setAntiAlias(true);
		// setBackgroundColor(Color.BLACK);
		// setBackgroundDrawable(images[0]);

		DisplayMetrics metrics = res.getDisplayMetrics();
		this.displayWidth = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math
				.max(metrics.widthPixels, metrics.heightPixels) : Math.min(
				metrics.widthPixels, metrics.heightPixels);
		this.displayHeight = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math
				.min(metrics.widthPixels, metrics.heightPixels) : Math.max(
				metrics.widthPixels, metrics.heightPixels);
	}

	/** Called by activity's onResume() method to load the images */
	public void loadImages(Context context) {
		int n = imageEntities.size();
		float cx = displayWidth;
		float cy = displayHeight;
		for (int i = 0; i < n; i++) {
			/*cx = SCREEN_MARGIN
					+ (float) (Math.random() * (displayWidth - 2 * SCREEN_MARGIN));
			cy = SCREEN_MARGIN
					+ (float) (Math.random() * (displayHeight - 2 * SCREEN_MARGIN));*/
			cx = imageEntities.get(i).getCenterX();
			cy = imageEntities.get(i).getCenterY();

			imageEntities.get(i).load(context, cx, cy);
		}
	}

	/**
	 * Called by activity's onPause() method to free memory used for loading the
	 * images
	 */
	public void unloadImages() {
		int n = imageEntities.size();
		for (int i = 0; i < n; i++)
			imageEntities.get(i).unload();
	}

	public void saveImage(Context context) {
		this.buildDrawingCache();
		Bitmap bitmap = this.getDrawingCache();
		OutputStream fOut = null;

		try {
			File root = new File(Environment.getExternalStorageDirectory()
					+ File.separator + getResources().getString(R.string.app_name) + File.separator);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmss") ;
			Log.v("flag","2");
			String fileName = sdf.format(new Date()) + CommonConstants.PICTURE_FILE_EXT;
			root.mkdirs();
			Log.v("flag","1");
			File sdImageMainDirectory = new File(root, fileName);
			outputPictireUri = Uri.fromFile(sdImageMainDirectory);
			Log.v("flag","1");
			fOut = new FileOutputStream(sdImageMainDirectory);
			Log.v("flag","2");
		} catch (Exception e) {
			Toast.makeText(context,
					"save_picture_failed",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

		try {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
		}
	}

	// ---------------------------------------------------------------------------------------------------

	@Override
	protected void onDraw(Canvas canvas) {
		try{
			super.onDraw(canvas);
			int n = imageEntities.size();
			for (int i = 0; i < n; i++)
				imageEntities.get(i).draw(canvas);
			if (mShowDebugInfo)
				drawMultitouchDebugMarks(canvas);
			// drawEntityBackground(canvas);
		}catch(Exception exception){
			
		}
		
	}

	// ---------------------------------------------------------------------------------------------------

	public void trackballClicked() {
		mUIMode = (mUIMode + 1) % 3;
		invalidate();
	}

	private void drawMultitouchDebugMarks(Canvas canvas) {
		if (currTouchPoint.isDown()) {
			float[] xs = currTouchPoint.getXs();
			float[] ys = currTouchPoint.getYs();
			float[] pressures = currTouchPoint.getPressures();
			int numPoints = Math.min(currTouchPoint.getNumTouchPoints(), 2);
			for (int i = 0; i < numPoints; i++)
				canvas.drawCircle(xs[i], ys[i], 50 + pressures[i] * 80,
						mLinePaintTouchPointCircle);
			if (numPoints == 2)
				canvas.drawLine(xs[0], ys[0], xs[1], ys[1],
						mLinePaintTouchPointCircle);
		}
	}

	// ---------------------------------------------------------------------------------------------------

	/** Pass touch events to the MT controller */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return multiTouchController.onTouchEvent(event);
	}

	/**
	 * Get the image that is under the single-touch point, or return null
	 * (canceling the drag op) if none
	 */
	public MultiTouchEntity getDraggableObjectAtPoint(PointInfo pt) {
		
		float x = pt.getX(), y = pt.getY();
		int n = imageEntities.size();
		for (int i = n - 1; i >= 0; i--) {
			ImageEntity im = (ImageEntity) imageEntities.get(i);
			if (im.containsPoint(x, y) && im.isTouchable())
				return im;
		}
		return null;
	}

	/**
	 * Select an object for dragging. Called whenever an object is found to be
	 * under the point (non-null is returned by getDraggableObjectAtPoint()) and
	 * a drag operation is starting. Called with null when drag op ends.
	 */
	public void selectObject(MultiTouchEntity img, PointInfo touchPoint) {
	//	if (((ImageEntity)img).isTouchable()){
			currTouchPoint.set(touchPoint);
			if (img != null) {
				// Move image to the top of the stack when selected
				imageEntities.remove(img);
				imageEntities.add(img);
				for (int i = 0; i < imageEntities.size(); i++) {
					if (((ImageEntity) imageEntities.get(i)).isSelected()) {
						((ImageEntity) imageEntities.get(i)).setSelected(false);
					}
				}
				((ImageEntity) img).setSelected(true);
			} else {
				// Called with img == null when drag stops.
	
			}
			invalidate();
	//	}
	}

	public void unselectObject(MultiTouchEntity img) {
		if (img != null) {
			((ImageEntity) img).setSelected(false);
			invalidate();
		}
	}
	public void unselectAllObject(){
		for (int i = 0; i < imageEntities.size(); i++) {
			if (((ImageEntity) imageEntities.get(i)).isSelected()) {
				((ImageEntity) imageEntities.get(i)).setSelected(false);
			}
		}
		invalidate();
	}

	/**
	 * Get the current position and scale of the selected image. Called whenever
	 * a drag starts or is reset.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void getPositionAndScale(MultiTouchEntity img,
			PositionAndScale objPosAndScaleOut) {
		// FIXME affine-izem (and fix the fact that the anisotropic_scale part
		// requires averaging the two scale factors)
		objPosAndScaleOut.set(img.getCenterX(), img.getCenterY(),
				(mUIMode & UI_MODE_ANISOTROPIC_SCALE) == 0,
				(img.getScaleX() + img.getScaleY()) / 2,
				(mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0, img.getScaleX(),
				img.getScaleY(), (mUIMode & UI_MODE_ROTATE) != 0,
				img.getAngle());
	}

	/** Set the position and scale of the dragged/stretched image. */
	public boolean setPositionAndScale(MultiTouchEntity img,
			PositionAndScale newImgPosAndScale, PointInfo touchPoint) {
		currTouchPoint.set(touchPoint);
		boolean ok = ((ImageEntity) img).setPos(newImgPosAndScale);
		if (ok)
			invalidate();
		return ok;
	}

	public boolean pointInObjectGrabArea(PointInfo pt, MultiTouchEntity img) {
		return false;
	}

	public MultiTouchEntity getSelectedImgEntity() {
		for (int i = 0; i < imageEntities.size(); i++) {
			if (((ImageEntity) imageEntities.get(i)).isSelected()) {
				return imageEntities.get(i);
			}
		}
		return null;
	}

	public void addImageEntity(Context context, MultiTouchEntity imageEntity) {
		imageEntities.add(imageEntity);

		float cx = SCREEN_MARGIN
				+ (float) ((getWidth() - 2 * SCREEN_MARGIN) / 2);
		float cy = SCREEN_MARGIN
				+ (float) ((getHeight() - 2 * SCREEN_MARGIN) / 2);
		imageEntities.get(imageEntities.size() - 1).load(context, cx, cy);
	}

	public void addImageEntity(Context context, MultiTouchEntity imageEntity,
			float cx, float cy) {
		imageEntities.add(imageEntity);
		imageEntities.get(imageEntities.size() - 1).load(context, cx, cy);
	}

	public void removeImageEntity(MultiTouchEntity imageEntity) {
		if (imageEntity != null && imageEntities.contains(imageEntity)) {
			imageEntities.remove(imageEntity);
			imageEntity.unload();
			imageEntity = null;
			invalidate();
		}

	}

	public void replaceImageEntity(Context context, MultiTouchEntity oldEntity,
			MultiTouchEntity newEntity) {
		if (oldEntity != null && imageEntities.contains(oldEntity)) {

			float cx = oldEntity.getCenterX();
			float cy = oldEntity.getCenterY();
			imageEntities.remove(oldEntity);
			invalidate();
			imageEntities.add(newEntity);
			((ImageEntity) newEntity).setSelected(true);
			imageEntities.get(imageEntities.size() - 1).load(context, cx, cy);

		}

	}

	public boolean isTouchOnEntity(float x, float y) {
		// PointInfo info = new PointInfo();
		// float x = pt.getX(), y = pt.getY();
		int n = imageEntities.size();
		for (int i = n - 1; i >= 0; i--) {
			ImageEntity im = (ImageEntity) imageEntities.get(i);
			if (im.containsPoint(x, y) && im.isTouchable())
				return true;
		}
		return false;
	}

	public Uri getOutputPictireUri() {
		return outputPictireUri;
	}

	public void cleanup(){
		try{
			if (imageEntities != null){
				for(MultiTouchEntity entity:imageEntities){
					((ImageEntity)entity).unload();
				}
				imageEntities.clear();
			}
			setBackgroundDrawable(null);
			setImageBitmap(null);
			
		}catch(Exception exception){
			exception.printStackTrace();
		}
	
	}
}
