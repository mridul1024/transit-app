package com.example.gaijinsmash.transitapp.model.user;

/**
 * Created by ryanj on 9/8/2017.
 */

public class User {

    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mPassword;
    private String mPasswordConfirm;

    //TODO: Add Facebook, Google, and Twitter API

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setmPassword(String password) {
        mPassword = password;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        mPasswordConfirm = passwordConfirm;
    }

    public String getFirstName() { return mFirstName; }
    public String getLastName() { return mLastName; }
    public String getEmail() { return mEmail; }
    public String getPassword() { return mPassword; }
}
