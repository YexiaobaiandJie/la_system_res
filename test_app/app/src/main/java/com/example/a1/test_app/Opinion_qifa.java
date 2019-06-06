package com.example.a1.test_app;

public class Opinion_qifa {
    private String la_op_id;  //意见所指向的审批单id 也就是审批单自身的id
    private String op_creator; //给出意见的企发部人员id
    private String op_content; //意见内容
    private String op_user;    //意见执行者
    private String op_exe_yn;  //意见是否被执行 yes-执行   no-不执行 none-待查看 force-强制执行
    private String op_unexe_res; //意见不执行的理由
    private String op_unexe_status; //不执行意见的状态 默认为0 部门领导同意为1 总经理同意为2 院长同意为3
    private String op_tri_yn;     //审批单是否为三重一大

    public void setOp_tri_yn(String op_tri_yn) {
        this.op_tri_yn = op_tri_yn;
    }

    public String getOp_tri_yn() {
        return op_tri_yn;
    }

    public void setLa_op_id(String la_op_id) {
        this.la_op_id = la_op_id;
    }

    public void setOp_content(String op_content) {
        this.op_content = op_content;
    }

    public void setOp_creator(String op_creator) {
        this.op_creator = op_creator;
    }

    public void setOp_exe_yn(String op_exe_yn) {
        this.op_exe_yn = op_exe_yn;
    }

    public void setOp_unexe_res(String op_unexe_res) {
        this.op_unexe_res = op_unexe_res;
    }

    public void setOp_unexe_status(String op_unexe_status) {
        this.op_unexe_status = op_unexe_status;
    }

    public void setOp_user(String op_user) {
        this.op_user = op_user;
    }

    public String getOp_exe_yn() {
        return op_exe_yn;
    }

    public String getOp_user() {
        return op_user;
    }

    public String getLa_op_id() {
        return la_op_id;
    }

    public String getOp_content() {
        return op_content;
    }

    public String getOp_creator() {
        return op_creator;
    }

    public String getOp_unexe_res() {
        return op_unexe_res;
    }

    public String getOp_unexe_status() {
        return op_unexe_status;
    }
}
