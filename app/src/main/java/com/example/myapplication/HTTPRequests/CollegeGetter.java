package com.example.myapplication.HTTPRequests;

import android.util.Log;

import com.example.myapplication.Interfaces.CollegeCallback;
import com.example.myapplication.Models.College;
import com.example.myapplication.Models.Faculty;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CollegeGetter {
    final private String URL_TO_READ = "http://185.250.44.61:5000/api/v1/colleges/";

    public void GetAll(CollegeCallback callback) throws Exception {
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
                    List<College> colleges = Arrays.asList(gson.fromJson(responseBody.string(), College[].class));
                    callback.OnSuccess(colleges);
                } catch (Exception e) {
                    Log.d("eeeeeee", e.getMessage());
                }
            }
        });
    }
}
