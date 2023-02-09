package com.example.scooby;

public class firestorestruc {
    private String task;
    private String time;
    private String tag;
    public firestorestruc(){ }
    public firestorestruc(String task,String time,String tag){
        this.tag=tag;
        this.task=task;
        this.time=time;
    }

    public String getTask() {
        return task;
    }

    public String getTime() {
        return time;
    }

    public String getTag() {
        return tag;
    }
}
