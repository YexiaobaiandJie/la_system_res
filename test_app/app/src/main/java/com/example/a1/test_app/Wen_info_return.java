package com.example.a1.test_app;

public class Wen_info_return {
    private String La_id; 		   	//委托单编号 主键
    private String La_name;		//委托单名称
    private String La_type;	 	//委托单类型
    private String La_dep;	 	//委托单发起单位
    private String La_creator; 	//委托单发起人
    private String La_user;    	//委托单录入人
    private String La_time;     	//委托单发起时间
    private String La_content;  //委托单内容
    private String La_remark;    //备注
    private String La_opinion;  //部门领导审阅意见
    private String La_opinion_yn; //部门领导审阅结果  no为不同意 yes为同意 change为退回
    private String La_node;    		//当前审核状态  审核状态详见文档
    private String La_prenode; 	//上一个审核状态
    private String La_op_id;    //委托单意见id
    private String La_tri_yn;  //委托单是否为三重一大 no为不是，yes为是，默认为no
    private String La_check_record; //委托单审核记录  即记录审核人，审核部门，审核意见，审核时间
    private String La_todo;
    private String Opinion_content;
    private String La_email;
    private String La_cellphone;

    public void setLa_cellphone(String la_cellphone) {
        La_cellphone = la_cellphone;
    }

    public String getLa_cellphone() {
        return La_cellphone;
    }

    public void setLa_email(String la_email) {
        La_email = la_email;
    }

    public String getLa_email() {
        return La_email;
    }

    public void setLa_content(String la_content) {
        La_content = la_content;
    }

    public void setLa_creator(String la_creator) {
        La_creator = la_creator;
    }

    public void setLa_dep(String la_dep) {
        La_dep = la_dep;
    }

    public void setLa_id(String la_id) {
        La_id = la_id;
    }

    public void setLa_name(String la_name) {
        La_name = la_name;
    }

    public void setLa_time(String la_time) {
        La_time = la_time;
    }

    public void setLa_opinion(String la_opinion) {
        La_opinion = la_opinion;
    }

    public void setLa_remark(String la_remark) {
        La_remark = la_remark;
    }

    public void setLa_opinion_yn(String la_opinion_yn) {
        La_opinion_yn = la_opinion_yn;
    }

    public void setLa_node(String la_node) {
        La_node = la_node;
    }

    public void setLa_check_record(String la_check_record) {
        La_check_record = la_check_record;
    }

    public void setLa_prenode(String la_prenode) {
        La_prenode = la_prenode;
    }

    public void setLa_type(String la_type) {
        La_type = la_type;
    }

    public void setLa_op_id(String la_op_id) {
        La_op_id = la_op_id;
    }

    public void setLa_user(String la_user) {
        La_user = la_user;
    }

    public void setLa_tri_yn(String la_tri_yn) {
        La_tri_yn = la_tri_yn;
    }

    public void setLa_todo(String la_todo) {
        La_todo = la_todo;
    }

    public void setOpinion_content(String opinion_content) {
        Opinion_content = opinion_content;
    }

    public String getLa_user() {
        return La_user;
    }

    public String getLa_content() {
        return La_content;
    }

    public String getLa_creator() {
        return La_creator;
    }

    public String getLa_check_record() {
        return La_check_record;
    }

    public String getLa_dep() {
        return La_dep;
    }

    public String getLa_id() {
        return La_id;
    }

    public String getLa_name() {
        return La_name;
    }

    public String getLa_node() {
        return La_node;
    }

    public String getLa_op_id() {
        return La_op_id;
    }

    public String getLa_opinion() {
        return La_opinion;
    }

    public String getLa_opinion_yn() {
        return La_opinion_yn;
    }

    public String getLa_prenode() {
        return La_prenode;
    }

    public String getLa_remark() {
        return La_remark;
    }

    public String getLa_time() {
        return La_time;
    }

    public String getLa_todo() {
        return La_todo;
    }

    public String getLa_tri_yn() {
        return La_tri_yn;
    }

    public String getLa_type() {
        return La_type;
    }

    public String getOpinion_content() {
        return Opinion_content;
    }

}
