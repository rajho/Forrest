package com.example.android.forrest.data.source.remote;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MealsJsonUtils {
  public static List<MealDto> getMealsListFromJson(String jsonMeals) throws IOException {
    Moshi                      moshi       = new Moshi.Builder().build();
    Type                       listMyData  = Types.newParameterizedType(List.class, MealDto.class);
    JsonAdapter<List<MealDto>> jsonAdapter = moshi.adapter(listMyData);

    return jsonAdapter.fromJson(jsonMeals);
  }
}
