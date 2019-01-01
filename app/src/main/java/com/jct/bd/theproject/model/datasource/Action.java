package com.jct.bd.theproject.model.datasource;

public interface Action<T> {
    void onSuccess(String obj);

    void onFailure(Exception exception);

    void onProgress(String status, double percent);
}
