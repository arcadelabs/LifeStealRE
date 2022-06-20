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

import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

@SuppressWarnings("unused")
public class HeartItemCooker {
  private final ItemStack heartItem;
  private final ItemMeta heartMeta;

  public HeartItemCooker(Material material) {
    this.heartItem = new ItemStack(material);
    this.heartMeta = this.heartItem.getItemMeta();
  }

  public HeartItemCooker setHeartName(String name) {
    this.heartMeta.setDisplayName(name);
    return this;
  }

  public HeartItemCooker setHeartLore(List<String> lore) {
    this.heartMeta.setLore(lore);
    return this;
  }

  public HeartItemCooker setModelData(int modelData) {
    this.heartMeta.setCustomModelData(modelData);
    return this;
  }

  public HeartItemCooker setPDCString(NamespacedKey namespacedKey, String value) {
    this.heartMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
    return this;
  }

  public HeartItemCooker setPDCDouble(NamespacedKey namespacedKey, double value) {
    this.heartMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.DOUBLE, value);
    return this;
  }

  public HeartItemCooker setPDCBoolean(NamespacedKey namespacedKey, boolean value) {
    this.heartMeta.getPersistentDataContainer().set(namespacedKey, DataType.BOOLEAN, value);
    return this;
  }

  public HeartItemCooker setPDCStringList(NamespacedKey namespacedKey, String... stringList) {
    this.heartMeta.getPersistentDataContainer().set(namespacedKey, DataType.STRING_ARRAY, stringList);
    return this;
  }

  public HeartItemCooker cook() {
    this.heartItem.setItemMeta(this.heartMeta);
    return this;
  }

  public ItemStack getCookedItem() {
    return this.heartItem;
  }
}
