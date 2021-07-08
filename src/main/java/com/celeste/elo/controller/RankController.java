package com.celeste.elo.controller;

import com.celeste.configuration.model.provider.Configuration;
import com.celeste.elo.CelesteElo;
import com.celeste.elo.controller.exception.RankNotFoundException;
import com.celeste.elo.model.entity.Rank;
import com.celeste.elo.model.entity.comparator.EloComparator;
import com.celeste.elo.model.map.RankMap;
import com.celeste.library.core.util.Validation;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class RankController {

  private final CelesteElo plugin;

  public RankController(final CelesteElo plugin) {
    this.plugin = plugin;

    load();
  }

  public Rank getRankByName(final String rank) {
    final RankMap ranks = plugin.getRankFactory().getRanks();

    if (!ranks.containsKey(rank)) {
      throw new RankNotFoundException("No rank was found with the name " + rank.toUpperCase());
    }

    return ranks.get(rank);
  }

  public Rank getRankByPrefix(final String prefix) {
    final RankMap ranks = plugin.getRankFactory().getRanks();

    final Rank rank = ranks.values().stream()
        .filter(otherRank -> otherRank.getPrefix().equalsIgnoreCase(prefix))
        .findFirst()
        .orElse(null);

    return Validation.notNull(rank, () ->
        new RankNotFoundException("No rank was found with the prefix " + prefix.toUpperCase()));
  }

  public Rank getRankByElo(final int elo) {
    final RankMap ranks = plugin.getRankFactory().getRanks();

    final Rank rank = ranks.values().stream()
        .filter(otherRank -> otherRank.getElo() <= elo)
        .max(Comparator.comparingInt(Rank::getElo))
        .orElse(null);

    return Validation.notNull(rank, () ->
        new RankNotFoundException("No rating was found with the elo below " + elo));
  }

  public List<Rank> getRanks() {
    final RankMap ranks = plugin.getRankFactory().getRanks();

    return ranks.values().stream()
        .sorted(EloComparator.getInstance())
        .collect(Collectors.toList());
  }

  public int getDefaultElo() {
    final Configuration settings = plugin.getSettingsFactory().getSettings();

    return settings.getInt("elo.default");
  }

  private void load() {
    final Configuration settings = plugin.getSettingsFactory().getSettings();
    final RankMap ranks = plugin.getRankFactory().getRanks();

    for (final String rankName : settings.getKeys("ranks")) {
      final String path = "ranks." + rankName + ".";

      final String name = settings.getString(path + "name");
      final String prefix = settings.getString(path + "prefix");
      final int elo = settings.getInt(path + "elo");

      final Rank rank = new Rank(name, prefix, elo);
      ranks.put(rank);
    }
  }

}
