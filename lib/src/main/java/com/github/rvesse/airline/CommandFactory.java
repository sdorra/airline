package com.github.rvesse.airline;

public interface CommandFactory<T> {
  T createInstance(Class<?> type);
}
