package com.example.myapplication.HTTPRequests;

import android.util.Log;

import com.example.myapplication.Interfaces.PairCallback;
import com.example.myapplication.Models.Audience.Group;
import com.example.myapplication.Models.Pair;
import com.example.myapplication.Models.Study;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PairGetter {
    final private String URL_TO_READ = "http://185.250.44.61:5000/api/v1/pairs?isCurrentDate=1&groupId=";

    public void GetAll(Group group, PairCallback pairCallback) throws Exception {
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(MessageFormat.format("{0}{1}", URL_TO_READ, group.id))
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
                    List<Pair> pairs = Arrays.asList(gson.fromJson(responseBody.string(), Pair[].class));
                    pairCallback.OnSuccess(pairs);
                } catch (Exception e) {
                    Log.d("eeeeeee", e.getMessage());
                }
            }
        });
    }
}
