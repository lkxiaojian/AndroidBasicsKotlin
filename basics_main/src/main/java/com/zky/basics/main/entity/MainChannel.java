package com.zky.basics.main.entity;


public enum MainChannel {
    NEWS(0,"NEWS"), FIND(1,"FIND"),ME(2,"ME");
    public int id;
    public String name;
    MainChannel(int id, String name){
        this.id = id;
        this.name = name;
    }
}
