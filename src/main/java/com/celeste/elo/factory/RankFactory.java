package com.celeste.elo.factory;

import com.celeste.elo.CelesteElo;
import com.celeste.elo.controller.RankController;
import com.celeste.elo.model.map.RankMap;
import java.util.TreeMap;
import lombok.Getter;

@Getter
public final class RankFactory {

  private final RankController rankController;
  private final RankMap ranks;

  public RankFactory(final CelesteElo plugin) {
    this.rankController = new RankController(plugin);
    this.ranks = new RankMap(TreeMap::new);
  }

}
