package main

import (
		"github.com/gin-gonic/gin"
		_"github.com/Go-SQL-Driver/MySQL"
		)
		


func main(){
	r :=gin.Default()
	r.POST("/login",Login)
	r.POST("/create_af",Cre_af)
	r.POST("/flsw_list",Flsw_list)
	r.POST("/flsw_todo_list",Flsw_todo_list)
	r.POST("/flsw_info_leader",Flsw_info_leader)
	r.POST("/flsw_opinion_yn",Flsw_opinion_yn)
	r.POST("/flsw_info_result",Flsw_info_result)
	r.POST("/submit_qifa",Submit_qifa)
	r.POST("/flsw_info_qifa",Flsw_info_qifa)
	r.POST("/opinion_post",Opinion_post)
	r.POST("/tri_post",Tri_post)
	r.POST("/get_opinion",Get_op)
	r.POST("/get_opinion_leader",Get_opinion_leader)//部门领导对不执行意见进行审核
	r.POST("/op_status_sub",Op_status_sub)  //委托人对意见执行进行判断
	r.POST("/check_la_op",Check_la_op)//查看不执行意见数量，修改审批单状态
	//r.POST("/create_")
	//r.POST("/create_af_wen",Cre_af_wen)
	r.POST("/post_wen_opinion",Post_wen_opinion)
	r.POST("/post_wen_result",Post_wen_result)
	//法人申请相关
	r.POST("/create_lp",Create_lp)
	r.POST("/get_lp_all_info",Get_lp_all_info)
	r.POST("/lp_opinion_yn",Lp_opinion_yn)
	r.POST("/lp_submit_qifa",Lp_submit_qifa)
	r.POST("/lp_submit_jy",Lp_submit_jy)
	r.POST("/lp_return_cre",Lp_return_cre)
	r.POST("/lp_submit_fw",Lp_submit_fw)
	r.POST("/lp_submit_qf_master",Lp_submit_qf_master)
	r.POST("/lp_submit_fg",Lp_submit_fg)
	r.POST("/lp_submit_z",Lp_submit_z)
	r.POST("/lp_submit_over",Lp_submit_over)
	//管理员相关
	r.POST("/admin_cre_user",Admin_cre_user)
	r.POST("/admin_user_list",Get_user_list)
	r.POST("/admin_del_user",Admin_del_user)
	r.POST("/admin_chg_pos",Admin_chg_pos)
	//上传文件
	r.POST("/get_upload_file",Get_upload_file)
	//下载文件
	r.GET("/post_download_file",Post_download__file)
	//用户相关
	r.POST("/change_pwd",Change_pwd)
	r.POST("/change_phone",Change_phone)
	r.POST("/change_email",Change_email)
	//返回审批意见结果
	r.POST("/op_res",Op_Res)
	r.Run(":3000")


	




}