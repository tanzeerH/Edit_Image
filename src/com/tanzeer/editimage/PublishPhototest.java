package com.tanzeer.editimage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.Session.OpenRequest;
import com.facebook.android.AsyncFacebookRunner;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PublishPhototest extends Activity implements OnClickListener{
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	Button  btnstatus,btnsend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.photosgareingtest);
		super.onCreate(savedInstanceState);
		btnstatus=(Button)findViewById(R.id.buttonstatus);
		btnsend=(Button)findViewById(R.id.buttonpublish);
		btnsend.setOnClickListener(this);
		btnstatus.setOnClickListener(this);
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
		// updateViwes();
		UpdateUI();
		//getStatus();
	}

	public void getStatus() {
		Session session=Session.getActiveSession();
		
		if(!session.isClosed() && !session.isClosed())
		{
			OpenRequest openRequest = new Session.OpenRequest(this);
		openRequest
				.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
		openRequest.setPermissions("friends_birthday,publish_actions");
		session.openForRead(openRequest.setCallback(statusCallback));
	} else {
		Session.openActiveSession(this, true, statusCallback);
	}

	}
	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
				if(session.isOpened())
				{
					toast("opened");
				}
			// updateViwes();
			UpdateUI();
		}
	}
	public void UpdateUI()
	{
		Session session=Session.getActiveSession();
		if(session.isOpened())
		{
			btnsend.setText("Logged in");
		}
		else
			btnsend.setText("Logged out");
		//getStatus();
		
	}
	private void toast(String str)
	{
		Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();
	}
	 private void onClickLogin() {
	        Session session = Session.getActiveSession();
	        if (!session.isOpened() && !session.isClosed()) {
	            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
	        } else {
	            Session.openActiveSession(this, true, statusCallback);
	        }
	    }
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.buttonpublish)
		{
			//Bundle params = new Bundle();
			//params.putString("name", "My Test Album Name Here");
			//params.putString("message", "My Test Album Description Here");
			/*File file=new File("rain.png");
			try {
				Request request=Request.newUploadPhotoRequest(Session.getActiveSession(), file, new Request.Callback() {
					
					@Override
					public void onCompleted(Response response) {
						Log.v("response",response.toString());
					
					}
				});
				Bundle params = request.getParameters();
				params.putString("message", "description  goes here");
				request.executeAsync();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent,2);
			
			
		}
		if(v.getId()==R.id.buttonstatus)
			onClickLogin();
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
		if(requestCode==2)
		{
		Uri uri=data.getData();
		File file=new File(getpath(uri));
		try {
			Request request=Request.newUploadPhotoRequest(Session.getActiveSession(), file, new Request.Callback() {
				
				@Override
				public void onCompleted(Response response) {
					Log.v("response",response.toString());
				
				}
			});
			request.executeAsync();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
		}
	}
	public String getpath(Uri uri)
	{
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
		.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return  cursor.getString(column_index);
		
	}

	@Override
	public void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
	}

	@Override
	public void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}


}
