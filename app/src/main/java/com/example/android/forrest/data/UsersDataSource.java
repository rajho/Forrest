package com.example.android.forrest.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.android.forrest.data.model.User;

public interface UsersDataSource {

  void insertUser(@NonNull User user);

  void updateUser(@NonNull User user);

  LiveData<User> getUserById(@NonNull String id);

}
