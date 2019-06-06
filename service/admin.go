package main

import(
	"fmt"
	"github.com/gin-gonic/gin"
	"database/sql"
	_"github.com/Go-SQL-Driver/MySQL"
	"time"
	"strconv"
)


func Admin_cre_user(cq *gin.Context){
	var cre_user Cre_user
	var dep_id string
	var user_info_all  Cre_user_all
	var position_int int
	if cq.ShouldBind(&cre_user) == nil{
		pwd :=cre_user.Userpwd
		position :=cre_user.Userposition
		sex :=cre_user.Usersex
		name :=cre_user.Username
		dep :=cre_user.Userdep

		if pwd == "" || position == "" || sex == "" || name == "" || dep == ""{
			fmt.Println("something is empty!")
			cq.JSON(400,"something is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
			}
			//根据dep获得depid
			err2 :=db.QueryRow("SELECT Dep_id FROM department WHERE Dep_name=?",dep).Scan(&dep_id)
			if err2 !=nil{
				panic(err2)
				fmt.Println("获得depid出错")
			}
			//根据position 获得权限
			if position == "普通员工"{
				position_int = 1
			}else if position == "企发部员工"{
				position_int = 2
			}else if position == "部门领导"{
				position_int = 3
			}else if position == "经营科员工"{
				position_int = 4
			}else if position == "法务科员工"{
				position_int = 5
			}else if position == "企发部主任"{
				position_int = 11
			}else if position == "分管副总经理"{
				position_int = 12
			}else if position == "总经理"{
				position_int = 13
			}else if position == "院长"{
				position_int = 14
			}
			t :=time.Now() 
			user_info_all.user_id = strconv.FormatInt(t.Unix(),10)
			user_info_all.user_name = name
			user_info_all.user_password = pwd
			user_info_all.user_sex = sex
			user_info_all.user_dep = dep_id
			user_info_all.user_position = position_int
			user_info_all.user_cellphone = ""
			user_info_all.user_email = ""

			stmt,err3:=db.Prepare("INSERT users SET user_id=?,user_name=?,user_password=?, user_sex=?,user_dep=?,user_position=?,user_cellphone=?,user_email=?")
				if err3 !=nil{
					panic(err3)
				}
				res,err4:=stmt.Exec(user_info_all.user_id,
									user_info_all.user_name,
									user_info_all.user_password,
									user_info_all.user_sex,
									user_info_all.user_dep,
									user_info_all.user_position,
									user_info_all.user_cellphone,
									user_info_all.user_email)
				if err4 !=nil{
					panic(err4)
					cq.JSON(400,"创建用户失败")
				}else{
					cq.JSON(200,"创建用户成功")
					
				}
				fmt.Println(res.LastInsertId())


		}
	}
}


func Get_user_list(cq *gin.Context){
	var user Creator
	var position int
	var count int
	var id string
	var name string 
	var password string
	var sex string
	var dep string 
	var position2 int
	var cellphone string
	var email string
	if cq.ShouldBind(&user) == nil{
		List_owner :=user.List_owner
		if List_owner == ""{
			cq.JSON(400,"ID IS EMPTY!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("连接数据库失败",err)
			}	
			err2 :=db.QueryRow("SELECT user_position FROM users WHERE user_id = ?",List_owner).Scan(&position)
			if err2 !=nil{
				panic(err2)
			}
			if position != 100{
				fmt.Println("用户不是管理员！无法提供用户列表")
				cq.JSON(400,"用户不是管理员！无法提供用户列表")
			}else{
				err3 :=db.QueryRow("SELECT count(*) FROM users").Scan(&count)
				if err3 !=nil{
					fmt.Println("查询用户数目出错")
				}
				fmt.Println("count=",count)
				rows,err4 :=db.Query("SELECT user_id,user_name,user_password,user_sex,user_dep,user_position,user_cellphone,user_email FROM users")
				if err4 !=nil{
					fmt.Println("获取错误",err4)
					panic(err4)
				}
				defer rows.Close()
				User_list :=make([]Cre_user_all2,count)
				i:=0
				for rows.Next(){
					rows.Scan(&id,&name,&password,&sex,&dep,&position2,&cellphone,&email)
					User_list[i].User_id = id
					User_list[i].User_name = name
					User_list[i].User_password = password
					User_list[i].User_sex = sex
					User_list[i].User_dep = dep
					User_list[i].User_position = position2
					User_list[i].User_cellphone = cellphone
					User_list[i].User_email = email
					i++
				}
				cq.JSON(200,User_list)

			}
		}
	}
}


func Admin_del_user(cq *gin.Context){
	var info User_short
	var count int
	if cq.ShouldBind(&info) == nil{
		userid :=info.Userid
		if userid == ""{
			cq.JSON(400,"用户id为空")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("连接数据库失败",err)
				cq.JSON(400,"数据库连接失败")
			}	
			err2 :=db.QueryRow("SELECT count(*) FROM users WHERE user_id = ?",userid).Scan(&count)
				if err2 !=nil{
					fmt.Println("查询用户是否存在出错")
				}
			if count == 0{
				cq.JSON(400,"用户不存在")
			}else if count == 1{
				result, err3 := db.Exec("DELETE FROM users WHERE user_id=?",userid)
				if err3 !=nil{
					fmt.Println("删除用户出错")
					panic(err3)
				}
				fmt.Println("result = ",result)
				cq.JSON(200,"删除用户成功！")
			}else{
				cq.JSON(400,"发生了用户重复事件！")
			}

		}
	}
}


func Admin_chg_pos(cq *gin.Context){
	var info User_short 
	var position_int int
	var count int
	if cq.ShouldBind(&info) == nil{
		userid :=info.Userid
		position :=info.Password
		if userid == "" || position == ""{
			fmt.Println("某些必要项为空！")
			fmt.Println(userid)
			fmt.Println(position)
			cq.JSON(400,"某些必要项为空！")
		}else{
			if position == "普通员工"{
				position_int = 1
			}else if position == "企发部员工"{
				position_int = 2
			}else if position == "部门领导"{
				position_int = 3
			}else if position == "经营科员工"{
				position_int = 4
			}else if position == "法务科员工"{
				position_int = 5
			}else if position == "企发部主任"{
				position_int = 11
			}else if position == "分管副总经理"{
				position_int = 12
			}else if position == "总经理"{
				position_int = 13
			}else if position == "院长"{
				position_int = 14
			}

			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("连接数据库失败",err)
				cq.JSON(400,"数据库连接失败")
			}	
			err2 :=db.QueryRow("SELECT count(*) FROM users WHERE user_id = ?",userid).Scan(&count)
				if err2 !=nil{
					fmt.Println("查询用户是否存在出错")
				}
			if count == 0{
				cq.JSON(400,"用户不存在")
			}else if count == 1{//修改用户权限
				result,err3 :=db.Exec("UPDATE users SET user_position=? WHERE user_id=?",position_int,userid)
				fmt.Println(result.RowsAffected())	
				if err3 !=nil{
					fmt.Println("修改权限出错")
				}else{
					cq.JSON(200,"operate succeed!")
				}
			}else{
				cq.JSON(400,"发生了用户重复事件！")
			}
		}
	}
}