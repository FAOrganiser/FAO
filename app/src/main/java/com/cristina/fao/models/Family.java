package com.cristina.fao.models;

import java.util.ArrayList;

public class Family {

    ArrayList<User> mUsers = new ArrayList<>();
    User mAdmin;

    public Family() {}

    public Family(ArrayList<User> users, User admin) {
        mAdmin = admin;
    }
}
