package com.celeste.elo;

import com.celeste.elo.factory.ConnectionFactory;
import com.celeste.elo.factory.RankFactory;
import com.celeste.elo.factory.SettingsFactory;
import com.celeste.elo.factory.UserFactory;
import com.celeste.elo.view.listener.UserListener;
import com.celeste.elo.view.placeholder.RankPlaceholder;
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

  private RankPlaceholder rankPlaceholder;

  @Override
  public void onLoad() {
    try {
      INSTANCE = this;

      this.settingsFactory = new SettingsFactory(this);
      this.connectionFactory = new ConnectionFactory(this);
      this.rankFactory = new RankFactory(this);
      this.userFactory = new UserFactory(this);

      this.rankPlaceholder = new RankPlaceholder(this);
    } catch (Exception exception) {
      Logger.getLogger().atSevere()
          .withCause(exception)
          .log("There was an error loading the plugin.");
    }
  }

  @Override
  public void onEnable() {
    try {
      CompletableFuture.runAsync(userFactory.getUserGetTask(), EXECUTOR);
      SCHEDULED.scheduleWithFixedDelay(userFactory.getUserUpdateTask(), 10, 10, TimeUnit.MINUTES);

      registerListeners(new UserListener(this));

      rankPlaceholder.register();
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
      rankPlaceholder.unregister();
    } catch (Exception exception) {
      Logger.getLogger().atSevere()
          .withCause(exception)
          .log("There was an error disabling the plugin.");
    }
  }

  public static CelesteElo getInstance() {
    return INSTANCE;
  }

}
