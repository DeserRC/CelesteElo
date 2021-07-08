package com.celeste.elo.factory;

import com.celeste.configuration.model.provider.Configuration;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.storage.factory.StorageFactory;
import com.celeste.databases.storage.model.database.dao.StorageDao;
import com.celeste.databases.storage.model.database.provider.Storage;
import com.celeste.elo.CelesteElo;
import com.celeste.elo.model.entity.User;
import com.celeste.library.core.model.entity.Data;
import lombok.Getter;

@Getter
public final class ConnectionFactory {

  private final Storage storage;
  private final StorageDao<User> dao;

  public ConnectionFactory(final CelesteElo plugin) throws FailedConnectionException {
    final Configuration settings = plugin.getSettingsFactory().getSettings();

    this.storage = StorageFactory.getInstance().start(Data.create()
        .setData("name", plugin.getName().toLowerCase())
        .setData("path", plugin.getDataFolder().getAbsolutePath())
        .setData("driver", settings.getString("storage.driver"))
        .setData("hostname", settings.getString("storage.data.hostname"))
        .setData("port", settings.getInt("storage.data.port"))
        .setData("database", settings.getString("storage.data.database"))
        .setData("username", settings.getString("storage.data.username"))
        .setData("password", settings.getString("storage.data.password"))
        .setData("ssl", settings.getBoolean("storage.data.ssl"))
        .setData("authentication", settings.getString("storage.data.mongodb.authentication"))
        .setData("uri", settings.getString("storage.data.mongodb.uri")));

    this.dao = storage.createDao(User.class);
  }

}
