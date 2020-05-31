package com.todoapplication.models;

public class TaskBlock {

    private String task;
    private boolean status;

    public TaskBlock(String task, boolean status) {
        this.task = task;
        this.status = status;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setStatus(boolean status){
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public boolean getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "TaskBlock{" +
                ", task='" + task + '\'' +
                ", status=" + status +
                '}';
    }

}
