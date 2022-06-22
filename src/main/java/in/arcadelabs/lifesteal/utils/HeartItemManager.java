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

import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class HeartItemManager {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final LifeStealPlugin instance = LifeStealPlugin.getInstance();
  private final FileConfiguration heartConfig = lifeSteal.getHeartConfig();
  private HeartItemCooker heartItemCooker;
  private ItemStack heartItem;

  private Material heartType;
  private String heartName;
  private List<String> heartLore;
  private int modelData;
  private double healthPoints;
  private Mode mode;
  private String type;
  private String index;

  private final Set<String> blessedHearts
          = this.heartConfig.getConfigurationSection("Hearts.Types.Blessed").getKeys(false);
  private final Set<String> normalHearts
          = this.heartConfig.getConfigurationSection("Hearts.Types.Normal").getKeys(false);
  private final Set<String> cursedHearts
          = this.heartConfig.getConfigurationSection("Hearts.Types.Cursed").getKeys(false);

  private final String[] blessedRarity = this.blessedHearts.toArray(new String[0]);
  private final String[] normalRarity = this.normalHearts.toArray(new String[0]);
  private final String[] cursedRarity = this.cursedHearts.toArray(new String[0]);

  private final ProbabilityCollection<Integer> randomBlessCol = new ProbabilityCollection<>();
  private final ProbabilityCollection<Integer> randomNormalCol = new ProbabilityCollection<>();
  private final ProbabilityCollection<Integer> randomCurseCol = new ProbabilityCollection<>();

  /**
   * Instantiates a new Heart item manager.
   *
   * @param mode the mode
   */
  public HeartItemManager(final Mode mode) {
    this.mode = mode;

    for (int i = 0; i < this.blessedHearts.size(); i++) {
      this.randomBlessCol.add(Integer.valueOf(this.blessedRarity[i]), Integer.parseInt(this.blessedRarity[i]));
    }

    for (int i = 0; i < this.normalHearts.size(); i++) {
      this.randomNormalCol.add(Integer.valueOf(this.normalRarity[i]), Integer.parseInt(this.normalRarity[i]));
    }

    for (int i = 0; i < this.cursedHearts.size(); i++) {
      this.randomCurseCol.add(Integer.valueOf(this.cursedRarity[i]), Integer.parseInt(this.cursedRarity[i]));
    }
  }

  /**
   * Prepare ingedients for heart item manager.
   *
   * @return the heart item manager
   */
  public HeartItemManager prepareIngedients() {
    switch (this.mode) {
      case FIXED_BLESSED:
        this.index = this.blessedRarity[0];
        this.type = "Blessed";
        break;
      case FIXED_NORMAL:
        this.index = this.normalRarity[0];
        this.type = "Normal";
        break;
      case FIXED_CURSED:
        this.index = this.cursedRarity[0];
        this.type = "Cursed";
        break;
      case RANDOM_BLESSED:
        this.index = String.valueOf(this.randomBlessCol.get());
        this.type = "Blessed";
        break;
      case RANDOM_NORMAL:
        this.index = String.valueOf(this.randomNormalCol.get());
        this.type = "Normal";
        break;
      case RANDOM_CURSED:
        this.index = String.valueOf(this.randomCurseCol.get());
        this.type = "Cursed";
        break;
      case RANDOM_ALL:
        final ProbabilityCollection<Mode> modeCol = new ProbabilityCollection<>();
        modeCol.add(Mode.RANDOM_BLESSED, 27);
        modeCol.add(Mode.RANDOM_NORMAL, 27);
        modeCol.add(Mode.RANDOM_CURSED, 27);
        this.mode = modeCol.get();
        this.prepareIngedients();
        break;
      default:
        this.mode = Mode.FIXED_NORMAL;
        this.prepareIngedients();
        break;
    }

    this.heartType = Material.valueOf(heartConfig.getString
            ("Hearts.Types." + type + "." + index + ".Properties.ItemType"));
    this.heartName = lifeSteal.getUtils().formatString(heartConfig.getString
            ("Hearts.Types." + type + "." + index + ".Properties.Name"));
    this.heartLore = lifeSteal.getUtils().formatStringList(heartConfig.getStringList
            ("Hearts.Types." + type + "." + index + ".Properties.Lore"));
    this.modelData = heartConfig.getInt
            ("Hearts.Types." + type + "." + index + ".Properties.ModelData");
    this.healthPoints = heartConfig.getDouble
            ("Hearts.Types." + type + "." + index + ".Properties.HealthPoints");

    return this;
  }

  /**
   * Cook heart with prepared ingredients.
   *
   * @return the heart item manager
   */
  public HeartItemManager cookHeart() {
    this.heartItemCooker = new HeartItemCooker(this.heartType)
            .setHeartName(this.heartName)
            .setHeartLore(this.heartLore)
            .setModelData(this.modelData)
            .setPDCString(new NamespacedKey(instance, "lifesteal_heart_item"), "No heart spoofing, dum dum.")
            .setPDCString(new NamespacedKey(instance, "lifesteal_heart_itemtype"), this.type)
            .setPDCString(new NamespacedKey(instance, "lifesteal_heart_itemindex"), this.index)
            .setPDCDouble(new NamespacedKey(instance, "lifesteal_heart_healthpoints"), this.healthPoints)
            .cook();
    this.heartItem = this.heartItemCooker.getCookedItem();
    return this;
  }

  /**
   * Heart item getter.
   *
   * @return the heart item
   */
  public ItemStack getHeartItem() {
    return this.heartItem;
  }

  public enum Mode {
    FIXED_BLESSED,
    FIXED_NORMAL,
    FIXED_CURSED,
    RANDOM_BLESSED,
    RANDOM_NORMAL,
    RANDOM_CURSED,
    RANDOM_ALL
  }

}
