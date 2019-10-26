package com.tap.taskassigningandplanning.utils.activities;

import android.widget.SeekBar;

import com.google.firebase.Timestamp;

import java.util.List;

public class Activities {

    private String title, notes, dateStart, dateEnd, plan_id;
    private int progress;
    List<String> user_id;
    private Timestamp created;

    public Activities(){
        //empty constructor needed
    }

    public Activities(String title, String notes, String dateStart, String dateEnd, String plan_id, List<String> user_id, Timestamp created) {
        this.title = title;
        this.notes = notes;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.plan_id = plan_id;
        this.user_id = user_id;
        this.created = created;
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public List<String> getUser_id() {
        return user_id;
    }

    public void setUser_id(List<String> user_id) {
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
        return "Activities{" +
                "title='" + title + '\'' +
                ", notes='" + notes + '\'' +
                ", dateStart='" + dateStart + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", plan_id='" + plan_id + '\'' +
                ", user_id=" + user_id +
                ", created=" + created +
                '}';
    }
}
