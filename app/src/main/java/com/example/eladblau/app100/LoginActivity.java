package com.example.eladblau.app100;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
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


public class LoginActivity extends Activity {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextPhoneNumber;
    private Button buttonLogIn;
    private Button buttonSignUp;

    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Model model;
    private SharedPreferences sharedPreferencesFile;
    private SharedPreferences.Editor editor;
    private ProgressDialog dialogLogin;
    private AlertDialog alertDialog;
    private boolean isUserExist;
    private String uuid; //Universally unique identifier


    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get the phone number from the device
        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        Log.d("EB", "Phone number = "+mPhoneNumber);
        if(mPhoneNumber.equals("")){
            mPhoneNumber = tMgr.getSimSerialNumber();
            Log.d("EB", "Phone number = "+mPhoneNumber);
        }


        //Toast.makeText(this,mPhoneNumber,Toast.LENGTH_LONG).show();

        sharedPreferencesFile = getApplicationContext()
                .getSharedPreferences(config.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferencesFile.edit();

        //Get all the view elements
        editTextFirstName = (EditText) findViewById(R.id.editText_Name);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextPhoneNumber = (EditText) findViewById(R.id.editText_PhoneNumber);
        buttonLogIn = (Button) findViewById(R.id.button_LogIn);
        buttonSignUp = (Button) findViewById(R.id.button_SignUp);

        //Get the login data if exist from the SharedPreferences file
        firstName = sharedPreferencesFile.getString(config.SHARED_PREFERENCES_KEY_USER_FIRST_NAME, null);
        lastName = sharedPreferencesFile.getString(config.SHARED_PREFERENCES_KEY_USER_LAST_NAME, null);
        phoneNumber = sharedPreferencesFile.getString(config.SHARED_PREFERENCES_KEY_PHONE_NUMBER, null);

        Log.d("EB","first name = "+firstName);
        Log.d("EB","last name = "+lastName);
        Log.d("EB","phone number = "+phoneNumber);

        //Set the data in the edit texts
        editTextFirstName.setText(firstName);
        editTextLastName.setText(lastName);
        editTextPhoneNumber.setText(phoneNumber);

        model = Model.getInstance(this);
        // Set up a progress dialog
        dialogLogin = new ProgressDialog(this);

        //Set sign up scenario
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneNumber = editTextPhoneNumber.getText().toString();
                //Send to validation first
                phoneNumber = validatePhoneNumber(phoneNumber);
                if(phoneNumber.length() != 13){
                    Toast.makeText(LoginActivity.this,
                            phoneNumber, Toast.LENGTH_LONG).show();
                    return;
                }
                firstName = editTextFirstName.getText().toString();
                lastName = editTextLastName.getText().toString();

                //Initializing
                isUserExist = false;


                alertDialog = new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Attention")
                        .setMessage("This action will send SMS from your device " +
                                "to "+phoneNumber+" to validate your phone number and it can cause a charge.\n\n" +
                                "Please approve this action.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Show the Progress dialog
                                dialogLogin.setMessage(config.SIGN_UP);
                                dialogLogin.show();
                                //First, check if the user is already exists in the db
                                model.checkIfUserExists(firstName, lastName, phoneNumber, new Model.CheckIfUserExistsCallBack() {
                                    @Override
                                    public void doneCheckingUser(boolean userExists) {
                                        isUserExist = userExists;
                                        if(userExists){
                                            Log.d("EB", "isUserExist = "+isUserExist);
                                            dialogLogin.dismiss();
                                            Toast.makeText(LoginActivity.this,
                                                    "This phone number is already registered.", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        else {
                                            //Generate a verification code
                                            uuid = UUID.randomUUID().toString();
                                            Log.d("EB","uuid = "+ uuid);
                                            //Send SMS to the number the user insert to the edit text
                                            SmsManager sm = SmsManager.getDefault();
                                            sm.sendTextMessage(editTextPhoneNumber.getText().toString(),
                                                    null, "Dear "+firstName+" "+lastName+",\n"+
                                                            config.SIGN_UP_BODY_MESSAGE + uuid, null, null);
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstName = editTextFirstName.getText().toString();
                lastName = editTextLastName.getText().toString();
                phoneNumber = editTextPhoneNumber.getText().toString();
                //Send to validation first
                phoneNumber = validatePhoneNumber(phoneNumber);
                if(phoneNumber.length() != 13){
                    Toast.makeText(LoginActivity.this,
                            phoneNumber, Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("EB", "firstName = "+firstName+"; lastName = "+lastName);


                //Show the Progress dialog
                dialogLogin.setMessage(config.LOGIN);
                dialogLogin.show();
                //check if this user exists in the db
                model.login(firstName, lastName, phoneNumber, new Model.loginCallBack() {
                    @Override
                    public void doneLogin(boolean isExists, String status) {
                        if(isExists){
                            //Update the sharedPreferences file
                            editor.putString(config.SHARED_PREFERENCES_KEY_USER_FIRST_NAME, firstName);
                            editor.putString(config.SHARED_PREFERENCES_KEY_USER_LAST_NAME, lastName);
                            editor.putString(config.SHARED_PREFERENCES_KEY_PHONE_NUMBER, phoneNumber);
                            editor.putBoolean(config.SHARED_PREFERENCES_KEY_IS_LOGGED_IN, true);
                            editor.commit();

                            //Dismiss dialog
                            dialogLogin.dismiss();

                            //Open the main app activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                            Log.d("EB", "intent = "+intent.toString());
                            startActivity(intent);
                            finish();

                        }
                        else{
                            //Dismiss dialog
                            dialogLogin.dismiss();
                            //Show the cause
                            Toast.makeText(LoginActivity.this, status, Toast.LENGTH_LONG).show();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(
                "android.provider.Telephony.SMS_RECEIVED");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();

                if (extras == null)
                    return;

                Object[] pdus = (Object[]) extras.get("pdus");
                SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdus[0]);
                String origNumber = msg.getOriginatingAddress();
                String msgBody = msg.getMessageBody();
                String verificationCode = "";

                //Get the verification code from the message
                if(msgBody.length()>36){
                    //Get the last 36 characters - this is the UUID
                    verificationCode = msgBody.substring(msgBody.length()-36);
                    Log.d("EB", "OnReceive verificationCode = "+verificationCode);
                }

                Log.d("EB", "msgBody = "+msgBody);
                Log.d("EB", "phoneNumber = "+phoneNumber);
                Log.d("EB", "origNumber = "+origNumber);
                //Check if the phone numbers are equals
                if(phoneNumber.equals(origNumber) && uuid.equals(verificationCode)){
                    //TODO: Set in Database the details
                    model.addNewUser(firstName, lastName, phoneNumber, new Model.SignUpCallBack() {
                        @Override
                        public void doneSignUp(boolean signedUp) {
                            if(signedUp){
                                //Update the details in the SharedPreferences file
                                editor.putString(config.SHARED_PREFERENCES_KEY_USER_FIRST_NAME, firstName);
                                editor.putString(config.SHARED_PREFERENCES_KEY_USER_LAST_NAME, lastName);
                                editor.putString(config.SHARED_PREFERENCES_KEY_PHONE_NUMBER, phoneNumber);
                                editor.putBoolean(config.SHARED_PREFERENCES_KEY_IS_LOGGED_IN, true);
                                editor.commit();

                                //Dismiss dialog
                                dialogLogin.dismiss();

                                //Go to the application
                                Intent intent1 = new Intent(LoginActivity.this, MainActivity2.class);
                                startActivity(intent1);
                                //finish this activity
                                finish();
                            }
                            else{
                                //Dismiss dialog
                                dialogLogin.dismiss();
                                //This phone number is already exists
                                Toast.makeText(LoginActivity.this, config.SIGN_UP_FAILD_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else if (uuid.equals(verificationCode)){
                    Toast.makeText(LoginActivity.this, "Phone number is not the same.", Toast.LENGTH_SHORT).show();
                }
                else {
                    //The verificationCode is not equals
                    Toast.makeText(LoginActivity.this, "Problem with the verification code.\nPlease try later.", Toast.LENGTH_SHORT).show();
                }


            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //unregister our receiver
        this.unregisterReceiver(this.mReceiver);
    }


    private String validatePhoneNumber(String phoneNumber){
        String phoneNumberAfterValidation = null;

        Log.d("EB","isGlobalPhoneNumber = "+PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber));

                //delete all chars which aren't digits ("+", letters, "-", etc.)
        phoneNumber = phoneNumber.replaceAll("\\D+","");

        if(phoneNumber.length() < 10){
            //Phone number is too short
            return "The Phone number is too short";
        }
        //e.g. 0545665444
        if(phoneNumber.startsWith("0") && phoneNumber.length() == 10){
            phoneNumber = "+972"+phoneNumber.substring(1);
        }
        //e.g. 972545665444
        if(phoneNumber.startsWith("9") && phoneNumber.length() == 12){
            phoneNumber = "+"+phoneNumber;
        }
        //e.g. 97254566544
        if(phoneNumber.startsWith("9") && phoneNumber.length() < 12){
            //Phone number is not valid
            return "The Phone number is not valid";
        }
        if(phoneNumber.length() >= 13 && !phoneNumber.startsWith("+")){
            return "The Phone number is too long";
        }
        phoneNumberAfterValidation = phoneNumber;
        return phoneNumberAfterValidation;

    }
}

