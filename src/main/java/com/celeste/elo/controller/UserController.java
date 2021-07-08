package com.celeste.elo.controller;

import com.celeste.databases.storage.model.database.dao.StorageDao;
import com.celeste.elo.CelesteElo;
import com.celeste.elo.controller.exception.UserNotFoundException;
import com.celeste.elo.factory.UserFactory;
import com.celeste.elo.model.entity.Rank;
import com.celeste.elo.model.entity.User;
import com.celeste.elo.model.map.UserMap;
import com.celeste.library.core.util.Validation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class UserController {

  private final CelesteElo plugin;

  @SneakyThrows
  public void register(final Player player) {
    final UserMap users = plugin.getUserFactory().getUsers();

    final User user = getOrCreate(player);
    setName(user, player.getName());

    users.put(user);
  }

  @SneakyThrows
  public void unregister(final Player player) {
    final UserMap users = plugin.getUserFactory().getUsers();
    final UserMap update = plugin.getUserFactory().getUpdate();
    final StorageDao<User> dao = plugin.getConnectionFactory().getDao();

    final User user = getOrCreate(player);
    setName(user, player.getName());

    if (update.containsKey(user)) {
      dao.save(user);
    }

    users.remove(user);
    update.remove(user);
  }

  @SneakyThrows
  public boolean containsById(final UUID id) {
    final UserMap users = plugin.getUserFactory().getUsers();
    final StorageDao<User> dao = plugin.getConnectionFactory().getDao();

    return users.containsKey(id) || dao.contains(id);
  }

  @SneakyThrows
  public boolean containsByName(final String name) {
    final UserMap users = plugin.getUserFactory().getUsers();
    final StorageDao<User> dao = plugin.getConnectionFactory().getDao();

    return users.values().stream()
        .anyMatch(user -> user.getName().equalsIgnoreCase(name))
        || dao.findAll().stream()
        .anyMatch(user -> user.getName().equalsIgnoreCase(name));
  }

  @SneakyThrows
  public User getById(final UUID id) {
    final UserFactory factory = plugin.getUserFactory();
    final StorageDao<User> dao = plugin.getConnectionFactory().getDao();

    final User user = factory.getUsers().containsKey(id)
        ? factory.getUsers().get(id)
        : dao.contains(id)
        ? dao.find(id)
        : null;

    return Validation.notNull(user, () ->
        new UserNotFoundException("User ID " + id + " was not found."));
  }

  @SneakyThrows
  public User getByName(final String name) {
    final UserMap users = plugin.getUserFactory().getUsers();
    final StorageDao<User> dao = plugin.getConnectionFactory().getDao();

    final User user = users.values().stream()
        .filter(otherUser -> otherUser.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElse(null);

    if (user != null) {
      return user;
    }

    return dao.findAll().stream()
        .filter(otherUser -> otherUser.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElseThrow(() -> new UserNotFoundException("User Name " + name + " was not found."));
  }

  public User getOrCreate(final Player player) {
    final RankController rankController = plugin.getRankFactory().getRankController();

    if (containsById(player.getUniqueId())) {
      return getById(player.getUniqueId());
    }

    return new User(player, rankController.getDefaultElo());
  }

  public Rank getRank(final User user) {
    final RankController rankController = plugin.getRankFactory().getRankController();

    return rankController.getRankByElo(user.getElo());
  }

  public void setName(final User user, final String name) {
    final UserMap update = plugin.getUserFactory().getUpdate();

    if (user.getName().equals(name)) {
      return;
    }

    user.setName(name);
    update.put(user);
  }

  public void addElo(final User user, final int elo) {
    final UserMap update = plugin.getUserFactory().getUpdate();

    final int newElo = user.getElo() + elo;
    user.setElo(newElo);

    update.put(user);
  }

  public void removeElo(final User user, final int elo) {
    final UserMap update = plugin.getUserFactory().getUpdate();

    final int newElo = user.getElo() - elo;
    user.setElo(newElo);

    update.put(user);
  }

  public void setElo(final User user, final int elo) {
    final UserMap update = plugin.getUserFactory().getUpdate();

    user.setElo(elo);
    update.put(user);
  }

  public void addKillStreak(final User user, final int killStreak) {
    final UserMap update = plugin.getUserFactory().getUpdate();

    final int newKillStreak = user.getKillStreak() + killStreak;
    user.setKillStreak(newKillStreak);

    update.put(user);
  }

  public void removeKillStreak(final User user, final int killStreak) {
    final UserMap update = plugin.getUserFactory().getUpdate();

    final int newKillStreak = user.getKillStreak() - killStreak;
    user.setKillStreak(newKillStreak);

    update.put(user);
  }

  public void setKillStreak(final User user, final int killStreak) {
    final UserMap update = plugin.getUserFactory().getUpdate();

    user.setKillStreak(killStreak);
    update.put(user);
  }

}
