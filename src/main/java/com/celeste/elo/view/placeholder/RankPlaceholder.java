package com.celeste.elo.view.placeholder;

import com.celeste.elo.CelesteElo;
import com.celeste.elo.controller.UserController;
import com.celeste.elo.model.entity.Rank;
import com.celeste.elo.model.entity.User;
import java.util.StringJoiner;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class RankPlaceholder extends PlaceholderExpansion {

  private static final String ERROR;

  static {
    ERROR = "-/-";
  }

  private final CelesteElo plugin;

  @Override
  public String onPlaceholderRequest(final Player player, @NotNull final String parameters) {
    final UserController userController = plugin.getUserFactory().getUserController();

    if (parameters.equalsIgnoreCase("rank")) {
      final User user = userController.getById(player.getUniqueId());
      final Rank rank = userController.getRank(user);

      return rank.getName();
    }

    if (parameters.equalsIgnoreCase("prefix")) {
      final User user = userController.getById(player.getUniqueId());
      final Rank rank = userController.getRank(user);

      return rank.getPrefix();
    }

    if (parameters.equalsIgnoreCase("elo")) {
      final User user = userController.getById(player.getUniqueId());

      return String.valueOf(user.getElo());
    }

    return ERROR;
  }

  @Override
  @NotNull
  public String getIdentifier() {
    return "elo";
  }

  @Override
  @NotNull
  public String getAuthor() {
    final PluginDescriptionFile description = plugin.getDescription();

    final StringJoiner joiner = new StringJoiner(", ");
    description.getAuthors().forEach(joiner::add);

    return joiner.toString().substring(joiner.length() - 3);
  }

  @Override
  @NotNull
  public String getVersion() {
    final PluginDescriptionFile description = plugin.getDescription();

    return description.getVersion();
  }

}
