package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.SpectatorConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collection;

public class PlayerInteractionEvent implements Listener {

    private static Showdown2 plugin;

    public PlayerInteractionEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        event.setCancelled(false);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) throws ReflectiveOperationException {
        if(plugin.currentMode.equals("Bridge Builders")) {
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {

                Block clickedBlock = e.getClickedBlock();

                if (clickedBlock != null || clickedBlock.getType() != Material.AIR) {
                    if(plugin.checkWithinBuildArea(clickedBlock, e.getPlayer().getName())) {
                        clickedBlock.setType(Material.AIR);
                        Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, clickedBlock.getLocation().clone().add(0.5, 0.5, 0.5), 3, 0.0, 0.0, 0.0, 0);
                    }
                }
            }
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(false);
            }
        } else if(plugin.currentMode.equals("Gub Game")) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getItem() != null && e.getItem().getType() == Material.TRIDENT) {
                    e.setCancelled(true);
                } else if (e.getItem() != null && (e.getItem().getType() == Material.BOW || e.getItem().getType() == Material.CROSSBOW)) {
                    e.setCancelled(false);
                } else {
                    e.setCancelled(true);
                }
            }
        } else if (plugin.currentMode.equals("Survival Games")) {
            e.setCancelled(false);
        } else if (plugin.currentMode.equals("Craftalot")) {
            e.setCancelled(false);
            if (e.getItem() != null && e.getItem().getType().name().endsWith("_BOAT")) {
                e.setCancelled(true);
            }
        } else if (plugin.currentMode.equals("Colour Dash")) {
            e.setCancelled(false);
        } else if (plugin.currentMode.equals("Slime Golf")) {
            if (e.getItem() != null && e.getItem().getType() == Material.FISHING_ROD) {
                e.setCancelled(false);
            }
        } else {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != Material.BELL) {
                if(plugin.getPlayers().contains(e.getPlayer())) {
                    e.setCancelled(true);
                }
            }
            if(e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.BARREL) {
                e.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof BlockDisplay)) return;

        if (event.getDamager() instanceof Player player) {
            player.sendMessage("You hit a BlockDisplay!");
            event.setCancelled(true); // prevent breaking animation/damage if desired
        }
    }
}
