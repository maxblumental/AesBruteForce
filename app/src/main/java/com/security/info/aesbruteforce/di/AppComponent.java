package com.security.info.aesbruteforce.di;

import com.security.info.aesbruteforce.decode.DecodeFragment;
import com.security.info.aesbruteforce.tune.TuneActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component
public interface AppComponent {

  void inject(TuneActivity tuneActivity);

  void inject(DecodeFragment decodeFragment);
}
