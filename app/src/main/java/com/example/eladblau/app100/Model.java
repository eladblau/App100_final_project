package com.example.eladblau.app100;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.eladblau.app100.objects.CarAccidentEventObject;
import com.example.eladblau.app100.objects.CarTheftEventObject;
import com.example.eladblau.app100.objects.DomesticViolenceObject;
import com.example.eladblau.app100.objects.Event;
import com.example.eladblau.app100.objects.OtherEventObject;
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;


/**
 * Created by eladblau on 18-Jan-15.
 */
public class Model {


    ////////////////////////////////////////
    //      Parse variables
    ////////////////////////////////////////
    private static String PARSE_APPLICATION_ID = "APP_ID_1234567890";
    private static String PARSE_CLIENT_KEY = "CLIENT_KEY_1234567890";

    /*****************************************

        Data Base Definition

     ******************************************/

    /////////////////////////////////////////////////
    //          Table Events
    /////////////////////////////////////////////////

    public static String TABLE_EVENTS = "events";
    public static String EVENTS_ID = "event_id";
    public static String EVENTS_CATEGORY_TYPE = "event_category";
    public static String EVENTS_CREATION_TIME = "creation_time";
    public static String EVENTS_UPDATED_TIME = "update_time";
    public static String EVENTS_USER_PHONE_NUMBER = "user_phone_number";
    public static String EVENTS_USER_FIRST_NAME = "user_first_name";
    public static String EVENTS_USER_LAST_NAME = "user_last_name";
    public static String EVENTS_LOCATION_BY_USER = "location_by_user";
    public static String EVENTS_LOCATION_BY_DEVICE = "location_by_device";
    public static String EVENTS_LIFE_THREATENING = "life_threatening";
    public static String EVENTS_PHOTO = "photo";

    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    //          Table Car Accident
    /////////////////////////////////////////////////

    public static String TABLE_CAR_ACCIDENTS = "car_accidents";
    public static String ACCIDENTS_EVENTS_ID = "event_id";
    public static String ACCIDENTS_NUMBER_OF_INVOLVED = "number_of_involved";
    public static String ACCIDENTS_VEHICLE_ID = "vehicle_id";
    public static String ACCIDENTS_INJURIES = "injuries";
    public static String ACCIDENTS_DESCRIPTION = "description";

    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    //          Table Others
    /////////////////////////////////////////////////

    public static String TABLE_OTHERS = "others";
    public static String OTHERS_EVENT_ID = "event_id";
    public static String OTHERS_SUB_CATEGORY = "sub_category";
    public static String OTHERS_DESCRIPTION = "description";

    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    //          Table events_types
    /////////////////////////////////////////////////

    public static String TABLE_EVENTS_TYPES = "events_types";
    public static String EVENT_TYPES_TYPE_ID = "type_id";
    public static String EVENT_TYPES_TYPE_NAME = "type_name";
    public static String EVENT_TYPES_AVAILABLE = "available";

    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    //          Table domestic_violence
    /////////////////////////////////////////////////

    public static String TABLE_DOMESTIC_VIOLENCE = "domestic_violence";
    public static String DOMESTIC_VIOLENCE_EVENT_ID = "event_id";
    public static String DOMESTIC_VIOLENCE_DESCRIPTION = "description";

    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    //          Table Car theft
    /////////////////////////////////////////////////

    public static String TABLE_CAR_THEFT = "car_theft";
    public static String CAR_THEFT_EVENTS_ID = "event_id";
    public static String CAR_THEFT_VEHICLE_ID = "vehicle_id";
    public static String CAR_THEFT_DESCRIPTION = "description";

    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    //          Table Users
    /////////////////////////////////////////////////

    public static String TABLE_USERS = "users";
    public static String USERS_FIRST_NAME = "first_name";
    public static String USERS_LAST_NAME = "last_name";
    public static String USERS_PHONE_NUMBER = "phone_number";

    public static String STATUS_PHONE_NUMBER_NOT_EXISTS = "The phone number is not exists";
    public static String STATUS_FIRST_NAME_INCORRECT = "The first name is incorrect";
    public static String STATUS_LAST_NAME_INCORRECT = "The last name is incorrect";
    public static String STATUS_OK = "Welcome back !";

    public static String STATUS_UPDATING_PROBLEM = "Something went wrong. Please try later.";
    public static String STATUS_DONE_UPDATE = "Your details has been updated";



    /////////////////////////////////////////////////

    // class member
    private static Model instance;

    // private constructor
    private Model(Context context){
        initialModel(context);
        //myDb = new DatabaseHelper(context);
    }

    private void initialModel(Context context){
        Log.d("Model", "Initializing DB ");
        // Enable Local Datastore.
        //Parse.enableLocalDatastore(this);
        Parse.initialize(context, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
    }

    //public accessor
    public static Model getInstance(Context context){
        if (instance == null) {
            instance = new Model(context);
        }
        return instance;
    }


    /////////////CallBack Interface //////////////////
    interface AddNewOtherEventClbck{
        public void doneAddNewOtherClbak(boolean succeed);
    }
    /////////////Find In Background Operation with CallBack//////////////////
    public void addNewOtherEvent(OtherEventObject otherEventObject, final AddNewOtherEventClbck clbck){

        final AddNewOtherEventClbck callBackObject = clbck;
        Event event = new Event(otherEventObject.get_id(),otherEventObject.getCategory(),
                otherEventObject.getCreation_time(), otherEventObject.getUpdated_time(),
                otherEventObject.getUser_phone_number(), otherEventObject.getUser_first_name(),
                otherEventObject.getUser_last_name(), otherEventObject.getLocation_by_user(),
                otherEventObject.getLocation_by_device(), otherEventObject.getLife_threatening(),
                otherEventObject.getPhoto());

        Log.d("EB","Event = "+event.toString());
        //Convert the event object to Parse object
        ParseObject parseObject_Event = eventToJson(event);
        final ParseObject parseObject_OtherEvent = otherEventToJson(otherEventObject);

       // Log.d("EB", "ParseObject_Event = "+parseObject_Event.toString());
        //Save on cloud

        parseObject_Event.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {

                parseObject_OtherEvent.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        callBackObject.doneAddNewOtherClbak(true);
                    }
                });

            }
        });
        Log.d("EB","Model saved in addNewOtherEvent "+otherEventObject);

        return;
    }





    /////////////CallBack Interface //////////////////
    interface AddNewCarAccidentEventClbck{
        public void doneAddNewCarAccidentClbak(boolean succeed);
    }
    /////////////Find In Background Operation with CallBack//////////////////
    public void addNewCarAccidentEvent(CarAccidentEventObject carAccidentEventObject, final AddNewCarAccidentEventClbck clbck){

        final AddNewCarAccidentEventClbck callBackObject = clbck;
        Event event = new Event(carAccidentEventObject.get_id(),carAccidentEventObject.getCategory(),
                carAccidentEventObject.getCreation_time(), carAccidentEventObject.getUpdated_time(),
                carAccidentEventObject.getUser_phone_number(), carAccidentEventObject.getUser_first_name(),
                carAccidentEventObject.getUser_last_name(), carAccidentEventObject.getLocation_by_user(),
                carAccidentEventObject.getLocation_by_device(), carAccidentEventObject.getLife_threatening(),
                carAccidentEventObject.getPhoto());

        Log.d("EB","Event = "+event.toString());
        //Convert the event object to Parse object
        ParseObject parseObject_Event = eventToJson(event);
        final ParseObject parseObject_CarAccidentEvent = carAccidentEventToJson(carAccidentEventObject);

        //Save to cloud
        parseObject_Event.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {

                parseObject_CarAccidentEvent.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        callBackObject.doneAddNewCarAccidentClbak(true);
                    }
                });

            }
        });
        return;
    }


    /////////////CallBack Interface //////////////////
    interface AddNewDomesticViolenceEventClbck{
        public void doneAddNewDomesticViolenceClbak(boolean succeed);
    }
    /////////////Find In Background Operation with CallBack//////////////////
    public void addNewDomesticViolenceEvent(DomesticViolenceObject domesticViolenceObject,
                                            final AddNewDomesticViolenceEventClbck clbck){

        final AddNewDomesticViolenceEventClbck callBackObject = clbck;
        Event event = new Event(domesticViolenceObject.get_id(),domesticViolenceObject.getCategory(),
                domesticViolenceObject.getCreation_time(), domesticViolenceObject.getUpdated_time(),
                domesticViolenceObject.getUser_phone_number(), domesticViolenceObject.getUser_first_name(),
                domesticViolenceObject.getUser_last_name(), domesticViolenceObject.getLocation_by_user(),
                domesticViolenceObject.getLocation_by_device(), domesticViolenceObject.getLife_threatening(),
                domesticViolenceObject.getPhoto());
        Log.d("EB","Event = "+event.toString());
        //Convert the event object to Parse object
        ParseObject parseObject_Event = eventToJson(event);
        final ParseObject parseObject_domesticViolenceEvent = new ParseObject(TABLE_DOMESTIC_VIOLENCE);
        parseObject_domesticViolenceEvent.put(DOMESTIC_VIOLENCE_EVENT_ID, domesticViolenceObject.get_id());
        parseObject_domesticViolenceEvent.put(DOMESTIC_VIOLENCE_DESCRIPTION, domesticViolenceObject.getDescription());
        //Save to cloud
        parseObject_Event.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                parseObject_domesticViolenceEvent.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        callBackObject.doneAddNewDomesticViolenceClbak(true);
                    }
                });
            }
        });
        return;
    }

    /////////////CallBack Interface //////////////////
    interface AddNewCarTheftEventClbck{
        public void doneAddNewCarTheftClbak(boolean succeed);
    }
    /////////////Find In Background Operation with CallBack//////////////////
    public void addNewCarTheftEvent(CarTheftEventObject carTheftEventObject,
                                            final AddNewCarTheftEventClbck clbck){

        final AddNewCarTheftEventClbck callBackObject = clbck;
        Event event = new Event(carTheftEventObject.get_id(),carTheftEventObject.getCategory(),
                carTheftEventObject.getCreation_time(), carTheftEventObject.getUpdated_time(),
                carTheftEventObject.getUser_phone_number(), carTheftEventObject.getUser_first_name(),
                carTheftEventObject.getUser_last_name(), carTheftEventObject.getLocation_by_user(),
                carTheftEventObject.getLocation_by_device(), carTheftEventObject.getLife_threatening(),
                carTheftEventObject.getPhoto());
        Log.d("EB","Event = "+event.toString());
        //Convert the event object to Parse object
        ParseObject parseObject_Event = eventToJson(event);
        final ParseObject parseObject_carTheftEvent = new ParseObject(TABLE_CAR_THEFT);
        parseObject_carTheftEvent.put(CAR_THEFT_EVENTS_ID, carTheftEventObject.get_id());
        parseObject_carTheftEvent.put(CAR_THEFT_VEHICLE_ID, carTheftEventObject.getCarID());
        parseObject_carTheftEvent.put(CAR_THEFT_DESCRIPTION, carTheftEventObject.getDescription());
        //Save to cloud
        parseObject_Event.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                parseObject_carTheftEvent.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        callBackObject.doneAddNewCarTheftClbak(true);
                    }
                });
            }
        });
        return;
    }



    /////////////CallBack Interface //////////////////
    public interface SignUpCallBack{
        public void doneSignUp(boolean signedUp);
    }
    //This method adds new user data to db - SignUp
    public void addNewUser(String firstName, String lastName, String phoneNumber, SignUpCallBack callBack){
        final SignUpCallBack signUpCallBack = callBack;
        final String first_name = firstName;
        final String last_name = lastName;
        final String phone_number = phoneNumber;
        //Check if this user is already exists
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_USERS);
        query.whereEqualTo(USERS_PHONE_NUMBER, phone_number);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(parseObject != null){
                    //This user is already exists in the db
                    signUpCallBack.doneSignUp(false);
                    return;
                }
                else{
                    ParseObject po = new ParseObject(TABLE_USERS);
                    po.put(USERS_FIRST_NAME, first_name);
                    po.put(USERS_LAST_NAME, last_name);
                    po.put(USERS_PHONE_NUMBER, phone_number);
                    //Save data in the db
                    po.saveInBackground();
                    signUpCallBack.doneSignUp(true);
                }
            }
        });
    }

    /////////////CallBack Interface //////////////////
    public interface CheckIfUserExistsCallBack{
        public void doneCheckingUser(boolean userExists);
    }
    //This method adds new user data to db - SignUp
    public void checkIfUserExists(String firstName, String lastName, String phoneNumber, CheckIfUserExistsCallBack callBack){
        final CheckIfUserExistsCallBack userExistsCallBack = callBack;
        final String first_name = firstName;
        final String last_name = lastName;
        final String phone_number = phoneNumber;
        //Check if this user is already exists
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_USERS);
        query.whereEqualTo(USERS_PHONE_NUMBER, phone_number);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(parseObject != null){
                    //This user is already exists in the db
                    userExistsCallBack.doneCheckingUser(true);
                    return;
                }
                else{
                    //The user is not exist yet
                    userExistsCallBack.doneCheckingUser(false);
                }
            }
        });
    }

    /////////////CallBack Interface //////////////////
    public interface loginCallBack{
        public void doneLogin(boolean isExists, String status);
    }
    //this method checks if the user exists in db
    public void login(final String firstName, final String lastName, String phoneNumber, final loginCallBack callBack){

        final loginCallBack callBackObject = callBack;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_USERS);
        query.whereEqualTo(USERS_PHONE_NUMBER, phoneNumber);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null){
                    //No such a user
                    callBackObject.doneLogin(false, STATUS_PHONE_NUMBER_NOT_EXISTS);
                }
                else if(firstName.equals(parseObject.getString(USERS_FIRST_NAME))) {
                    //The first name is ok. Check the last name
                    if (lastName.equals(parseObject.getString(USERS_LAST_NAME))) {
                        callBackObject.doneLogin(true, STATUS_OK);
                    } else {
                        //The last name is incorrect
                        callBackObject.doneLogin(false, STATUS_LAST_NAME_INCORRECT);
                    }
                }
                else{
                    //The first name is incorrect
                    callBackObject.doneLogin(false, STATUS_FIRST_NAME_INCORRECT);
                }

            }
        });

        return;
    }



    /////////////CallBack Interface //////////////////
    public interface updateCallBack{
        public void doneUpdate(boolean succeed, String status);
    }
    //this method checks if the user exists in db
    public void updateUserDetails(final String firstName, final String lastName, final String phoneNumber,
                                  final updateCallBack callBack){

        final updateCallBack callBackObject = callBack;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_USERS);
        query.whereEqualTo(USERS_PHONE_NUMBER, phoneNumber);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null){
                    //No such a user
                    callBackObject.doneUpdate(false, STATUS_UPDATING_PROBLEM);
                }
                else { //There is such a user
                    parseObject.put(USERS_FIRST_NAME, firstName);
                    parseObject.put(USERS_LAST_NAME, lastName);
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            callBack.doneUpdate(true,STATUS_DONE_UPDATE);
                        }
                    });
                }

            }
        });

        return;
    }



    // ---------- Parse to Json ----------

    ////////////Helper method to covert from ParseObject to Event //////////////
    public Event jsonToEvent(ParseObject p){

        ParseFile imageFile = (ParseFile)p.get(EVENTS_PHOTO);
        imageFile.getDataInBackground(new GetDataCallback() {
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    Bitmap photo = BitmapFactory.decodeByteArray(data, 0, data.length);
                } else {
                    // something went wrong
                }
            }
        });

        Event event = new Event(p.getInt(EVENTS_ID),p.getInt(EVENTS_CATEGORY_TYPE),
                p.getString(EVENTS_CREATION_TIME),p.getString(EVENTS_UPDATED_TIME),
                p.getString(EVENTS_USER_PHONE_NUMBER), p.getString(EVENTS_USER_FIRST_NAME),
                p.getString(EVENTS_USER_LAST_NAME), p.getString(EVENTS_LOCATION_BY_USER),
                p.getString(EVENTS_LOCATION_BY_DEVICE), p.getInt(EVENTS_LIFE_THREATENING),
                null);


        Log.d("EB", "Model - jsonToEvent " +event );
        return event;
    }



    /////////////CallBack Interface //////////////////
    interface GetLastIdClbck{
        public void done(int last_Id);
    }

    //Get the last id that was inserted to the DB
    public void getLastID(GetLastIdClbck clbck){
        final GetLastIdClbck callBack = clbck;

        Log.d("EB", "Model - Getting last Id");

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery(TABLE_EVENTS);

        query1.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    callBack.done(count);
                } else {
                    e.getMessage();
                }
            }
        });

    }








    /* ///////////////////////////////////////////////////////
     --------------------- Json to Parse ---------------------
      *///////////////////////////////////////////////////////

    ////////////Helper method to covert from Event to ParseObject /////////////
    public ParseObject eventToJson(Event event){
        ParseObject po = new ParseObject(TABLE_EVENTS);
        po.put(EVENTS_ID, event.get_id());
        po.put(EVENTS_CATEGORY_TYPE, event.getCategory());
        po.put(EVENTS_CREATION_TIME, event.getCreation_time());
        po.put(EVENTS_UPDATED_TIME, event.getUpdated_time());
        po.put(EVENTS_USER_PHONE_NUMBER, event.getUser_phone_number());
        po.put(EVENTS_USER_FIRST_NAME, event.getUser_first_name());
        po.put(EVENTS_USER_LAST_NAME, event.getUser_last_name());
        po.put(EVENTS_LOCATION_BY_USER, event.getLocation_by_user());
        po.put(EVENTS_LOCATION_BY_DEVICE, event.getLocation_by_device());
        po.put(EVENTS_LIFE_THREATENING, event.getLife_threatening());
        if(event.getPhoto() != null){

            Log.d("EB", "image is not NULL: "+event.getPhoto().toString());
            Bitmap bitmap = event.getPhoto();
            // Convert it to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Compress image to lower quality scale 1 - 100
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();
            Log.d("EB", "image: "+image.toString());
            // Create the ParseFile
            ParseFile file = new ParseFile("photo.png" , image);
            // Upload the image into Parse Cloud
            file.saveInBackground();
            po.put(EVENTS_PHOTO, file);
        }

        return po;
    }

    ////////////Helper method to covert from OtherEventObject to ParseObject /////////////
    public ParseObject otherEventToJson(OtherEventObject otherEventObject){
        ParseObject po = new ParseObject(TABLE_OTHERS);
        po.put(OTHERS_EVENT_ID, otherEventObject.get_id());
        po.put(OTHERS_SUB_CATEGORY, otherEventObject.getEventType());
        po.put(OTHERS_DESCRIPTION, otherEventObject.getDescription());
        return po;
    }
    ////////////Helper method to covert from CarAccidentEventObject to ParseObject /////////////
    public ParseObject carAccidentEventToJson(CarAccidentEventObject carAccidentEventObject){
        ParseObject po = new ParseObject(TABLE_CAR_ACCIDENTS);
        po.put(ACCIDENTS_EVENTS_ID, carAccidentEventObject.get_id());
        po.put(ACCIDENTS_NUMBER_OF_INVOLVED, carAccidentEventObject.getInvolvedNumber());
        po.put(ACCIDENTS_VEHICLE_ID, carAccidentEventObject.getCarID());
        po.put(ACCIDENTS_INJURIES, carAccidentEventObject.getInjures());
        po.put(ACCIDENTS_DESCRIPTION, carAccidentEventObject.getDescription());
        return po;
    }






}
