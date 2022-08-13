package in.arcadelabs.lifesteal.elimination;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@Getter
public enum EliminationReason {

  COMMAND("ByCommand"),
  BY_PLAYER("ByPlayer"),
  BY_DAMAGING_BLOCKS("ByDamagingBlocks"),
  BY_MOB("ByMob"),
  BY_PROJECTILE("ByProjectile"),
  BY_SUFFOCATION("BySuffocation"),
  BY_FALLDAMAGE("ByFallDamage"),
  BY_BURN("ByBurn"),
  BY_LAVA("ByLava"),
  BY_DROWNING("ByDrowning"),
  BY_EXPLOSION("ByExplosion"),
  BY_CREEPER("ByCreeper"),
  BY_SUICIDE("BySuicide"),
  BY_VOID("ByVoid"),
  BY_LIGHTNING("ByLightning"),
  BY_STARVATION("ByStarvation"),
  BY_POISON("ByPoison"),
  BY_MAGIC("ByMagic"),
  BY_WITHER("ByWither"),
  BY_FALLING_BLOCK("ByFallingBlock"),
  BY_THORNS("ByThorns"),
  BY_DRAGONBREATH("ByDragonBreath"),
  BY_KINETIC_ENERGY("ByKineticEnergy"),
  BY_MAGMA_BLOCK("ByMagmaBlock"),
  BY_FREEZE("ByFreeze"),
  BY_CRAMMING("ByCramming"),
  OTHER("Other");

  String key;

  EliminationReason(String key) {
    this.key = key;
  }

  public Component getMessageComponent() {
    return MiniMessage.miniMessage().deserialize(
            LifeStealPlugin.getLifeSteal().getKey("Messages.Elimination." + key));
  }

  public String getMessageString() {
    return LifeStealPlugin.getLifeSteal().getKey("Messages.Elimination." + key);
  }

}