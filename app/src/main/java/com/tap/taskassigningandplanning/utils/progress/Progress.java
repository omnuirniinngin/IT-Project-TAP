package com.tap.taskassigningandplanning.utils.progress;

import com.google.type.Date;

public class Progress {

    String title;

    public Progress(){
        //Must be empty
    }

    public Progress(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Progress{" +
                "title='" + title + '\'' +
                '}';
    }
}
