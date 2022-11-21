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

package in.arcadelabs.lifesteal.core.elimination.impl;

import in.arcadelabs.labaide.experience.ExperienceManager;
import in.arcadelabs.lifesteal.api.database.profile.LifeState;
import in.arcadelabs.lifesteal.api.elimination.EliminationCause;
import in.arcadelabs.lifesteal.api.elimination.IElimination;
import in.arcadelabs.lifesteal.core.LifeSteal;
import in.arcadelabs.lifesteal.core.LifeStealPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Ban implements IElimination {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final MiniMessage miniMessage = MiniMessage.miniMessage();
  private final LegacyComponentSerializer legecySerializer = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();

  @Override
  public void eliminate(Player player, EliminationCause cause) {
    switch (this.lifeSteal.getConfig().getString("InventoryMode")) {
      case "DROP" -> {
        if (player.getInventory().isEmpty()) return;
        for (final ItemStack stuff : player.getInventory().getContents()) {
          assert stuff != null;
          player.getWorld().dropItemNaturally(player.getLocation(), stuff);
        }
      }
      case "SAVE_TO_RESTORE" -> this.lifeSteal.getSpiritFactory().saveInventory(player);
      case "CLEAR" -> player.getInventory().clear();
      case "NONE" -> player.saveData();
    }

    switch (this.lifeSteal.getConfig().getString("ExperienceMode")) {
      case "DROP" -> {
        player.getWorld().spawn(player.getLocation(), ExperienceOrb.class).setExperience(ExperienceManager.getExp(player));
        player.setLevel(0);
        player.setExp(0);
        this.lifeSteal.getSpiritFactory().saveXP(player);
      }
      case "SAVE_TO_RESTORE" -> this.lifeSteal.getSpiritFactory().saveXP(player);
      case "CLEAR" -> {
        player.setExp(0);
        player.setLevel(0);
        this.lifeSteal.getSpiritFactory().saveXP(player);
      }
      case "NONE" -> {
        player.saveData();
        this.lifeSteal.getSpiritFactory().saveXP(player);
      }
    }

    this.commandDispatcher(this.lifeSteal.getConfig().getString("Ban-Command-URI"), player);
    this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setLifeState((LifeState.BANNED));
    this.lifeSteal.getInteraction().broadcast(cause.getKey(), player);

  }

  @Override
  public void handleJoinEvent(Player player) {

  }

  public void commandDispatcher(final String URI, final Player player) {
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
            this.legecySerializer.serialize(this.miniMessage.deserialize(URI, Placeholder.component("player", player.name()))));
  }
}
