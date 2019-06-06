package main

import(
	"fmt"
	"github.com/gin-gonic/gin"
	"database/sql"
	_"github.com/Go-SQL-Driver/MySQL"
)

//Login 登录后返回用户信息
//接口{
//  	"userid":"123",
//		"password":"123"	
//    }
//{	
// 返回
//	type Loginres struct{
//	Result int `json:"result"`
//	Userinfo Userinfo `json:"userinfo"`
//}




func Login(cq *gin.Context){
	//var users []user
	
	var User_info Userinfo
	var userinput User
	var Login_res Loginres
	if cq.ShouldBind(&userinput) == nil {
		Userid := userinput.Userid
		Userpwd := userinput.Password
		fmt.Println(Userid)
		fmt.Println(Userpwd)
		if Userid == "" || Userpwd == "" {
			fmt.Println("账号或密码不能为空")
			cq.JSON(400,"Userid or Userpwd should not be empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				panic(err)
			}
			err2 :=db.QueryRow("SELECT * FROM users WHERE user_id = ?",Userid).Scan(&User_info.User_id,
																					&User_info.User_name,
																					&User_info.User_password,
																					&User_info.User_sex,
																					&User_info.User_dep,
																					&User_info.User_position,
																					&User_info.User_cellphone,
																					&User_info.User_email)
			if err2 == sql.ErrNoRows{
				//此处返回登录失败，用户名不存在
				fmt.Println("登录失败，用户名不存在")
				fmt.Println(User_info.User_id)
				fmt.Println(User_info.User_password)
				Login_res.Result=0
				cq.JSON(400,Login_res)
			} else if Userpwd != User_info.User_password{
				//此处返回登录失败，密码错误
				fmt.Println("登录失败，密码错误")
				fmt.Println(User_info.User_id)
				fmt.Println(User_info.User_password)
				Login_res.Result=1
				cq.JSON(400,Login_res)
			} else{
				//此处返回登录成功并返回用户详细信息
				fmt.Println("登录成功")
				Login_res.Result=2
				Login_res.Userinfo=User_info
				cq.JSON(200,Login_res)
			}

		
		}
	}
	
}