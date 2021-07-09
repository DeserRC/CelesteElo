package com.celeste.elo.model.entity;

import com.celeste.databases.storage.model.annotation.Key;
import com.celeste.databases.storage.model.annotation.Storable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
@Storable("elo_user")
public class User {

  @Key
  private final UUID id;
  private String name;

  private int elo;
  private int killStreak;

  private User() {
    this(null, null);
  }

  public User(final Player player) {
    this(player, 0);
  }

  public User(final Player player, final int elo) {
    this(player.getUniqueId(), player.getName(), elo);
  }

  public User(final UUID id, final String name) {
    this(id, name, 0);
  }

  public User(final UUID id, final String name, final int elo) {
    this(id, name, elo, 0);
  }

}
