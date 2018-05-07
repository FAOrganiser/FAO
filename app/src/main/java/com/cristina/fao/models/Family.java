package com.cristina.fao.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Family {

    private String adminId;
    private String name;
    private List<String> users = new ArrayList<>();

    public Family() {
    }

    public Family(String adminId, String name, List<String> users) {
        this.adminId = adminId;
        this.name = name;
        Log.i("nume", name);
        this.users = users;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public HashMap<String, Object> familyToMap() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("adminId", adminId);
        hashMap.put("name", name);
        hashMap.put("users", users);

        return hashMap;
    }
}
