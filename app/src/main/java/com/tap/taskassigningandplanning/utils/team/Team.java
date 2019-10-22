package com.tap.taskassigningandplanning.utils.team;

public class Team {
    public String email, plan_id, name, user_id;
    private boolean status;

    public Team(){
        //Must be empty
    }

    public Team(String email, String plan_id, String name, String user_id, boolean status) {
        this.email = email;
        this.plan_id = plan_id;
        this.name = name;
        this.user_id = user_id;
        this.status = status;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Team{" +
                "email='" + email + '\'' +
                ", plan_id='" + plan_id + '\'' +
                ", name='" + name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", status=" + status +
                '}';
    }
}
