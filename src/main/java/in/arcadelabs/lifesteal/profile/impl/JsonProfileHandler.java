package in.arcadelabs.lifesteal.profile.impl;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.handler.JsonHandler;
import in.arcadelabs.lifesteal.profile.Profile;
import in.arcadelabs.lifesteal.profile.ProfileStorage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class JsonProfileHandler implements ProfileStorage {

  private final LifeStealPlugin plugin;
  private final Writer jsonWriter;
  private final Reader jsonReader;
  private JsonHandler jsonHandler;

  public JsonProfileHandler(LifeStealPlugin plugin) throws IOException {
    this.plugin = plugin;
    this.jsonHandler = new JsonHandler(plugin);
    this.jsonWriter = new FileWriter(jsonHandler.getJsonFile());
    this.jsonReader = new FileReader(jsonHandler.getJsonFile());
  }

  @Override
  public void load(Profile profile) {

    final Profile profileFinder = LifeStealPlugin.getLifeSteal().getGSON().fromJson(jsonReader, Profile.class);
    if (profileFinder == null) {
      LifeStealPlugin.getLifeSteal().getGSON().toJson(new Profile(profile.getUniqueID()));
      try {
        jsonWriter.flush();
        jsonWriter.close();
        return;
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      profile.setCurrentHearts(profileFinder.getCurrentHearts());
      profile.setTotalHeartsGained(profileFinder.getTotalHeartsGained());
      profile.setTotalHeartsLost(profileFinder.getTotalHeartsLost());
      profile.setTotalHeartsConsumed(profileFinder.getTotalHeartsConsumed());
      profile.setTotalHeartsWithdrawn(profileFinder.getTotalHeartsWithdrawn());
      profile.setDeaths(profileFinder.getDeaths());
      profile.setKills(profileFinder.getKills());
      try {
        jsonWriter.flush();
        jsonWriter.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  @Override
  public void save(Profile profile) {
    final String JSON = LifeStealPlugin.getLifeSteal().getGSON().toJson(profile, Profile.class);
    final Profile profileFinder = LifeStealPlugin.getLifeSteal().getGSON().fromJson(jsonReader, Profile.class);
    if (profileFinder == null) {
      LifeStealPlugin.getLifeSteal().getGSON().toJson(new Profile(profile.getUniqueID()));
      try {
        jsonWriter.flush();
        jsonWriter.close();
        return;
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    final Profile profileReplaced = LifeStealPlugin.getLifeSteal().getGSON().fromJson(JSON, Profile.class);
    profile.setCurrentHearts(profileReplaced.getCurrentHearts());
    profile.setTotalHeartsGained(profileReplaced.getTotalHeartsGained());
    profile.setTotalHeartsLost(profileReplaced.getTotalHeartsLost());
    profile.setTotalHeartsConsumed(profileReplaced.getTotalHeartsConsumed());
    profile.setTotalHeartsWithdrawn(profileReplaced.getTotalHeartsWithdrawn());
    profile.setDeaths(profileReplaced.getDeaths());
    profile.setKills(profileReplaced.getKills());
    try {
      jsonWriter.flush();
      jsonWriter.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void disconnect() {
    try {
      jsonWriter.flush();
      jsonWriter.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
