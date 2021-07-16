package com.example.android.forrest.data.source.remote;

import android.net.Uri;

import com.example.android.forrest.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkApi {

  final static String SPOONACULAR_BASE_URL =
      "https://api.spoonacular.com/recipes/findByNutrients";

  private static final String PARAM_FOOD_MAX_CALORIES = "maxCalories";
  private static final String PARAM_FOOD_MIN_CALORIES = "minCalories";
  private static final String PARAM_FOOD_NUMBER       = "number";
  private static final String PARAM_FOOD_API_KEY      = "apiKey";

  public static URL buildUrl(String maxCalories, String minCalories, String number) {
    Uri buildUri = Uri.parse(SPOONACULAR_BASE_URL).buildUpon()
                      .appendQueryParameter(PARAM_FOOD_MAX_CALORIES, maxCalories)
                      .appendQueryParameter(PARAM_FOOD_MIN_CALORIES, minCalories)
                      .appendQueryParameter(PARAM_FOOD_NUMBER, number)
                      .appendQueryParameter(PARAM_FOOD_API_KEY, BuildConfig.SPOONACULAR_API_KEY)
                      .build();

    try {
      return new URL(buildUri.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static String getResponseFromHttpUrl(URL url) throws IOException {
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    try {
      InputStream in = urlConnection.getInputStream();

      Scanner scanner = new Scanner(in);
      scanner.useDelimiter("\\A");

      boolean hasInput = scanner.hasNext();
      if (hasInput) {
        return scanner.next();
      } else {
        return null;
      }
    } finally {
      urlConnection.disconnect();
    }
  }
}
