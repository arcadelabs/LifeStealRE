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

package in.arcadelabs.lifesteal.hearts;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class HeartItemCooker {
  private final ItemStack heartItem;
  private final ItemMeta heartMeta;

  /**
   * Instantiates a new Heart item cooker.
   *
   * @param material the material
   */
  public HeartItemCooker(final Material material) {
    this.heartItem = new ItemStack(material);
    this.heartMeta = this.heartItem.getItemMeta();
  }

  public HeartItemCooker(final Material material, final Map<String, Object> textureMap) {
    this.heartItem = new ItemStack(material);
    this.heartMeta = (ItemMeta) ConfigurationSerialization.deserializeObject(textureMap);
  }

  /**
   * Sets heart name.
   *
   * @param name the name
   * @return the heart item cooker with heart name
   */
  public HeartItemCooker setHeartName(final Component name) {
    this.heartMeta.displayName(name);
    return this;
  }

  /**
   * Sets heart lore.
   *
   * @param loreList the lore
   * @return the heart item cooker with heart lore
   */
  public HeartItemCooker setHeartLore(final List<Component> loreList) {
    this.heartMeta.lore(loreList);
    return this;
  }

  /**
   * Sets model data.
   *
   * @param modelData the model data
   * @return the heart item cooker with model data
   */
  public HeartItemCooker setModelData(final int modelData) {
    this.heartMeta.setCustomModelData(modelData);
    return this;
  }

  /**
   * Sets persistent data container string.
   *
   * @param namespacedKey the namespaced key
   * @param value         the value
   * @return the heart item cooker with persistent data container string
   */
  public HeartItemCooker setPDCString(final NamespacedKey namespacedKey, final String value) {
    this.heartMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
    return this;
  }

  /**
   * Sets pdc double.
   *
   * @param namespacedKey the namespaced key
   * @param value         the value
   * @return the heart item cooker with persistent data container double
   */
  public HeartItemCooker setPDCDouble(final NamespacedKey namespacedKey, final double value) {
    this.heartMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.DOUBLE, value);
    return this;
  }

  /**
   * Cook heart item.
   *
   * @return the heart item cooker with updated values
   */
  public HeartItemCooker cook() {
    this.heartItem.setItemMeta(this.heartMeta);
    return this;
  }

  /**
   * Cooked heart item getter.
   *
   * @return the cooked heart item
   */
  public ItemStack getCookedItem() {
    return this.heartItem;
  }
}