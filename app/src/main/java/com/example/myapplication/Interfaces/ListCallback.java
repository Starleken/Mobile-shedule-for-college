package com.example.myapplication.Interfaces;

import java.util.List;

public interface ListCallback<T> {
    public void onSuccess(List<T> result);
}
