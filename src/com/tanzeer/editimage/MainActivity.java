package com.tanzeer.editimage;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	private Button btnCamera,btnSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnCamera = (Button) findViewById(R.id.btncamera);
		btnSettings=(Button)findViewById(R.id.btnsettings);
		btnSettings.setOnClickListener(this);
		btnCamera.setOnClickListener(this);
		printCameraData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v.getId()== R.id.btncamera) {
			
			Intent intent=new Intent(getApplicationContext(),CameraActivity.class);
			startActivity(intent);

		}
		else if(v.getId()==R.id.btnsettings)
		{
			Intent intent=new Intent(getApplicationContext(),SettingsActivity.class);
			startActivity(intent);
			
		}
	}
	public void  printCameraData()
	{
		int cameraCount=0;
		Camera cam=null;
		Camera.CameraInfo camInfo=new Camera.CameraInfo();
		cameraCount=Camera.getNumberOfCameras();
		for(int i=0;i<cameraCount;i++){
			Camera.getCameraInfo(i, camInfo);
			if(camInfo.facing==Camera.CameraInfo.CAMERA_FACING_FRONT)
				Log.v("camera_id",""+i);
		}
		
	}

}
