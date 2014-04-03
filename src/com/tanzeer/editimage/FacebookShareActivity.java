package com.tanzeer.editimage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class FacebookShareActivity extends Activity implements OnClickListener {

	private static final String MY_PHOTOS = "me/photos";

	ImageView imgView;
	ImageButton btnshare;
	File file;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fbshare);
		imgView = (ImageView) findViewById(R.id.imageView1);
		btnshare = (ImageButton) findViewById(R.id.imageButton1);
		btnshare.setOnClickListener(this);
		
		Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		file = new File(path);
		if (file.exists()) {
			Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			imgView.setImageBitmap(myBitmap);
		}
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		
		Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }
        Session.NewPermissionsRequest newPermissionsRequest=new Session.NewPermissionsRequest(this,Arrays.asList("friends_birthday"));
    	session.requestNewReadPermissions(newPermissionsRequest);

	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (session.isOpened()) {
				toast("session opened");
			}
			// updateViwes();
			UpdateUI();
		}
	}

	public void UpdateUI() {
		Session session = Session.getActiveSession();
		if (session.isOpened()) {
			btnshare.setVisibility(View.VISIBLE);
		} else
		{
			btnshare.setVisibility(View.GONE);
			onClickLogin();
		}
		// getStatus();

	}

	private void toast(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
	}

	private void onClickLogin() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForPublish(new Session.OpenRequest(this)
					.setCallback(statusCallback));
		} else {
			Session.openActiveSession(this, true, statusCallback);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	@Override
	public void onClick(View v) {
		try {
			Request request=Request.newUploadPhotoRequest(Session.getActiveSession(), file, new Request.Callback() {
				
				@Override
				public void onCompleted(Response response) {
					Log.v("response",response.toString());
					toast("Photo shared");
				
				}
			});
			request.executeAsync();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
