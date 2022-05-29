/*
 * LifeSteal - Yet another lifecore smp core.
 * Copyright (C) 2022  Arcade Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
  private final MongoHandler mongoHandler;
  private final LifeStealPlugin plugin;

  public MongoProfileHandler(LifeStealPlugin plugin) {
    this.plugin = plugin;
    this.mongoHandler = new MongoHandler(plugin);
  }

  @Override
  public void load(Profile profile) {
    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
      final Document document = mongoHandler.getPlayerData().find(Filters.eq("_id", profile.getUniqueID())).first();
      if (document == null) {
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
