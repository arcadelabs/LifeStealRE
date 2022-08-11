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

import in.arcadelabs.labaide.logger.Logger;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

public class FancyStuff {

  private final LifeStealPlugin instance;
  private final MiniMessage miniMessage;
  private final Logger noPrefixLogger;
  private boolean configStatus, langStatus, heartsStatus,
          databaseMode, databaseStatus, profilesStatus,
          commandsStatus, listenersStatus, recipeStatus,
          blessedHeartsStatus, normalHeartsStatus, cursedHeartsStatus;
  private int blessedHeartsCount, normalHeartsCount, cursedHeartsCount;

  public FancyStuff(final LifeStealPlugin instance, final MiniMessage miniMessage) {
    this.instance = instance;
    this.miniMessage = miniMessage;
    this.noPrefixLogger = new Logger("LifeSteal", Component.empty(), null, null);
  }

  public void setConfigStatus(final boolean configStatus) {
    this.configStatus = configStatus;
  }

  public void setLangStatus(final boolean langStatus) {
    this.langStatus = langStatus;
  }

  public void setHeartsStatus(final boolean heartsStatus) {
    this.heartsStatus = heartsStatus;
  }

  public void setDatabaseMode(final boolean databaseMode) {
    this.databaseMode = databaseMode;
  }

  public void setDatabaseStatus(final boolean databaseStatus) {
    this.databaseStatus = databaseStatus;
  }

  public void setProfilesStatus(final boolean profilesStatus) {
    this.profilesStatus = profilesStatus;
  }

  public void setCommandsStatus(final boolean commandsStatus) {
    this.commandsStatus = commandsStatus;
  }

  public void setListenersStatus(final boolean listenersStatus) {
    this.listenersStatus = listenersStatus;
  }

  public void setRecipeStatus(final boolean recipeStatus) {
    this.recipeStatus = recipeStatus;
  }

  public void setBlessedHeartsStatus(final boolean blessedHeartsStatus) {
    this.blessedHeartsStatus = blessedHeartsStatus;
  }

  public void setNormalHeartsStatus(final boolean normalHeartsStatus) {
    this.normalHeartsStatus = normalHeartsStatus;
  }

  public void setCursedHeartsStatus(final boolean cursedHeartsStatus) {
    this.cursedHeartsStatus = cursedHeartsStatus;
  }

  public void setBlessedHeartsCount(final int blessedHeartsCount) {
    this.blessedHeartsCount = blessedHeartsCount;
  }

  public void setNormalHeartsCount(final int normalHeartsCount) {
    this.normalHeartsCount = normalHeartsCount;
  }

  public void setCursedHeartsCount(final int cursedHeartsCount) {
    this.cursedHeartsCount = cursedHeartsCount;
  }

  public void consolePrint() {

    final String configStatus = this.configStatus ? "<green>❥" : "<red>❥";
    final String langStatus = this.langStatus ? "<green>❥" : "<red>❥";
    final String heartsStatus = this.heartsStatus ? "<green>❥" : "<red>❥";
    final String databaseMode = this.databaseMode ? "│   └── MySQL-Hikari  " : "│   └── database.db   ";
    final String databaseStatus = this.databaseStatus ? "<green>❥" : "<red>❥";
    final String profilesStatus = this.profilesStatus ? "<green>❥" : "<red>❥";
    final String commandsStatus = this.commandsStatus ? "<green>❥" : "<red>❥";
    final String listenersStatus = this.listenersStatus ? "<green>❥" : "<red>❥";
    final String recipeStatus = this.recipeStatus ? "<green>❥" : "<red>❥";
    final String blessedHeartsCount = this.blessedHeartsStatus ?
            "<red>" + this.blessedHeartsCount + "x" : "<green>" + this.blessedHeartsCount + "x";
    final String normalHeartsCount = this.normalHeartsStatus ?
            "<red>" + this.normalHeartsCount + "x" : "<green>" + this.normalHeartsCount + "x";
    final String cursedHeartsCount = this.cursedHeartsStatus ?
            "<red>" + this.cursedHeartsCount + "x" : "<green>" + this.cursedHeartsCount + "x";


    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B>  _     _   __       ___  _               _ "));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#EB554F> | |   (_) / _| ___ / __|| |_  ___  __ _ | |    "));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#E53A43> | |__ | ||  _|/ -_)\\__ \\|  _|/ -_)/ _` || |  " +
                    "<color:#F72585>LifeSteal Reimagined v" + instance.getDescription().getVersion()));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#E01E37> |____||_||_|  \\___||___/ \\__|\\___|\\__,_||_|  " +
                    "<color:#E01E37>Running on " + Bukkit.getVersion() + " " + Bukkit.getBukkitVersion()));

    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#E01E37> ."));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#E01E37> ├── I/O"));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#e21f3d> │   ├── config.yml    " + configStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#E41F43> │   ├── language.yml  " + langStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#E52049> │   ├── hearts.yml    " + heartsStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#E7204F> " + databaseMode + databaseStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#E92155> └── Functions"));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#EB215B>     ├── Profiles      " + profilesStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#EC2261>     ├── Commands      " + commandsStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#EE2267>     ├── Listeners     " + listenersStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0236D>     ├── Recipes       " + recipeStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F22373>     └── Hearts        "));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F32479>         ├── Blessed   " + blessedHeartsCount));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F5247F>         ├── Normal    " + normalHeartsCount));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F72585>         └── Cursed    " + cursedHeartsCount));
    noPrefixLogger.log(Logger.Level.INFO, Component.empty());
  }
}