package com.tap.taskassigningandplanning.utils.team;

import com.google.firebase.Timestamp;

public class Team {
    public String email, plan_id, name, user_id, plan_name, creator, creator_id, request;
    private boolean status;
    private int rating, task_completed, total_activity;
    private Timestamp created;

    public Team(){
        //Must be empty
    }

    public Team(String email, String plan_id, String name, String user_id, String plan_name, String creator, String creator_id, String request, int task_completed, boolean status, Timestamp created) {
        this.email = email;
        this.plan_id = plan_id;
        this.name = name;
        this.user_id = user_id;
        this.plan_name = plan_name;
        this.creator = creator;
        this.creator_id = creator_id;
        this.request = request;
        this.status = status;
        this.created = created;
        this.rating = rating;
        this.total_activity = total_activity;
        this.task_completed = task_completed;
    }

    public int getTotal_activity() {
        return total_activity;
    }

    public void setTotal_activity(int total_activity) {
        this.total_activity = total_activity;
    }

    public int getTask_completed() {
        return task_completed;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public void setTask_completed(int task_completed) {
        this.task_completed = task_completed;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Team{" +
                "email='" + email + '\'' +
                ", plan_id='" + plan_id + '\'' +
                ", name='" + name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", plan_name='" + plan_name + '\'' +
                ", creator='" + creator + '\'' +
                ", request='" + request + '\'' +
                ", status=" + status +
                ", rating=" + rating +
                ", task_completed=" + task_completed +
                ", created=" + created +
                '}';
    }
}
