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

package in.arcadelabs.lifesteal.api.elimination;

import lombok.Getter;

@Getter
public enum EliminationCause {

  COMMAND("ByCommand"),
  PLAYER("ByPlayer"),
  DAMAGING_BLOCKS("ByDamagingBlocks"),
  MOB("ByMob"),
  PROJECTILE("ByProjectile"),
  SUFFOCATION("BySuffocation"),
  FALLDAMAGE("ByFallDamage"),
  BURN("ByBurn"),
  LAVA("ByLava"),
  DROWNING("ByDrowning"),
  EXPLOSION("ByExplosion"),
  CREEPER("ByCreeper"),
  SUICIDE("BySuicide"),
  VOID("ByVoid"),
  LIGHTNING("ByLightning"),
  STARVATION("ByStarvation"),
  POISON("ByPoison"),
  MAGIC("ByMagic"),
  WITHER("ByWither"),
  FALLING_BLOCK("ByFallingBlock"),
  THORNS("ByThorns"),
  DRAGONBREATH("ByDragonBreath"),
  KINETIC_ENERGY("ByKineticEnergy"),
  MAGMA_BLOCK("ByMagmaBlock"),
  FREEZE("ByFreeze"),
  CRAMMING("ByCramming"),
  OTHER("Other");

  final String key;

  EliminationCause(String key) {
    this.key = key;
  }
}
