package main

import(
	"fmt"
	"github.com/gin-gonic/gin"
	"database/sql"
	_"github.com/Go-SQL-Driver/MySQL"
	"time"
	"strconv"
)


//接收、补充审批单信息并保存至数据库

//接口
//{
//	"la_name":"dsad",
//	"la_type":"23123",
//	"la_creator":"123",
//	"la_content":"3213231232312321"
//}
func Cre_af(cq *gin.Context){
	
	var Afshort Af_short_info
	var Afinfo Af_info
	
	if cq.ShouldBind(&Afshort) == nil {
		La_name_short := Afshort.La_name
		La_type_short := Afshort.La_type
		La_creator_short := Afshort.La_creator
		La_content_short := Afshort.La_content
		File_name_short :=Afshort.File_name
		if La_name_short == "" || La_type_short == "" || La_creator_short == "" || La_content_short == ""{
			fmt.Println(La_name_short)
			fmt.Println(La_type_short)
			fmt.Println(La_creator_short)
			fmt.Println(La_content_short)
			fmt.Println("必要项为空，创建失败")
			cq.JSON(400,"something is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				panic(err)
			}
			//补充审批单信息
			t :=time.Now() 
			Afinfo.La_id = strconv.FormatInt(t.Unix(),10)
			Afinfo.La_name = La_name_short
			Afinfo.La_type = La_type_short
			//根据发起人id获取委托单发起人的部门
			err2 :=db.QueryRow("SELECT user_dep FROM users WHERE user_id = ?",La_creator_short).Scan(&Afinfo.La_dep)
			if err2 == sql.ErrNoRows{
				//部门不存在，返回用户信息错误
				cq.JSON(400,"查询用户所在部门时出错")
			}else{
				Afinfo.La_creator = La_creator_short
				Afinfo.La_user = ""
				//获取当前时间并转换为字符串
				timeUnix :=time.Now().Unix()
				Afinfo.La_time = time.Unix(timeUnix,0).Format("2006-01-02 15:04:05")
				Afinfo.La_content =La_content_short
				Afinfo.La_remark = File_name_short
				Afinfo.La_opinion = ""
				Afinfo.La_opinion_yn = "no"
				Afinfo.La_node = "0"
				Afinfo.La_prenode = "0"
				Afinfo.La_op_id = Afinfo.La_id
				Afinfo.La_tri_yn = "no"
				Afinfo.La_check_record = ""
				Afinfo.La_todo="two"
				stmt,err3:=db.Prepare("INSERT flsws SET La_id=?,La_name=?,La_type=?, La_dep=?,La_creator=?, La_user=?,La_time=?,La_content=?,La_remark=?,La_opinion=?,La_opinion_yn=?,La_node=?,La_prenode=?,La_op_id=?,La_tri_yn=?,La_check_record=?,La_todo=?")
				if err3 !=nil{
					panic(err3)
				}
				res,err4:=stmt.Exec(Afinfo.La_id,
									 Afinfo.La_name,
									 Afinfo.La_type,
									 Afinfo.La_dep,
									 Afinfo.La_creator,
									 Afinfo.La_user,
									 Afinfo.La_time,
									 Afinfo.La_content,
									 Afinfo.La_remark,
									 Afinfo.La_opinion,
									 Afinfo.La_opinion_yn,
									 Afinfo.La_node,
									 Afinfo.La_prenode,
									 Afinfo.La_op_id,
									 Afinfo.La_tri_yn,
									 Afinfo.La_check_record,
									 Afinfo.La_todo)
				if err4 !=nil{
					panic(err4)
					cq.JSON(400,"创建审批单失败")
				}else{
					cq.JSON(200,"创建审批单成功")
					
				}
				fmt.Println(res.LastInsertId())
			}






		}
	}
}


//返回审批单简单数据
//{
//	"name":"321312",
//	"creator":"123",
//	"time":"2017-01-12",
//	"status":"2",
//	"id":"1524625263",
//}
//接收
//{
//	"listowner":"123"
//}
func Flsw_list(cq *gin.Context){
	var user Creator
	
	var id string
	var name string
	var creator string
	var time string
	var status string
	var itype string
	var count int
	var count2 int
	var opinion_yn string
	if cq.ShouldBind(&user) == nil {
		List_creator :=user.List_owner
		if List_creator == ""{
			cq.JSON(400,"id is empty!")
		}else{
			fmt.Println("用户id为"+List_creator)
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("连接数据库失败",err)
			}
			rows,err2 :=db.Query("SELECT La_id,La_name,La_creator,La_time,La_opinion_yn,La_node,La_type FROM flsws WHERE La_creator = ?",List_creator)
			err3 :=db.QueryRow("SELECT count(*) FROM flsws WHERE La_creator = ?",List_creator).Scan(&count)
			fmt.Println("count = ",count)
			if err3 !=nil{
				panic(err3)
			}
			defer rows.Close()
			if err2 !=nil{
				fmt.Println("获取错误",err)
				panic(err2)
			}
			err5 :=db.QueryRow("SELECT count(*) FROM lps WHERE Lp_creator=? ",List_creator).Scan(&count2)
			if err5 !=nil{
				fmt.Println("获取错误",err)
				panic(err5)
			}
			i:=0
			List_item :=make([]Flsw_list_item,count+count2)
			for rows.Next(){
				rows.Scan(&id,&name,&creator,&time,&opinion_yn,&status,&itype)
				List_item[i].Item_id = id
				List_item[i].Item_name = name
				List_item[i].Item_creator = creator
				List_item[i].Item_time = time
				List_item[i].Item_opinion_yn = opinion_yn
				List_item[i].Item_status = status
				List_item[i].Item_type = itype
				i++
			}
			//在员工列表中加入法人授权审批单
			rows2,err4 :=db.Query("SELECT Lp_id,Lp_name,Lp_creator,Lp_time,Lp_opinion_yn,Lp_node,Lp_type FROM lps WHERE Lp_creator=?",List_creator)
			fmt.Println("count2 = ",count2)
			if err4 !=nil{
				panic(err4)
			}
			defer rows2.Close()
			for rows2.Next(){
				rows2.Scan(&id,&name,&creator,&time,&opinion_yn,&status,&itype)
				List_item[i].Item_id = id
				List_item[i].Item_name = name
				List_item[i].Item_creator = creator
				List_item[i].Item_time = time
				List_item[i].Item_opinion_yn = opinion_yn
				List_item[i].Item_status = status
				List_item[i].Item_type = itype
				i++
			}
			
			//
			cq.JSON(200,List_item)
	
		}
	}
}


//返回审批单简单数据
//{
//	"name":"321312",
//	"creator":"123",
//	"time":"2017-01-12",
//	"status":"2",
//	"id":"1524625263",
//}
//接收
//{
//	"listowner":"123"
//}
func Flsw_todo_list(cq *gin.Context){
	var user Creator
	
	var id string
	var name string
	var creator string
	var cr_position string
	var cr_dep string
	var time string
	var status string
	var count int //记录审核类审批单和文书类审批单的数目
	var opinion_yn string
	var itype string
	var count2 int  //记录单项法人申请的数目
	var cre_dep_name string //记录申请人的部门name
	if cq.ShouldBind(&user) == nil {
		List_creator :=user.List_owner
		if List_creator == ""{
			cq.JSON(400,"id is empty!")
		}else{
			fmt.Println("用户id为"+List_creator)
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("连接数据库失败",err)
			}
			err5 :=db.QueryRow("SELECT user_position FROM users WHERE user_id = ?",List_creator).Scan(&cr_position)
			if err5 !=nil{
				panic(err5)
			}
			fmt.Println("用户权限为",cr_position)
			err15 :=db.QueryRow("SELECT user_dep FROM users WHERE user_id = ?",List_creator).Scan(&cr_dep)
			if err15 !=nil{
				panic(err15)
			}
			fmt.Println("用户部门为",cr_dep)
			//如果用户是企发部员工
			if cr_position == "2"{
				rows,err2 :=db.Query("SELECT La_id,La_name,La_creator,La_time,La_opinion_yn,La_node,La_type FROM flsws WHERE La_todo = ?","three")
				err3 :=db.QueryRow("SELECT count(*) FROM flsws WHERE La_todo = ?","three").Scan(&count)
				fmt.Println("count = ",count)
				if err3 !=nil{
					panic(err3)
				}
				defer rows.Close()
				if err2 !=nil{
					fmt.Println("获取错误",err)
					panic(err2)
				}
				err4 :=db.QueryRow("SELECT count(*) FROM lps WHERE Lp_todo = ?","three").Scan(&count2)
				if err4 !=nil{
					fmt.Println("查询单项法人申请的数目出错")
				}
				fmt.Println("count2=",count2)
				List_item :=make([]Flsw_list_item,count+count2)
				i:=0
				for rows.Next(){
					rows.Scan(&id,&name,&creator,&time,&opinion_yn,&status,&itype)
					List_item[i].Item_id = id
					List_item[i].Item_name = name
					List_item[i].Item_creator = creator
					List_item[i].Item_time = time
					List_item[i].Item_opinion_yn = opinion_yn
					List_item[i].Item_status = status
					List_item[i].Item_type = itype
					i++
				}
				//载入单项法人信息
				rows2,err5 :=db.Query("SELECT Lp_id,Lp_name,Lp_creator,Lp_time,Lp_opinion_yn,Lp_node,Lp_type FROM lps WHERE Lp_todo = ?","three")
				if err5 !=nil{
					panic(err5)
				}
				defer rows2.Close()
				for rows2.Next(){
					rows2.Scan(&id,&name,&creator,&time,&opinion_yn,&status,&itype)
					List_item[i].Item_id = id
					List_item[i].Item_name = name
					List_item[i].Item_creator = creator
					List_item[i].Item_time = time
					List_item[i].Item_opinion_yn = opinion_yn
					List_item[i].Item_status = status
					List_item[i].Item_type = itype
					i++
				}
				cq.JSON(200,List_item)
			}else if cr_position == "3"{  //如果用户是部门领导
				rows,err2 :=db.Query("SELECT La_id,La_name,La_creator,La_time,La_opinion_yn,La_node,La_type FROM flsws WHERE La_dep = ? AND La_todo = ?",cr_dep,"two")
				err3 :=db.QueryRow("SELECT count(*) FROM flsws WHERE La_dep = ? AND La_todo = ?",cr_dep,"two").Scan(&count)
				errnum :=db.QueryRow("SELECT Dep_name FROM department WHERE Dep_id=?",cr_dep).Scan(&cre_dep_name)
				if errnum !=nil{
					panic(errnum)
				}
				err4 :=db.QueryRow("SELECT count(*) FROM lps WHERE Lp_cre_dep=? AND Lp_todo = ?",cre_dep_name,"two").Scan(&count2)
				if err4 != nil{
					panic(err4)
				}
				if err3 !=nil{
					panic(err3)
				}
				defer rows.Close()
				if err2 !=nil{
					fmt.Println("获取错误",err)
					panic(err2)
				}
				

				i:=0
				List_item :=make([]Flsw_list_item,count+count2)
				for rows.Next(){
					rows.Scan(&id,&name,&creator,&time,&opinion_yn,&status,&itype)
					List_item[i].Item_id = id
					List_item[i].Item_name = name
					List_item[i].Item_creator = creator
					List_item[i].Item_time = time
					List_item[i].Item_opinion_yn = opinion_yn
					List_item[i].Item_status = status
					List_item[i].Item_type = itype
					i++
				}
				//载入单项法人申请信息
				rows2,err5 :=db.Query("SELECT Lp_id,Lp_name,Lp_creator,Lp_time,Lp_opinion_yn,Lp_node,Lp_type FROM lps WHERE Lp_cre_dep = ? AND Lp_todo = ?",cre_dep_name,"two")
				if err5 !=nil{
					panic(err5)
				}
				defer rows2.Close()
				for rows2.Next(){
					rows2.Scan(&id,&name,&creator,&time,&opinion_yn,&status,&itype)
					List_item[i].Item_id = id
					List_item[i].Item_name = name
					List_item[i].Item_creator = creator
					List_item[i].Item_time = time
					List_item[i].Item_opinion_yn = opinion_yn
					List_item[i].Item_status = status
					List_item[i].Item_type = itype
					i++
				}
				cq.JSON(200,List_item)
			}else if cr_position == "4"{//如果用户是经营科
				//载入单项法人信息
				err3 :=db.QueryRow("SELECT count(*) FROM lps WHERE Lp_todo = ?","six").Scan(&count)
				if err3 !=nil{
					panic(err3)
				}
				fmt.Println("count=",count)
				rows,err5 :=db.Query("SELECT Lp_id,Lp_name,Lp_creator,Lp_time,Lp_opinion_yn,Lp_node,Lp_type FROM lps WHERE Lp_todo = ?","six")
				if err5 !=nil{
					panic(err5)
				}
				defer rows.Close()
				i:=0
				List_item :=make([]Flsw_list_item,count)
				for rows.Next(){
					rows.Scan(&id,&name,&creator,&time,&opinion_yn,&status,&itype)
					List_item[i].Item_id = id
					List_item[i].Item_name = name
					List_item[i].Item_creator = creator
					List_item[i].Item_time = time
					List_item[i].Item_opinion_yn = opinion_yn
					List_item[i].Item_status = status
					List_item[i].Item_type = itype
					i++
				}
				cq.JSON(200,List_item)
			}else if cr_position == "5"{//如果用户是法务科
				//载入单项法人信息
				err3 :=db.QueryRow("SELECT count(*) FROM lps WHERE Lp_todo = ?","seven").Scan(&count)
				if err3 !=nil{
					panic(err3)
				}
				fmt.Println("count=",count)
				rows,err5 :=db.Query("SELECT Lp_id,Lp_name,Lp_creator,Lp_time,Lp_opinion_yn,Lp_node,Lp_type FROM lps WHERE Lp_todo = ?","seven")
				if err5 !=nil{
					panic(err5)
				}
				defer rows.Close()
				i:=0
				List_item :=make([]Flsw_list_item,count)
				for rows.Next(){
					rows.Scan(&id,&name,&creator,&time,&opinion_yn,&status,&itype)
					List_item[i].Item_id = id
					List_item[i].Item_name = name
					List_item[i].Item_creator = creator
					List_item[i].Item_time = time
					List_item[i].Item_opinion_yn = opinion_yn
					List_item[i].Item_status = status
					List_item[i].Item_type = itype
				i++
			}
				cq.JSON(200,List_item)
			}else if cr_position == "11"{ //如果用户是企发部主任
				//载入单项法人信息
				err3 :=db.QueryRow("SELECT count(*) FROM lps WHERE Lp_todo = ?","eight").Scan(&count)
				if err3 !=nil{
					panic(err3)
				}
				fmt.Println("count=",count)
				rows,err5 :=db.Query("SELECT Lp_id,Lp_name,Lp_creator,Lp_time,Lp_opinion_yn,Lp_node,Lp_type FROM lps WHERE Lp_todo = ?","eight")
				if err5 !=nil{
					panic(err5)
				}
				defer rows.Close()
				i:=0
				List_item :=make([]Flsw_list_item,count)
				for rows.Next(){
					rows.Scan(&id,&name,&creator,&time,&opinion_yn,&status,&itype)
					List_item[i].Item_id = id
					List_item[i].Item_name = name
					List_item[i].Item_creator = creator
					List_item[i].Item_time = time
					List_item[i].Item_opinion_yn = opinion_yn
					List_item[i].Item_status = status
					List_item[i].Item_type = itype
				i++
			}
				cq.JSON(200,List_item)
			}else if cr_position == "12"{//如果用户是分管副总经理
				//载入单项法人信息
				err3 :=db.QueryRow("SELECT count(*) FROM lps WHERE Lp_todo = ?","nine").Scan(&count)
				if err3 !=nil{
					panic(err3)
				}
				fmt.Println("count=",count)
				rows,err5 :=db.Query("SELECT Lp_id,Lp_name,Lp_creator,Lp_time,Lp_opinion_yn,Lp_node,Lp_type FROM lps WHERE Lp_todo = ?","nine")
				if err5 !=nil{
					panic(err5)
				}
				defer rows.Close()
				i:=0
				List_item :=make([]Flsw_list_item,count)
				for rows.Next(){
					rows.Scan(&id,&name,&creator,&time,&opinion_yn,&status,&itype)
					List_item[i].Item_id = id
					List_item[i].Item_name = name
					List_item[i].Item_creator = creator
					List_item[i].Item_time = time
					List_item[i].Item_opinion_yn = opinion_yn
					List_item[i].Item_status = status
					List_item[i].Item_type = itype
				i++
				}
				cq.JSON(200,List_item)
			}else if cr_position == "13"{//如果用户是总经理
				//载入单项法人信息
				err3 :=db.QueryRow("SELECT count(*) FROM lps WHERE Lp_todo = ?","four").Scan(&count)
				if err3 !=nil{
					panic(err3)
				}
				fmt.Println("count=",count)
				rows,err5 :=db.Query("SELECT Lp_id,Lp_name,Lp_creator,Lp_time,Lp_opinion_yn,Lp_node,Lp_type FROM lps WHERE Lp_todo = ?","four")
				if err5 !=nil{
					panic(err5)
				}
				defer rows.Close()
				err6 :=db.QueryRow("SELECT count(*) FROM flsws WHERE La_todo=?","four").Scan(&count2)
				if err6 != nil{
					fmt.Println("查询审核类审批单数目出错！")
				}
				i:=0
				List_item :=make([]Flsw_list_item,count+count2)
				for rows.Next(){
					rows.Scan(&id,&name,&creator,&time,&opinion_yn,&status,&itype)
					List_item[i].Item_id = id
					List_item[i].Item_name = name
					List_item[i].Item_creator = creator
					List_item[i].Item_time = time
					List_item[i].Item_opinion_yn = opinion_yn
					List_item[i].Item_status = status
					List_item[i].Item_type = itype
					i++
				}
				//载入审核类审批单信息
				
				rows2,err7 :=db.Query("SELECT La_id,La_name,La_creator,La_time,La_opinion_yn,La_node,La_type FROM flsws WHERE La_todo = ?","four")
				if err7 != nil{
					fmt.Println("查询审核类审批单信息出错！")
				}
				defer rows2.Close()
				for rows2.Next(){
					rows2.Scan(&id,&name,&creator,&time,&opinion_yn,&status,&itype)
					List_item[i].Item_id = id
					List_item[i].Item_name = name
					List_item[i].Item_creator = creator
					List_item[i].Item_time = time
					List_item[i].Item_opinion_yn = opinion_yn
					List_item[i].Item_status = status
					List_item[i].Item_type = itype
					i++
				}



				cq.JSON(200,List_item)
			}else if cr_position == "14"{//如果用户是院长
			//载入审核类审批单信息
				rows,err7 :=db.Query("SELECT La_id,La_name,La_creator,La_time,La_opinion_yn,La_node,La_type FROM flsws WHERE La_todo = ?","five")
				if err7 != nil{
					fmt.Println("查询审核类审批单信息出错！")
				}
				err6 :=db.QueryRow("SELECT count(*) FROM flsws WHERE La_todo=?","five").Scan(&count)
				if err6 != nil{
					fmt.Println("查询审核类审批单数目出错！")
				}
				defer rows.Close()
				i:=0
				List_item :=make([]Flsw_list_item,count)
				for rows.Next(){
					rows.Scan(&id,&name,&creator,&time,&opinion_yn,&status,&itype)
					List_item[i].Item_id = id
					List_item[i].Item_name = name
					List_item[i].Item_creator = creator
					List_item[i].Item_time = time
					List_item[i].Item_opinion_yn = opinion_yn
					List_item[i].Item_status = status
					List_item[i].Item_type = itype
					i++
				}
				cq.JSON(200,List_item)
				
				
				
				
				



				
			}
			
	
		}
	}
}

//返回审批单详细数据
//{
//	"name":"321312",
//	"creator":"123",
//	"time":"2017-01-12",
//	"status":"2",
//	"id":"1524625263",
//}
//接收
//{
//	"listowner":"123"
//}



/*获得信息
{
	"flsw_id":"12325266",
	"user_id":"129"
}
{
	"flsw_name":"",
	"flsw_type":"审批类型审批单",
	"flsw_creator":"王某",
	"flsw_time":"2017-01-22",
	"flsw_content":"",
	"flsw_id":"",
	"flsw_dep":"",
	"flsw_email":"",
	"flsw_cellphone":"",
	"flsw_remark":"",
	"flsw_record":"",
	"flsw_opinions":
	[
		{
			"opinion_creator":"",
			"opinion_content":"",
			"opinion_user":"",
			"opinion_exe_yn":"",
			"opinion_unexe_res":""
		},
		{

		}
	]}


*/
func Flsw_info_leader(cq *gin.Context){
	var user_flsw_id User_flsw_id
	var Af_all_info Af_info
	var Return_af_info Return_af_info
	var todo string
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
			//如果审批单是文书类型

			//如果审批单是审核类型
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
																				  &Af_all_info.La_check_record,
																				  &todo)
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

//
//{
//	"opinion":"",
//	"yn":"",
//	"flswid":"",
//	"userid":""
//}
//
//
//
//
//
func Flsw_opinion_yn(cq *gin.Context){
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
				//将审批单状态改为1
				//将opinion放到审批单中
				//将opinion_yn改为yes
				//将审批单todo状态改为one
				result2,err2 :=db.Exec("UPDATE flsws SET La_node=?,La_todo=? WHERE La_id=?","1","one",Flswid)
				if err2 !=nil{
					fmt.Println("修改node出现错误")
				}
				fmt.Println(result2.RowsAffected())
			}else if Yn == "no"{
				//将审批单状态改为7
				//将opinion放到审批单中
				//将opinion_yn改为no
				//将审批单todo状态改为one
				result2,err2 :=db.Exec("UPDATE flsws SET La_node=?,La_todo=? WHERE La_id=?","7","one",Flswid)
				if err2 !=nil{
					fmt.Println("修改node出现错误")
				}
				fmt.Println(result2.RowsAffected())
			}else if Yn == "change"{
				//将审批单状态改为-1
				//将opinion放到审批单中
				//将opinion_yn改为change
				//将审批单todo状态改为one
				result2,err2 :=db.Exec("UPDATE flsws SET La_node=?,La_todo=? WHERE La_id=?","-1","one",Flswid)
				if err2 !=nil{
					fmt.Println("修改node出现错误")
				}
				fmt.Println(result2.RowsAffected())
			}
			result3,err3 :=db.Exec("UPDATE flsws SET La_opinion=? WHERE La_id=?",Opinion,Flswid)
			if err3 !=nil{
				fmt.Println("修改opinion出现错误")
			}
			fmt.Println(result3.RowsAffected())
			result4,err4 :=db.Exec("UPDATE flsws SET La_opinion_yn=? WHERE La_id=?",Yn,Flswid)
			if err4 !=nil{
				fmt.Println("修改opinion_yn出现错误")
			}
			fmt.Println(result4.RowsAffected())

			if err3 == nil && err4 ==nil{
				cq.JSON(200,"operate succeed!")
			}else{
				cq.JSON(400,"operate failed!")
			}
		}
	}
}



//{
//	"flsw":"",
//	"user_id":""
//}
func Flsw_info_result(cq *gin.Context){
	var user_flsw_id User_flsw_id
	var Af_all_info Af_info
	var Return_af_info Return_af_info2
	var todo string
	if cq.ShouldBind(&user_flsw_id) == nil{
		Userid := user_flsw_id.User_id
		Flswid := user_flsw_id.Flsw_id
		fmt.Println(Userid)
		fmt.Println(Flswid)
		if Userid == "" || Flswid == ""{
			fmt.Println("账号或密码不能为空")
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
																				  &Af_all_info.La_check_record,
																				  &todo)
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
			Return_af_info.Flsw_opinion = Af_all_info.La_opinion
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


func Op_status_sub(cq *gin.Context){
	var op_info Op_status_get
	if cq.ShouldBind(&op_info) == nil{
		op_id :=op_info.Op_id
		op_exe :=op_info.Op_exe
		op_res :=op_info.Op_res
		if op_id == "" || op_exe == ""{
			fmt.Println("id or exe is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("连接数据库失败",err)
			}
			if op_exe == "yes"{//如果发起人选择执行该意见
				//修改意见执行状态为yes
				//修改意见执行理由为op_res
				//修改意见状态为0
				result,err2 :=db.Exec("UPDATE opinions SET Opinion_exe_yn=?,Opinion_unexe_res=?,Opinion_unexe_status=? WHERE Opinion_id=?","yes",op_res,0,op_id)
				if err2 !=nil{
					fmt.Println("修改opinion状态出现错误")
					cq.JSON(400,"operate failed!")
				}else{
					cq.JSON(200,"operate succeed!")
				}
				fmt.Println(result.RowsAffected())

			}else if op_exe == "no"{//如果发起人选择不执行该意见
				result2,err3 :=db.Exec("UPDATE opinions SET Opinion_exe_yn=?,Opinion_unexe_res=?,Opinion_unexe_status=? WHERE Opinion_id=?","no",op_res,0,op_id)
				if err3 !=nil{
					fmt.Println("修改opinion状态出现错误")
					cq.JSON(400,"operate failed!")
				}else{
					cq.JSON(200,"operate succeed!")
				}
				fmt.Println(result2.RowsAffected())
			}else if op_exe == "agree"{ //agree表示领导同意不执行意见
				result3,err4 :=db.Exec("UPDATE opinions SET Opinion_unexe_status=? WHERE Opinion_id=?","1",op_id)
				if err4 !=nil{
					fmt.Println("修改opinion状态出现错误")
					cq.JSON(400,"operate failed!")
				}else{
					cq.JSON(200,"operate succeed!")
				}
				fmt.Println(result3.RowsAffected())		
			}else if op_exe == "force"{//force表示领导不同意不执行意见，强制执行意见
				result4,err5 :=db.Exec("UPDATE opinions SET Opinion_exe_yn=?, Opinion_unexe_status=? WHERE Opinion_id=?","force","0",op_id)
				if err5 !=nil{
					fmt.Println("修改opinion状态出现错误")
					cq.JSON(400,"operate failed!")
				}else{
					cq.JSON(200,"operate succeed!")
				}
				fmt.Println(result4.RowsAffected())
			}

		}
	}
}



func Check_la_op(cq *gin.Context){
	var la_id_json Get_opinion
	var count int
	var status string
	if cq.ShouldBind(&la_id_json) == nil{
		la_id :=la_id_json.Flsw_id
		if la_id == ""{
			fmt.Println("flsw_id is empty!")
		}else{
			db,err :=sql.Open("mysql","root:123456@tcp(localhost:3306)/la_database?charset=utf8")
			if err !=nil{
				fmt.Println("连接数据库失败",err)
			}

			err2 :=db.QueryRow("SELECT count(*) FROM opinions WHERE La_op_id=? AND Opinion_exe_yn=?",la_id,"no").Scan(&count)
			if err2 != nil{
				fmt.Println("查询不执行意见出错！")	
				cq.JSON(400,"operate failed!")
			}
			if count == 0{//没有不执行意见，进入最终状态7，one
				result,err3 :=db.Exec("UPDATE flsws SET La_node=?,La_prenode=?,La_todo=? WHERE La_id=?","7","3","one",la_id)
				if err3 !=nil{
					fmt.Println("修改审核审批单状态出现错误")
					cq.JSON(400,"operate failed!")
				}else{
					cq.JSON(200,"operate succeed!")
				}
				fmt.Println(result.RowsAffected())
			}else{ //存在不执行意见，进入状态4，领导审核，two
				//判断当前状态是否为3，若是3，变为状态4
				//若为4，变为状态5（总经理审核）
				//若为5，变为状态6（院长审核）
				err5 :=db.QueryRow("SELECT La_node FROM flsws WHERE La_id=?",la_id).Scan(&status)
				if err5 !=nil{
					fmt.Println("查询审批单状态出错！")
					cq.JSON(400,"查询审批单状态出错！")
				}
				if status == "3"{//当前状态为3，刚结束企发部给意见
					result2,err4 :=db.Exec("UPDATE flsws SET La_node=?,La_prenode=?,La_todo=? WHERE La_id=?","4","3","two",la_id)
					if err4 !=nil{
						fmt.Println("修改审核审批单状态出现错误")
						cq.JSON(400,"operate failed!")
					}else{
						cq.JSON(200,"operate succeed!")
					}
					fmt.Println(result2.RowsAffected())
				}else if status == "4"{//当前状态为4，刚结束领导审核
					result2,err4 :=db.Exec("UPDATE flsws SET La_node=?,La_prenode=?,La_todo=? WHERE La_id=?","5","4","four",la_id)
					if err4 !=nil{
						fmt.Println("修改审核审批单状态出现错误")
						cq.JSON(400,"operate failed!")
					}else{
						cq.JSON(200,"operate succeed!")
					}
					fmt.Println(result2.RowsAffected())
				}else if status == "5"{//当前状态为5，刚结束总经理审核
					result2,err4 :=db.Exec("UPDATE flsws SET La_node=?,La_prenode=?,La_todo=? WHERE La_id=?","6","5","five",la_id)
					if err4 !=nil{
						fmt.Println("修改审核审批单状态出现错误")
						cq.JSON(400,"operate failed!")
					}else{
						cq.JSON(200,"operate succeed!")
					}
					fmt.Println(result2.RowsAffected())
				}else if status == "6"{//当前状态为6，刚结束院长审核，等待执行人查看
					result2,err4 :=db.Exec("UPDATE flsws SET La_node=?,La_prenode=?,La_todo=? WHERE La_id=?","7","6","one",la_id)
					if err4 !=nil{
						fmt.Println("修改审核审批单状态出现错误")
						cq.JSON(400,"operate failed!")
					}else{
						cq.JSON(200,"operate succeed!")
					}
					fmt.Println(result2.RowsAffected())

				}
				

			}
		}
	}
}





