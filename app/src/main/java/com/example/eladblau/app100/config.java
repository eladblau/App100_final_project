package com.example.eladblau.app100;

/**
 * Created by eladblau on 23-Jan-15.
 */
public class config {


    /*SharedPreferences Section */
    public static final String SHARED_PREFERENCES_FILE_NAME = "App100Pref";
    public static final String SHARED_PREFERENCES_KEY_USER_FIRST_NAME = "user_first_name_app100";
    public static final String SHARED_PREFERENCES_KEY_USER_LAST_NAME = "user_last_name_app100";
    public static final String SHARED_PREFERENCES_KEY_PASSWORD = "password_app100";
    public static final String SHARED_PREFERENCES_KEY_PHONE_NUMBER = "phone_number_app100";
    public static final String SHARED_PREFERENCES_KEY_SIM_SERIAL_NUMBER = "phone_number_app100";
    public static final String SHARED_PREFERENCES_KEY_IS_LOGGED_IN = "isLoggedIn_app100";

    //For Sign Up Section
    public static final String SIGN_UP = "Signing up";
    public static final String SIGN_UP_BODY_MESSAGE = "Welcome to App100 !\nYour verification code is: \n";
    public static final String SIGN_UP_FAILD_MESSAGE = "This phone number is already registered";
    public static final String LOGIN = "Login";
    public static final String LOGOUT = "Logout";
    public static final String LOGOUT_ALERT_MESSAGE = "Are you sure you want to logout?";

    public static final String UPDATING_MESSAGE = "Updating";



    public static String DISPACH_NUMBER = "100";



    /********************************************************************************
     *
     *          EVENTS SECTION
     *
     *********************************************************************************/
   // public static final int EVENTS_FRAGMENT_CODE = 0;
   public static final String EVENTS_EMERGENCY = "Emergency Events";
    public static final String EVENTS_LESS_EMERGENCY = "Other Events";

    public static final int EVENTS_FRAGMENT_EMERGENCY_CODE = 900;
    public static final int EVENTS_FRAGMENT_LESS_EMERGENCY_CODE = 901;

    public static final int EVENT_CODE_ACCIDENT = 1;
    public static final int EVENT_CODE_THEFT = 2;
    public static final int EVENT_CODE_OTHER = 3;
    public static final int EVENT_CODE_CAR_THEFT = 4;
    public static final int EVENT_CODE_DOMESTIC_VIOLENCE = 5;
    public static final int EVENT_CODE_KIDNAPPING = 6;
    public static final int EVENT_CODE_TERRORISM = 7;
    public static final int EVENT_CODE_RAPE = 8;


    //For the Other event

    public static final String EVENT_NAME_ACCIDENT = "Accident";
    public static final String EVENT_NAME_THEFT = "Robbery / Theft";
    public static final String EVENT_NAME_OTHER = "Other";
    public static final String EVENT_NAME_CAR_THEFT = "Car theft";
    public static final String EVENT_NAME_DOMESTIC_VIOLENCE = "Domestic violence";
    public static final String EVENT_NAME_KIDNAPPING = "Kidnapping";
    public static final String EVENT_NAME_TERRORISM = "Terrorism";
    public static final String EVENT_NAME_RAPE = "Rape";


    public static final String FAILD_TO_ATTACH_PHOTO_MESSAGE = "Faild to attach this photo. \nPlease try a different photo";


    public static final String TIMESTAMP_FORMAT = "dd-MM-yyyy hh:mm:ss";
    public static final String SENDING_REPORT_MESSAGE = "Sending Report...";
    public static final String REPORT_SENT_MESSAGE = "Your report has been sent!\nThanks!";


}
