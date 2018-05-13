package com.example.android.lovemeter;

public class UserModel {
    private String mFirstName;
    private String mSecondName;
    private String mUserName;
    private String mEmail;
    private String mPhone;
    private String mPassword;
    private String mProfilePicture;

    public UserModel() {
    }

    public UserModel(String mFirstName, String mSecondName, String mUserName, String mEmail, String mPhone, String mPassword, String mProfilePicture) {
        this.mFirstName = mFirstName;
        this.mSecondName = mSecondName;
        this.mUserName = mUserName;
        this.mEmail = mEmail;
        this.mPhone = mPhone;
        this.mPassword = mPassword;
        this.mProfilePicture = mProfilePicture;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmSecondName() {
        return mSecondName;
    }

    public void setmSecondName(String mSecondName) {
        this.mSecondName = mSecondName;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmProfilePicture() {
        return mProfilePicture;
    }

    public void setmProfilePicture(String mProfilePicture) {
        this.mProfilePicture = mProfilePicture;
    }
}
