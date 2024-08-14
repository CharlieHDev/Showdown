package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.EventListener;
import java.util.HashMap;

public class VoteWalkEvent implements Listener {

    private static Showdown2 plugin;

    public VoteWalkEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.getTo() != null) {
            for(Material concrete : getConcreteColours()) {
                if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(concrete)) {
                    String colour = concrete.toString().toUpperCase().replace("CONCRETE", "WOOL");
                    Material wool = Material.getMaterial(colour);
                    plugin.playerVote.put(event.getPlayer(), wool);
                }
            }
            for(Material wool : getWoolColors()) {
                if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(wool)) {
                    if(plugin.playerVote.containsKey(event.getPlayer())) {
                        event.getTo().getBlock().getRelative(BlockFace.DOWN).setType(plugin.playerVote.get(event.getPlayer()));
                    }
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

    private Material[] getConcreteColours() {
        return new Material[]{
                Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.MAGENTA_CONCRETE,
                Material.LIGHT_BLUE_CONCRETE, Material.YELLOW_CONCRETE, Material.LIME_CONCRETE,
                Material.PINK_CONCRETE, Material.GRAY_CONCRETE, Material.LIGHT_GRAY_CONCRETE,
                Material.CYAN_CONCRETE, Material.PURPLE_CONCRETE, Material.BLUE_CONCRETE,
                Material.BROWN_CONCRETE, Material.GREEN_CONCRETE, Material.RED_CONCRETE,
                Material.BLACK_CONCRETE
        };
    }
}
