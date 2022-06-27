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

package in.arcadelabs.lifesteal.utils;

import in.arcadelabs.libs.adventure.adventure.text.Component;
import in.arcadelabs.libs.adventure.adventure.text.minimessage.MiniMessage;
import in.arcadelabs.libs.adventure.adventure.text.minimessage.tag.resolver.Placeholder;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class I18n {
  private final ResourceBundle defaultBundle, userBundle;
  private final LifeStealPlugin instance;
  private final LifeSteal lifeSteal;
  private final Locale defualtLocale;
//  private final File langEn, langEs, langFr;

  public I18n(Locale userSpecified) {
    defualtLocale = Locale.ENGLISH;
    instance = LifeStealPlugin.getInstance();
    lifeSteal = LifeStealPlugin.getLifeSteal();

//    langEn = new File(instance.getDataFolder(), "languages/lang_en.properties");
//    langEs = new File(instance.getDataFolder(), "languages/lang_es.properties");
//    langFr = new File(instance.getDataFolder(), "languages/lang_fr.properties");
//    Doesn't work, I dunno what was I thinking while making this lol

    defaultBundle = ResourceBundle.getBundle("languages/lang", defualtLocale);
    userBundle = ResourceBundle.getBundle("languages/lang", userSpecified);
  }

  public void translate(Level level, String message) {
    try {
      instance.getLogger().log(level, lifeSteal.getUtils().formatString(userBundle.getString(message)));
    } catch (MissingResourceException exception) {
      Component component = MiniMessage.builder().build().deserialize
              ("Missing locale key \"<key>\" in <localeFile>",
                      Placeholder.component("key", Component.text(exception.getKey())),
                      Placeholder.component("localeFile", Component.text(userBundle.getLocale().toString())));
      instance.getLogger().warning(lifeSteal.getUtils().formatString(component));
      instance.getLogger().warning(exception.getLocalizedMessage());
      instance.getLogger().log(level, lifeSteal.getUtils().formatString(defaultBundle.getString(message)));
    }
  }
}