package com.tap.taskassigningandplanning.ui.plan_joined;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanJoined {

    String dateStart, dateEnd, title, plan_id;

    public PlanJoined(){

    }

    public PlanJoined(String dateStart, String dateEnd, String title, String plan_id) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.title = title;
        this.plan_id = plan_id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    @Override
    public String toString() {
        return "PlanJoined{" +
                "dateStart='" + dateStart + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", title='" + title + '\'' +
                ", plan_id='" + plan_id + '\'' +
                '}';
    }
}
