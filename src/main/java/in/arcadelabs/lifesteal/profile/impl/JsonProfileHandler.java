package in.arcadelabs.lifesteal.profile.impl;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.handler.JsonHandler;
import in.arcadelabs.lifesteal.profile.Profile;
import in.arcadelabs.lifesteal.profile.ProfileStorage;

import java.io.*;

public class JsonProfileHandler implements ProfileStorage {

    private final LifeStealPlugin plugin;
    private JsonHandler jsonHandler;

    private Writer jsonWriter;
    private Reader jsonReader;

    public JsonProfileHandler(LifeStealPlugin plugin) throws IOException {
        this.plugin = plugin;
        this.jsonHandler = new JsonHandler(plugin);
        this.jsonWriter = new FileWriter(jsonHandler.getJsonFile());
        this.jsonReader = new FileReader(jsonHandler.getJsonFile());
    }

    @Override
    public void load(Profile profile) {

        final Profile profileFinder = plugin.getGSON().fromJson(jsonReader, Profile.class);
        if (profileFinder == null) {
            plugin.getGSON().toJson(new Profile(profile.getUniqueID()));
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
        final String JSON = plugin.getGSON().toJson(profile, Profile.class);
        final Profile profileFinder = plugin.getGSON().fromJson(jsonReader, Profile.class);
        if (profileFinder == null) {
            plugin.getGSON().toJson(new Profile(profile.getUniqueID()));
            try {
                jsonWriter.flush();
                jsonWriter.close();
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        final Profile profileReplaced = plugin.getGSON().fromJson(JSON, Profile.class);
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
