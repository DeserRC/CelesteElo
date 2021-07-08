package com.celeste.elo.view.listener;

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

    final int difference;
    final int elo;
    final int eloReward;

    if (targetIndex == userIndex) {
      difference = 0;
      eloReward = eloBase;
    } else if (targetIndex < userIndex) {
      difference = userIndex - targetIndex;

      double multiply = ((userRank.getElo() - targetRank.getElo()) / 25) / 10;

      if (multiply < 0) {
        multiply = 0;
      }

      eloReward = Math.toIntExact(Math.round(eloBase * (multiply / 4 + 1)));
    } else {
      difference = targetIndex - userIndex;
      eloReward = eloBase;
    }

    final int eloLose = eloReward / 2;
  }

}
