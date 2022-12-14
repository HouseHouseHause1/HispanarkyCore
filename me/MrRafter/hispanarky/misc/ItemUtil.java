package me.MrRafter.hispanarky.misc;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {
    private ItemUtil() {
    }

    public static boolean isBook(ItemStack item) {
        switch (item.getType()) {
            case BOOK_AND_QUILL:
            case WRITTEN_BOOK:
                return true;
            default:
                return false;
        }
    }

    public static boolean isShulker(ItemStack item) {
        switch (item.getType()) {
            case BLACK_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case SILVER_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case WHITE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
                return true;
            default:
                return false;
        }
    }

    // Don't count certain "entities" in the entity limit, just in case.
    public static boolean isEntity(Entity e) {
        switch (e.getType()) {
            case PLAYER:
            case WEATHER:
            case LIGHTNING:
            case PAINTING:
            case ENDER_DRAGON:
            case WOLF:
            case DONKEY:
            case BAT:
            case RABBIT:
            case POLAR_BEAR:
            case ENDERMITE:
                return false;
            default:
                return true;
        }
    }
}