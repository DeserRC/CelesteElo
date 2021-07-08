package com.celeste.elo.view.task;

import com.celeste.elo.CelesteElo;
import com.celeste.elo.controller.UserController;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public final class UserGetTask implements Runnable {

  private final CelesteElo plugin;

  @Override
  public void run() {
    final UserController controller = plugin.getUserFactory().getUserController();
    Bukkit.getOnlinePlayers().forEach(controller::register);
  }

}
