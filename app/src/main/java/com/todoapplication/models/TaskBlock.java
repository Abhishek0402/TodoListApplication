package com.todoapplication.models;

public class TaskBlock {

   // private int id;
    private String task;
    private boolean status;

    public TaskBlock(String task, boolean status) {
     //   this.id = id;
        this.task = task;
        this.status = status;
    }

//    public void setId(int id){
//        this.id = id;
//    }
//
//    public int getId() {
//        return id;
//    }

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
