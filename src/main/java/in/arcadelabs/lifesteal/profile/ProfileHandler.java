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

package in.arcadelabs.lifesteal.profile;

import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ProfileHandler {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();

  private final LifeStealPlugin plugin;
  private final Map<UUID, Profile> profileMap = new HashMap<>();

  public ProfileHandler(LifeStealPlugin plugin) {
    this.plugin = plugin;
  }

  public Profile createProfile(UUID uuid) throws IOException {
    final Profile profile = new Profile(uuid);

    lifeSteal.getProfileStorage().load(profile);
    profileMap.put(uuid, profile);

    return profile;
  }

  public void handleRemove(Profile profile) throws IOException {
    lifeSteal.getProfileStorage().save(profile);
    profileMap.remove(profile.getUniqueID());
  }

  public Profile getProfile(UUID uuid) {
    return profileMap.getOrDefault(uuid, null);
  }
}
