package com.example.a1.test_app;

public class User_admin {
    private String User_id;
    private String User_name;
    private String User_password;
    private String User_sex;
    private String User_dep;
    private int User_position;
    private String User_cellphone;
    private String User_email;

    public User_admin(String userid,String username,String userpassword,String usersex,String userdep,int userposition,String user_cellphone,String user_email){
        this.User_id = userid;
        this.User_name = username;
        this.User_password = userpassword;
        this.User_sex = usersex;
        this.User_dep = userdep;
        this.User_position = userposition;
        this.User_cellphone = user_cellphone;
        this.User_email = user_email;
    }

    public String getUserid() {
        return User_id;
    }

    public int getUserposition() {
        return User_position;
    }

    public String getUsername() {
        return User_name;
    }

    public String getUserdep() {
        return User_dep;
    }

    public int getUser_position() {
        return User_position;
    }

    public String getUser_cellphone() {
        return User_cellphone;
    }

    public String getUser_dep() {
        return User_dep;
    }

    public String getUser_email() {
        return User_email;
    }

}
