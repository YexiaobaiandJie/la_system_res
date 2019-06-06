package main

import(
	"fmt"
	"github.com/gin-gonic/gin"
	"database/sql"
	_"github.com/Go-SQL-Driver/MySQL"
	//"time"
	//"strconv"
)


func Change_pwd(cq *gin.Context){
	var pwd_info Ch_pwd
	var Ch_p_res Ch_pwd_res 
	var pwd_indb string
	if cq.ShouldBind(&pwd_info) == nil{
		userid :=pwd_info.Userid
		oldpwd :=pwd_info.Oldpwd
		newpwd :=pwd_info.Newpwd
		if userid == "" || oldpwd == "" || newpwd == ""{
			fmt.Println("something is empty!")
			cq.JSON(400,"something is empty!")
		} else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
			    Ch_p_res.Ch_res="数据库连接失败"
				cq.JSON(200,Ch_p_res)
				fmt.Println("数据库连接失败")
				panic(err)
			}
			err2 :=db.QueryRow("SELECT user_password FROM users WHERE user_id=?",userid).Scan(&pwd_indb)
			if err2 !=nil{
				fmt.Println("获得原密码出错")
				panic(err2)
			}else{
				if pwd_indb == oldpwd{
					result,err3 :=db.Exec("UPDATE users SET user_password=? WHERE user_id=?",newpwd,userid)
					if err3 !=nil{
						Ch_p_res.Ch_res="修改密码出现错误"
						fmt.Println("修改密码出现错误")
						cq.JSON(200,Ch_p_res)
					}else{
						fmt.Println(result.RowsAffected())
						Ch_p_res.Ch_res="密码修改成功"
						cq.JSON(200,Ch_p_res)
					}
					
				}else{
					Ch_p_res.Ch_res="原密码错误"
					cq.JSON(200,Ch_p_res)
				}
			}
			
		}
	} 
}

func Change_phone(cq *gin.Context){
	var info Ch_phone
	if cq.ShouldBind(&info) == nil{
		userid :=info.Userid
		newphone :=info.User_phone
		if userid == "" || newphone == ""{
			fmt.Println("something is empty!")
			fmt.Println(userid)
			fmt.Println(newphone)
			cq.JSON(400,"something is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
				cq.JSON(400,"数据库连接失败")
				panic(err)
			}else{
				result,err2 :=db.Exec("UPDATE users SET user_cellphone=? WHERE user_id=?",newphone,userid)
				if err2 !=nil{
					fmt.Println("修改电话出现错误")
				}else{
					fmt.Println(result.RowsAffected())
					fmt.Println("修改电话成功")
					cq.JSON(200,"修改电话成功")
				}
			}
			
		}
	}
}

func Change_email(cq *gin.Context){
	var info Ch_phone
	if cq.ShouldBind(&info) == nil{
		userid :=info.Userid
		newphone :=info.User_phone
		if userid == "" || newphone == ""{
			fmt.Println("something is empty!")
			fmt.Println(userid)
			fmt.Println(newphone)
			cq.JSON(400,"something is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("数据库连接失败")
				fmt.Println("数据库连接失败")
				panic(err)
			}else{
				result,err2 :=db.Exec("UPDATE users SET user_email=? WHERE user_id=?",newphone,userid)
				if err2 !=nil{
					fmt.Println("修改邮箱出现错误")
				}else{
					fmt.Println(result.RowsAffected())
					cq.JSON(200,"修改邮箱成功")
				}
			}
			
		}
	}
}