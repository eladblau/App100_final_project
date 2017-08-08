package com.example.eladblau.app100.objects;

import android.graphics.Bitmap;

/**
 * Created by eladblau on 23-Jan-15.
 */
public class CarTheftEventObject extends Event {

    private String carID;
    private String description;

    public CarTheftEventObject(int _id, int category, String creation_time, String updated_time,
                               String user_phone_number, String user_first_name, String user_last_name,
                               String carID, String description, String location_by_user,
                               String location_by_device, int life_threatening, Bitmap photo) {
        super(_id, category, creation_time, updated_time, user_phone_number, user_first_name,
                user_last_name, location_by_user, location_by_device, life_threatening, photo);
        this.carID = carID;
        this.description = description;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "CarTheftEventObject{" +
                "carID='" + carID + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
