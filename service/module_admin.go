package main
type Cre_user struct{
	Userpwd string `json:userpwd`
	Userposition string `json:userposition`
	Usersex string `json:usersex`
	Username string `json:username`
	Userdep string `json:userdep`
}

type Cre_user_all struct{
	user_id string 
	user_name string
	user_password string
	user_sex string
	user_dep string
	user_position int
	user_cellphone string
	user_email string 
}
type Cre_user_all2 struct{
	User_id string `json:user_id`
	User_name string `json:user_name`
	User_password string `json:user_password`
	User_sex string `json:user_sex`
	User_dep string `json:user_dep`
	User_position int `json:user_position`
	User_cellphone string `json:user_cellphone`
	User_email string `json:user_email`
}

type Ch_pwd struct{
	Userid string `json:userid`
	Oldpwd string `json:oldpwd`
	Newpwd string `json:newpwd`
} 

type Ch_pwd_res struct{
	Ch_res string `json:ch_res`
}

type Ch_phone struct{
	Userid string `json:userid`
	User_phone string `json:user_phone`
}

type User_short struct{
	Userid string `json:userid`
	Password string `json:password`
}