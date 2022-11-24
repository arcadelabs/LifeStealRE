package in.arcadelabs.lifesteal;

import in.arcadelabs.lifesteal.api.ILifeStealAPI;
import in.arcadelabs.lifesteal.api.enums.Mode;
import in.arcadelabs.lifesteal.api.interfaces.IStatisticsManager;
import in.arcadelabs.labaide.cooldown.CooldownManager;
import in.arcadelabs.lifesteal.hearts.HeartItemManager;
import lombok.NonNull;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.ServicePriority;

import java.util.Map;
import java.util.Objects;

public class LifeStealAPI implements ILifeStealAPI {

    private final LifeSteal lifeSteal;

    public LifeStealAPI(LifeSteal lifeSteal) {
        this.lifeSteal = lifeSteal;
        lifeSteal.getInstance().getServer().getServicesManager().register(ILifeStealAPI.class, this, lifeSteal.getInstance(), ServicePriority.Low);
    }

    /**
     * Gets statistics manager.
     *
     * @return {@link IStatisticsManager} instance
     */
    @Override
    public @NonNull IStatisticsManager getStatisticsManager() {
        return lifeSteal.getStatisticsManager();
    }

    /**
     * Gets heart recipe manager.
     *
     * @return {@link ShapedRecipe} instance of defualt heart recipe
     */
    @Override
    public @NonNull ShapedRecipe getHeartRecipe() {
        return lifeSteal.getHeartRecipeManager().getHeartRecipe();
    }

    /**
     * Gets placeholder heart.
     *
     * @return {@link ItemStack} instance of placeholder heart item
     */
    @Override
    public @NonNull ItemStack getPlaceholderHeart() {
        return lifeSteal.getPlaceholderHeart();
    }

    /**
     * Gets craft cooldown.
     *
     * @return {@link CooldownManager} instance of heart craft cooldown
     */
    @Override
    public @NonNull CooldownManager getCraftCooldown() {
        return lifeSteal.getCraftCooldown();
    }

    /**
     * Gets consume cooldown.
     *
     * @return {@link CooldownManager} instance of heart consume cooldown
     */
    @Override
    public @NonNull CooldownManager getConsumeCooldown() {
        return lifeSteal.getConsumeCooldown();
    }

    /**
     * Gets withdraw manager.
     *
     * @return {@link CooldownManager} instance of heart withdraw cooldown
     */
    @Override
    public @NonNull CooldownManager getWithdrawCooldown() {
        return lifeSteal.getWithdrawCooldown();
    }

    /**
     * Gets player hearts.
     *
     * @param player the player
     * @return the player hearts
     */
    @Override
    public double getPlayerHearts(final Player player) {
        return Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue() / 2;
    }

    /**
     * Sets player hearts.
     *
     * @param player the player
     * @param hearts the hearts
     */
    @Override
    public void setPlayerHearts(final Player player, final double hearts) {
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(hearts * 2);
    }

    /**
     * Transfer hearts.
     *
     * @param victim the victim
     * @param killer the killer
     */
    @Override
    public void transferHearts(final Player victim, final Player killer, final double hearts) {
        setPlayerHearts(killer, getPlayerHearts(killer) + hearts);
        setPlayerHearts(victim, getPlayerHearts(victim) - hearts);
    }

    /**
     * Give hearts.
     *
     * @param target the target
     * @param mode   the mode
     * @param amount the amount
     */
    @Override
    public void giveHearts(final Player target, final Mode mode, final int amount) {
        HeartItemManager heartItemManager = new HeartItemManager()
                .setMode(mode)
                .prepareIngedients()
                .cookHeart();
        ItemStack replacementHeart = heartItemManager.getHeartItem();
        replacementHeart.setAmount(amount);

        final Map<Integer, ItemStack> items = target.getInventory().addItem(replacementHeart);
        for (final Map.Entry<Integer, ItemStack> leftovers : items.entrySet()) {
            target.getWorld().dropItemNaturally(target.getLocation(), leftovers.getValue());
        }
    }
}
