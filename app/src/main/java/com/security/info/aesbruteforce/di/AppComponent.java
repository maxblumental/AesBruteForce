package com.security.info.aesbruteforce.di;

import com.security.info.aesbruteforce.MainActivity;
import com.security.info.aesbruteforce.tune.TuneActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component
public interface AppComponent {

    void inject(TuneActivity tuneActivity);

    void inject(MainActivity mainActivity);
}
