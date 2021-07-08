package com.celeste.elo.model.map;

import com.celeste.library.core.util.Validation;
import com.google.common.collect.ForwardingMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("NullableProblems")
public class LowerCaseMap<T> extends ForwardingMap<String, T> {

  private final Map<String, T> map;

  public LowerCaseMap() {
    this.map = new ConcurrentHashMap<>();
  }

  public LowerCaseMap(final Supplier<Map<String, T>> supplier) {
    this.map = supplier.get();
  }

  public LowerCaseMap(final Map<String, T> values) {
    this.map = new ConcurrentHashMap<>();

    putAll(values);
  }

  public LowerCaseMap(final Supplier<Map<String, T>> supplier, final Map<String, T> values) {
    this.map = supplier.get();

    putAll(values);
  }

  @Override
  public T get(final Object key) {
    return super.get(Validation.notNull(key).toString().toLowerCase());
  }

  @Override
  public T getOrDefault(final Object key, final T orElse) {
    return super.getOrDefault(key.toString().toLowerCase(), orElse);
  }

  @Override
  public T put(final String key, final T value) {
    return super.put(key.toLowerCase(), value);
  }

  @Override
  public T putIfAbsent(final String key, final T value) {
    return super.putIfAbsent(key.toLowerCase(), value);
  }

  @Override
  public void putAll(final Map<? extends String, ? extends T> map) {
    map.forEach(this::put);
  }

  @Override
  public T compute(final String key, final BiFunction<? super String, ? super T,
      ? extends T> biFunction) {
    return super.compute(key.toLowerCase(), biFunction);
  }

  @Override
  public T computeIfAbsent(final String key, final Function<? super String, ? extends T> function) {
    return super.computeIfAbsent(key.toLowerCase(), function);
  }

  @Override
  public T computeIfPresent(final String key, final BiFunction<? super String, ? super T,
      ? extends T> biFunction) {
    return super.computeIfPresent(key.toLowerCase(), biFunction);
  }

  @Override
  public T remove(final Object key) {
    return super.remove(key.toString().toLowerCase());
  }

  @Override
  public boolean remove(final Object key, final Object value) {
    return super.remove(key.toString().toLowerCase(), value);
  }

  @Override
  public boolean containsKey(final Object key) {
    return super.containsKey(key.toString().toLowerCase());
  }

  @Override
  public boolean replace(final String key, final T oldValue, final T newValue) {
    return super.replace(key.toLowerCase(), oldValue, newValue);
  }

  @Override
  public T replace(final String key, final T value) {
    return super.replace(key.toLowerCase(), value);
  }

  @Override
  public T merge(final String key, final T value, final BiFunction<? super T, ? super T,
      ? extends T> biFunction) {
    return super.merge(key.toLowerCase(), value, biFunction);
  }

  @Override
  protected Map<String, T> delegate() {
    return map;
  }

}
