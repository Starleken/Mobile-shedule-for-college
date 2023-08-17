package com.example.myapplication.HTTPRequests;

import android.util.Log;

import com.example.myapplication.Models.Study;
import java.io.*;
import  java.net.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import com.google.gson.Gson;

public class StudyGetter {
    final private String URL_TO_READ = "http://10.0.2.2:5002/api/v1/studies/2";

    public void GetAll() throws Exception{
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL_TO_READ)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Failure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Запрос к серверу не был успешен: " +
                                response.code() + " " + response.message());
                    }

                    // вывод тела ответа
                    Log.d("Body",responseBody.string());
                    Study study = gson.fromJson(responseBody.string(), Study.class);
                }
                catch(Exception e){
                    Log.d("eeeeeee", e.getMessage());
                }
            }
        });
    }
}
