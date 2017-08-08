package com.example.eladblau.app100.objects;

import android.graphics.Bitmap;

/**
 * Created by eladblau on 06-Feb-15.
 */
public class DomesticViolenceObject extends Event{

    private String description;

    public DomesticViolenceObject(int _id, int category, String creation_time, String updated_time,
                                  String user_phone_number, String user_first_name, String user_last_name,
                                  String description, String location_by_user, String location_by_device,
                                  int life_threatening, Bitmap photo) {
        super(_id, category, creation_time, updated_time, user_phone_number, user_first_name,
                user_last_name, location_by_user, location_by_device, life_threatening, photo);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DomesticViolenceObject{" +
                "description='" + description + '\'' +
                "} " + super.toString();
    }
}
