package main 

import(
	"fmt"
	"github.com/gin-gonic/gin"
	"database/sql"
	_"github.com/Go-SQL-Driver/MySQL"
	"time"
	"strconv"
)


func Create_lp(cq *gin.Context){
	var cre_lp Cre_lp_post
	var Lp_info Lp_all_info
	var dep_num string
	var dep_name string
	var dep_num2 string
	var dep_name2 string
	if cq.ShouldBind(&cre_lp) == nil{
		if cre_lp.Lp_name == "" || cre_lp.Lp_type == "" || cre_lp.Lp_creator == "" || cre_lp.Lp_user == "" || cre_lp.Lp_date == "" || cre_lp.Lp_range == "" || cre_lp.Lp_tou == ""{
			fmt.Println("something is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			t :=time.Now() 
			Lp_info.Lp_id = strconv.FormatInt(t.Unix(),10)
			Lp_info.Lp_name = cre_lp.Lp_name
			
			if cre_lp.Lp_type == "单项法人申请"{
				if cre_lp.Lp_tou == "yes"{
					Lp_info.Lp_type = "单项投标"
					Lp_info.Lp_tou="投标"
				
				}else if cre_lp.Lp_tou == "no"{
					Lp_info.Lp_type = "单项不投标"
					Lp_info.Lp_tou = "不投标"
				}
			}else if cre_lp.Lp_type == "常年法人申请"{
				Lp_info.Lp_type = "常年法人"
				Lp_info.Lp_tou = ""
			}
			
			
			Lp_info.Lp_creator = cre_lp.Lp_creator
			//查询委托人部门
			err2 :=db.QueryRow("SELECT user_dep FROM users WHERE user_id=?",cre_lp.Lp_creator).Scan(&dep_num)
			if err2 !=nil{
				fmt.Println("查询委托人部门id出错")
			}
			err3 :=db.QueryRow("SELECT Dep_name FROM department WHERE Dep_id=?",dep_num).Scan(&dep_name)
			if err3 !=nil{
				fmt.Println("查询委托人部门name出错")
			}
			Lp_info.Lp_cre_dep = dep_name
			err4 :=db.QueryRow("SELECT user_dep FROM users WHERE user_name=?",cre_lp.Lp_user).Scan(&dep_num2)
			if err4 !=nil{
				fmt.Println("查询经办人部门id出错")
			}
			err5 :=db.QueryRow("SELECT Dep_name FROM department WHERE Dep_id=?",dep_num2).Scan(&dep_name2)
			if err5 != nil{
				fmt.Println("查询经办人部门name出错")
			}
			Lp_info.Lp_use_dep = dep_name2
			Lp_info.Lp_user = cre_lp.Lp_user
			Lp_info.Lp_range = cre_lp.Lp_range
			Lp_info.Lp_date = cre_lp.Lp_date
			timeUnix :=time.Now().Unix()
			Lp_info.Lp_time =  time.Unix(timeUnix,0).Format("2006-01-02 15:04:05")
			Lp_info.Lp_remark = cre_lp.Lp_remark
			Lp_info.Lp_todo = "two"
			Lp_info.Lp_node = "0"
			Lp_info.Lp_opinion_yn = "no"
			Lp_info.Lp_opinion = ""
			

			//上传至数据库
			stmt,err6:=db.Prepare("INSERT lps SET Lp_id=?,Lp_name=?,Lp_type=?, Lp_tou=?,Lp_creator=?, Lp_cre_dep=?,Lp_use_dep=?,Lp_user=?,Lp_range=?,Lp_date=?,Lp_time=?,Lp_remark=?,Lp_todo=?,Lp_node=?,Lp_opinion_yn=?,Lp_opinion=?")
				if err6 !=nil{
					panic(err6)
				}
				res,err7:=stmt.Exec(Lp_info.Lp_id,
									Lp_info.Lp_name,
									Lp_info.Lp_type,
									Lp_info.Lp_tou,
									Lp_info.Lp_creator,
									Lp_info.Lp_cre_dep,
									Lp_info.Lp_use_dep,
									Lp_info.Lp_user,
									Lp_info.Lp_range,
									Lp_info.Lp_date,
									Lp_info.Lp_time,
									Lp_info.Lp_remark,
									Lp_info.Lp_todo,
									Lp_info.Lp_node,
									Lp_info.Lp_opinion_yn,
									Lp_info.Lp_opinion)
				if err7 !=nil{
					panic(err7)
					cq.JSON(400,"创建法人申请失败")
				}else{
					cq.JSON(200,"创建法人申请成功")
					
				}
				fmt.Println(res.LastInsertId())



		}
	}
}


func Get_lp_all_info(cq *gin.Context){
	var get_lp_id Get_lp_id
	var Lp_info_return Lp_all_info
	if cq.ShouldBind(&get_lp_id) == nil{
		lp_id :=get_lp_id.Flsw_id
		if lp_id == ""{
			fmt.Println("lp_id=",lp_id)
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			err2 :=db.QueryRow("SELECT * FROM lps WHERE Lp_id=?",lp_id).Scan(&Lp_info_return.Lp_id,
																			 &Lp_info_return.Lp_name,
																			 &Lp_info_return.Lp_type,
																			 &Lp_info_return.Lp_tou,
																			 &Lp_info_return.Lp_creator,
																			 &Lp_info_return.Lp_cre_dep,
																			 &Lp_info_return.Lp_use_dep,
																			 &Lp_info_return.Lp_user,
																			 &Lp_info_return.Lp_range,
																			 &Lp_info_return.Lp_date,
																			 &Lp_info_return.Lp_time,
																			 &Lp_info_return.Lp_remark,
																			 &Lp_info_return.Lp_todo,
																			 &Lp_info_return.Lp_node,
																			 &Lp_info_return.Lp_opinion_yn,
																			 &Lp_info_return.Lp_opinion)
			if err2 !=nil{
				fmt.Println("导出lp数据失败")
			}else{
				cq.JSON(200,Lp_info_return)
			}

		}
	}
	
}

func Lp_opinion_yn(cq *gin.Context){
	var post_info Post_opinion_yn

	if cq.ShouldBind(&post_info) == nil{
		Opinion := post_info.Opinion
		Yn := post_info.Yn
		Flswid :=post_info.Flswid
		Userid :=post_info.Userid
		fmt.Println(Opinion)
		fmt.Println(Yn)
		fmt.Println(Flswid)
		fmt.Println(Userid)
		if Opinion == "" || Yn == "" || Flswid == "" || Userid == ""{
			fmt.Println("something is empty")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				panic(err)
			}
			if Yn == "yes"{
				//node改为1
				//opinion_yn 改为yes
				//todo改为one
				result,err2 :=db.Exec("UPDATE lps SET Lp_node=?,Lp_opinion_yn=?,Lp_todo=? WHERE Lp_id=?","1","yes","one",Flswid)
				if err2 !=nil{
					fmt.Println("修改法人申请状态出错")
				}
				fmt.Println(result.RowsAffected())
			}else if Yn == "no"{
				//node改为7b
				//opinion_yn改为no
				//todo改为one
				result,err2 :=db.Exec("UPDATE lps SET Lp_node=?,Lp_opinion_yn=?,Lp_todo=? WHERE Lp_id=?","7b","no","one",Flswid)
				if err2 !=nil{
					fmt.Println("修改法人申请状态出错")
				}
				fmt.Println(result.RowsAffected())
			}else if Yn == "change"{
				//node改为-1
				//opinion_yn改为change
				//todo改为one
				result,err2 :=db.Exec("UPDATE lps SET Lp_node=?,Lp_opinion_yn=?,Lp_todo=? WHERE Lp_id=?","-1","change","one",Flswid)
				if err2 !=nil{
					fmt.Println("修改法人申请状态出错")
				}
				fmt.Println(result.RowsAffected())
			}
			//把opinion放入其中
			result3,err3 :=db.Exec("UPDATE lps SET Lp_opinion=? WHERE Lp_id=?",Opinion,Flswid)
			if err3 !=nil{
				panic(err3)
				fmt.Println("修改法人申请意见出错")
			}
			fmt.Println(result3.RowsAffected())



		}
	}
}


func Lp_submit_qifa(cq *gin.Context){
	var flsw_info Submit_flsw_id
	if cq.ShouldBind(&flsw_info) == nil{
		Flsw_id :=flsw_info.Flsw_id
		if Flsw_id == ""{
			fmt.Println("法人申请id为空")
			cq.JSON(400,"LP_ID IS EMPTY!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			//修改法人申请状态
			result,err3 :=db.Exec("UPDATE lps SET Lp_node=? ,Lp_todo=? WHERE Lp_id=?","2b","three",Flsw_id)
			fmt.Println(result.RowsAffected())	
			if err3 !=nil{
					fmt.Println("修改法人申请状态出错")
			}else{
				cq.JSON(200,"operate succeed!")
			}
				
		}

	}
}
//如果企发部同意，提交至经营科
func Lp_submit_jy(cq *gin.Context){
	var flsw_info Submit_flsw_id
	if cq.ShouldBind(&flsw_info) == nil{
		Flsw_id :=flsw_info.Flsw_id
		if Flsw_id == ""{
			fmt.Println("法人申请id为空")
			cq.JSON(400,"lp_id is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			//修改法人申请状态
			result,err3 :=db.Exec("UPDATE lps SET Lp_node=? ,Lp_todo=? WHERE Lp_id=?","3b","six",Flsw_id)
			fmt.Println(result.RowsAffected())	
			if err3 !=nil{
					fmt.Println("修改法人申请状态出错")
			}else{
				cq.JSON(200,"operate succeed!")
			}
		}
	}
}


//如果不同意，返回至发起人
func Lp_return_cre(cq *gin.Context){
	var flsw_info Submit_flsw_id
	if cq.ShouldBind(&flsw_info) == nil{
		Flsw_id :=flsw_info.Flsw_id
		if Flsw_id == ""{
			fmt.Println("法人申请id为空")
			cq.JSON(400,"lp_id is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			//修改法人申请状态
			result,err3 :=db.Exec("UPDATE lps SET Lp_node=? ,Lp_todo=? WHERE Lp_id=?","9b","one",Flsw_id)
			fmt.Println(result.RowsAffected())	
			if err3 !=nil{
					fmt.Println("修改法人申请状态出错")
			}else{
				cq.JSON(200,"operate succeed!")
			}
		}
	}
}

//如果经营科同意，提交至法务科
func Lp_submit_fw(cq *gin.Context){
	var flsw_info Submit_flsw_id
	if cq.ShouldBind(&flsw_info) == nil{
		Flsw_id :=flsw_info.Flsw_id
		if Flsw_id == ""{
			fmt.Println("法人申请id为空")
			cq.JSON(400,"lp_id is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			//修改法人申请状态
			result,err3 :=db.Exec("UPDATE lps SET Lp_node=? ,Lp_todo=? WHERE Lp_id=?","4b","seven",Flsw_id)
			fmt.Println(result.RowsAffected())	
			if err3 !=nil{
					fmt.Println("修改法人申请状态出错")
			}else{
				cq.JSON(200,"operate succeed!")
			}
		}
	}
}

//如果法务科同意，提交至企发部主任
func Lp_submit_qf_master(cq *gin.Context){
	var flsw_info Submit_flsw_id
	if cq.ShouldBind(&flsw_info) == nil{
		Flsw_id :=flsw_info.Flsw_id
		if Flsw_id == ""{
			fmt.Println("法人申请id为空")
			cq.JSON(400,"lp_id is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			//修改法人申请状态
			result,err3 :=db.Exec("UPDATE lps SET Lp_node=? , Lp_todo=? WHERE Lp_id=?","5b","eight",Flsw_id)
			fmt.Println(result.RowsAffected())	
			if err3 !=nil{
					fmt.Println("修改法人申请状态出错")
			}else{
				cq.JSON(200,"operate succeed!")
			}
		}
	}
}


//如果企发部主任同意，提交至分管副总经理，并根据选择修改投标状态
func Lp_submit_fg(cq *gin.Context){
	var get_tou Get_fenguan_tou 
	if cq.ShouldBind(&get_tou) == nil{
		lp_id :=get_tou.Lp_id
		tou_res :=get_tou.Tou_result
		if lp_id == "" || tou_res == ""{
			fmt.Println("lp_id or tou_res is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			//修改法人申请状态
			if tou_res == "none"{//代表修改的是单项不投标
				result,err3 :=db.Exec("UPDATE lps SET Lp_node=? ,Lp_todo=? WHERE Lp_id=?","6b","nine",lp_id)
				fmt.Println(result.RowsAffected())	
				if err3 !=nil{
						fmt.Println("修改法人申请状态出错")
				}else{
					cq.JSON(200,"operate succeed!")
				}
			}else{//修改的是单项投标
				result,err3 :=db.Exec("UPDATE lps SET Lp_node=? ,Lp_todo=?,Lp_tou=? WHERE Lp_id=?","6b","nine",tou_res,lp_id)
				fmt.Println(result.RowsAffected())	
				if err3 !=nil{
						fmt.Println("修改法人申请状态出错")
				}else{
					cq.JSON(200,"operate succeed!")
				}
			}
			
		}
	}
}

//如果分管副总经理同意，提交至总经理
func Lp_submit_z(cq *gin.Context){
	var flsw_info Submit_flsw_id
	if cq.ShouldBind(&flsw_info) == nil{
		Flsw_id :=flsw_info.Flsw_id
		if Flsw_id == ""{
			fmt.Println("法人申请id为空")
			cq.JSON(400,"lp_id is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			//修改法人申请状态
			result,err3 :=db.Exec("UPDATE lps SET Lp_node=? ,Lp_todo=? WHERE Lp_id=?","7b","four",Flsw_id)
			fmt.Println(result.RowsAffected())	
			if err3 !=nil{
					fmt.Println("修改法人申请状态出错")
			}else{
				cq.JSON(200,"operate succeed!")
			}
		}
	}
}


//如果总经理同意，流转至发起人
func Lp_submit_over(cq *gin.Context){
	var flsw_info Submit_flsw_id
	if cq.ShouldBind(&flsw_info) == nil{
		Flsw_id :=flsw_info.Flsw_id
		if Flsw_id == ""{
			fmt.Println("法人申请id为空")
			cq.JSON(400,"lp_id is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			//修改法人申请状态
			result,err3 :=db.Exec("UPDATE lps SET Lp_node=? ,Lp_todo=? WHERE Lp_id=?","8b","one",Flsw_id)
			fmt.Println(result.RowsAffected())	
			if err3 !=nil{
					fmt.Println("修改法人申请状态出错")
			}else{
				cq.JSON(200,"operate succeed!")
			}
		}
	}
}