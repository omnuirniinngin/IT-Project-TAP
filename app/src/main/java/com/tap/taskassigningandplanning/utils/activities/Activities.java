package com.tap.taskassigningandplanning.utils.activities;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.Date;

public class Activities {

    private String title;
    private String dateStart;
    private String dateEnd;
    private @ServerTimestamp
    Date timestamp;
    private String activity_id;
    private String user_id;

    public Activities(){

    }

    public Activities(String title, String dateStart, String dateEnd, Date timestamp, String activity_id, String user_id) {
        this.title = title;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.timestamp = timestamp;
        this.activity_id = activity_id;
        this.user_id = user_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setActivity_id(String plan_id) {
        this.activity_id = plan_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
