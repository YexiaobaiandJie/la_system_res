package com.example.a1.test_app;

public class Post_opinion_yn {
  private String opinion;
  private String yn;
  private String flswid;
  private String userid;
  public void set_opinion(String opinion){
    this.opinion = opinion;
  }
  public void set_yn(String yn){
    this.yn = yn;
  }
  public void set_flswid(String flswid){
    this.flswid = flswid;
  }
  public void set_userid(String userid){
    this.userid = userid;
  }
  public String get_flswid(){
    return this.flswid;
  }
  public String get_userid(){
    return this.userid;
  }
  public String get_opinion(){
    return this.opinion;
  }
  public String get_yn(){
    return this.yn;
  }
}
