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

import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpiritFactory {
  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private static final LifeStealPlugin instance = LifeStealPlugin.getInstance();

  private final List<Player> spirits = new ArrayList<>();
  private ItemStack spiritModel;

  private ItemStack bakeSpiritModel() {
    spiritModel = new ItemStack(Material.valueOf(lifeSteal.getConfig().getString("Spirits.Spirit-Model.ItemType")));
    spiritModel.getItemMeta().setDisplayName(lifeSteal.getUtils().formatString
            (lifeSteal.getConfig().getString("Spirits.Spirit-Model.Name")));
    spiritModel.getItemMeta().setLore(lifeSteal.getUtils().formatStringList
            (lifeSteal.getConfig().getStringList("Spirits.Spirit-Model.Lore")));
    spiritModel.getItemMeta().setCustomModelData(lifeSteal.getConfig().getInt("Spirits.Spirit-Model.ModelData"));
    return spiritModel;
  }

  /**
   * Add spirit.
   *
   * @param player the player
   */
  public void addSpirit(final Player player) {
    if (spirits.contains(player)) return;
    spirits.add(player);
    lifeSteal.getUtils().setPlayerBaseHealth(player, lifeSteal.getConfig().getInt("Spirits.Hearts", 1));
    player.setGameMode(GameMode.valueOf(lifeSteal.getConfig().getString("Spirits.GameMode", "ADVENTURE")));
    player.setInvisible(lifeSteal.getConfig().getBoolean("Spirits.Invisible", true));
    player.setInvulnerable(lifeSteal.getConfig().getBoolean("Spirits.Invulnerable", true));
    player.setCanPickupItems(lifeSteal.getConfig().getBoolean("Spirits.CanPickupItems", false));
    player.setCollidable(lifeSteal.getConfig().getBoolean("Spirits.Collidable", false));
    player.setSleepingIgnored(lifeSteal.getConfig().getBoolean("Spirits.SleepingIgnored", true));
    player.setSilent(lifeSteal.getConfig().getBoolean("Spirits.Silent", true));
    if (lifeSteal.getConfig().getBoolean("Spirits.Black-Hearts", true))
      player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, Integer.MIN_VALUE,
              false,
              false,
              false));
    if (!lifeSteal.getConfig().getBoolean("Spirits.Spirit-Model.Enabled")) return;
    player.getInventory().setHelmet(bakeSpiritModel());
  }

  /**
   * Remove spirit.
   *
   * @param player the player
   */
  public void removeSpirit(final Player player) {
    if (!spirits.contains(player)) return;
    spirits.remove(player);
    lifeSteal.getUtils().setPlayerBaseHealth(player, lifeSteal.getConfig().getInt("DefaultHealth", 20));
    player.setInvisible(false);
    player.setInvulnerable(false);
    player.setGameMode(GameMode.SURVIVAL);
    player.setCanPickupItems(true);
    player.setCollidable(true);
    player.setSleepingIgnored(false);
    player.setSilent(false);
    player.removePotionEffect(PotionEffectType.WITHER);
    loadInventory(player);
  }

  /**
   * Gets spirits.
   *
   * @return the spirits
   */
  public List<Player> getSpirits() {
    return spirits;
  }

  /**
   * Save & clear inventory.
   *
   * @param player the player
   */
  public void saveInventory(final Player player) {
    final PlayerInventory inventory = player.getInventory();
    try (
            final ByteArrayOutputStream invStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream invOutput = new BukkitObjectOutputStream(invStream)) {
      invOutput.writeInt(inventory.getSize());
      for (int i = 0; i < inventory.getSize(); i++) {
        invOutput.writeObject(inventory.getItem(i));
      }
      invOutput.close();
      player.getPersistentDataContainer().set(new NamespacedKey(instance, "lifesteal_player_inventory"),
              PersistentDataType.STRING,
              Base64Coder.encodeLines(invStream.toByteArray()));
      inventory.clear();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Load inventory.
   *
   * @param player the player
   */
  public void loadInventory(final Player player) {
    final PlayerInventory inventory = player.getInventory();
    inventory.clear();
    final String base64Inv = player.getPersistentDataContainer().get(new NamespacedKey(instance,
            "lifesteal_player_inventory"), PersistentDataType.STRING);
    try (
            final ByteArrayInputStream invStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64Inv));
            final BukkitObjectInputStream invOutput = new BukkitObjectInputStream(invStream)) {
      final int invSize = invOutput.readInt();
      for (int i = 0; i < invSize; i++) {
        inventory.setItem(i, (ItemStack) invOutput.readObject());
      }
      player.getPersistentDataContainer().remove(new NamespacedKey(instance, "lifesteal_player_inventory"));
    } catch (final ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }
  }
}