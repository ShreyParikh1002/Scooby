package com.example.scooby;

public class task_struc {
//    bcoz default scope in java is package private
    public String task;
    public String time;
    public String tag;
    public String hour;
    public task_struc(){ }
    public task_struc(String task,String time,String tag,String hour){
        this.task=task;
        this.time=time;
        this.tag=tag;
        this.hour=hour;
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

    public String getHour() {
        return hour;
    }
}
