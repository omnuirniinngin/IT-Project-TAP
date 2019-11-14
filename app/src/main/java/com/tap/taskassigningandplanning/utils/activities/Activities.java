package com.tap.taskassigningandplanning.utils.activities;

import android.widget.SeekBar;

import com.google.firebase.Timestamp;

import java.util.List;

public class Activities {

    private String title, notes, dateStart, dateEnd, plan_id, creator;
    private int completed_task;
    private int total_task;
    private boolean completed;
    List<String> user_id;
    private Timestamp created;

    public Activities(){
        //empty constructor needed
    }

    public Activities(String title, String dateStart, String dateEnd, String plan_id, String creator, List<String> user_id, Timestamp created) {
        this.title = title;
        this.notes = notes;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.plan_id = plan_id;
        this.creator = creator;
        this.user_id = user_id;
        this.created = created;
        this.completed = completed;
        this.completed_task = completed_task;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getCompleted_task() {
        return completed_task;
    }

    public void setCompleted_task(int completed_task) {
        this.completed_task = completed_task;
    }

    public int getTotal_task() {
        return total_task;
    }

    public void setTotal_task(int total_task) {
        this.total_task = total_task;
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
                ", creator='" + creator + '\'' +
                ", completed_task=" + completed_task +
                ", total_task=" + total_task +
                ", completed=" + completed +
                ", user_id=" + user_id +
                ", created=" + created +
                '}';
    }
}
