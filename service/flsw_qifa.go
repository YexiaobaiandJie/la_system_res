package main 

import(
	"fmt"
	"github.com/gin-gonic/gin"
	"database/sql"
	_"github.com/Go-SQL-Driver/MySQL"
	"time"
	//"strconv"
)


//
//{
//	"flsw_id":"32312"	
//}
//
func Submit_qifa(cq *gin.Context){
	var flsw_info Submit_flsw_id
	var prenode string
	var flsw_type string
	if cq.ShouldBind(&flsw_info) == nil{
		Flsw_id :=flsw_info.Flsw_id
		fmt.Println(Flsw_id)
		if Flsw_id == ""{
			fmt.Println("审批单id为空")
			cq.JSON(400,"Flsw_id should not be empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			//根据审批单id获得审批单类型
			err = db.QueryRow("SELECT La_type FROM flsws WHERE La_id=?",Flsw_id).Scan(&flsw_type)
			if err !=nil{
				fmt.Println("查询审批单类型失败")	
			}
			if flsw_type == "审核类审批单"{
				err2 :=db.QueryRow("SELECT La_node FROM flsws WHERE La_id=?",Flsw_id).Scan(&prenode)
				if err2 !=nil{
					fmt.Println("获得审批单上一状态失败")
				}
				result,err3 :=db.Exec("UPDATE flsws SET La_node=? ,La_prenode =?,La_todo=? WHERE La_id=?","2",prenode,"three",Flsw_id)
				if err3 !=nil{
					fmt.Println("修改审批单状态出错")
				}
				fmt.Println(result.RowsAffected())
				if err == nil && err2 == nil && err3 ==nil{
					cq.JSON(200,"operate succeed!")
				}
			}else if flsw_type == "文书类审批单"{
				err22 :=db.QueryRow("SELECT La_node FROM flsws WHERE La_id=?",Flsw_id).Scan(&prenode)
				if err22 !=nil{
					fmt.Println("获得审批单上一状态失败")
				}
				result,err23 :=db.Exec("UPDATE flsws SET La_node=? ,La_prenode =?,La_todo=? WHERE La_id=?","2a",prenode,"three",Flsw_id)
				if err23 !=nil{
					fmt.Println("修改审批单状态出错")
				}
				fmt.Println(result.RowsAffected())
				if err == nil && err22 == nil && err23 ==nil{
					cq.JSON(200,"operate succeed!")
				}
			}



			
		}

	}
}




func Flsw_info_qifa(cq *gin.Context){
	var user_flsw_id User_flsw_id
	var Af_all_info Af_info
	var Return_af_info Return_af_info
	if cq.ShouldBind(&user_flsw_id) == nil{
		Userid := user_flsw_id.User_id
		Flswid := user_flsw_id.Flsw_id
		fmt.Println(Userid)
		fmt.Println(Flswid)
		if Userid == "" || Flswid == ""{
			fmt.Println("账号或审批单id不能为空")
			cq.JSON(400,"Userid or Flswid should not be empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("连接数据库失败",err)
			}
			err2 :=db.QueryRow("SELECT * FROM flsws WHERE La_id = ?",Flswid).Scan(&Af_all_info.La_id,
																				  &Af_all_info.La_name,
																				  &Af_all_info.La_type,
																				  &Af_all_info.La_dep,
																				  &Af_all_info.La_creator,
																				  &Af_all_info.La_user,
																				  &Af_all_info.La_time,
																				  &Af_all_info.La_content,
																				  &Af_all_info.La_remark,
																				  &Af_all_info.La_opinion,
																				  &Af_all_info.La_opinion_yn,
																				  &Af_all_info.La_node,
																				  &Af_all_info.La_prenode,
																				  &Af_all_info.La_op_id,
																				  &Af_all_info.La_tri_yn,
																				  &Af_all_info.La_check_record)
			if err2 !=nil{
				panic(err2)
			}
			Return_af_info.Flsw_name = Af_all_info.La_name
			Return_af_info.Flsw_type = Af_all_info.La_type
			Return_af_info.Flsw_creator = Af_all_info.La_creator
			Return_af_info.Flsw_time = Af_all_info.La_time
			Return_af_info.Flsw_content = Af_all_info.La_content
			Return_af_info.Flsw_id = Af_all_info.La_id
			Return_af_info.Flsw_dep = Af_all_info.La_dep
			//email
			//cellphone
			Return_af_info.Flsw_remark = Af_all_info.La_remark
			Return_af_info.Flsw_record = Af_all_info.La_check_record
			
			err3 :=db.QueryRow("SELECT user_email,user_cellphone FROM users WHERE user_id =?",Return_af_info.Flsw_creator).Scan(&Return_af_info.Flsw_email,
				&Return_af_info.Flsw_cellphone)
			if err3 !=nil{
				panic(err3)
			}
			//后续需要添加意见栏，
			//Return_af_info.Flsw_opinions
			cq.JSON(200,Return_af_info)

		}
	}
}



//接收来自前端的opinion信息

func Opinion_post(cq *gin.Context){
	var opinion_post Opinions_post 
	var Create_opinion Opinion_info
	//var creator_name string
	if cq.ShouldBind(&opinion_post) == nil{
		if opinion_post.Op_content == ""{
			fmt.Println("意见内容为空！")
		}else{
			//t:=time.Now()
			//Create_opinion.Opinion_id = strconv.FormatInt(t.Unix(),10)
			Create_opinion.La_op_id = opinion_post.La_op_id
			Create_opinion.Opinion_creator = opinion_post.Op_creator
			Create_opinion.Opinion_content = opinion_post.Op_content
			Create_opinion.Opinion_user = opinion_post.Op_user
			Create_opinion.Opinion_exe_yn = opinion_post.Op_exe_yn
			Create_opinion.Opinion_unexe_res = opinion_post.Op_unexe_res
			Create_opinion.Opinion_unexe_status = opinion_post.Op_unexe_status
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			stmt,err3:=db.Prepare("INSERT opinions SET La_op_id=?,Opinion_creator=?, Opinion_content=?,Opinion_user=?,Opinion_exe_yn=?,Opinion_unexe_res=?,Opinion_unexe_status=?")
			if err3 !=nil{
				panic(err3)
			}
			//res,err4:=stmt.Exec(Create_opinion.Opinion_id,
			res,err4:=stmt.Exec(
				Create_opinion.La_op_id,
				Create_opinion.Opinion_creator,
				Create_opinion.Opinion_content,
				Create_opinion.Opinion_user,
				Create_opinion.Opinion_exe_yn,
				Create_opinion.Opinion_unexe_res,
				Create_opinion.Opinion_unexe_status)
				if err4 !=nil{
				panic(err4)
				cq.JSON(400,"创建意见失败")
				}else{
				cq.JSON(200,"创建意见成功")

				}
				fmt.Println(res.LastInsertId())
				/*
				fmt.Println(opinion_post.Op_tri_yn)
				if opinion_post.Op_tri_yn == "是" {
					result,err10 :=db.Exec("UPDATE flsws SET La_tri_yn=?,La_node=?,La_prenode=? WHERE La_id=?","yes","3","2",opinion_post.La_op_id)
					if err10 !=nil{
						fmt.Println("修改审批单状态出错")	 //三重一大以及审批单状态
					}
					fmt.Println(result.RowsAffected())
				}else if opinion_post.Op_tri_yn == "否"{
					result2,err11 :=db.Exec("UPDATE flsws SET La_tri_yn=?,La_node=?,La_prenode=? WHERE La_id=?","yes","0","2",opinion_post.La_op_id)
					if err11 !=nil{
						fmt.Println("修改审批单状态出错")	
					}
					fmt.Println(result2.RowsAffected())
				}
				err12 :=db.QueryRow("SELECT user_name FROM users WHERE user_id=?",opinion_post.Op_creator).Scan(creator_name)
				if err12 !=nil{
					panic(err)
				}
				timeUnix :=time.Now().Unix()
				op_time := time.Unix(timeUnix,0).Format("2006-01-02 15:04:05")
				record :="审批部: "+creator_name+"给出意见\n"+"审核时间: "+op_time+"\n"
				fmt.Println(record)
				result3,err13 :=db.Exec("UPDATE flsws SET La_check_record=? WHERE La_id=?",record,opinion_post.La_op_id)
				if err13 !=nil{
					panic(err13)
				}
				fmt.Println(result3.RowsAffected())
				*/
				}

				
	}
}


//传输三重一大状态，修改审批单状态（三重一大状态，审批单node,prenode,check_record）
//{
//	"la_id":"",
//	"la_qifa_id":"",
//	"la_tri_yn":""	
//}
//
//
func Tri_post(cq *gin.Context){
	var tri_post_info Tri_post_str
	var creator_name string
	var used_record string
	if cq.ShouldBind(&tri_post_info) == nil{
		db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				panic(err)
			}
		fmt.Println(tri_post_info.La_tri_yn)
				if tri_post_info.La_tri_yn == "yes" {
					result,err10 :=db.Exec("UPDATE flsws SET La_tri_yn=?,La_node=?,La_prenode=?,La_todo=? WHERE La_id=?","yes","3","2","one",tri_post_info.La_id)
					if err10 !=nil{
						fmt.Println("修改审批单状态出错")	 //三重一大以及审批单状态
					}
					fmt.Println(result.RowsAffected())
				}else if tri_post_info.La_tri_yn == "no"{
					result2,err11 :=db.Exec("UPDATE flsws SET La_tri_yn=?,La_node=?,La_prenode=? WHERE La_id=?","no","10","2",tri_post_info.La_id)
					if err11 !=nil{
						fmt.Println("修改审批单状态出错")	
					}
					fmt.Println(result2.RowsAffected())
				}
				err12 :=db.QueryRow("SELECT user_name FROM users WHERE user_id=?",tri_post_info.La_qifa_id).Scan(&creator_name)
				if err12 !=nil{
					fmt.Println("查询企发部人员名称出错")
				}
				err14 :=db.QueryRow("SELECT La_check_record FROM flsws WHERE La_id=?",tri_post_info.La_id).Scan(&used_record)
				if err14 !=nil{
					fmt.Println("查询之前的审核记录出错")
				}
				timeUnix :=time.Now().Unix()
				op_time := time.Unix(timeUnix,0).Format("2006-01-02 15:04:05")
				record :=used_record+"\n审批部: "+creator_name+"给出意见\n"+"审核时间: "+op_time+"\n"
				fmt.Println(record)
				result3,err13 :=db.Exec("UPDATE flsws SET La_check_record=? WHERE La_id=?",record,tri_post_info.La_id)
				if err13 !=nil{
					fmt.Println("修改审核记录出错")
				}
				fmt.Println(result3.RowsAffected())
				
	}
}