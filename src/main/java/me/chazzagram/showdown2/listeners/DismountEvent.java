package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SulfurCube;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;

public class DismountEvent implements Listener {

    private static Showdown2 plugin;

    public DismountEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player p) {
            if(plugin.currentMode.equals("Slime Golf")) {
                if (event.getDismounted() instanceof SulfurCube || event.getDismounted() instanceof Player) {
                    event.setCancelled(true);
                }
            }
        }
    }
}