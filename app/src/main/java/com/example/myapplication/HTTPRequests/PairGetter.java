package com.example.myapplication.HTTPRequests;

import android.util.Log;

import com.example.myapplication.Interfaces.ListCallback;
import com.example.myapplication.Models.Pair;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PairGetter {
    final private String URL_TO_READ = "http://185.250.44.61:5000/api/v1/pairs?isCurrentDate=1";

    public void GetAllPairs(ListCallback<Pair> pairCallback) throws Exception {
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(MessageFormat.format("{0}", URL_TO_READ))
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
                    pairCallback.onSuccess(pairs);
                } catch (Exception e) {
                    Log.d("eeeeeee", e.getMessage());
                }
            }
        });
    }

    public void GetGroupPairs(int groupId, ListCallback<Pair> pairCallback) throws Exception {
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(MessageFormat.format("{0}&groupId={1}", URL_TO_READ, groupId))
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
                    pairCallback.onSuccess(pairs);
                } catch (Exception e) {
                    Log.d("eeeeeee", e.getMessage());
                }
            }
        });
    }

    public void GetTeacherPairs(int teacherId, ListCallback<Pair> pairCallback){
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(MessageFormat.format("{0}&teacherId={1}", URL_TO_READ, teacherId))
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
                    pairCallback.onSuccess(pairs);
                } catch (Exception e) {
                    Log.d("eeeeeee", e.getMessage());
                }
            }
        });
    }
}
