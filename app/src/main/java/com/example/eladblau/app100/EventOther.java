package com.example.eladblau.app100;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eladblau.app100.objects.OtherEventObject;

import java.io.FileDescriptor;
import java.io.IOException;

import static com.example.eladblau.app100.config.*;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.example.eladblau.app100.EventOther.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.example.eladblau.app100.EventOther#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventOther extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_TAKE_PHOTO_CODE = 1;
    private static final int REQUEST_ATTACH_PHOTO_CODE = 2;

    private Activity activity;

    private Spinner spinner;
    private EditText editTextLocation;
    private Button sendButton;
    private ImageButton takePhotoButton;
    private ImageButton attachPhotoButton;
    private EditText editTextDescription;
    private RadioButton radioButtonLifeThreatening;
    private ImageView imageView;
    private Bitmap photo;
    private SharedPreferences sharedPreferencesFile;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventOther.
     */

    public static EventOther newInstance(String param1, String param2) {
        EventOther fragment = new EventOther();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public EventOther() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.layout_event_other,
                container, false);

        //Getting all views
        editTextLocation = (EditText) rootView.findViewById(R.id.editTextLocation);
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        editTextDescription = (EditText) rootView.findViewById(R.id.editTextDescription);
        radioButtonLifeThreatening = (RadioButton) rootView.findViewById(R.id.radioButton_life_threatening_yes);
        sendButton = (Button) rootView.findViewById(R.id.buttonSend);
        takePhotoButton = (ImageButton) rootView.findViewById(R.id.imageButtonTakePhoto);
        attachPhotoButton = (ImageButton) rootView.findViewById(R.id.imageButtonAttachPic);
        imageView = (ImageView) rootView.findViewById(R.id.imageView11);

        //Getting the location from the previous fragment
        Bundle bundle = getArguments();
        if(bundle.getCharSequence("location") != null  ){
            CharSequence location = bundle.getCharSequence("location");
            Log.d("EB","Location is: "+location);
            editTextLocation.setText(location);
        }
        final CharSequence latLng = bundle.getCharSequence("latLng");




        //-------------  Spinner ------------

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.events_types_1, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.d("EB", "adapter = " +adapter.toString());
    // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.deviceHasCamera){
                    Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(photoIntent, REQUEST_TAKE_PHOTO_CODE );
                }
                else{
                    Toast.makeText(activity, "No camera detected", Toast.LENGTH_SHORT).show();
                }

            }

        });

        attachPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, REQUEST_ATTACH_PHOTO_CODE);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int event_id = 0;
                int event_category = EVENT_CODE_OTHER;
                String timestamp = (DateFormat.format(TIMESTAMP_FORMAT, new java.util.Date()).toString());
                String updatedTime = (DateFormat.format(TIMESTAMP_FORMAT, new java.util.Date()).toString());
                String user_phone_number = "NULL";
                String user_first_name = "NULL";
                String user_last_name = "NULL";
                //Set the event type code
                int event_type = getEventTypeFromSpinner();
                String description = editTextDescription.getText().toString();
                String location_by_user = editTextLocation.getText().toString();
                String location_by_device = (String) latLng;
                int life_threatening = (radioButtonLifeThreatening.isChecked() ? 1 : 0);
                Bitmap photo_to_object = photo;
                Log.d("EB","Timestamp: "+timestamp);
                OtherEventObject otherEventObject = new OtherEventObject(event_id, event_category,
                        timestamp,updatedTime, user_phone_number, user_first_name, user_last_name,
                        event_type,description,location_by_user, location_by_device,
                        life_threatening, photo_to_object);
                Log.d("EB","OtherEventObj = "+otherEventObject.toString());
                mListener.onSendButtonClicked(otherEventObject);
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = activity;
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
        public void onSendButtonClicked(OtherEventObject otherEventObject);
        public void onTakePhotoButtonClicked(ImageView imageView1);

    }


    //This method return the selected event type from the spinner
    private int getEventTypeFromSpinner(){
        String eventName = spinner.getSelectedItem().toString();
        switch (eventName){
            case EVENT_NAME_ACCIDENT:
                return EVENT_CODE_ACCIDENT;
            case EVENT_NAME_THEFT:
                return EVENT_CODE_THEFT;
            case EVENT_NAME_OTHER:
                return EVENT_CODE_OTHER;
            case EVENT_NAME_DOMESTIC_VIOLENCE:
                return EVENT_CODE_DOMESTIC_VIOLENCE;
            case EVENT_NAME_CAR_THEFT:
                return EVENT_CODE_CAR_THEFT;
            case EVENT_NAME_KIDNAPPING:
                return EVENT_CODE_KIDNAPPING;
            case EVENT_NAME_TERRORISM:
                return EVENT_CODE_TERRORISM;
            case EVENT_NAME_RAPE:
                return EVENT_CODE_RAPE;

            default:
                return EVENT_CODE_OTHER;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("EB", "onActivityResult");
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO_CODE:
                //This case is when the user decide to Approve the captured photo
                if (resultCode == Activity.RESULT_OK) {
                    photo = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(photo);
                    Log.d("EB", "BitMap = " + photo.toString());
                }
                break;
            case REQUEST_ATTACH_PHOTO_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    Log.d("EB", "Uri fata.getData() = " + data.getData().toString());
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.activity.getContentResolver().query(
                            selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    photo = BitmapFactory.decodeFile(filePath);
                    //For the case that is Android 5.0 and the photo is on the server
                    // and not on the device
                    if (photo == null){
                        try {
                            photo = getBitmapFromUri(data.getData());
                            Log.d("EB", "photo = getBitmapFromUri(data.getData()) = " + photo.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this.activity, FAILD_TO_ATTACH_PHOTO_MESSAGE,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    //Set the photo to the imageView
                    imageView.setImageBitmap(photo);
                   Log.d("EB", "attached image = " + ((photo != null) ? photo.toString() : "NULL"));
                }
                break;

        }
    }


/*    This method handling cases of attaching photo from the Google server (Android 5.0 Lolly-Pop)*/

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                this.activity.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

}
