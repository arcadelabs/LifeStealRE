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

package in.arcadelabs.lifesteal;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class LifeStealPlugin extends JavaPlugin {

  @Getter
  private static LifeStealPlugin instance;
  @Getter
  private static LifeSteal lifeSteal;


  @Override
  public void onEnable() {
    instance = this;
    lifeSteal = new LifeSteal();
    try {
      lifeSteal.init();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onDisable() {
    lifeSteal.getMessenger().closeMessenger();
    lifeSteal.getProfileStorage().disconnect();
    getLogger().info(ChatColor.of("#f72585") + "  ___  _  _   __   ");
    getLogger().info(ChatColor.of("#b5179e") + " / __)( \\/ ) /__\\  ");
    getLogger().info(ChatColor.of("#7209b7") + "( (__  \\  / /(__)\\ ");
    getLogger().info(ChatColor.of("#560bad") + " \\___) (__)(__)(__)... on the other side");
    getLogger().info(ChatColor.of("#560bad") + " ");
  }
}
