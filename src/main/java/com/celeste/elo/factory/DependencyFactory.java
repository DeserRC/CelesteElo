package com.celeste.elo.factory;

import com.celeste.elo.CelesteElo;
import com.celeste.elo.view.placeholder.RankPlaceholder;
import lombok.Getter;

@Getter
public final class DependencyFactory {

  private RankPlaceholder rankPlaceholder;

  public DependencyFactory(final CelesteElo plugin) {
    try {
      Class.forName("me.clip.placeholderapi.expansion.PlaceholderExpansion");
      this.rankPlaceholder = new RankPlaceholder(plugin);
    } catch (Exception ignored) {
      // TODO: If it generates an exception it is because it is not using this dependency.
    }
  }

  public void init() {
    if (rankPlaceholder != null) {
      rankPlaceholder.register();
    }
  }

  public void shutdown() {
    if (rankPlaceholder != null) {
      rankPlaceholder.unregister();
    }
  }

}
