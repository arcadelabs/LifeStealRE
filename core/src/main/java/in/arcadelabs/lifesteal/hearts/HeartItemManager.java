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

import in.arcadelabs.lifesteal.api.enums.Mode;
import in.arcadelabs.labaide.item.ItemBuilder;
import in.arcadelabs.labaide.libs.boostedyaml.YamlDocument;
import in.arcadelabs.labaide.randomizer.ProbabilityCollection;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Set;

public class HeartItemManager {

  private final LifeSteal lifeSteal;
  private final YamlDocument heartConfig;
  @Getter private final Set<String> blessedHearts;
  @Getter private final Set<String> normalHearts;
  @Getter private final Set<String> cursedHearts;
  private final String[] blessedRarity;
  private final String[] normalRarity;
  private final String[] cursedRarity;
  private final ProbabilityCollection<Integer> randomBlessCol;
  private final ProbabilityCollection<Integer> randomNormalCol;
  private final ProbabilityCollection<Integer> randomCurseCol;
  private ItemBuilder itemBuilder;
  private ItemStack heartItem;
  private String skullData;
  private Material heartType;
  private Component heartName;
  private List<Component> heartLore;
  private int modelData;
  private double healthPoints;
  private Mode mode;
  private String type;
  private String index;
  private String consumeSound;

  /**
   * Instantiates a new Heart item manager.
   */
  public HeartItemManager() {
    this.lifeSteal = LifeStealPlugin.getLifeSteal();
    this.heartConfig = this.lifeSteal.getHeartConfig();
    this.blessedHearts = this.heartConfig.getSection("Hearts.Types.Blessed").getRoutesAsStrings(false);
    this.normalHearts = this.heartConfig.getSection("Hearts.Types.Normal").getRoutesAsStrings(false);
    this.cursedHearts = this.heartConfig.getSection("Hearts.Types.Cursed").getRoutesAsStrings(false);
    this.blessedRarity = this.blessedHearts.toArray(new String[0]);
    this.normalRarity = this.normalHearts.toArray(new String[0]);
    this.cursedRarity = this.cursedHearts.toArray(new String[0]);
    this.randomBlessCol = new ProbabilityCollection<>();
    this.randomNormalCol = new ProbabilityCollection<>();
    this.randomCurseCol = new ProbabilityCollection<>();

    for (int i = 0; i < this.blessedHearts.size(); i++) {
      this.randomBlessCol.add(Integer.parseInt(this.blessedRarity[i]), Integer.parseInt(this.blessedRarity[i]));
    }

    for (int i = 0; i < this.normalHearts.size(); i++) {
      this.randomNormalCol.add(Integer.parseInt(this.normalRarity[i]), Integer.parseInt(this.normalRarity[i]));
    }

    for (int i = 0; i < this.cursedHearts.size(); i++) {
      this.randomCurseCol.add(Integer.parseInt(this.cursedRarity[i]), Integer.parseInt(this.cursedRarity[i]));
    }
  }

  /**
   * Sets mode.
   *
   * @param mode the mode
   * @return the mode
   */
  public HeartItemManager setMode(final Mode mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Prepare ingedients for heart item manager.
   *
   * @return the heart item manager
   */
  public HeartItemManager prepareIngedients() {
    switch (this.mode) {
      case FIXED_BLESSED -> {
        this.index = this.blessedRarity[0];
        this.type = "Blessed";
      }
      case FIXED_NORMAL -> {
        this.index = this.normalRarity[0];
        this.type = "Normal";
      }
      case FIXED_CURSED -> {
        this.index = this.cursedRarity[0];
        this.type = "Cursed";
      }
      case RANDOM_BLESSED -> {
        this.index = String.valueOf(this.randomBlessCol.get());
        this.type = "Blessed";
      }
      case RANDOM_NORMAL -> {
        this.index = String.valueOf(this.randomNormalCol.get());
        this.type = "Normal";
      }
      case RANDOM_CURSED -> {
        this.index = String.valueOf(this.randomCurseCol.get());
        this.type = "Cursed";
      }
      case RANDOM_ALL -> {
        final ProbabilityCollection<Mode> modeCol = new ProbabilityCollection<>();
        modeCol.add(Mode.RANDOM_BLESSED, 27);
        modeCol.add(Mode.RANDOM_NORMAL, 27);
        modeCol.add(Mode.RANDOM_CURSED, 27);
        this.mode = modeCol.get();
        this.prepareIngedients();
      }
      default -> {
        this.mode = Mode.FIXED_NORMAL;
        this.prepareIngedients();
      }
    }

    this.heartType = Material.valueOf(heartConfig.getString
            ("Hearts.Types." + type + "." + index + ".Properties.ItemType"));
    this.heartName = lifeSteal.getUtils().formatString(heartConfig.getString
            ("Hearts.Types." + type + "." + index + ".Properties.Name"));
    this.heartLore = lifeSteal.getUtils().stringToComponentList(heartConfig.getStringList
            ("Hearts.Types." + type + "." + index + ".Properties.Lore"), true);
    this.modelData = heartConfig.getInt
            ("Hearts.Types." + type + "." + index + ".Properties.ModelData");
    this.healthPoints = heartConfig.getDouble
            ("Hearts.Types." + type + "." + index + ".Properties.HeartPoints");
    this.consumeSound = heartConfig.getString
            ("Hearts.Types." + type + "." + index + ".Properties.ConsumeSound");
    if (this.heartType == Material.PLAYER_HEAD) this.skullData = heartConfig.getString
            ("Hearts.Types." + type + "." + index + ".Properties.SkullTexture");

    return this;
  }

  /**
   * Cook heart with prepared ingredients.
   *
   * @return the heart item manager
   */
  public HeartItemManager cookHeart() {
    if (this.heartType == Material.PLAYER_HEAD) {
      this.itemBuilder = new ItemBuilder(this.heartType, lifeSteal.getHeadBuilder().createSkullMap(this.skullData));
    } else {
      this.itemBuilder = new ItemBuilder(this.heartType);
    }
    this.itemBuilder
            .setName(this.heartName.decoration(TextDecoration.ITALIC, false))
            .setLore(this.heartLore)
            .setModelData(this.modelData)
            .setPDCObject(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_item"), PersistentDataType.STRING, "No heart spoofing, dum dum.")
            .setPDCObject(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_itemtype"), PersistentDataType.STRING, this.type)
            .setPDCObject(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_itemindex"), PersistentDataType.STRING, this.index)
            .setPDCObject(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_healthpoints"), PersistentDataType.DOUBLE, this.healthPoints)
            .setPDCObject(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_consumesound"), PersistentDataType.STRING, this.consumeSound)
            .build();
    this.heartItem = this.itemBuilder.getBuiltItem();
    return this;
  }

  /**
   * Cook heart with prepared ingerdients + given hp value.
   *
   * @param healthPoints the health points
   * @return the heart item manager
   */
  public HeartItemManager cookHeart(final int healthPoints) {
    if (this.heartType == Material.PLAYER_HEAD) {
      this.itemBuilder = new ItemBuilder(this.heartType, lifeSteal.getHeadBuilder().createSkullMap(this.skullData));
    } else {
      this.itemBuilder = new ItemBuilder(this.heartType);
    }
    this.itemBuilder
            .setName(this.heartName.decoration(TextDecoration.ITALIC, false))
            .setLore(this.heartLore)
            .setModelData(this.modelData)
            .setPDCObject(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_item"), PersistentDataType.STRING, "No heart spoofing, dum dum.")
            .setPDCObject(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_itemtype"), PersistentDataType.STRING, this.type)
            .setPDCObject(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_itemindex"), PersistentDataType.STRING, this.index)
            .setPDCObject(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_healthpoints"), PersistentDataType.DOUBLE, (double) healthPoints)
            .setPDCObject(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_consumesound"), PersistentDataType.STRING, this.consumeSound)
            .build();
    this.heartItem = this.itemBuilder.getBuiltItem();
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

}
