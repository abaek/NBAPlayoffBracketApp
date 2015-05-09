package com.example.andy.nbaplayoffbracket;

public interface Injector {
  <T> T get(Class<? extends T> type);

  <T> void inject(T instance);
}
