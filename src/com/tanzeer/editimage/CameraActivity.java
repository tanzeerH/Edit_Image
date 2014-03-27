package com.tanzeer.editimage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.tanzeer.editimage.fragment.CameraFragment;
import com.tanzeer.editimage.listener.CameraFragmentListener;

@SuppressLint("NewApi")
public class CameraActivity extends Activity implements CameraFragmentListener {
	public static final String TAG = "Mustache/CameraActivity";
	TextView textView1;
	// private ImageView mOverLayImage;

	Dialog overlayInfo;

	private static final int PICTURE_QUALITY = 90;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_camera);

		overlayInfo = new Dialog(CameraActivity.this);
		overlayInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
		overlayInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		overlayInfo.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		overlayInfo.setContentView(R.layout.overlay);
		overlayInfo.show();

		// mOverLayImage = (ImageView)
		// overlayInfo.findViewById(R.id.over_lay_image);
		textView1 = (TextView) overlayInfo.findViewById(R.id.timer_text);

		startTimer();
	}

	/**
	 * On fragment notifying about a non-recoverable problem with the camera.
	 */
	@Override
	public void onCameraError() {
		Toast.makeText(this, getString(R.string.toast_error_camera_preview), Toast.LENGTH_SHORT).show();

		finish();
	}

	/**
	 * The user wants to take a picture.
	 * 
	 * @param view
	 */
	public void takePicture() {
		// view.setEnabled(false);

		CameraFragment fragment = (CameraFragment) getFragmentManager().findFragmentById(R.id.camera_fragment);

		fragment.takePicture();
	}

	/**
	 * A picture has been taken.
	 */
	public void onPictureTaken(Bitmap bitmap) {
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				getString(R.string.app_name));

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				showSavingPictureErrorToast();
				return;
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "HiHunnyAcctNumber_" + timeStamp + ".jpg");

		try {
			FileOutputStream stream = new FileOutputStream(mediaFile);
			bitmap.compress(CompressFormat.JPEG, PICTURE_QUALITY, stream);
		} catch (IOException exception) {
			showSavingPictureErrorToast();

			Log.w(TAG, "IOException during saving bitmap", exception);
			return;
		}

		MediaScannerConnection.scanFile(this, new String[] { mediaFile.toString() }, new String[] { "image/jpeg" },
				null);

		Intent intent = new Intent(this,EditImageActivity.class);
		intent.setData(Uri.fromFile(mediaFile));
		startActivity(intent);
//
//		finish();
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
//		pictureFileName = Environment.getExternalStorageDirectory() + "/"
//				+ getResources().getString(R.string.app_name) + "/" + sdf.format(new Date())
//				+ CommonConstants.PICTURE_FILE_EXT;

		
//		/****************************************************/
//		File fi = new File(mediaFile.toString());
//		ContentValues values = new ContentValues(6);
//		values.put(Images.Media.TITLE, fi.getName());
//		values.put(Images.Media.DISPLAY_NAME, fi.getName());
//		values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
//		values.put(Images.Media.MIME_TYPE, "image/jpeg");
//		values.put(Images.Media.DATA, fi.getAbsolutePath());
//
//		Uri imageuri = getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
//		Log.i("Taken image uri", "-----> " + imageuri);
//
//		Intent i = new Intent(getApplicationContext(), PictureViewActivity.class);
//
//		i.putExtra("PICTURE_SOURCE", 2);
//		i.setData(imageuri);
//		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//		startActivity(i);
//		
//		
//		/****************************************************/
		
		
//		Log.i("Loaded image uri", "----->" + Uri.fromFile(mediaFile));
//		Intent i = new Intent(getApplicationContext(), PictureViewActivity.class);
//		i.putExtra("PICTURE_SOURCE", 1);
//		i.setData(Uri.fromFile(mediaFile));
////		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//		startActivity(i);
		
		
	}

	private void showSavingPictureErrorToast() {
		Toast.makeText(this, getText(R.string.toast_error_save_picture), Toast.LENGTH_SHORT).show();
	}

	public void startTimer() {
		// 5000ms=5s at intervals of 1000ms=1s so that means it lasts 5 seconds
		new CountDownTimer(5000, 1000) {

			@Override
			public void onFinish() {
				// count finished
				overlayInfo.cancel();
				textView1.setVisibility(View.GONE);

				takePicture();

				// camera.takePicture(shutterCallback, rawCallback,
				// jpegCallback);
			}

			@Override
			public void onTick(long millisUntilFinished) {
				// every time 1 second passes
				textView1.setText("" + millisUntilFinished / 1000);
			}

		}.start();
	}

}
