package com.celeste.elo.model.map;

import com.celeste.elo.model.entity.Rank;
import com.celeste.elo.model.entity.User;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class RankMap extends LowerCaseMap<Rank> {

  public RankMap() {
    super();
  }

  public RankMap(final Supplier<Map<String, Rank>> supplier) {
    super(supplier);
  }

  public RankMap(final Map<String, Rank> values) {
    super(values);
  }

  public RankMap(final Supplier<Map<String, Rank>> supplier, final Map<String, Rank> values) {
    super(supplier, values);
  }

  public Rank put(final Rank rank) {
    return super.put(rank.getName(), rank);
  }

  public Rank putIfAbsent(final Rank rank) {
    return super.putIfAbsent(rank.getName(), rank);
  }

  public Rank remove(final Rank rank) {
    return super.remove(rank.getName());
  }

  public boolean containsKey(final User rank) {
    return super.containsKey(rank.getName());
  }

  public Rank replace(final Rank rank) {
    return super.replace(rank.getName(), rank);
  }

  public Rank merge(final Rank rank, final BiFunction<? super Rank, ? super Rank,
      ? extends Rank> biFunction) {
    return super.merge(rank.getName(), rank, biFunction);
  }

}
