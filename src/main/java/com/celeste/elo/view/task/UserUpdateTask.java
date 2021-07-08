package com.celeste.elo.view.task;

import com.celeste.databases.storage.model.database.dao.StorageDao;
import com.celeste.elo.CelesteElo;
import com.celeste.elo.controller.UserController;
import com.celeste.elo.model.entity.User;
import com.celeste.elo.model.map.UserMap;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public final class UserUpdateTask implements Runnable {

  private final CelesteElo plugin;

  @Override
  @SneakyThrows
  public void run() {
    final UserMap update = plugin.getUserFactory().getUpdate();
    final StorageDao<User> dao = plugin.getConnectionFactory().getDao();

    dao.save(update.values());
    update.clear();
  }

}
