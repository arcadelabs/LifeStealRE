package in.arcadelabs.lifesteal.utils.backend;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lucanius
 * @since June 12, 2022.
 * BW1058 Addon - All Rights Reserved.
 */
@UtilityClass
public final class CC {

    public final String BLUE = ChatColor.BLUE.toString();
    public final String AQUA = ChatColor.AQUA.toString();
    public final String YELLOW = ChatColor.YELLOW.toString();
    public final String RED = ChatColor.RED.toString();
    public final String GRAY = ChatColor.GRAY.toString();
    public final String GOLD = ChatColor.GOLD.toString();
    public final String GREEN = ChatColor.GREEN.toString();
    public final String WHITE = ChatColor.WHITE.toString();
    public final String BLACK = ChatColor.BLACK.toString();
    public final String DARK_BLUE = ChatColor.DARK_BLUE.toString();
    public final String DARK_AQUA = ChatColor.DARK_AQUA.toString();
    public final String DARK_GRAY = ChatColor.DARK_GRAY.toString();
    public final String DARK_GREEN = ChatColor.DARK_GREEN.toString();
    public final String DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
    public final String DARK_RED = ChatColor.DARK_RED.toString();
    public final String PINK = ChatColor.LIGHT_PURPLE.toString();

    public final String BOLD = ChatColor.BOLD.toString();
    public final String ITALIC = ChatColor.ITALIC.toString();
    public final String UNDER_LINE = ChatColor.UNDERLINE.toString();
    public final String STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
    public final String RESET = ChatColor.RESET.toString();
    public final String MAGIC = ChatColor.MAGIC.toString();

    public String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public List<String> translate(List<String> list) {
        return list.stream().map(CC::translate).collect(Collectors.toList());
    }

    public List<String> translate(String[] array) {
        return Arrays.stream(array).map(CC::translate).collect(Collectors.toList());
    }
}
