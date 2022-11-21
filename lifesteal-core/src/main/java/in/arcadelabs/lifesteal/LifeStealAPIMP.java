package in.arcadelabs.lifesteal;

import in.arcadelabs.LifeStealAPI;
import in.arcadelabs.interfaces.IStatisticsManager;
import in.arcadelabs.labaide.cooldown.CooldownManager;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.ServicePriority;

public class LifeStealAPIMP implements LifeStealAPI {

    private final LifeSteal lifeSteal;

    public LifeStealAPIMP(LifeSteal lifeSteal) {
        this.lifeSteal = lifeSteal;

        lifeSteal.getInstance().getServer().getServicesManager().register(LifeStealAPI.class, this, lifeSteal.getInstance(), ServicePriority.Low);
    }

    @Override
    public @NonNull IStatisticsManager getStatisticsManager() {
        return lifeSteal.getStatisticsManager();
    }

    @Override
    public @NonNull ShapedRecipe getHeartRecipe() {
        return lifeSteal.getHeartRecipeManager().getHeartRecipe();
    }

    @Override
    public @NonNull ItemStack getPlaceholderHeart() {
        return lifeSteal.getPlaceholderHeart();
    }

    @Override
    public @NonNull CooldownManager getCraftCooldown() {
        return lifeSteal.getCraftCooldown();
    }

    @Override
    public @NonNull CooldownManager getConsumeCooldown() {
        return lifeSteal.getConsumeCooldown();
    }

    @Override
    public @NonNull CooldownManager getWithdrawCooldown() {
        return lifeSteal.getWithdrawCooldown();
    }
}
