package com.example.android.forrest.data.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.android.forrest.data.model.Exercise;
import com.example.android.forrest.data.model.User;

@Database(entities = { User.class, Exercise.class }, version = 1, exportSchema = false)
public abstract class ForrestDatabase extends RoomDatabase {

  public abstract UsersDao usersDao();

  public abstract ExercisesDao exercisesDao();
}
