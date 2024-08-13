package me.chazzagram.showdown2.listeners;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.EventListener;

public class VoteWalkEvent implements EventListener, Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.getTo() != null) {
            for(Material wool : getWoolColors()) {
                if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(wool)) {
                    event.getTo().getBlock().getRelative(BlockFace.DOWN).setType(Material.GLOWSTONE);
                }
            }
        }
    }

    private Material[] getWoolColors() {
        return new Material[]{
                Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL,
                Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL,
                Material.PINK_WOOL, Material.GRAY_WOOL, Material.LIGHT_GRAY_WOOL,
                Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL,
                Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL,
                Material.BLACK_WOOL
        };
    }
}
