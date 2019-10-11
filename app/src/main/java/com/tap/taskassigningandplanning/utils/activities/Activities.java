package com.tap.taskassigningandplanning.utils.activities;

import com.google.firebase.Timestamp;

public class Activities {

    private String title, dateStart;

    public Activities(){
        //empty constructor needed
    }

    public Activities(String title, String dateStart) {
        this.title = title;
        this.dateStart = dateStart;
    }

    public String getTitle() {
        return title;
    }

    public String getDateStart() {
        return dateStart;
    }
}
