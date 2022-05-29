package in.arcadelabs.lifesteal.handler;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

@Getter
public class JsonHandler {

  private final LifeStealPlugin plugin;
  private final File jsonFile;

  public JsonHandler(LifeStealPlugin plugin) throws IOException {
    this.plugin = plugin;

    this.jsonFile = new File(plugin.getDataFolder(), "playerdata.json");

    if (!jsonFile.exists()) {
      jsonFile.createNewFile();
    }
  }
}
