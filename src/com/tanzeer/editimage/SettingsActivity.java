package com.tanzeer.editimage;

import com.tanzeer.editimage.utils.PreferenceConnector;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener{
  private 	EditText edtSmtp,edtPort,edtUserName,edtPassword,edtMailSubject,edtMailBody;
  private Button btnSave;
  private Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.emailsettings);
		super.onCreate(savedInstanceState);
		edtSmtp=(EditText)findViewById(R.id.editTextsmtp);
		edtPort=(EditText)findViewById(R.id.editTextport);
		edtUserName=(EditText)findViewById(R.id.editTextuser);
		edtPassword=(EditText)findViewById(R.id.editTextpassword);
		edtMailSubject=(EditText)findViewById(R.id.editTextSubject);
		edtMailBody=(EditText)findViewById(R.id.editTextBody);
		btnSave=(Button)findViewById(R.id.buttonSave);
		btnSave.setOnClickListener(this);
		setEditTexts();
	}
	@Override
	public void onClick(View v) {
		
		
		if(v.getId()==R.id.buttonSave)
		{
			
			//editor=PreferenceConnector.getEditor(getApplicationContext());
			PreferenceConnector.writeString(getApplicationContext(),PreferenceConnector.SMTP,edtSmtp.getText().toString());
			PreferenceConnector.writeString(getApplicationContext(),PreferenceConnector.PORT_NUMBER,edtPort.getText().toString());
			PreferenceConnector.writeString(getApplicationContext(),PreferenceConnector.EMAIL_USERNAME,edtUserName.getText().toString());
			PreferenceConnector.writeString(getApplicationContext(),PreferenceConnector.EMAIL_PASS,edtPassword.getText().toString());
			PreferenceConnector.writeString(getApplicationContext(),PreferenceConnector.EMAIL_SUBJECT,edtMailSubject.getText().toString());
			PreferenceConnector.writeString(getApplicationContext(),PreferenceConnector.EMAIL_BODY,edtMailBody.getText().toString());
			toast("update saved");
			//editor.commit();
		}
		
		
	}
	public void setEditTexts()
	{
		edtSmtp.setText(PreferenceConnector.readString(getApplicationContext(),PreferenceConnector.SMTP,""));
		edtPort.setText(PreferenceConnector.readString(getApplicationContext(),PreferenceConnector.PORT_NUMBER,""));
		edtUserName.setText(PreferenceConnector.readString(getApplicationContext(),PreferenceConnector.EMAIL_USERNAME,""));
		edtPassword.setText(PreferenceConnector.readString(getApplicationContext(),PreferenceConnector.EMAIL_PASS,""));
		edtMailSubject.setText(PreferenceConnector.readString(getApplicationContext(),PreferenceConnector.EMAIL_SUBJECT,""));
		edtMailBody.setText(PreferenceConnector.readString(getApplicationContext(),PreferenceConnector.EMAIL_BODY,""));
		
		
	}
	private void toast(String str)
	{
		Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();
	}
}
