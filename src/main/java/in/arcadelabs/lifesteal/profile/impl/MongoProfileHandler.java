package in.arcadelabs.lifesteal.profile.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.handler.MongoHandler;
import in.arcadelabs.lifesteal.profile.Profile;
import in.arcadelabs.lifesteal.profile.ProfileStorage;
import lombok.Getter;
import org.bson.Document;

public class MongoProfileHandler implements ProfileStorage {

    @Getter
    private static MongoHandler mongoHandler;
    private LifeStealPlugin plugin;

    public MongoProfileHandler(LifeStealPlugin plugin) {
        this.plugin = plugin;
        this.mongoHandler = new MongoHandler(plugin);
    }

    @Override
    public void load(Profile profile) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
           final Document document = mongoHandler.getPlayerData().find(Filters.eq("_id", profile.getUniqueID())).first();
           if (document==null) {
               this.save(profile);
               return;
           }
           profile.setCurrentHearts(document.getDouble("currentHearts"));
           profile.setTotalHeartsGained(document.getDouble("totalHeartsGained"));
           profile.setTotalHeartsLost(document.getDouble("totalHeartsLost"));
           profile.setTotalHeartsConsumed(document.getDouble("totalHeartsConsumed"));
           profile.setTotalHeartsWithdrawn(document.getDouble("totalHeartsWithdrawn"));
           profile.setDeaths(document.getInteger("deaths"));
           profile.setKills(document.getInteger("kills"));
        });
    }

    @Override
    public void save(Profile profile) {
        final Document document = mongoHandler.getPlayerData().find(Filters.eq("_id", profile.getUniqueID())).first();
        if (document == null) {
            mongoHandler.getPlayerData().insertOne(Document.parse(profile.toJson()));
            return;
        }
        mongoHandler.getPlayerData().replaceOne(document, Document.parse(profile.toJson()), new ReplaceOptions().upsert(true));
    }

    @Override
    public void disconnect() {
        mongoHandler.getClient().close();
    }
}
