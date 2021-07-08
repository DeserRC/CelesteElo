package com.celeste.elo.view.listener;

import com.celeste.configuration.model.provider.Configuration;
import com.celeste.elo.CelesteElo;
import com.celeste.elo.controller.RankController;
import com.celeste.elo.controller.UserController;
import com.celeste.elo.model.entity.Rank;
import com.celeste.elo.model.entity.User;
import com.celeste.library.spigot.AbstractBukkitPlugin;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public final class UserListener implements Listener {

  private static final Random RANDOM;

  static {
    RANDOM = new Random();
  }

  private final CelesteElo plugin;

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    final UserController userController = plugin.getUserFactory().getUserController();
    final Player player = event.getPlayer();

    AbstractBukkitPlugin.getExecutor().execute(() -> userController.register(player));
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    final UserController userController = plugin.getUserFactory().getUserController();
    final Player player = event.getPlayer();

    AbstractBukkitPlugin.getExecutor().execute(() -> userController.unregister(player));
  }

  @EventHandler
  public void onDeath(final PlayerDeathEvent event) {
    final Configuration messages = plugin.getSettingsFactory().getMessages();

    final UserController userController = plugin.getUserFactory().getUserController();
    final RankController rankController = plugin.getRankFactory().getRankController();

    final Player player = event.getEntity();
    final Player killer = player.getKiller();

    final User user = CompletableFuture.supplyAsync(() ->
        userController.getById(player.getUniqueId()), AbstractBukkitPlugin.getExecutor()).join();

    final User target = CompletableFuture.supplyAsync(() ->
        userController.getById(killer.getUniqueId()), AbstractBukkitPlugin.getExecutor()).join();

    final Rank userRank = userController.getRank(user);
    final Rank targetRank = userController.getRank(target);

    final List<Rank> ranks = rankController.getRanks();

    final int userIndex = ranks.indexOf(userRank);
    final int targetIndex = ranks.indexOf(targetRank);

    final int eloBase = RANDOM.ints(10, 25)
        .findFirst()
        .orElseThrow(() -> new RuntimeException("An error occurred getting the elo base."));

    int multiply = targetIndex < userIndex
        ? (userRank.getElo() - targetRank.getElo()) / 25 / 10
        : userIndex < targetIndex
        ? (targetRank.getElo() - userRank.getElo()) / 25 / 10
        : 0;

    if (multiply < 0) {
      multiply = 0;
    }

    int eloReward = eloBase * (multiply / 4 + 1);

    if (user.getKillStreak() > 10) {
      eloReward = (user.getKillStreak() / 100 + 1) * eloReward;
    }

    int eloLose = eloReward / 2;

    if (user.getElo() < eloLose) {
      eloLose = user.getElo();
    }

    userController.removeElo(user, eloLose);
    userController.addElo(target, eloReward);

    player.sendMessage(messages.getString("events.elo.death")
        .replace("%target%", killer.getDisplayName())
        .replace("%elo% ", String.valueOf(eloLose))
        .replace("%new_elo%", String.valueOf(user.getElo())));

    killer.sendMessage(messages.getString("events.elo.kill")
        .replace("%target%", player.getDisplayName())
        .replace("%elo% ", String.valueOf(eloReward))
        .replace("%new_elo%", String.valueOf(target.getElo())));

    if (user.getKillStreak() > 0 && user.getKillStreak() % 10 == 0) {
      player.sendMessage(messages.getString("events.killstreak.death")
          .replace("%target%", killer.getDisplayName())
          .replace("%killstreak%", String.valueOf(user.getKillStreak())));

      if (messages.getBoolean("events.killstreak.broadcast.use")) {
        Bukkit.broadcastMessage(messages.getString("events.killstreak.broadcast.death")
            .replace("%player%", player.getDisplayName())
            .replace("%target%", killer.getDisplayName())
            .replace("%killstreak%", String.valueOf(user.getKillStreak())));
      }
    }

    userController.setKillStreak(user, 0);
    userController.addKillStreak(target, 1);

    if (target.getKillStreak() > 0 && target.getKillStreak() % 10 == 0) {
      killer.sendMessage(messages.getString("events.killstreak.kill")
          .replace("%target%", player.getDisplayName())
          .replace("%killstreak%", String.valueOf(target.getKillStreak())));

      if (messages.getBoolean("events.killstreak.broadcast.use")) {
        Bukkit.broadcastMessage(messages.getString("events.killstreak.broadcast.kill")
            .replace("%player%", killer.getDisplayName())
            .replace("%target%", player.getDisplayName())
            .replace("%killstreak%", String.valueOf(target.getKillStreak())));
      }
    }
  }

}
