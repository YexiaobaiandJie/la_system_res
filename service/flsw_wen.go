package main

import(
	"fmt"
	"github.com/gin-gonic/gin"
	"database/sql"
	_"github.com/Go-SQL-Driver/MySQL"
	"time"
	//"strconv"
)



//接口
//{
//	"la_name":"dsad",
//	"la_type":"23123",
//	"la_creator":"123",
//	"la_content":"3213231232312321"
//}
/*
func Cre_af_wen(cq *gin.Context){
	var Afshort Af_short_info
	var Af_wen Af_wen_info

	if cq.ShouldBind(&Afshort) == nil{
		La_name_short :=Afshort.La_name
		La_type_short :=Afshort.La_type
		La_creator_short :=Afshort.La_creator
		La_content_short :=Afshort.La_content
		if La_name_short == "" || La_type_short == "" || La_creator_short == "" || La_content_short == ""{
			fmt.Println("必要项为空，创建失败")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				panic(err)
			}
			//补充审批单信息
			t:=time.Now()
			Af_wen.La_wen_id = strconv.FormatInt(t.Unix(),10)
			Af_wen.La_wen_name = La_name_short
			Af_wen.La_wen_type = La_type_short
			//根据发起人id获取委托单发起人部门
			err2 :=db.QueryRow("SELECT user_dep FROM users WHERE user_id = ?",La_creator_short).Scan(&Af_wen.La_wen_dep)
			if err2 ==sql.ErrNoRows{
				//部门不存在，返回用户信息错误
				cq.JSON(400,"查询用户所在部门时出错")
			}else{
				Af_wen.La_wen_creator = La_creator_short
				Af_wen.La_wen_user = ""
				timeUnix :=time.Now().Unix()
				Af_wen.La_wen_time = time.Unix(timeUnix,0).Format("2006-01-02 15:04:05")
				Af_wen.La_wen_content = La_content_short
				Af_wen.La_wen_remark = ""
				Af_wen.La_wen_opinion = ""
				Af_wen.La_wen_opinion_yn = "no"
				Af_wen.La_wen_node = "0"
				Af_wen.La_wen_check_record = ""
				stmt,err3 :=db.Prepare("INSERT flsws_wen SET La_wen_id=?,La_wen_name=?,La_wen_type=?,La_wen_dep=?,La_wen_creator=?,La_wen_user=?,La_wen_time=?,La_wen_content=?,La_wen_remark=?,La_wen_opinion=?,La_wen_opinion_yn=?,La_wen_node=?,La_wen_check_record=?")
				if err3 != nil{
					panic(err3)
				}
				res,err4:=stmt.Exec(Af_wen.La_wen_id,
									Af_wen.La_wen_name,
									Af_wen.La_wen_type,
									Af_wen.La_wen_dep,
									Af_wen.La_wen_creator,
									Af_wen.La_wen_user,
									Af_wen.La_wen_time,
									Af_wen.La_wen_content,
									Af_wen.La_wen_remark,
									Af_wen.La_wen_opinion,
									Af_wen.La_wen_opinion_yn,
									Af_wen.La_wen_node,
									Af_wen.La_wen_check_record)
				if err4 !=nil{
					panic(err4)
					cq.JSON(400,"创建审批单失败")
				}else{
					cq.JSON(200,"创建审批单成功")
				}
			}

		}
	}
}
*/

func Post_wen_opinion(cq *gin.Context){
	var op_wen Op_wen
	var Create_opinion Opinion_info
	var userid string
	var username string
	var creatorname string
	var record string
	var record_now string
	if cq.ShouldBind(&op_wen) == nil{
		flsw_id :=op_wen.Flsw_wen_id
		op_content :=op_wen.Op_wen_con
		op_creator :=op_wen.Op_creator
		fmt.Println(flsw_id)
		fmt.Println(op_content)
		fmt.Println(op_creator)
		if flsw_id == ""  || op_content == ""{
			fmt.Println("something is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			//根据审批单id获得执行人id
			err2 := db.QueryRow("SELECT La_creator,La_check_record FROM flsws WHERE La_id=?",flsw_id).Scan(&userid,&record)
			if err2 !=nil{
				fmt.Println("查询执行人id出错")	
			}
			//根据userid获得执行人姓名
			err3 := db.QueryRow("SELECT user_name FROM users WHERE user_id=?",userid).Scan(&username)
			if err3 !=nil{
				fmt.Println("查询执行人姓名出错")
			}
			Create_opinion.La_op_id = flsw_id
			Create_opinion.Opinion_creator = op_creator
			Create_opinion.Opinion_content = op_content
			Create_opinion.Opinion_user = username
			Create_opinion.Opinion_exe_yn = "force"
			Create_opinion.Opinion_unexe_res = "此为文书类审批单意见"
			Create_opinion.Opinion_unexe_status = "强制执行（文书）"
			stmt,err4:=db.Prepare("INSERT opinions SET La_op_id=?,Opinion_creator=?, Opinion_content=?,Opinion_user=?,Opinion_exe_yn=?,Opinion_unexe_res=?,Opinion_unexe_status=?")
			if err4 !=nil{
				panic(err4)
			}
			//res,err4:=stmt.Exec(Create_opinion.Opinion_id,
			res,err5:=stmt.Exec(
				Create_opinion.La_op_id,
				Create_opinion.Opinion_creator,
				Create_opinion.Opinion_content,
				Create_opinion.Opinion_user,
				Create_opinion.Opinion_exe_yn,
				Create_opinion.Opinion_unexe_res,
				Create_opinion.Opinion_unexe_status)
				if err5 !=nil{
				panic(err5)
				cq.JSON(400,"创建意见失败")
				}else{
				cq.JSON(200,"创建意见成功")

				}
				fmt.Println(res.LastInsertId())
			//以下更新审批记录
			//根据userid获得审批人姓名
			err6 := db.QueryRow("SELECT user_name FROM users WHERE user_id=?",op_creator).Scan(&creatorname)
			if err6 !=nil{
				fmt.Println("查询审核人姓名出错")
			}
			//获得当前时间作为审批时间
			timeUnix :=time.Now().Unix()
			op_time := time.Unix(timeUnix,0).Format("2006-01-02 15:04:05")
			//根据之前的审批记录更新审批记录
			record_now = record +"\n审批部:"+creatorname+"给出意见\n"+"审核时间:"+op_time+"\n"
			result,err6 :=db.Exec("UPDATE flsws SET La_check_record=? WHERE La_id=?",record_now,flsw_id)
			if err6 !=nil{
				fmt.Println("修改check_record出现错误")
			}
			fmt.Println(result.RowsAffected())

			//更新审批单状态
			result2,err7 :=db.Exec("UPDATE flsws SET La_node=?,La_prenode=?,La_todo=? WHERE La_id=?","7a","2a","one",flsw_id)
			if err7 !=nil{
				fmt.Println("修改审批单状态出现错误")
			}
			fmt.Println(result2.RowsAffected())
		}
	}
}


func Post_wen_result(cq *gin.Context){
	var flsw_id	Submit_flsw_id
	var info_return Wen_info_return
	var opinion_count int64
	var content string
	var temp string
	if cq.ShouldBind(&flsw_id) == nil {
		la_id :=flsw_id.Flsw_id
		if la_id == ""{
			fmt.Println("审批单id为空")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			err2 :=db.QueryRow("SELECT La_id,La_name,La_type,La_dep,La_creator,La_time,La_content,La_remark,La_check_record FROM flsws WHERE La_id = ?",la_id).Scan(&info_return.La_id,
				&info_return.La_name,
				&info_return.La_type,
				&info_return.La_dep,
				&info_return.La_creator,
				&info_return.La_time,
				&info_return.La_content,
				&info_return.La_remark,
				&info_return.La_check_record)
			if err2 !=nil{
				fmt.Println("查询审批单数据出错")
			}
			err13 :=db.QueryRow("SELECT user_cellphone,user_email FROM users WHERE user_id=?",&info_return.La_creator).Scan(&info_return.La_cellphone,&info_return.La_email)
			if err13 !=nil{
				fmt.Println("查询用户电话和邮件出错")
			}

			err3 :=db.QueryRow("SELECT count(*) FROM opinions WHERE La_op_id=?",la_id).Scan(&opinion_count)
			if err3 !=nil{
				fmt.Println("查询意见数目出错")
			}
			rows,err4 :=db.Query("SELECT Opinion_content FROM opinions WHERE La_op_id = ?",la_id)
			if err4 !=nil{
				fmt.Println("获得意见内容出错")
			}
			for rows.Next(){
				rows.Scan(&temp)
				content = content +"\n"+temp
			}
			info_return.Opinion_content = content
			cq.JSON(200,info_return)
		}
	}
}