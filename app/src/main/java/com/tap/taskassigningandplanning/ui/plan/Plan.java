package com.tap.taskassigningandplanning.ui.plan;

import com.google.firebase.Timestamp;

public class Plan {

    private String title, dateStart, dateEnd, plan_id, user_id;
    private Timestamp created;


    public Plan(){
        // Must be empty
    }

    public Plan(String title, String dateStart, String dateEnd, String plan_id, String user_id, Timestamp created) {
        this.title = title;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.plan_id = plan_id;
        this.user_id = user_id;
        this.created = created;
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

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "title='" + title + '\'' +
                ", dateStart='" + dateStart + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", plan_id='" + plan_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", created=" + created +
                '}';
    }
}
