package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
            if(plugin.currentMode.equals("Voting")) {
                for (Material concrete : getConcreteColours()) {
                    if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(concrete)) {
                        String colour = concrete.toString().toUpperCase().replace("CONCRETE", "WOOL");
                        Material wool = Material.getMaterial(colour);
                        if (!wool.equals(plugin.playerVote.get(event.getPlayer()))) {
                            event.getPlayer().sendTitle(plugin.woolLogos.get(wool), "", 0, 20, 10);
                        }
                        plugin.playerVote.put(event.getPlayer(), wool);
                    }
                }
                if (plugin.votingEnabled) {
                    for (Material wool : getWoolColors()) {
                        if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(wool)) {
                            if (plugin.playerVote.containsKey(event.getPlayer())) {
                                event.getTo().getBlock().getRelative(BlockFace.DOWN).setType(plugin.playerVote.get(event.getPlayer()));
                            }
                        }
                    }
                }
            } else if (plugin.currentMode.equals("Zoomo Go") && plugin.doubleJumpEnabled) {
                Block block = event.getPlayer().getLocation().subtract(0, 1, 0).getBlock();
                if (block.getType() != Material.AIR) {
                    event.getPlayer().setAllowFlight(true);
                }
            }
        }
    }

    private Material[] getWoolColors() {
        return new Material[]{
                Material.RED_WOOL, Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.LIGHT_BLUE_WOOL, Material.LIME_WOOL,
                Material.YELLOW_WOOL, Material.PURPLE_WOOL, Material.BLACK_WOOL
        };
    }

    private Material[] getConcreteColours() {
        return new Material[]{
                Material.RED_CONCRETE, Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.LIGHT_BLUE_CONCRETE, Material.LIME_CONCRETE,
                Material.YELLOW_CONCRETE, Material.PURPLE_CONCRETE

        };
    }
}
