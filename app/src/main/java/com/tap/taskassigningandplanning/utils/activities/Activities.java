package com.tap.taskassigningandplanning.utils.activities;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.Date;

public class Activities {

    private String title;
    private String dateStart;
    private String dateEnd;
    private @ServerTimestamp
    Date timestamp;
    private String plan_id;
    private String user_id;

    public Activities(){

    }

    public Activities(String title, String dateStart, String dateEnd, Date timestamp, String plan_id, String user_id) {
        this.title = title;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.timestamp = timestamp;
        this.plan_id = plan_id;
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public String getUser_id() {
        return user_id;
    }
}
