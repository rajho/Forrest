package com.example.android.forrest.data;

import androidx.annotation.NonNull;

import com.example.android.forrest.data.model.User;

public interface UsersDataSource {

  void insertUser(@NonNull User user);

}
