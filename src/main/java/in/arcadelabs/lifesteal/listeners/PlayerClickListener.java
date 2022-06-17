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

package in.arcadelabs.lifesteal.listeners;

import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.Set;

public class PlayerClickListener implements Listener {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final LifeStealPlugin instance = LifeStealPlugin.getInstance();

  @EventHandler
  public void onPlayerClick(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if (!(event.getAction() == Action.RIGHT_CLICK_AIR)) return;
    ItemMeta heartMeta = player.getInventory().getItemInMainHand().getItemMeta();
    if (!(heartMeta.getPersistentDataContainer()
            .has(new NamespacedKey(instance, "lifesteal_heart_item"), PersistentDataType.STRING))) return;
//    if (player.getInventory().getItemInMainHand().getType().isEdible()) return;
    double healthPoints = Objects.requireNonNull(heartMeta.getPersistentDataContainer()
            .get(new NamespacedKey(instance, "lifesteal_heart_healthpoints"), PersistentDataType.DOUBLE));
    lifeSteal.getUtils().setPlayerBaseHealth(player,
            lifeSteal.getUtils().getPlayerBaseHealth(player) + healthPoints);
    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);

    final String itemType = heartMeta.getPersistentDataContainer().get
            (new NamespacedKey(instance, "lifesteal_heart_itemtype"), PersistentDataType.STRING);
    final String itemIndex = heartMeta.getPersistentDataContainer().get
            (new NamespacedKey(instance, "lifesteal_heart_itemindex"), PersistentDataType.STRING);

    String effectsPath = "Hearts.Types." + itemType + "." + itemIndex + ".Properties.Effects";
    Set<String> indexSet = Objects.requireNonNull(lifeSteal.getHeartConfig().getConfigurationSection(effectsPath)).getKeys(false);

    for (int i = 0; i < indexSet.size(); i++) {
      String[] indexList = indexSet.toArray(new String[0]);
      player.addPotionEffect(new PotionEffect(Objects.requireNonNull
              (PotionEffectType.getByName(Objects.requireNonNull(lifeSteal.getHeartConfig().getString(effectsPath + "." + indexList[i] + ".Type")))),
              lifeSteal.getHeartConfig().getInt(effectsPath + "." + indexList[i] + ".Duration") * 20,
              lifeSteal.getHeartConfig().getInt(effectsPath + "." + indexList[i] + ".Power"),
              lifeSteal.getHeartConfig().getBoolean(effectsPath + "." + indexList[i] + ".ShowParticles", false),
              lifeSteal.getHeartConfig().getBoolean(effectsPath + "." + indexList[i] + ".ShowParticles", false)));
    }


  }
}
