package com.example.sharemybike;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class BikesContent {

    public static List<Bike> ITEMS = new ArrayList<>();
    public static String selectedDate;

    public static class Bike extends com.example.sharemybike.Bike {
        private Bitmap photo;
        private String owner;
        private String description;
        private String city;
        private String location;
        private String email;
        private String country;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Bitmap getPhoto() {
            return photo;
        }

        public void setPhoto(Bitmap photo) {
            this.photo = photo;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }



        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Bike(Bitmap photo, String owner, String description, String city, String location, String email, String country) {
            this.photo = photo;
            this.owner = owner;
            this.description = description;
            this.city = city;
            this.location = location;
            this.email = email;
            this.country = country;
        }

        @Override
        public String toString() {
            return owner + " " + description;
        }
    }
}
