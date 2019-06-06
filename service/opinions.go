package main

import(
	"fmt"
	"github.com/gin-gonic/gin"
	"database/sql"
	_"github.com/Go-SQL-Driver/MySQL"
)


//{
//	"flsw_id":""
//}

func Get_op(cq *gin.Context){
	var get_opinion Get_opinion
	var count int
	var Opinion_return Opinion_exe
	var id string
	var content string
	if cq.ShouldBind(&get_opinion) == nil{
		flsw_id :=get_opinion.Flsw_id
		if flsw_id == ""{
			fmt.Println("审批单编号为空！")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				panic(err)
			}
			err2 :=db.QueryRow("SELECT count(*) FROM opinions WHERE La_op_id = ?",flsw_id).Scan(&count)
			if err2 !=nil{
				fmt.Println("查询意见数目出错")
			}
			rows,err3 :=db.Query("SELECT Opinion_id ,Opinion_content FROM opinions WHERE La_op_id = ?",flsw_id)
			if err3 !=nil{
				fmt.Println("查询意见出错")
			}
			defer rows.Close()
			i:=0
			Op_item :=make([]Opinion_info_return,count)
			for rows.Next(){
				rows.Scan(&id,&content)
				Op_item[i].Opinion_id = id
				Op_item[i].Opinion_content = content
				i++
			}
			Opinion_return.Opinion_info = Op_item
			Opinion_return.Opinion_count = count
			cq.JSON(200,Opinion_return)

		}
	}
}


func Get_opinion_leader(cq *gin.Context){
	var flsw_id_json Get_opinion 
	var count int
	var id string 
	var content string
	var exe_yn string
	var unexe_res string
	if cq.ShouldBind(&flsw_id_json) == nil{
		flsw_id :=flsw_id_json.Flsw_id
		if flsw_id == ""{
			fmt.Println("flsw_id is empty!")
			cq.JSON(400,"flsw_id is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("连接数据库失败！")
			}else{
				//查询信息的数目
				err2 :=db.QueryRow("SELECT count(*) FROM opinions WHERE La_op_id=? AND Opinion_exe_yn=?",flsw_id,"no").Scan(&count)
				if err2 !=nil{
					fmt.Println("查询不执行意见数目出错")
				}
				rows,err3 :=db.Query("SELECT Opinion_id ,Opinion_content,Opinion_exe_yn,Opinion_unexe_res FROM opinions WHERE La_op_id = ? AND Opinion_exe_yn=?",flsw_id,"no")
				if err3 !=nil{
					fmt.Println("查询不执行意见出错")
				}
				defer rows.Close()
				i:=0
				Op_return :=make([]Opinion_info_return_leader,count)
				for rows.Next(){
					rows.Scan(&id,&content,&exe_yn,&unexe_res)
					Op_return[i].Opinion_id = id
					Op_return[i].Opinion_content = content
					Op_return[i].Opinion_exe_yn = exe_yn
					Op_return[i].Opinion_unexe_res = unexe_res
					i++
				}
				cq.JSON(200,Op_return)
			}
		}
	}
}


func Op_Res(cq *gin.Context){
	var get_opinion Get_opinion
	var count int
	var id string
	var content string
	var exe_yn string
	var status string
	if cq.ShouldBind(&get_opinion) == nil{
		flsw_id :=get_opinion.Flsw_id
		if flsw_id == ""{
			fmt.Println("flsw_id is empty!")
			cq.JSON(400,"flsw_id is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("连接数据库失败！")
			}else{
				//查询信息的数目
				err2 :=db.QueryRow("SELECT count(*) FROM opinions WHERE La_op_id=?",flsw_id).Scan(&count)
				if err2 !=nil{
					fmt.Println("查询不执行意见数目出错")
				}
				rows,err3 :=db.Query("SELECT Opinion_id ,Opinion_content,Opinion_exe_yn,Opinion_unexe_status FROM opinions WHERE La_op_id = ?",flsw_id)
				if err3 !=nil{
					fmt.Println("查询不执行意见出错")
				}
				defer rows.Close()
				i:=0
				Op_return :=make([]Op_res,count)
				for rows.Next(){
					rows.Scan(&id,&content,&exe_yn,&status)
					Op_return[i].Op_id = id
					Op_return[i].Op_content = content
					Op_return[i].Op_exe_yn = exe_yn
					Op_return[i].Op_status = status
					i++
				}
				cq.JSON(200,Op_return)
			}
		}
	}
}