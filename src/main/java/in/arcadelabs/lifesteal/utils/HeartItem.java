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
import in.arcadelabs.libs.adventure.adventure.text.serializer.legacy.LegacyComponentSerializer;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class HeartItem {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final ItemStack heart;
  private final FileConfiguration config = lifeSteal.getConfig();
  private final LegacyComponentSerializer legecySerializer = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();

  public HeartItem(int amount) {

    heart = new ItemStack(Material.valueOf(config.getString("HeartRecipe.Properties.ItemType")));
    ItemMeta heartMeta = heart.getItemMeta();

    heartMeta.setDisplayName(config.getString("HeartRecipe.Properties.Name"));
    heartMeta.setLore(formatLore(config.getStringList("HeartRecipe.Properties.Lore"), amount));

    if (config.getBoolean("HeartRecipe.Properties.CustomModel")) {
      heartMeta.setCustomModelData(config.getInt("HeartRecipe.Properties.ModelData"));
    }
    heartMeta.setUnbreakable(true);
    heartMeta.getPersistentDataContainer().set(new NamespacedKey(LifeStealPlugin.getInstance(), "lifesteal_heart_item"),
            PersistentDataType.STRING, "You can't spoof hearts, bozo.");
    heartMeta.getPersistentDataContainer().set(new NamespacedKey(LifeStealPlugin.getInstance(), "lifesteal_heart_item_value"),
            PersistentDataType.INTEGER, amount);
    heart.setItemMeta(heartMeta);
  }

  private List<String> formatLore(List<String> loreList, int amount) {
    List<String> formattedLore = new ArrayList<>();
    for (String lore : loreList) {
      Component component = MiniMessage.builder().build().deserialize(lore, Placeholder.component("hp", Component.text(amount)));
      String formattedString = this.legecySerializer.serialize(component);
      formattedLore.add(formattedString);
    }
    return formattedLore;
  }


  public ItemStack getHeartItemStack() {
    return this.heart;
  }
}
