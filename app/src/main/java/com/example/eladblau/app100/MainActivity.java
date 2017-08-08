package com.example.eladblau.app100;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.example.eladblau.app100.objects.CarAccidentEventObject;
import com.example.eladblau.app100.objects.CarTheftEventObject;
import com.example.eladblau.app100.objects.DomesticViolenceObject;
import com.example.eladblau.app100.objects.OtherEventObject;

import static com.example.eladblau.app100.config.EVENT_CODE_ACCIDENT;

public class MainActivity extends Activity implements EventsFragment.OnFragmentInteractionListener,
        LocationListener, EventOther.OnFragmentInteractionListener, EventsFragment_2.OnFragmentInteractionListener,
        EventCarAccidentFragment.OnFragmentInteractionListener, EventDomesticViolenceFragment.OnFragmentInteractionListener,
        EventCarTheftFragment.OnFragmentInteractionListener{


    EventsFragment myEventsFragment;
    FragmentManager manager;
    FragmentTransaction transaction;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private Location location;
    protected boolean gps_enabled,network_enabled;
    protected GPSTracker mGPS;
    private String curr_language = "en";
    private String address_from_geoCoder;
    private ProgressDialog dialogAddingNewEvent;
    private Model model;
    private int currentFragmentCode;
    private boolean isInternetPresent;
    private ConnectionDetector cd;
    private SharedPreferences sharedPreferencesFile;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private ActionBar actionBar;
    private boolean tabVisible;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public static boolean deviceHasCamera;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tabVisible = false;

        //Get an access to the Model
        model = Model.getInstance(MainActivity.this);
        // Set up a progress dialog
        dialogAddingNewEvent = new ProgressDialog(this);

        //Check if there is an internet connection
        isInternetPresent = false;
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();


        //Get the user details
        sharedPreferencesFile = getSharedPreferences(config.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        firstName = sharedPreferencesFile.getString(config.SHARED_PREFERENCES_KEY_USER_FIRST_NAME, "NULL");
        lastName = sharedPreferencesFile.getString(config.SHARED_PREFERENCES_KEY_USER_LAST_NAME, "NULL");
        phoneNumber = sharedPreferencesFile.getString(config.SHARED_PREFERENCES_KEY_PHONE_NUMBER, "NULL");

        //Check if the device has camera
        Context context =  this;
        PackageManager packageManager = context.getPackageManager();
         /*
        FEATURE_CAMERA_FRONT (checks for a FRONT facing camera)
        FEATURE_CAMERA (checks for a REAR facing camera)
        FEATURE_CAMERA_ANY (checks for ANY camera)
        */
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
            deviceHasCamera = false;
            //Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT) .show();
        }
        else{
            deviceHasCamera = true;
        }
        Log.d("EB","deviceHasCamera = "+deviceHasCamera);

        // *********************** Location *********************

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                GPSTracker.MIN_TIME_BW_UPDATES, GPSTracker.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                GPSTracker.MIN_TIME_BW_UPDATES, GPSTracker.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        mGPS = new GPSTracker(this);
        if(mGPS.canGetLocation ){
            location = mGPS.getLocation();
            Log.d("EB1", "Latitude: " + mGPS.getLatitude()+ " Longitude: " +mGPS.getLongitude());

                    //Convert the LatLng to an address
            if (Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent()) {
                Log.d("EB", "Geocoder is running");
            /*
             * Reverse geocoding is long-running and synchronous.
             * Run it on a background thread.
             * Pass the current location to the background task.
             * When the task finishes,
             * onPostExecute() displays the address.
             */
                //(new GetAddressTask(this)).execute(location);
                GetAddressTask getAddressTask = new GetAddressTask(this);
                if(location != null){
                    getAddressTask.execute(location);
                }
            }

        }else{
            mGPS.showSettingsAlert();
            Log.d("EB", "Can't get location");
        }


        //Load the events fragments
        myEventsFragment = new EventsFragment();
        changeFragment(myEventsFragment, false, config.EVENTS_FRAGMENT_EMERGENCY_CODE);

    }

    public void changeFragment(Fragment fragment, boolean addToStack, int fragmentCode){


        Log.d("EB", "Change fragment: "+fragment.toString()+" addToStack: "+addToStack);
        //Send location to the fragment
        Bundle bundle = new Bundle();
        bundle.putCharSequence("location",address_from_geoCoder);
        bundle.putCharSequence("latLng", mGPS.getLatitude()+"."+mGPS.getLongitude());
        fragment.setArguments(bundle);

        manager = getFragmentManager();
        //3. Set the transaction object by the manager
        transaction = manager.beginTransaction();
        //4. perform the transactions
        transaction.replace(R.id.fragment_container, fragment);
        //Add to back stack if true
        if(addToStack){
            Log.d("EB","addToBack: "+addToStack);
            transaction.addToBackStack(null);//- add transactions to backstack
        }
        //5. commit the transactions
        transaction.commit();
        //Update the currentFragmentCode
        currentFragmentCode = fragmentCode;

        Log.d("EB","currentFragmentCode = " +currentFragmentCode);
        Log.d("EB","tabVisable = " +tabVisible);
        //Consider to add tabs
        //Show tabs only when the events list is shown
        if(!tabVisible &&
                (currentFragmentCode == config.EVENTS_FRAGMENT_EMERGENCY_CODE
                        || currentFragmentCode == config.EVENTS_FRAGMENT_LESS_EMERGENCY_CODE)){
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new Fragment1()).commit();
            actionBar.addTab(actionBar.newTab().setText(config.EVENTS_EMERGENCY)
                    .setTabListener(new MyTabListener(new EventsFragment())));
            actionBar.addTab(actionBar.newTab().setText(config.EVENTS_LESS_EMERGENCY)
                    .setTabListener(new MyTabListener(new EventsFragment_2())));
            tabVisible = true;
        }
        else{
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.removeAllTabs();
            tabVisible = false;
        }

        //optional:
        //manager.executePendingTransactions(); - to excecute immidiatly (it isnt always the case and might cause null pointers)


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d("EB", "Menu item: "+ item.getItemId());
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {

            //TO DO - Taking care Up navigation in fragment
            case android.R.id.home: //Up navigation
                if (currentFragmentCode != config.EVENTS_FRAGMENT_EMERGENCY_CODE) {
                    changeFragment(new EventsFragment(), false, config.EVENTS_FRAGMENT_EMERGENCY_CODE);

                } else {
                    finish();
                }
                break;
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

/*            case R.id.action_language:

                Locale locale = getResources().getConfiguration().locale;
                Log.d("EB", "locale: " + locale.toString());
                if (curr_language == "en") {
                    Log.d("EB", "Language English to Hebrew: " + locale.toString());
                    Toast.makeText(this, "Locale in Hebrew !", Toast.LENGTH_SHORT).show();
                    setLocale("he");
                    curr_language = "he";
                    break;
                } else if (curr_language == "he") {
                    Toast.makeText(this, "Locale in English !", Toast.LENGTH_SHORT).show();
                    setLocale("en");
                    curr_language = "en";
                    break;
                }*/
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
                                Toast.makeText(MainActivity.this, "Good Bye", Toast.LENGTH_LONG).show();
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


    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSendButtonClicked(CarTheftEventObject carTheftEventObject) {
        carTheftEventObject.setUser_first_name(firstName);
        carTheftEventObject.setUser_last_name(lastName);
        carTheftEventObject.setUser_phone_number(phoneNumber);
        final CarTheftEventObject carTheftEventObject_temp = carTheftEventObject;
            //Show the Progress dialog
        dialogAddingNewEvent.setMessage(config.SENDING_REPORT_MESSAGE);
        dialogAddingNewEvent.show();
        //Update the Event id
        model.getLastID(new Model.GetLastIdClbck() {
            @Override
            public void done(int last_Id) {

                carTheftEventObject_temp.set_id(last_Id);
                Log.d("EB", "Last ID = " + last_Id);

                //Add the event to cloud
                model.addNewCarTheftEvent(carTheftEventObject_temp, new Model.AddNewCarTheftEventClbck() {
                    @Override
                    public void doneAddNewCarTheftClbak(boolean succeed) {
                        dialogAddingNewEvent.dismiss();
                        Toast.makeText(getApplicationContext(), config.REPORT_SENT_MESSAGE, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onSendButtonClicked(final DomesticViolenceObject domesticViolenceObject) {
        domesticViolenceObject.setUser_first_name(firstName);
        domesticViolenceObject.setUser_last_name(lastName);
        domesticViolenceObject.setUser_phone_number(phoneNumber);     //Show the Progress dialog
        dialogAddingNewEvent.setMessage(config.SENDING_REPORT_MESSAGE);
        dialogAddingNewEvent.show();
        //Update the Event id
        model.getLastID(new Model.GetLastIdClbck() {
            @Override
            public void done(int last_Id) {
                domesticViolenceObject.set_id(last_Id);
                Log.d("EB", "Last ID = " + last_Id);

                //Add the event to cloud
                model.addNewDomesticViolenceEvent(domesticViolenceObject, new Model.AddNewDomesticViolenceEventClbck() {
                    @Override
                    public void doneAddNewDomesticViolenceClbak(boolean succeed) {
                        dialogAddingNewEvent.dismiss();
                        Toast.makeText(getApplicationContext(), config.REPORT_SENT_MESSAGE, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onSendButtonClicked(final CarAccidentEventObject carAccidentEventObject) {
        carAccidentEventObject.setUser_first_name(firstName);
        carAccidentEventObject.setUser_last_name(lastName);
        carAccidentEventObject.setUser_phone_number(phoneNumber);
        //Show the Progress dialog
        dialogAddingNewEvent.setMessage(config.SENDING_REPORT_MESSAGE);
        dialogAddingNewEvent.show();
        //Update the Event id
        model.getLastID(new Model.GetLastIdClbck() {
            @Override
            public void done(int last_Id) {
                carAccidentEventObject.set_id(last_Id);
                Log.d("EB", "Last ID = " + last_Id);

                //Add the event to cloud
                model.addNewCarAccidentEvent(carAccidentEventObject, new Model.AddNewCarAccidentEventClbck() {

                    @Override
                    public void doneAddNewCarAccidentClbak(boolean succeed) {

                        dialogAddingNewEvent.dismiss();
                        Toast.makeText(getApplicationContext(), config.REPORT_SENT_MESSAGE, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onSendButtonClicked(final OtherEventObject otherEventObject) {
        Log.d("EB","OtherEventObject: "+otherEventObject.toString());
        //Toast.makeText(getApplicationContext(),config.SENDING_REPORT_MESSAGE ,Toast.LENGTH_LONG).show();
        Log.d("EB", firstName+" "+lastName+" "+phoneNumber);
        //Update the user details
        otherEventObject.setUser_first_name(firstName);
        otherEventObject.setUser_last_name(lastName);
        otherEventObject.setUser_phone_number(phoneNumber);

        //Show the Progress dialog
        dialogAddingNewEvent.setMessage(config.SENDING_REPORT_MESSAGE);
        dialogAddingNewEvent.show();
        //Update the Event id
        model.getLastID(new Model.GetLastIdClbck() {
            @Override
            public void done(int last_Id) {
                otherEventObject.set_id(last_Id);
                Log.d("EB", "Last ID = " + last_Id);
                Log.d("EB", "EventOtherObj = " + otherEventObject.toString());

                //Add the event to cloud
                model.addNewOtherEvent(otherEventObject, new Model.AddNewOtherEventClbck() {

                    @Override
                    public void doneAddNewOtherClbak(boolean succeed) {

                        dialogAddingNewEvent.dismiss();
                        Toast.makeText(getApplicationContext(), config.REPORT_SENT_MESSAGE, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });

    }

    @Override
    public void onTakePhotoButtonClicked(ImageView imageView1) {

    }

    @Override
    public void onEventButtonClicked(int eventNumber, String eventName) {

        Log.d("EB", "onClick in MainActivity. Event name: "+eventName);
        //Toast.makeText(getApplicationContext(),"Event number "+eventNumber+" was clicked",Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),eventName,Toast.LENGTH_SHORT).show();

        if(eventNumber == config.EVENT_CODE_OTHER){
            Log.d("EB","in the if condition with event number: "+eventNumber);
            EventOther eventOther = new EventOther();
            Bundle bundle = new Bundle();
            bundle.putCharSequence("location",address_from_geoCoder);
            bundle.putCharSequence("latLng", mGPS.getLatitude()+"."+mGPS.getLongitude());
            eventOther.setArguments(bundle);
            boolean addToStack = false;
            changeFragment(eventOther, addToStack, eventNumber);
        }
        if(eventNumber == config.EVENT_CODE_ACCIDENT){
            Log.d("EB","in the if condition with event number: "+eventNumber);
            EventCarAccidentFragment eventCarAccidentFragment = new EventCarAccidentFragment();
            Bundle bundle = new Bundle();
            bundle.putCharSequence("location",address_from_geoCoder);
            bundle.putCharSequence("latLng", mGPS.getLatitude()+"."+mGPS.getLongitude());
            eventCarAccidentFragment.setArguments(bundle);
            boolean addToStack = false;
            changeFragment(eventCarAccidentFragment, addToStack, eventNumber);
        }
        if(eventNumber == config.EVENT_CODE_DOMESTIC_VIOLENCE){
            Log.d("EB","in the if condition with event number: "+eventNumber);
            EventDomesticViolenceFragment eventDomesticViolenceFragment = new EventDomesticViolenceFragment();
            Bundle bundle = new Bundle();
            bundle.putCharSequence("location",address_from_geoCoder);
            bundle.putCharSequence("latLng", mGPS.getLatitude()+"."+mGPS.getLongitude());
            eventDomesticViolenceFragment.setArguments(bundle);
            boolean addToStack = false;
            changeFragment(eventDomesticViolenceFragment, addToStack, eventNumber);
        }
        if(eventNumber == config.EVENT_CODE_CAR_THEFT){
            Log.d("EB","in the if condition with event number: "+eventNumber);
            EventCarTheftFragment eventCarTheftFragment = new EventCarTheftFragment();
            Bundle bundle = new Bundle();
            bundle.putCharSequence("location",address_from_geoCoder);
            bundle.putCharSequence("latLng", mGPS.getLatitude()+"."+mGPS.getLongitude());
            eventCarTheftFragment.setArguments(bundle);
            boolean addToStack = false;
            changeFragment(eventCarTheftFragment, addToStack, eventNumber);
        }


    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("EB", "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());
       // Toast.makeText(getApplicationContext(),
       //         "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude(),
       //           Toast.LENGTH_SHORT).show();

        //update the location
        this.location = location;

        //Get the updated address from the Geocoder
        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.GINGERBREAD
                &&
                Geocoder.isPresent()) {
            Log.d("EB", "Geocoder is running in main");
            /*
             * Reverse geocoding is long-running and synchronous.
             * Run it on a background thread.
             * Pass the current location to the background task.
             * When the task finishes,
             * onPostExecute() displays the address.
             */
            //(new GetAddressTask(this)).execute(location);

            GetAddressTask getAddressTask = new GetAddressTask(this);
            getAddressTask.execute(location);

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("EB","Status changed to: "+provider);
        Log.d("EB","Status int = "+status);
        Log.d("EB","Bundle extras = "+extras.toString());
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("EB","Provider enabled: "+provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("EB","Provider disabled: "+provider);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGPS.stopUsingGPS();
        dialogAddingNewEvent.dismiss();
    }
    @Override
    protected void onResume() {
        super.onResume();

       /* switch (currentFragmentCode){
            case config.EVENT_CODE_OTHER:
                changeFragment(new EventOther(),false, config.EVENT_CODE_OTHER);
                break;
            case config.EVENT_CODE_ACCIDENT:
                changeFragment(new EventCarAccidentFragment(), false, config.EVENT_CODE_ACCIDENT);
                break;
            case config.EVENTS_FRAGMENT_EMERGENCY_CODE:
                changeFragment(new EventsFragment(), false, config.EVENTS_FRAGMENT_EMERGENCY_CODE);
                break;
            case config.EVENTS_FRAGMENT_LESS_EMERGENCY_CODE:
                changeFragment(new EventsFragment_2(), false, config.EVENTS_FRAGMENT_LESS_EMERGENCY_CODE);
                break;
            default: //config.EVENTS_FRAGMENT_EMERGENCY_CODE
                changeFragment(new EventsFragment(), false, config.EVENTS_FRAGMENT_EMERGENCY_CODE);
                break;
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dialogAddingNewEvent.dismiss();
    }


    /**
     * A subclass of AsyncTask that calls getFromLocation() in the
     * background. The class definition has these generic types:
     * Location - A Location object containing
     * the current location.
     * Void     - indicates that progress units are not used
     * String   - An address passed to onPostExecute()
     */
    public class GetAddressTask extends
            AsyncTask<Location, Void, String> {
        Context mContext;

        public GetAddressTask(Context context) {
            super();
            mContext = context;
        }

        /**
         * Get a Geocoder instance, get the latitude and longitude
         * look up the address, and return it
         *
         * @return A string containing the address of the current
         * location, or an empty string if no address can be found,
         * or an error message
         * @params params One or more Location objects
         */
        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder =
                    new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            Location loc = params[0];
            // Create a list to contain the result address
            List<Address> addresses = null;
            try {
                /*
                 * Return 1 address.
                 */
                Log.d("EB", "Lat = "+loc.getLatitude()+" Long = "+loc.getLongitude());
                addresses = geocoder.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
            } catch (IOException e1) {
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments " +
                        Double.toString(loc.getLatitude()) +
                        " , " +
                        Double.toString(loc.getLongitude()) +
                        " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
                /*
                 * Format the first line of address (if available),
                 * city, and country name.
                 */
                String addressText = String.format(
                        "%s, %s, %s",
                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",
                        // Locality is usually a city
                        address.getLocality(),
                        // The country of the address
                        address.getCountryName());
                // Return the text
                return addressText;
            } else {
                //No address found
                return "";
            }
        }

        protected void onPostExecute(String address) {
            //Toast.makeText(getApplicationContext(), "Address: " + address, Toast.LENGTH_SHORT).show();
            Log.d("EB","Address: "+address);
            address_from_geoCoder = address;

        }


    }



    /********************** TABS CONFIGURATION ******************/
    public class MyTabListener implements ActionBar.TabListener {
        private final android.app.Fragment frag;
        public MyTabListener(Fragment f){
            frag=f;
        }
        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if(frag!=null){
                ft.replace(R.id.fragment_container, frag);// commit is called automatically
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            //		Toast.makeText(getApplicationContext(), "Tab "+(tab.getPosition()+1)+" Unselected", Toast.LENGTH_SHORT).show();
            if(frag!=null){
                ft.remove(frag);//This is redundant since the tab is replaced anyway

            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("EB", "onActivityResult in MainActivity");
    }
}
