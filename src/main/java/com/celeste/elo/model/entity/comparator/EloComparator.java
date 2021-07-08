package com.celeste.elo.model.entity.comparator;

import com.celeste.elo.model.entity.Rank;
import java.util.Comparator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EloComparator implements Comparator<Rank> {

  private static final EloComparator INSTANCE;

  static {
    INSTANCE = new EloComparator();
  }

  @Override
  public int compare(final Rank rank1, final Rank rank2) {
    return 0;
  }

  public static EloComparator getInstance() {
    return INSTANCE;
  }

}
