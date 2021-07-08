package com.celeste.elo.model.map;

import com.celeste.elo.model.entity.User;
import com.google.common.collect.ForwardingMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class UserMap extends ForwardingMap<UUID, User> {

  private final Map<UUID, User> map;

  public UserMap() {
    this.map = new ConcurrentHashMap<>();
  }

  public UserMap(final Supplier<Map<UUID, User>> supplier) {
    this.map = supplier.get();
  }

  public UserMap(final Map<UUID, User> values) {
    this.map = new ConcurrentHashMap<>();

    putAll(values);
  }

  public UserMap(final Supplier<Map<UUID, User>> supplier, final Map<UUID, User> values) {
    this.map = supplier.get();

    putAll(values);
  }

  public User put(final User user) {
    return super.put(user.getId(), user);
  }

  public User putIfAbsent(final User user) {
    return super.putIfAbsent(user.getId(), user);
  }

  public User remove(final User user) {
    return super.remove(user.getId());
  }

  public boolean containsKey(final User user) {
    return super.containsKey(user.getId());
  }

  public User replace(final User user) {
    return super.replace(user.getId(), user);
  }

  public User merge(final User user, final BiFunction<? super User, ? super User,
      ? extends User> biFunction) {
    return super.merge(user.getId(), user, biFunction);
  }

  @Override
  protected Map<UUID, User> delegate() {
    return map;
  }

}
