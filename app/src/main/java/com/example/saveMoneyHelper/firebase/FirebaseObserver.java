package com.example.saveMoneyHelper.firebase;

public interface FirebaseObserver<T> {
    void onChanged(T t);
}
