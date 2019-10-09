package com.tap.taskassigningandplanning.ui.plan;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Plan {
    private String title;
    private String dateStart;
    private String dateEnd;
    private String plan_id;
    private String user_id;


    public Plan(){

    }

    public Plan(String title, String dateStart, String dateEnd, String plan_id, String user_id) {
        this.title = title;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.plan_id = plan_id;
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
