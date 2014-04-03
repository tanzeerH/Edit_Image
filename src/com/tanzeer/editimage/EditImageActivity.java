package com.tanzeer.editimage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.metalev.multitouch.controller.ImageEntity;

import com.tanzeer.editimage.asynktask.SendMailTask;
import com.tanzeer.editimage.utils.CommonConstants;
import com.tanzeer.editimage.utils.ImageUtils;
import com.tanzeer.editimage.utils.PreferenceConnector;
import com.tanzeer.editimage.views.PictureView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.FaceDetector.Face;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditImageActivity extends Activity implements OnClickListener {
	private final static String t = EditImageActivity.class.getName();
	private PictureView pictureView;
	Button btnMail, btnAdd;
	Bitmap picture;
	private Uri imageUri;
	private float scaleFractor;
	private int pictureSource;
	private ImageUtils imageUtils = ImageUtils.getInstance();
	int marginLeft = 0;
	protected int displayWidth;
	protected int displayHeight;
	protected int pictureDisplayWidth;
	protected int pictureDisplayHeight;
	protected int footerBarHeight;

	private float cx;
	private float cy;
	List<Face> faces = null;
	private List<ImageEntity> entities = new ArrayList<ImageEntity>();
	private int entityIndex = 0;

	public static Bitmap sendPicture;
	private ImageView imgSunglass, imgRain, imgSun, imgHambuger, imgMarker;
	private Button btnDelete, btnMailSmtp,btnFbShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.editpicture);
		pictureView = (PictureView) findViewById(R.id.imageView2);
		imgSunglass = (ImageView) findViewById(R.id.sunglass);
		imgSunglass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View av) {
				String[] paths = { "sunglass.png" };
				ImageEntity imageEntity = new ImageEntity(
						getApplicationContext(), paths, pictureDisplayWidth,
						pictureDisplayHeight);
				imageEntity.setScaleFactor(0.75);
				pictureView.addImageEntity(EditImageActivity.this, imageEntity);
				pictureView.invalidate();

			}
		});
		imgRain = (ImageView) findViewById(R.id.rain);
		imgRain.setOnClickListener(this);

		imgMarker = (ImageView) findViewById(R.id.marker);
		imgMarker.setOnClickListener(this);

		imgHambuger = (ImageView) findViewById(R.id.hamurger);
		imgHambuger.setOnClickListener(this);
		imgSun = (ImageView) findViewById(R.id.sun);
		imgSun.setOnClickListener(this);
		btnDelete = (Button) findViewById(R.id.deletebtn);
		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pictureView != null) {
					ImageEntity entity = (ImageEntity) pictureView
							.getSelectedImgEntity();
					if (entity != null) {
						pictureView.removeImageEntity(entity);
					}

				}

			}
		});
		btnMailSmtp = (Button) findViewById(R.id.mailbtn);
		btnMailSmtp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pictureView.buildDrawingCache();
				pictureView.saveImage(getApplicationContext());
				showMailDialog();

			}
		});
		/*
		 * btnAdd.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { String[]
		 * paths={"antonio_2x.png"}; ImageEntity imageEntity = new
		 * ImageEntity(getApplicationContext(), paths, pictureDisplayWidth,
		 * pictureDisplayHeight); imageEntity.setScaleFactor(0.75);
		 * pictureView.addImageEntity(EditImageActivity.this, imageEntity);
		 * pictureView.invalidate();
		 * 
		 * } });
		 */
		btnFbShare=(Button)findViewById(R.id.facebookbtn);
		btnFbShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(hasInternet(getApplicationContext()))
				{
					pictureView.buildDrawingCache();
					pictureView.saveImage(getApplicationContext());
					Intent intent=new Intent(getApplicationContext(),FacebookShareActivity.class);
					intent.putExtra("path",pictureView.getEditedImagePath());
					startActivity(intent);
				}
				
				
			}
		});
		pictureView.setDrawingCacheEnabled(true);
		calcDisplayWidthAndHeight();
		Intent intent = getIntent();
		imageUri = intent.getData();

		String imagpath = imageUri.toString().substring(7,
				imageUri.toString().length());
		Log.v("uri", imagpath);
		imagpath = imagpath.replace("%20", " ");
		// imageUri=Uri.parse("file:///storage/sdcard0/Pictures/Edit%20Image/HiHunnyAcctNumber_20140327_125536.jpg");
		if (imageUri != null) {
			loadPicture(imagpath);

		}
		// pictureView.setImageURI(imageUri);
	}

	private void loadPicture(String binarypath) {
		Bitmap bmp = null;
		Uri uri = null;
		try {
			if (picture != null) {
				picture.recycle();
				picture = null;
			}
			pictureView.setBackgroundDrawable(null);
			pictureView.setImageBitmap(null);

			// uri = Uri.withAppendedPath(
			// MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + url);
			// String binarypath = getPathFromUri((Uri) uri);

			bmp = imageUtils.decodeFileByWidthHeight(binarypath,
					pictureDisplayWidth, pictureDisplayHeight);

			// Rotate image according to orientation start
			// rotate image if 90, 180, 270 degree
			float rotation = imageUtils.rotationForImage(
					EditImageActivity.this, binarypath);
			if (rotation != 0f) {
				Matrix matrix = new Matrix();
				matrix.preRotate(rotation);
				Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0,
						bmp.getWidth(), bmp.getHeight(), matrix, true);
				if (resizedBitmap != bmp) {
					bmp.recycle();
					bmp = null;
					bmp = resizedBitmap;
					Log.i(t, "setBinaryData - rotate image " + rotation
							+ " degree successfully !");
				}
			} else {
				Log.i(t, "setBinaryData - NOT rotate image due to " + rotation
						+ " degree");
			}

			picture = bmp;
			if (picture == null) {
				Log.e(t, "Could not load picture.");

				finish();
			} else {
				displayPicture();
			}

		} catch (Exception e) {
			e.printStackTrace();
			finish();
		} catch (OutOfMemoryError outOfMemoryError) {
			// TODO: handle exception
			outOfMemoryError.printStackTrace();
			finish();
		}
	}

	protected String getPathFromUri(Uri uri) {
		// find entry in content provider
		Cursor c = getContentResolver().query(uri, null, null, null, null);
		c.moveToFirst();

		// get data path
		String colString = c.getString(c.getColumnIndex("_data"));
		c.close();
		return colString;
	}

	public String convertImageUriToFile(Uri imageUri, Activity activity) {

		Cursor cursor = null;
		int imageID = 0;

		try {

			/*********** Which columns values want to get *******/
			String[] proj = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID,
					MediaStore.Images.Thumbnails._ID,
					MediaStore.Images.ImageColumns.ORIENTATION };

			cursor = getContentResolver().query(

			imageUri, // Get data for specific image URI
					proj, // Which columns to return
					null, // WHERE clause; which rows to return (all rows)
					null, // WHERE clause selection arguments (none)
					null // Order-by clause (ascending by name)

					);

			// Get Query Data

			int columnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media._ID);

			int size = cursor.getCount();

			/******* If size is 0, there are no images on the SD Card. *****/

			if (size == 0) {
				// imageDetails.setText("No Image");
			} else {
				if (cursor.moveToFirst()) {
					/***** Used to show image on view in LoadImagesFromSDCard class ******/
					imageID = cursor.getInt(columnIndex);
				}
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return "" + imageID;
	}

	protected void calcDisplayWidthAndHeight() {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		this.displayWidth = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math
				.max(metrics.widthPixels, metrics.heightPixels) : Math.min(
				metrics.widthPixels, metrics.heightPixels);
		this.displayHeight = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math
				.min(metrics.widthPixels, metrics.heightPixels) : Math.max(
				metrics.widthPixels, metrics.heightPixels);
		Log.i("BaseActivity", "displayWidth: " + displayWidth
				+ " ; displayHeight: " + displayHeight);
		this.footerBarHeight = this.displayWidth / 6;
		this.pictureDisplayWidth = this.displayWidth;
		this.pictureDisplayHeight = (int) (this.displayHeight / 1.3);

	}

	private void displayPicture() {
		Log.d(t,
				"Screen: w " + displayWidth + " h " + displayHeight
						+ " ; Image w " + picture.getWidth() + " h "
						+ picture.getHeight());
		if (picture.getWidth() > pictureDisplayWidth
				|| pictureSource == CommonConstants.GET_PICTURE_FROM_FACEBOOK) {
			scaleFractor = imageUtils.getScaleFactor(picture.getWidth(),
					picture.getHeight(), pictureDisplayWidth,
					pictureDisplayHeight);

			picture = imageUtils
					.rescaleBitmapAspectRatio(picture, scaleFractor);
		} else {
			scaleFractor = 1;
		}

		pictureView.setEnabled(true);
		pictureView.setImageBitmap(picture);
		pictureView.invalidate();
		//

		// Set onTouch listener
		pictureView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.d(t, "onTouch");
				boolean ret = pictureView.onTouchEvent(event);

				if (!pictureView.isTouchOnEntity(event.getX(), event.getY())) {
					pictureView.unselectObject(pictureView
							.getSelectedImgEntity());
				}

				PictureView pictureView = (PictureView) v;
				ImageEntity entity = (ImageEntity) pictureView
						.getSelectedImgEntity();

				return ret;
			}
		});

		// commenting
		/*
		 * Bitmap bitmap = ((BitmapDrawable) pictureView.getDrawable())
		 * .getBitmap(); /*faces = ImageUtils.getInstance().detectFaces(bitmap);
		 */
		// showProgressDialog();
		/*
		 * faces = ImageUtils.getInstance().detectFaces( bitmap); new
		 * Handler().postDelayed(new Runnable() { public void run() {
		 * //dismissProgressDialog(); }
		 * 
		 * }, 2000);
		 * 
		 * if (faces != null && faces.size() > 0) {
		 * 
		 * int facesCount = faces.size(); // Rect rect = null; PointF midPoint =
		 * new PointF(); float eyeDistance = 0.0f; float confidence = 0.0f;
		 * float scaleX = 1 / getResources().getDisplayMetrics().density;//
		 * /getResources
		 * ().getDisplayMetrics().density;//picture.getScaledWidth(getResources
		 * ().getDisplayMetrics());//getResources().getDisplayMetrics().density;
		 * float scaleY = scaleX;// ((BitmapDrawable) //
		 * pictureView.getDrawable(
		 * )).getBitmap().getScaledHeight(getResources().getDisplayMetrics());
		 * 
		 * for (int index = 0; index < facesCount; index++) {
		 * faces.get(index).getMidPoint(midPoint); eyeDistance =
		 * faces.get(index).eyesDistance(); confidence =
		 * faces.get(index).confidence(); final Rect rect =
		 * getFaceRect(faces.get(index)); Log.i(t,
		 * "displayPicture: Confidence: " + confidence + ", Eye distance: " +
		 * eyeDistance + ", Mid Point: (" + midPoint.x + ", " + midPoint.y + ")"
		 * + ", Pose: " + faces.get(index).pose(Face.EULER_Y)); cx = scaleX *
		 * midPoint.x; cy = scaleY * midPoint.y; // Add Glasses /* Handler
		 * mHandler = new Handler(); mHandler.postDelayed(new Runnable() {
		 * public void run() {
		 */
		/*
		 * ImageEntity glasses = getGlasses2(rect, index); if (glasses != null)
		 * { glasses.setCenterXY(cx, cy); entities.add(glasses); }
		 * 
		 * cx = scaleX * midPoint.x; cy = scaleY * (midPoint.y + eyeDistance);
		 * 
		 * ImageEntity mustache; if (getRandomIndex() == 1) { mustache =
		 * getMustache2(rect, index); mustache.setCenterXY(cx, cy);
		 * entities.add(mustache); } else { mustache = getBread2(rect, index);
		 * // cy = scaleY * (midPoint.y + //
		 * mustache.getDrawable().getIntrinsicHeight()/2); // cy = cy +
		 * mustache.getDrawable().getIntrinsicHeight()/2; cy = cy + cy / 4;
		 * mustache.setCenterXY(cx, cy); entities.add(mustache); }
		 * 
		 * } faces.clear(); faces = null; }
		 * 
		 * if (entities.size() > 0) { Handler mHandler = new Handler();
		 * 
		 * mHandler.postDelayed(new Runnable() { public void run() {
		 * dropStache(); }
		 * 
		 * }, 1000);
		 * 
		 * }
		 * 
		 * /* } }, 100);
		 */

	}

	@Override
	public void onClick(View v) {
		ArrayList<String> list = new ArrayList<String>();
		if (v.getId() == R.id.marker) {
			list.add("marker.png");
		} else if (v.getId() == R.id.sun) {
			list.add("sun.png");
		} else if (v.getId() == R.id.hamurger) {
			list.add("hamburger.png");
		} else if (v.getId() == R.id.rain) {
			list.add("rain.png");
		}
		String[] paths = list.toArray(new String[list.size()]);
		ImageEntity imageEntity = new ImageEntity(getApplicationContext(),
				paths, pictureDisplayWidth, pictureDisplayHeight);
		imageEntity.setScaleFactor(0.75);
		pictureView.addImageEntity(EditImageActivity.this, imageEntity);
		pictureView.invalidate();

	}

	private void showMailDialog() {
		final Dialog dialog = new Dialog(EditImageActivity.this);
		dialog.setContentView(R.layout.dialog_send_mail);
		Button btn = (Button) dialog.findViewById(R.id.buttonsendmail);
		dialog.show();
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//String fromEmail = ((EditText) dialog
					//	.findViewById(R.id.fromMail)).getText().toString();
				//String fromPassword = ((EditText) dialog
					//	.findViewById(R.id.password)).getText().toString();
				String toEmails = ((EditText) dialog.findViewById(R.id.Tomail))
					.getText().toString();
				List<String> toEmailList = Arrays.asList(toEmails
					.split("\\s*,\\s*"));
				// test

				Log.i("SendMailActivity", "To List: " + toEmailList);
				// String emailSubject = "Mail With Image";
				// String emailBody = "";
				Log.v("here path", pictureView.getEditedImagePath());
				new SendMailTask(EditImageActivity.this).execute(
						PreferenceConnector.readString(getApplicationContext(),
								PreferenceConnector.EMAIL_USERNAME, ""),
						PreferenceConnector.readString(getApplicationContext(),
								PreferenceConnector.EMAIL_PASS, ""),
						toEmailList, PreferenceConnector.readString(
								getApplicationContext(),
								PreferenceConnector.EMAIL_SUBJECT, ""),
						PreferenceConnector.readString(getApplicationContext(),
								PreferenceConnector.EMAIL_BODY, ""),
						pictureView.getEditedImagePath(), PreferenceConnector
								.readString(getApplicationContext(),
										PreferenceConnector.SMTP, ""),
						PreferenceConnector.readString(getApplicationContext(),
								PreferenceConnector.PORT_NUMBER, ""));
				dialog.cancel();

			}
		});
	}

	@Override
	protected void onDestroy() {
		pictureView.buildDrawingCache();
		pictureView.saveImage(getApplicationContext());
		super.onDestroy();
	}
	 public static boolean hasInternet(Context context) {
	        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        if (connectivity != null){
	            NetworkInfo[] info = connectivity.getAllNetworkInfo();
	            if (info != null){
	                for (int i = 0; i < info.length; i++){
	                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
	                        return true;
	                    }
	                }
	            }
	        }
	        return false;
	    }
	

}
