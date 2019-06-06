package com.example.a1.test_app;

public class Opinion_info_short {
    private String Opinion_id;  //意见的id
    private String Opinion_content; //意见的内容
    private String Opinion_exe_yn;  //意见是否执行
    private String Opinion_unexe_res; //意见不执行的理由


    public Opinion_info_short(String op_id,String op_content){
        this.Opinion_id = op_id;
        this.Opinion_content = op_content;
    }


    public void setOpinion_content(String op_content) {
        this.Opinion_content = op_content;
    }

    public void setOpinion_id(String op_id) {
        this.Opinion_id = op_id;
    }

    public String getOpinion_content() {
        return Opinion_content;
    }

    public String getOpinion_id() {
        return Opinion_id;
    }

    public void setOpinion_exe_yn(String opinion_exe_yn) {
        Opinion_exe_yn = opinion_exe_yn;
    }

    public void setOpinion_unexe_res(String opinion_unexe_res) {
        Opinion_unexe_res = opinion_unexe_res;
    }

    public String getOpinion_exe_yn() {
        return Opinion_exe_yn;
    }

    public String getOpinion_unexe_res() {
        return Opinion_unexe_res;
    }

}
