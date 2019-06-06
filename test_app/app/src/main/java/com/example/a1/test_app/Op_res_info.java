package com.example.a1.test_app;

public class Op_res_info {
    private String Op_id;
    private String Op_content;
    private String Op_exe_yn;
    private String Op_status;

    public Op_res_info(String Op_id,String Op_content,String Op_exe_yn,String Op_status){
        this.Op_id = Op_id;
        this.Op_content = Op_content;
        this.Op_exe_yn = Op_exe_yn;
        this.Op_status = Op_status;
    }


    public void setOp_id(String op_id) {
        Op_id = op_id;
    }

    public void setOp_content(String op_content) {
        Op_content = op_content;
    }

    public void setOp_exe_yn(String op_exe_yn) {
        Op_exe_yn = op_exe_yn;
    }

    public void setOp_status(String op_status) {
        Op_status = op_status;
    }

    public String getOp_id() {
        return Op_id;
    }

    public String getOp_content() {
        return Op_content;
    }

    public String getOp_exe_yn() {
        return Op_exe_yn;
    }

    public String getOp_status() {
        return Op_status;
    }
}
