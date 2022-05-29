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
