package com.example.android.forrest.utils;

public class FirebaseUtils {

  public static String getUsername(String firebaseDisplayName) {
    return firebaseDisplayName != null ? firebaseDisplayName.split(" ")[0] : "";
  }
}
