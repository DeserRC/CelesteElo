package com.celeste.elo.factory;

import com.celeste.elo.CelesteElo;
import com.celeste.elo.controller.UserController;
import com.celeste.elo.model.map.UserMap;
import com.celeste.elo.view.task.UserGetTask;
import com.celeste.elo.view.task.UserUpdateTask;
import lombok.Getter;

@Getter
public final class UserFactory {

  private final UserController userController;
  private final UserMap users;
  private final UserMap update;

  private final UserGetTask userGetTask;
  private final UserUpdateTask userUpdateTask;

  public UserFactory(final CelesteElo plugin) {
    this.userController = new UserController(plugin);
    this.users = new UserMap();
    this.update = new UserMap();

    this.userGetTask = new UserGetTask(plugin);
    this.userUpdateTask = new UserUpdateTask(plugin);
  }

}
