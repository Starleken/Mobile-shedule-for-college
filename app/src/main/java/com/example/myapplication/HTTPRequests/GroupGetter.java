package com.example.myapplication.HTTPRequests;

import android.util.Log;

import com.example.myapplication.Interfaces.ElementCallback;
import com.example.myapplication.Models.Audience.Group;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.MessageFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GroupGetter {
    final private String URL_TO_READ = "http://185.250.44.61:5000/api/v1/groups";

    public void GetGroupById(int groupId, ElementCallback<Group> groupCallback) throws Exception {
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(MessageFormat.format("{0}/{1}", URL_TO_READ, groupId))
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
                    Group group = gson.fromJson(responseBody.string(), Group.class);
                    groupCallback.onSuccess(group);
                } catch (Exception e) {
                    Log.d("eeeeeee", e.getMessage());
                }
            }
        });
    }
}