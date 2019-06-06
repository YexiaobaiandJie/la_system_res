package main




type User struct{
	Userid string 	`json:"userid"`
	Password string	`json:"password"`
}

type Userinfo struct{
	User_id string 			`json:"user_id"`
	User_name string		`json:"user_name"`
	User_password string	`json:"user_password"`
	User_sex string 		`json:"user_sex"`
	User_dep string			`json:"user_dep"`
	User_position int		`json:"user_position"`
	User_cellphone string	`json:"user_cellphone"`
	User_email string		`json:"user_email"`
}

type Loginres struct{
	Result int `json:"result"`
	Userinfo Userinfo `json:"userinfo"`
}


type Af_short_info struct{
	La_name string `json:la_name`    	//委托单名称
	La_type string `json:la_type`	 	//委托单类型
	La_creator string `json:la_creator` //委托单发起人
	La_content string `json:la_content` //委托单内容
	File_name string `json:file_name`    //附件属性

}
type Af_wen_info struct{
	La_wen_id string `json:la_wen_id`
	La_wen_name string `json:la_wen_name`
	La_wen_type string `json:la_wen_type`
	La_wen_dep string `json:la_wen_dep`
	La_wen_creator string `json:la_wen_creator`
	La_wen_user string `json:la_wen_user`
	La_wen_time string `json:la_wen_time`
	La_wen_content string `json:la_wen_content`
	La_wen_remark string `json:la_wen_remark`
	La_wen_opinion string `json:la_wen_opinion`
	La_wen_opinion_yn string `json:la_wen_opinion_yn`
	La_wen_node string `json:la_wen_node`
	La_wen_check_record string `josn:la_wen_check_record`
}
type Af_info struct{
	La_id 		string `json:la_id`  	//委托单编号 主键 
	La_name 	string `json:la_name` 	//委托单名称
	La_type 	string `json:la_type` 	//委托单类型
	La_dep 		string `json:la_dep`  	//委托单发起单位
	La_creator 	string `json:la_creator`//委托单发起人
	La_user     string `json:la_user`	//委托单录入人
	La_time     string `json:la_time`	//委托单发起时间
	La_content  string `json:la_content`//委托单内容
	La_remark   string `json:la_remark` //备注
	La_opinion  string `json:la_opinion`//部门领导审阅意见
	La_opinion_yn  string `json:la_opinion`//部门领导审阅结果  no为不同意 yes为同意 change为退回
	La_node    	string `json:la_node`	//当前审核状态  审核状态详见文档
										//初始状态为0  （待部门领导审核状态，此状态下可修改内容）
						//部门领导审核       			同意后，状态为1(待发起人提交至企发部状态)   不同意 状态为7  退回 状态为0
						//发起人点击提交至企发部后       状态为2(待企发部人员审核状态)
						//企发部人员审核后（点击流转）   状态为3(待发起人对意见进行执行判断，每条意见的初始执行状态都为none)
						//发起人对意见逐条进行执行判断后  若全部执行 进入最终状态7        若有意见不执行，状态为4(待部门领导对不执行意见进行审核状态)
						//部门领导审核     				若不存在部门领导同意的不执行意见，进入最终状态7 ，（部门领导不同意的不执行意见全部强制执行）
						//							   若存在部门领导同意不执行意见，状态为5(待总经理对不执行意见进行审核状态)
						//总经理审核      				若不存在总经理同意的不执行意见，进入最终状态7，(总经理不同意的不执行意见全部强制执行)
						//							   若存在总经理同意的不执行意见，状态为6(待院长对不执行意见进行审核状态)
						//院长审核						若不存在院长同意的不执行意见，进入最终状态7(院长不同意的不执行意见全部强制执行)
						//							   若存在院长同意的不执行意见，进入最终状态7(返回发起人，待执行状态，不可修改)

										//
	La_prenode 	string `json:la_prenode`//上一个审核状态
	La_op_id    string `json:la_op_id`  //委托单意见id 
	La_tri_yn   string `json:la_tri_yn` //委托单是否为三重一大 no为不是，yes为是，默认为no
	La_check_record string `json:la_check_record` //委托单审核记录  即记录审核人，审核部门，审核意见，审核时间
	La_todo string `json:la_todo`
}

type Opinion_info struct{
	Opinion_id 	int `json:opinion_id` //意见编号 主键
	La_op_id 	string `json:la_op_id`   //意见id表明所属审批单
	Opinion_creator string `json:opinion_creator` //意见提交人
	Opinion_content string `json:opinion_content` //意见内容
	Opinion_user string `json:opinion_user`       //意见待执行人 即委托单的发起人
	Opinion_exe_yn string `json:opinion_exe_yn`   //意见执行情况 no为不执行 yes为执行 none为待查看 force为强制执行
	Opinion_unexe_res string `json:opinion_unexe_res`  //意见不执行的理由
	Opinion_unexe_status string `json:opinion_exe_status` //意见不执行状态 默认为0 1为部门领导同意 
																			   //2为总经理同意
																			   //3为院长同意
//	Opinion_leader_res string `json:opinion_leader_res`  //领导审批意见
}

type Flsw_list_item struct{
	Item_name string `json:item_name` 		//项目名称
	Item_creator string `json:item_creator`  //项目发起人
	Item_time string `json:item_time`  		//项目上传时间
	Item_status string `json:item_status`    //项目状态
	Item_id     string `json:item_id`        //项目id
	Item_opinion_yn string `json:item_opinion_yn`  //项目领导同意状态
	Item_type string `json:item_type`   //项目类型
}


type Creator struct{
	List_owner string `json:list_owner` //项目发起人
}
type Get_opinion struct{
	Flsw_id string `json:flsw_id`   //审批单id	
}
type Opinion_exe struct{   //返回的意见信息用于用户对每条信息进行执行判定
	Opinion_info []Opinion_info_return `json:opinion_info`//意见数组
	Opinion_count int `json:opinion_count`  //意见的数目
}
type Opinion_info_return struct{
	Opinion_id string `json:opinion_id`
	Opinion_content string `json:opinion_content`
}

type Opinion_info_return_leader struct{ //用于领导对不执行意见进行审核
	Opinion_id string `json:opinion_id`
	Opinion_content string `json:opinion_content`
	Opinion_exe_yn string `json:opinion_exe_yn`
	Opinion_unexe_res string `json:opinion_unexe_res`
}



//领导初次审核时af_info的信息
type Return_af_info struct{
	Flsw_name string `json:flsw_name`  //项目名称
	Flsw_type string `json:flsw_type`  //项目类型
	Flsw_creator string `json:flsw_creator` //项目发起人
	Flsw_time string `json:flsw_time`   //项目发起时间
	Flsw_content string `json:flsw_content` //项目内容
	Flsw_id string 	`json:flsw_id`   //项目id
	Flsw_dep string `json:flsw_dep`  //项目发起人部门
	Flsw_email string `json:flsw_email` //项目发起人邮箱
	Flsw_cellphone string `json:flsw_cellphone`  //项目发起人电话
	Flsw_remark string `json:flsw_remark`   //项目备注
	Flsw_record string `json:flsw_record`   //项目审核记录
	//Flsw_opinions []Opinion_info `json:flsw_opinions`  //项目意见
}

//员工在领导初次审核后af_info的信息
type Return_af_info2 struct{
	Flsw_name string `json:flsw_name`  //项目名称
	Flsw_type string `json:flsw_type`  //项目类型
	Flsw_creator string `json:flsw_creator` //项目发起人
	Flsw_time string `json:flsw_time`   //项目发起时间
	Flsw_content string `json:flsw_content` //项目内容
	Flsw_id string 	`json:flsw_id`   //项目id
	Flsw_dep string `json:flsw_dep`  //项目发起人部门
	Flsw_email string `json:flsw_email` //项目发起人邮箱
	Flsw_cellphone string `json:flsw_cellphone`  //项目发起人电话
	Flsw_remark string `json:flsw_remark`   //项目备注
	Flsw_record string `json:flsw_record`   //项目审核记录
	Flsw_opinion string `json:flsw_opinion` //领导初次审核的意见
}



type User_flsw_id struct{
	Flsw_id string `json:flsw_id`
	User_id string `json:user_id`
}

type Post_opinion_yn struct{
	Opinion string `json:opinion`
	Yn      string `json:yn`	
	Flswid 	string `json:flswid`
	Userid  string `json:userid`
}

type Submit_flsw_id struct{
	Flsw_id string `json:flsw_id`   //用于向企发部提交审批单时的数据传输
}

type Opinions_post struct{
	La_op_id string `json:la_op_id`  //意见所指向的审批单id
	Op_creator string `json:op_creator`  //给出意见的企发部人员id
	Op_content string `json:op_content`  //意见内容
	Op_user string `json:op_user`		 //意见执行者
	Op_exe_yn string `json:op_exe_yn`    //意见是否被执行 yes-执行 no-不执行 none-待查看 force-强制执行
	Op_unexe_res string `json:op_unexe_res` //意见不执行的理由
	Op_unexe_status string `json:op_unexe_status` //不执行意见的状态 默认为0
	Op_tri_yn string `json:op_tri_yn` //										
} 

type Tri_post_str struct{
	La_id 		string `json:la_id`        //审批单id
	La_qifa_id 	string `json:la_qifa_id` //审核审批单的企发部人员id
	La_tri_yn	string `json:la_tri_yn`	//审核单三重一大的状态
}

///////////////////文书类审批单相关//////////////////////////
type Op_wen struct{
	Flsw_wen_id string `json:flsw_wen_id`   //审批单id
	Op_wen_con string `json:op_wen_con`		//意见内容
	Op_creator string `json:op_creator`     //意见创建者
}

type Wen_info_return struct{
	La_id 		string `json:la_id`  	//委托单编号 主键 
	La_name 	string `json:la_name` 	//委托单名称
	La_type 	string `json:la_type` 	//委托单类型
	La_dep 		string `json:la_dep`  	//委托单发起单位
	La_creator 	string `json:la_creator`//委托单发起人
	La_time     string `json:la_time`	//委托单发起时间
	La_content  string `json:la_content`//委托单内容
	La_remark   string `json:la_remark` //备注
	La_cellphone string `json:la_cellphone` //电话
	La_email string `json:la_email`   //邮件
	La_check_record string `json:la_check_record` //委托单审核记录  即记录审核人，审核部门，审核意见，审核时间	
	Opinion_content string `json:content` //委托单所对应的所有意见总和
}

type Op_status_get struct{
	Op_id string `json:op_id`   //意见id
	Op_exe string `json:op_exe` //意见是否执行
	Op_res string `json:op_res` //意见理由
}