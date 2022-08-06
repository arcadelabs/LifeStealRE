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

package in.arcadelabs.lifesteal;

import com.j256.ormlite.logger.Level;
import in.arcadelabs.labaide.logger.Logger;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

@Getter
public final class LifeStealPlugin extends JavaPlugin {

    @Getter
    private static LifeStealPlugin instance;
    @Getter
    private static LifeSteal lifeSteal;

    private boolean labaideExist;

    @Override
    public void onEnable() {
        instance = this;

        if (Bukkit.getPluginManager().getPlugin("LabAide") == null) {
            this.labaideExist = false;
            this.getLogger().severe("LabAide was not found! Disabling LifeSteal...");
            this.getLogger()
                    .severe("Download LabAide at https://github.com/arcadelabs/LabAide/releases/latest");
            this.setEnabled(false);
            return;
        }

        this.labaideExist = true;
        lifeSteal = new LifeSteal();
        try {
            lifeSteal.init();
            lifeSteal.getLogger().logger(Logger.Level.INFO, lifeSteal.getMiniMessage().deserialize(lifeSteal.getKey("Messages.LifestealLoad")));
        } catch (Exception e) {
            this.getLogger()
                    .warning(
                            "There was an error while loading LifeSteal, gotta be a hooman error, blame Aniket#7102.");
            this.getLogger().warning(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

        if (this.labaideExist) {
            lifeSteal.getDatabaseHandler().getHikariExecutor()
                    .execute(() -> lifeSteal.getProfileManager()
                            .getProfileCache()
                            .values().forEach(profile -> {
                                try {
                                    lifeSteal.getProfileManager().saveProfile(profile);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    lifeSteal.getDatabaseHandler().disconnect();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }));
        }
        this.getLogger().info(ChatColor.of("#f72585") + "  ___  _  _   __   ");
        this.getLogger().info(ChatColor.of("#b5179e") + " / __)( \\/ ) /__\\  ");
        this.getLogger().info(ChatColor.of("#7209b7") + "( (__  \\  / /(__)\\ ");
        this.getLogger().info(ChatColor.of("#560bad") + " \\___) (__)(__)(__)... on the other side");
        this.getLogger().info(ChatColor.of("#560bad") + " ");
    }
}
