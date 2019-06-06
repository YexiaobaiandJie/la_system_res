package com.example.a1.test_app;

public class chg_pwd_struct {
    private String userid;
    private String oldpwd;
    private String newpwd;

    public void setNewpwd(String newpwd) {
        this.newpwd = newpwd;
    }

    public void setOldpwd(String oldpwd) {
        this.oldpwd = oldpwd;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNewpwd() {
        return newpwd;
    }

    public String getOldpwd() {
        return oldpwd;
    }

    public String getUserid() {
        return userid;
    }
}
