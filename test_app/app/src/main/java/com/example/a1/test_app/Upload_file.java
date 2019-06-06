package com.example.a1.test_app;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Upload_file {
    private String path;

    //获得真实路径
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if("content".equalsIgnoreCase(uri.getScheme())){
            String[] projection = {"_data"};
            Cursor cursor = null;
            try{
                cursor = context.getContentResolver().query(uri,projection,null,null,null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if(cursor.moveToFirst()){
                    return cursor.getString(column_index);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return uri.getPath();
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            return uri.getPath();
        }
        return null;
    }

    //上传文件
    public  static void upload(String uploadFilePath,String uploadFileName) throws IOException {
        final String filepath = uploadFilePath;
        final String filename = uploadFileName;
        File file2 = new File(filepath);
        if(file2.exists()){
            //Toast.makeText(FirstAcitivity.this,"找到文件,开始上传",Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file",filename,
                                    RequestBody.create(MediaType.parse("multipart/form-data"),new File(filepath)))
                            .build();
                    Request request = new Request.Builder()
                            .header("Content-Type","multipart/form-data")
                            .url("http://192.168.191.1:3000/get_upload_file")
                            .post(requestBody)
                            .build();

                    try {
                        Response response = client.newCall(request).execute();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }).start();
        }else{
            System.out.println("未找到文件");
           // Toast.makeText(FirstAcitivity.this,"未找到文件",Toast.LENGTH_SHORT).show();
        }



    }

}
