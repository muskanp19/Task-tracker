package com.example.myapplication;

import java.util.Date;

public class AddTaskHelperClass {
    String taskName, taskDesc,dueDate;
    boolean completed;


    public AddTaskHelperClass() {

    }

    public AddTaskHelperClass(String taskName, String taskDesc, String dueDate, boolean completed) {
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
