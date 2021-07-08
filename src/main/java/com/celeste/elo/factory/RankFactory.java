package com.celeste.elo.factory;

import com.celeste.elo.CelesteElo;
import com.celeste.elo.controller.RankController;
import com.celeste.elo.model.map.RankMap;
import com.celeste.elo.view.placeholder.RankPlaceholder;
import java.util.TreeMap;
import lombok.Getter;

@Getter
public final class RankFactory {

  private final RankController rankController;
  private final RankMap ranks;

  private final RankPlaceholder rankPlaceholder;

  public RankFactory(final CelesteElo plugin) {
    this.rankController = new RankController(plugin);
    this.ranks = new RankMap(TreeMap::new);

    this.rankPlaceholder = new RankPlaceholder(plugin);
  }

}
