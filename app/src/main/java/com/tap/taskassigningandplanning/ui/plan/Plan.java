package com.tap.taskassigningandplanning.ui.plan;

public class Plan {
    public String name, dateStart, dateEnd, id;

    public Plan(){

    }

    public Plan(String name, String email, String password) {
        this.name = name;
        this.dateStart = email;
        this.dateEnd = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
