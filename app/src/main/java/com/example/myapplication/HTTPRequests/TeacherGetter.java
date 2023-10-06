package com.example.myapplication.HTTPRequests;

import android.util.Log;

import com.example.myapplication.Interfaces.ListCallback;
import com.example.myapplication.Models.Study;
import com.example.myapplication.Models.Teacher;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Array;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TeacherGetter {
    final private String URL_TO_READ = "http://188.225.77.116:5000/api/v1/teachers";

    public void GetAll(ListCallback<Teacher> callback) throws Exception{
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
                    List<Teacher> teacher = Arrays.asList(gson.fromJson(responseBody.string(), Teacher[].class));
                    callback.onSuccess(teacher);
                }
                catch(Exception e){
                    Log.d("eeeeeee", e.getMessage());
                }
            }
        });
    }
}
