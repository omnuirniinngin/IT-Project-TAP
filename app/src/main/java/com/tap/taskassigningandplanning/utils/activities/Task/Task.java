package com.tap.taskassigningandplanning.utils.activities.Task;

import com.google.firebase.Timestamp;

import java.util.List;

public class Task {

    private String title, activity_id, plan_id;
    List<String> user_id;
    private boolean completed;
    private Timestamp created;

    public Task() {
    }

    public Task(String title, String activity_id, String plan_id, List<String> user_id, boolean completed, Timestamp created) {
        this.title = title;
        this.activity_id = activity_id;
        this.plan_id = plan_id;
        this.user_id = user_id;
        this.completed = completed;
        this.created = created;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public List<String> getUser_id() {
        return user_id;
    }

    public void setUser_id(List<String> user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", activity_id='" + activity_id + '\'' +
                ", completed=" + completed +
                ", created=" + created +
                '}';
    }
}