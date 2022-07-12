/*
 *          LifeSteal - Yet another lifecore smp core.
 *                Copyright (C) 2022  Arcade Labs
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

package in.arcadelabs.lifesteal.utils;

import in.arcadelabs.labaide.libs.boostedyaml.YamlDocument;
import in.arcadelabs.labaide.libs.boostedyaml.dvs.versioning.BasicVersioning;
import in.arcadelabs.labaide.libs.boostedyaml.settings.dumper.DumperSettings;
import in.arcadelabs.labaide.libs.boostedyaml.settings.general.GeneralSettings;
import in.arcadelabs.labaide.libs.boostedyaml.settings.loader.LoaderSettings;
import in.arcadelabs.labaide.libs.boostedyaml.settings.updater.UpdaterSettings;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public class I18n {
  private final LifeStealPlugin instance;
  private final LifeSteal lifeSteal;
  private final YamlDocument language;

  public I18n() throws IOException {
    instance = LifeStealPlugin.getInstance();
    lifeSteal = LifeStealPlugin.getLifeSteal();

    language = YamlDocument.create(new File(instance.getDataFolder(), "language.yml"),
            Objects.requireNonNull(instance.getResource("language.yml")),
            GeneralSettings.DEFAULT,
            LoaderSettings.builder().setAutoUpdate(true).build(),
            DumperSettings.DEFAULT,
            UpdaterSettings.builder().setVersioning(new BasicVersioning("Version")).build());
  }

  public void translate(Level level, String key) {
    try {
      instance.getLogger().log(level, lifeSteal.getUtils().formatString(language.getString(key)));
    } catch (NullPointerException exception) {
      instance.getLogger().warning(lifeSteal.getUtils().formatString(
              "<red>Missing " + key + " language key in language.yml</red>"
      ));
      instance.getLogger().warning(exception.getLocalizedMessage());
    }
  }

  public String getKey(String path) {
    return language.getString(path);
  }
}