package com.example.eladblau.app100;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;


public class SettingActivity extends Activity {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private Button buttonUpdate;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Model model;
    private SharedPreferences sharedPreferencesFile;
    private SharedPreferences.Editor editor;
    private ProgressDialog dialogUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedPreferencesFile = getApplicationContext()
                .getSharedPreferences(config.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferencesFile.edit();

        //Get all the view elements
        editTextFirstName = (EditText) findViewById(R.id.editText_Name);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        buttonUpdate = (Button) findViewById(R.id.button_Update);

        //Get the login data if exist from the SharedPreferences file
        firstName = sharedPreferencesFile.getString(config.SHARED_PREFERENCES_KEY_USER_FIRST_NAME, null);
        lastName = sharedPreferencesFile.getString(config.SHARED_PREFERENCES_KEY_USER_LAST_NAME, null);
        phoneNumber = sharedPreferencesFile.getString(config.SHARED_PREFERENCES_KEY_PHONE_NUMBER, null);

        Log.d("EB","first name = "+firstName);
        Log.d("EB","last name = "+lastName);

        //Set the data in the edit texts
        editTextFirstName.setText(firstName);
        editTextLastName.setText(lastName);

        model = Model.getInstance(this);
        // Set up a progress dialog
        dialogUpdate = new ProgressDialog(this);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstName = editTextFirstName.getText().toString();
                lastName = editTextLastName.getText().toString();

                //Show the Progress dialog
                dialogUpdate.setMessage(config.UPDATING_MESSAGE);
                dialogUpdate.show();
                //check if this user exists in the db
                model.updateUserDetails(firstName, lastName, phoneNumber, new Model.updateCallBack() {

                    @Override
                    public void doneUpdate(boolean succeed, String status) {
                        if(!succeed){
                            Log.d("EB", "Update succeed = "+succeed);
                            Log.d("EB", "Update status = "+status);
                            dialogUpdate.dismiss();
                            Toast.makeText(SettingActivity.this, status, Toast.LENGTH_LONG).show();
                        }
                        else{
                            Log.d("EB", "Update succeed = "+succeed);
                            Log.d("EB", "Update status = "+status);
                            editor.putString(config.SHARED_PREFERENCES_KEY_USER_FIRST_NAME, firstName);
                            editor.putString(config.SHARED_PREFERENCES_KEY_USER_LAST_NAME, lastName);
                            editor.commit();
                            Toast.makeText(SettingActivity.this, status, Toast.LENGTH_LONG).show();
                            dialogUpdate.dismiss();
                            finish();
                        }
                    }
                });
            }


        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

}

