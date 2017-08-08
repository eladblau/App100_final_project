package com.example.eladblau.app100.objects;


import android.graphics.Bitmap;

/**
 * Created by eladblau on 21-Jan-15.
 */
public class Event {

    protected int _id;
    protected int category;
    protected String creation_time;
    protected String updated_time;
    protected String user_phone_number;
    protected String user_first_name;
    protected String user_last_name;
    protected String location_by_user;
    protected String location_by_device;
    protected int life_threatening;
    protected Bitmap photo;

    //Constructor
    public Event(int _id, int category, String creation_time, String updated_time,
                 String user_phone_number, String user_first_name, String user_last_name,
                 String location_by_user, String location_by_device, int life_threatening, Bitmap photo) {
        this._id = _id;
        this.category = category;
        this.creation_time = creation_time;
        this.updated_time = updated_time;
        this.user_phone_number = user_phone_number;
        this.user_first_name = user_first_name;
        this.user_last_name = user_last_name;
        this.location_by_user = location_by_user;
        this.location_by_device = location_by_device;
        this.life_threatening = life_threatening;
        this.photo = photo;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public String getUser_phone_number() {
        return user_phone_number;
    }

    public void setUser_phone_number(String user_phone_number) {
        this.user_phone_number = user_phone_number;
    }

    public String getUser_first_name() {
        return user_first_name;
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name = user_first_name;
    }

    public String getUser_last_name() {
        return user_last_name;
    }

    public void setUser_last_name(String user_last_name) {
        this.user_last_name = user_last_name;
    }

    public String getLocation_by_user() {
        return location_by_user;
    }

    public void setLocation_by_user(String location_by_user) {
        this.location_by_user = location_by_user;
    }

    public String getLocation_by_device() {
        return location_by_device;
    }

    public void setLocation_by_device(String location_by_device) {
        this.location_by_device = location_by_device;
    }

    public int getLife_threatening() {
        return life_threatening;
    }

    public void setLife_threatening(int life_threatening) {
        this.life_threatening = life_threatening;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Event{" +
                "_id=" + _id +
                ", category=" + category +
                ", creation_time='" + creation_time + '\'' +
                ", updated_time='" + updated_time + '\'' +
                ", user_phone_number='" + user_phone_number + '\'' +
                ", user_first_name='" + user_first_name + '\'' +
                ", user_last_name='" + user_last_name + '\'' +
                ", location_by_user='" + location_by_user + '\'' +
                ", location_by_device='" + location_by_device + '\'' +
                ", life_threatening=" + life_threatening +
                ", photo=" + photo +
                '}';
    }
}
