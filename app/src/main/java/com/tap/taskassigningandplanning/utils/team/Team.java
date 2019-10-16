package com.tap.taskassigningandplanning.utils.team;

public class Team {
    public String email, plan_id;

    public Team(){
        //Must be empty
    }

    public Team(String email, String plan_id) {
        this.email = email;
        this.plan_id = plan_id;
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

    @Override
    public String toString() {
        return "Team{" +
                "email='" + email + '\'' +
                ", plan_id='" + plan_id + '\'' +
                '}';
    }
}
