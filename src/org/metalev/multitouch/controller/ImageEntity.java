/*
 * Code based off the PhotoSortrView from Luke Hutchinson's MTPhotoSortr
 * example (http://code.google.com/p/android-multitouch-controller/)
 *
 * License:
 *   Dual-licensed under the Apache License v2 and the GPL v2.
 */
package org.metalev.multitouch.controller;

import java.io.IOException;

import com.tanzeer.editimage.utils.ImageUtils;




import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import android.content.res.Resources;
import android.content.Context;

public class ImageEntity extends MultiTouchEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 411940954701202792L;

	public ImageEntity(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private final static String t = ImageEntity.class.getName();
	private static final double INITIAL_SCALE_FACTOR = 0.25;

	private double scaleFactor = INITIAL_SCALE_FACTOR;

	private transient Drawable drawable;

	// private int mResourceId;

	private boolean selected;
	//private transient Drawable[] drawables;
	private int drawableIndex;
	private boolean touchable = true;

	private String[] drawablePaths;

	/*
	 * public ImageEntity(int resourceId, Resources res) { // super(res);
	 * this.scaleFactor = INITIAL_SCALE_FACTOR; mResourceId = resourceId; }
	 */
	/*
	 * public ImageEntity(Drawable drawable, Resources res) { // super(res);
	 * this.scaleFactor = INITIAL_SCALE_FACTOR; this.drawable = drawable; }
	 */
	/*
	 * public ImageEntity(Drawable[] drawables, Resources res) { // super(res);
	 * this.scaleFactor = INITIAL_SCALE_FACTOR; this.drawables = drawables;
	 * this.drawableIndex =0 ; if (this.drawables != null &&
	 * this.drawables.length > 0){ this.drawable =
	 * this.drawables[drawableIndex]; } } public ImageEntity(Drawable[]
	 * drawables, Resources res,int displayWidth,int displayHeight) { //
	 * super(res); this.mDisplayWidth = displayWidth; this.mDisplayHeight =
	 * displayHeight; this.scaleFactor = INITIAL_SCALE_FACTOR; this.drawables =
	 * drawables; this.drawableIndex =0 ; if (this.drawables != null &&
	 * this.drawables.length > 0){ this.drawable =
	 * this.drawables[drawableIndex]; } }
	 * 
	 * public ImageEntity(ImageEntity e, Resources res) { // super(res);
	 * this.scaleFactor = INITIAL_SCALE_FACTOR; drawable = e.drawable;
	 * mResourceId = e.mResourceId; mScaleX = e.mScaleX; mScaleY = e.mScaleY;
	 * mCenterX = e.mCenterX; mCenterY = e.mCenterY; mAngle = e.mAngle; }
	 */
	/*public ImageEntity(Context context, Drawable[] drawables, Resources res,
			int displayWidth, int displayHeight) {
		// super(res);
		super(context);
		this.mDisplayWidth = displayWidth;
		this.mDisplayHeight = displayHeight;
		this.scaleFactor = INITIAL_SCALE_FACTOR;
		this.drawables = drawables;
		this.drawableIndex = 0;
		if (this.drawables != null && this.drawables.length > 0) {
			this.drawable = this.drawables[drawableIndex];
		}
	}*/

	public ImageEntity(Context context, String[] drawablePaths,
			int displayWidth, int displayHeight) {
		// super(res);
		super(context);
		this.mDisplayWidth = displayWidth;
		this.mDisplayHeight = displayHeight;
		this.scaleFactor = INITIAL_SCALE_FACTOR;
		this.drawablePaths = drawablePaths;
		this.drawableIndex = 0;
		if (this.drawablePaths != null && this.drawablePaths.length > 0) {
			this.drawable = loadDrawable(context, this.drawableIndex);
		}
	}

	/*public ImageEntity(Context context, Drawable[] drawables) {
		// super(res);
		super(context);
		this.scaleFactor = INITIAL_SCALE_FACTOR;
		this.drawables = drawables;
		this.drawableIndex = 0;
		if (this.drawables != null && this.drawables.length > 0) {
			this.drawable = this.drawables[drawableIndex];
		}
	}*/

	public ImageEntity(Context context, String[] drawablePaths) {
		// super(res);
		super(context);
		this.scaleFactor = INITIAL_SCALE_FACTOR;
		this.drawablePaths = drawablePaths;
		this.drawableIndex = 0;
		if (this.drawablePaths != null && this.drawablePaths.length > 0) {
			this.drawable = loadDrawable(context, this.drawableIndex);
		}
	}

	/*public ImageEntity(Context context, Drawable[] drawables, float centerX,
			float centerY, float scaleFactor) {
		// super(res);
		super(context);

		this.drawables = drawables;
		this.drawableIndex = 0;
		if (this.drawables != null && this.drawables.length > 0) {
			this.drawable = this.drawables[drawableIndex];
		}
		this.scaleFactor = scaleFactor;
		this.mCenterX = centerX;
		this.mCenterY = centerY;
	}*/

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		// super.onDraw(canvas);
		draw(canvas);
	}

	public void draw(Canvas canvas) {
		canvas.save();

		float dx = (mMaxX + mMinX) / 2;
		float dy = (mMaxY + mMinY) / 2;

		drawable.setBounds((int) mMinX, (int) mMinY, (int) mMaxX, (int) mMaxY);

		canvas.translate(dx, dy);
		canvas.rotate(mAngle * 180.0f / (float) Math.PI);
		canvas.translate(-dx, -dy);

		if (selected) {
			Paint drawPaint = new Paint();
			drawPaint.setColor(Color.WHITE);
			drawPaint.setStyle(Paint.Style.STROKE);
			drawPaint.setStrokeWidth(1);
			canvas.drawRect((int) mMinX, (int) mMinY, (int) mMaxX, (int) mMaxY,
					drawPaint);
		}

		drawable.draw(canvas);

		canvas.restore();
	}

	/**
	 * Called by activity's onPause() method to free memory used for loading the
	 * images
	 */
	@Override
	public void unload() {
		if (this.drawable != null){
			this.drawable.setCallback(null);
		}
		this.drawable = null;
	}

	/** Called by activity's onResume() method to load the images */
	@Override
	public void load(Context context, float startMidX, float startMidY) {
		Resources res = context.getResources();
		getMetrics(res);

		mStartMidX = startMidX;
		mStartMidY = startMidY;
		Log.d(t, "startMidX = " + startMidX + ";startMidY = " + startMidY);
		// mDrawable = res.getDrawable(mResourceId);

		mWidth = drawable.getIntrinsicWidth();
		mHeight = drawable.getIntrinsicHeight();

		float centerX;
		float centerY;
		float scaleX;
		float scaleY;
		if (mFirstLoad) {
			centerX = startMidX;
			centerY = startMidY;

			float scaleFactor = (float) this.scaleFactor;/*
														 * (float)
														 * (Math.max(mDisplayWidth
														 * , mDisplayHeight) /
														 * (float)
														 * Math.max(mWidth,
														 * mHeight) *
														 * this.scaleFactor);//
														 * INITIAL_SCALE_FACTOR
														 * );
														 */
			scaleX = scaleY = scaleFactor;
			mFirstLoad = false;
		} else {
			centerX = mCenterX;
			centerY = mCenterY;
			scaleX = mScaleX;
			scaleY = mScaleY;
		}
		setPos(centerX, centerY, scaleX, scaleY, mAngle);
	}

	public void load(Context context) {
		load(context, mCenterX, mCenterY);
	}

	public void reload(Context context, float startMidX, float startMidY) {
		load(context, startMidX, startMidY);
	}

	public double getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getDrawableIndex() {
		return drawableIndex;
	}

	public void setDrawableIndex(int drawableIndex) {
		if ( 0< drawableIndex && drawableIndex < getDrawablesCount()){
			this.drawableIndex = drawableIndex;
		}else{
			this.drawableIndex = 0;
		}
	
		if (this.drawablePaths != null && this.drawablePaths.length > 0) {
			this.drawable.setCallback(null);
			this.drawable = null;
			this.drawable = loadDrawable(this.getContext(), this.drawableIndex);
		}
	}

	public int getDrawablesCount() {
		if (drawablePaths != null) {
			return drawablePaths.length;
		}
		return 0;
	}

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	public boolean isTouchable() {
		return touchable;
	}

	public boolean canChangeColor() {
		if (getDrawablesCount() > 1) {
			return true;
		}
		return false;
	}

	private float translationY = 0;

	public float getTranslationY() {
		return translationY;
	}

	public void setTranslationY(float translationY) {
		this.translationY = translationY;
	}

	public void setCenterXY(float centerX, float centerY) {
		this.mCenterX = centerX;
		this.mCenterY = centerY;
	}

	private Drawable loadDrawable(Context context, int index) {
		Bitmap bitmap = null;
		try {
			String fullPath = this.drawablePaths[ index];
			bitmap = ImageUtils.getInstance(). decodeStreamByWidthHeight(context
					.getAssets().open(fullPath),this.mDisplayWidth,this.mDisplayHeight);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BitmapDrawable(bitmap);
	}
}
