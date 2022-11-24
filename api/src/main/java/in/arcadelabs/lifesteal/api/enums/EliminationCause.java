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

package in.arcadelabs.lifesteal.api.enums;

import lombok.Getter;

@Getter
public enum EliminationCause {

  /**
   * Command elimination cause.
   */
  COMMAND("ByCommand"),
  /**
   * Player elimination cause.
   */
  PLAYER("ByPlayer"),
  /**
   * Damaging blocks elimination cause.
   */
  DAMAGING_BLOCKS("ByDamagingBlocks"),
  /**
   * Mob elimination cause.
   */
  MOB("ByMob"),
  /**
   * Projectile elimination cause.
   */
  PROJECTILE("ByProjectile"),
  /**
   * Suffocation elimination cause.
   */
  SUFFOCATION("BySuffocation"),
  /**
   * Falldamage elimination cause.
   */
  FALLDAMAGE("ByFallDamage"),
  /**
   * Burn elimination cause.
   */
  BURN("ByBurn"),
  /**
   * Lava elimination cause.
   */
  LAVA("ByLava"),
  /**
   * Drowning elimination cause.
   */
  DROWNING("ByDrowning"),
  /**
   * Explosion elimination cause.
   */
  EXPLOSION("ByExplosion"),
  /**
   * Creeper elimination cause.
   */
  CREEPER("ByCreeper"),
  /**
   * Suicide elimination cause.
   */
  SUICIDE("BySuicide"),
  /**
   * Void elimination cause.
   */
  VOID("ByVoid"),
  /**
   * Lightning elimination cause.
   */
  LIGHTNING("ByLightning"),
  /**
   * Starvation elimination cause.
   */
  STARVATION("ByStarvation"),
  /**
   * Poison elimination cause.
   */
  POISON("ByPoison"),
  /**
   * Magic elimination cause.
   */
  MAGIC("ByMagic"),
  /**
   * Wither elimination cause.
   */
  WITHER("ByWither"),
  /**
   * Falling block elimination cause.
   */
  FALLING_BLOCK("ByFallingBlock"),
  /**
   * Thorns elimination cause.
   */
  THORNS("ByThorns"),
  /**
   * Dragonbreath elimination cause.
   */
  DRAGONBREATH("ByDragonBreath"),
  /**
   * Kinetic energy elimination cause.
   */
  KINETIC_ENERGY("ByKineticEnergy"),
  /**
   * Magma block elimination cause.
   */
  MAGMA_BLOCK("ByMagmaBlock"),
  /**
   * Freeze elimination cause.
   */
  FREEZE("ByFreeze"),
  /**
   * Cramming elimination cause.
   */
  CRAMMING("ByCramming"),
  /**
   * Other elimination cause.
   */
  OTHER("Other");

  /**
   * The message Key.
   */
  final String key;

  EliminationCause(String key) {
    this.key = key;
  }
}
