package com.security.info.aesbruteforce;

import com.security.info.aesbruteforce.di.AppComponent;
import com.security.info.aesbruteforce.di.DaggerAppComponent;

public class App extends android.app.Application {

  public static AppComponent component;

  @Override
  public void onCreate() {
    super.onCreate();
    component = DaggerAppComponent.create();
  }
}
