package com.celeste.elo;

import com.celeste.elo.factory.ConnectionFactory;
import com.celeste.elo.factory.RankFactory;
import com.celeste.elo.factory.SettingsFactory;
import com.celeste.elo.factory.UserFactory;
import com.celeste.elo.view.listener.UserListener;
import com.celeste.library.core.util.Logger;
import com.celeste.library.spigot.AbstractBukkitPlugin;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.bukkit.event.HandlerList;

@Getter
public final class CelesteElo extends AbstractBukkitPlugin {

  private static CelesteElo INSTANCE;

  private SettingsFactory settingsFactory;
  private ConnectionFactory connectionFactory;
  private RankFactory rankFactory;
  private UserFactory userFactory;

  @Override
  public void onLoad() {
    try {
      INSTANCE = this;

      this.settingsFactory = new SettingsFactory(this);
      this.connectionFactory = new ConnectionFactory(this);
      this.rankFactory = new RankFactory(this);
      this.userFactory = new UserFactory(this);
    } catch (Exception exception) {
      Logger.getLogger().atSevere()
          .withCause(exception)
          .log("There was an error loading the plugin.");
    }
  }

  @Override
  public void onEnable() {
    try {
      loadTasks();
      loadCommands();
      loadListeners();
    } catch (Exception exception) {
      Logger.getLogger().atSevere()
          .withCause(exception)
          .log("There was an error enabling the plugin.");
    }
  }

  @Override
  public void onDisable() {
    try {
      CompletableFuture.runAsync(userFactory.getUserUpdateTask(), EXECUTOR).join();
      connectionFactory.getStorage().shutdown();

      EXECUTOR.shutdown();
      SCHEDULED.shutdown();

      HandlerList.unregisterAll();
    } catch (Exception exception) {
      Logger.getLogger().atSevere()
          .withCause(exception)
          .log("There was an error disabling the plugin.");
    }
  }

  private void loadTasks() {
    CompletableFuture.runAsync(userFactory.getUserGetTask(), EXECUTOR);
    SCHEDULED.scheduleWithFixedDelay(userFactory.getUserUpdateTask(), 10, 10, TimeUnit.MINUTES);
  }

  private void loadCommands() {

  }

  private void loadListeners() {
    registerListeners(new UserListener(this));
  }

  public static CelesteElo getInstance() {
    return INSTANCE;
  }

}
