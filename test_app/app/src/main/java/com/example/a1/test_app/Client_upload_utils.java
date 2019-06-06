package com.example.a1.test_app;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Client_upload_utils {

    public static void upload_file(String uploadFilePath, String uploadFileName){


                        OkHttpClient client = new OkHttpClient();
                        MultipartBody.Builder builder = new MultipartBody.Builder();
                        builder.setType(MultipartBody.FORM);
                        RequestBody requestBody =RequestBody.create(MediaType.parse("multipart/form-data"),new File(uploadFilePath));
                        builder.addFormDataPart("file",uploadFileName,requestBody);
                        MultipartBody multipartBody = builder.build();
                        Request request = new Request.Builder()
                                .header("Authorization","Client-ID"+ UUID.randomUUID())
                                .url("http://192.168.191.1:3000/get_upload_file")
                                .post(multipartBody)
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                            }
                        });
                        System.out.println("running!");
                        System.out.println("path="+uploadFilePath);
                        System.out.println("name="+uploadFileName);
                    }

}

