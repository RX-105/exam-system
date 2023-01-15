package io.n0sense.examsystem.entity;

public class AdminBuilder{
    private final Admin admin;
    public AdminBuilder(){
        admin = new Admin();
    }
    public AdminBuilder name(String name){
        this.admin.setName(name);
        return this;
    }
    public AdminBuilder password(String password){
        this.admin.setPassword(password);
        return this;
    }
    public AdminBuilder groupName(String groupName){
        this.admin.setGroupName(groupName);
        return this;
    }
    public Admin get() {
        return admin;
    }
}