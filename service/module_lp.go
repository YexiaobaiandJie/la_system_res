package main

type Cre_lp_post struct{
	Lp_name string `json:lp_name`
	Lp_type string `json:lp_type`
	Lp_creator string `json:lp_creator`
	Lp_user string `json:lp_user`
	Lp_date string `json:lp_date`
	Lp_remark string `json:lp_remark`
	Lp_range string `json:lp_range`
	Lp_tou string `json:lp_tou`
}

type Lp_all_info struct{
	Lp_id string `json:lp_id`           //
	Lp_name string `json:lp_name`
	Lp_type string `json:lp_type`
	Lp_tou string `json:lp_tou`
	Lp_creator string `json:lp_creator`
	Lp_cre_dep string `json:lp_cre_dep`   //
	Lp_use_dep string `json:lp_use_dep`   //
	Lp_user string `json:lp_user`
	Lp_range string `json:lp_range`
	Lp_date string `json:lp_date`
	Lp_time string `json:time`           //
	Lp_remark string `json:lp_remark`
	Lp_todo string `json:lp_todo`        //
	Lp_node string `json:lp_node`   //创建完毕时状态为0 待部门领导审核
									//领导同意 状态为1 yes one
	Lp_opinion_yn string `json:lp_opinion_yn`  //部门领导意见 默认为0 no two （刚创建完成）  
												//同意即           1 yes one (等待申请人提交企发部)
												//不同意即         7b no one (等待申请人结束流转)  
												//退回即           0 change one (等待申请人修改)
												/////////////////////////////////////////
												//状态一览
												//提交至企发部   2b yes three
												//企发部同意     3b yes six    不同意  7b yes one
												//经营科同意     4b yes seven  不同意  7b yes one
												//法务科同意     5b yes eight  不同意  7b yes one
												//企发部主任同意（可修改是否投标） 6b yes nine 不同意7b yes one
												//分管副总经理同意 7b yes four 不同意 7b yes one 
												//总经理同意    8b yes one (流转至经办人)   不同意7b yes one 
	Lp_opinion string `json:lp_opinion`//
	}

type Get_lp_id struct{
	Flsw_id string `json:flsw_id`    //法人委托id
}


type Get_fenguan_tou struct{
	Lp_id string `json:lp_id`
	Tou_result string `json:tou_result`
}