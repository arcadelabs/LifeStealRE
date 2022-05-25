package in.arcadelabs.lifesteal.utils;

import in.arcadelabs.libs.boostedyaml.YamlDocument;
import in.arcadelabs.libs.boostedyaml.dvs.versioning.BasicVersioning;
import in.arcadelabs.libs.boostedyaml.settings.dumper.DumperSettings;
import in.arcadelabs.libs.boostedyaml.settings.general.GeneralSettings;
import in.arcadelabs.libs.boostedyaml.settings.loader.LoaderSettings;
import in.arcadelabs.libs.boostedyaml.settings.updater.UpdaterSettings;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static in.arcadelabs.lifesteal.LifeSteal.LOGGER;
import static in.arcadelabs.lifesteal.LifeSteal.plugin;

public class ConfigUtils {

  private YamlDocument configYML;

  public void loadFiles() {
    try {
      configYML = YamlDocument.create(new File("Config.yml"),
              Objects.requireNonNull(plugin.getResource("Config.yml")),
              GeneralSettings.DEFAULT,
              LoaderSettings.builder().setAutoUpdate(true).build(),
              DumperSettings.DEFAULT,
              UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
    } catch (IOException e) {
      LOGGER.severe("Failed to load Config.yml");
      throw new RuntimeException(e);
    }
  }


  public YamlDocument getConfig() { return configYML; }

  public void reload() {
    try { configYML.reload(); } catch (Exception e) {
      LOGGER.severe("Failed to reload Config.yml");
      e.printStackTrace();
    }
  }
}
