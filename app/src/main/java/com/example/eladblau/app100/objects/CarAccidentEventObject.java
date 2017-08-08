package com.example.eladblau.app100.objects;

import android.graphics.Bitmap;

/**
 * Created by eladblau on 23-Jan-15.
 */
public class CarAccidentEventObject extends Event {

    private int involvedNumber;
    private String carID;
    private String description;
    private int injures;

    public CarAccidentEventObject(int _id, int category, String creation_time, String updated_time,
                                  String user_phone_number, String user_first_name, String user_last_name,
                                  int involvedNumber, String carID, String description, String location_by_user,
                                  String location_by_device, int injures, int life_threatening, Bitmap photo) {
        super(_id, category, creation_time, updated_time, user_phone_number, user_first_name,
                user_last_name, location_by_user, location_by_device, life_threatening, photo);
        this.involvedNumber = involvedNumber;
        this.carID = carID;
        this.description = description;
        this.injures = injures;
    }

    public int getInvolvedNumber() {
        return involvedNumber;
    }

    public void setInvolvedNumber(int involvedNumber) {
        this.involvedNumber = involvedNumber;
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

    public int getInjures() {
        return injures;
    }

    public void setInjures(int injures) {
        this.injures = injures;
    }

    @Override
    public String toString() {
        return "CarAccidentEventObject{" +
                "involvedNumber=" + involvedNumber +
                ", carID='" + carID + '\'' +
                ", description='" + description + '\'' +
                ", injures=" + injures +
                "} " + super.toString();
    }
}
