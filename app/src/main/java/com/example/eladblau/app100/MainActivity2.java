package com.example.eladblau.app100;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;


public class MainActivity2 extends Activity {



    private SharedPreferences sharedPreferencesFile;
    private Button report_button;
    private Button call_button;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);


        //Check if the user logged in
        checkIfTheUserLoggedIn();


        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        report_button = (Button) findViewById(R.id.button_report);
        call_button = (Button) findViewById(R.id.button_call);

        //Open the events reporting Activity when clicking on the button
        report_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent report_intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(report_intent);
                //finish();
            }
        });


        //Call the Police when clicking on the button
        call_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String tel = "tel:" + config.DISPACH_NUMBER;
                callIntent.setData(Uri.parse(tel));
                startActivity(callIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
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
/*            case R.id.action_language:
                Log.d("EB","language changing");
                String language_code = "he";
                Resources res = this.getResources();
                // Change locale settings in the app.
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.locale = new Locale(language_code.toLowerCase());
                res.updateConfiguration(conf, dm);
                break;*/
            case R.id.action_settings:
                Intent settingIntent = new Intent(this, SettingActivity.class);
                startActivity(settingIntent);
                //finish();
                break;
            case R.id.action_call:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String tel = "tel:" + config.DISPACH_NUMBER;
                callIntent.setData(Uri.parse(tel));
                startActivity(callIntent);
                //finish();
                break;
            case R.id.action_logout:
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle(config.LOGOUT)
                        .setMessage(config.LOGOUT_ALERT_MESSAGE)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Get the sharedPreferences file
                                sharedPreferencesFile = getSharedPreferences(config.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferencesFile.edit();
                                //Set that the user is logged out
                                editor.putBoolean(config.SHARED_PREFERENCES_KEY_IS_LOGGED_IN, false);
                                editor.commit();
                                //God bye message
                                Toast.makeText(MainActivity2.this, "Good Bye", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfTheUserLoggedIn();
    }

    public void checkIfTheUserLoggedIn() {
        //Get the SharedPreferences file
        sharedPreferencesFile = getApplicationContext()
                .getSharedPreferences(config.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesFile.edit();
        //Check if the user is logged - if the key isn't exist return FALSE
        boolean isLoggedIn = sharedPreferencesFile.getBoolean(config.SHARED_PREFERENCES_KEY_IS_LOGGED_IN, false);
        Log.d("EB", "isLoggedIn = " + isLoggedIn);
        if (!isLoggedIn) {
            //The user should log in and then he can use the app
            //TODO: Create Log in section
            Intent logInIntent = new Intent(MainActivity2.this, LoginActivity.class);
            startActivity(logInIntent);
            finish();
        }
    }
}
