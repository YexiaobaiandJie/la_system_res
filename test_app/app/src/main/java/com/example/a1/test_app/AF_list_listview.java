package com.example.a1.test_app;

public class AF_list_listview {
    private String Item_name;
    private String Item_creator;
    private String Item_time;
    private String Item_status;
    private String Item_id;
    private String Item_opinion_yn;
    private String Item_type;


    public AF_list_listview(String name,String creator,String time,String status){
        this.Item_name = name;
        this.Item_creator = creator;
        this.Item_time = time;
        this.Item_status = status;
    }
    public AF_list_listview(String name,String creator,String time,String status,String id,String opinion_yn,String type){
        this.Item_name = name;
        this.Item_creator = creator;
        this.Item_time = time;
        this.Item_status = status;
        this.Item_id = id;
        this.Item_opinion_yn = opinion_yn;
        this.Item_type = type;
    }

    public String getname(){
        return this.Item_name;
    }
    public String getcreator(){
        return this.Item_creator;
    }
    public String gettime(){
        return this.Item_time;
    }
    public String getid(){  return this.Item_id; }
    public String getstatus(){
        return this.Item_status;
    }
    public String get_opinion_yn(){return this.Item_opinion_yn;}

    public String getItem_type() {
        return Item_type;
    }
}
