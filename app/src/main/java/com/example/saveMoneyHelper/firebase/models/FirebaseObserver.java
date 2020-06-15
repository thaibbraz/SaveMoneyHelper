package com.example.saveMoneyHelper.firebase.models;

public interface FirebaseObserver<T> {
    void onChanged(T t);
}
