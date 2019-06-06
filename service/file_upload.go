package main

import (
	"github.com/gin-gonic/gin"
	"io"
	"fmt"
	"net/http"
	"os"
)

func Get_upload_file(cq *gin.Context){
	file,handler,err :=cq.Request.FormFile("file")
	if err == nil{
		filename :=handler.Filename
		fmt.Println("Received file:",handler.Filename)
		/*
		out,err :=os.Create(filename)
		if err !=nil{
			panic(err)
		}
		defer out.Close()
		_,err = io.Copy(out,file)
		if err != nil {
			panic(err)
		}*/
	
		var uploadir string 
		uploadir = "upload_file/"
		_,err2 :=os.Stat(uploadir)
		if os.IsNotExist(err2){
			os.Mkdir(uploadir,os.ModePerm)
		}
		//创建文件
		out,err3 :=os.Create(uploadir+filename)
		if err3 !=nil{
			panic(err3)
		}
		defer out.Close()
		_,err4 := io.Copy(out,file)
		if err4 !=nil{
			panic(err4)
		}
		cq.JSON(200,"UPLOADED....")
	}else{
		cq.JSON(400,"UPLOAD FAILED")
	}
	
}



func Post_download__file(cq *gin.Context){
	filename :=cq.Request.URL.Query().Get("filename")
	//path_to_file :="\\upload_file\\"+filename
	path_to_file :="./upload_file\\"+filename
	fmt.Println("path_to_file",path_to_file)
	http.ServeFile(cq.Writer,cq.Request,path_to_file)
	
}