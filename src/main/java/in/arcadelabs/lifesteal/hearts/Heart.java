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

import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Heart {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();

  private final ItemMeta heartMeta;

  public Heart(ItemMeta heartMeta) {
    this.heartMeta = heartMeta;
  }

  public ItemMeta getHeartMeta() {
    return this.heartMeta;
  }

  public Component getName() {
    return this.heartMeta.displayName();
  }

  public List<Component> getLore() {
    return this.heartMeta.lore();
  }

  public double getHealthPoints() {
    return this.heartMeta.getPersistentDataContainer().get(
            this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_healthpoints"), PersistentDataType.DOUBLE);
  }

  public String getType() {
    return this.heartMeta.getPersistentDataContainer().get(
            this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_itemtype"), PersistentDataType.STRING);
  }

  public String getIndex() {
    return this.heartMeta.getPersistentDataContainer().get(
            this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_itemindex"), PersistentDataType.STRING);
  }

  public String getConsumeSound() {
    return this.heartMeta.getPersistentDataContainer().get(
            this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_consumesound"), PersistentDataType.STRING);
  }

  public List<Component> getConsumeMessages() {
    return this.lifeSteal.getUtils().stringToComponentList(this.lifeSteal.getHeartConfig().getStringList
            ("Hearts.Types." + this.getType() + "." + this.getIndex() + ".Properties.ConsumeMessage"), false);
  }

}