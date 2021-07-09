package com.celeste.elo.factory;

import com.celeste.configuration.factory.ConfigurationFactory;
import com.celeste.configuration.model.exception.FailedCreateException;
import com.celeste.configuration.model.provider.Configuration;
import com.celeste.configuration.model.registry.ReplaceRegistry.ReplaceValue;
import com.celeste.configuration.model.registry.type.ReplaceType;
import com.celeste.elo.CelesteElo;
import com.celeste.library.core.model.entity.Data;
import lombok.Getter;

@Getter
public final class SettingsFactory {

  private static final ReplaceValue REPLACE;

  static {
    REPLACE = ReplaceValue.builder()
        .value("§")
        .type(ReplaceType.GET)
        .build();
  }

  private final Configuration settings;
  private final Configuration messages;

  public SettingsFactory(final CelesteElo plugin) throws FailedCreateException {
    final Data data = Data.create()
        .setData("driver", "YAML")
        .setData("path", plugin.getDataFolder().getAbsolutePath())
        .setData("replace", "false");

    this.settings = ConfigurationFactory.getInstance().start(
        data.setData("resource", "settings.yml"));

    this.messages = ConfigurationFactory.getInstance().start(
        data.setData("resource", "language/" + settings.getString("language") + ".yml"));

    messages.getReplaceRegistry().register("&", REPLACE);
  }

}
