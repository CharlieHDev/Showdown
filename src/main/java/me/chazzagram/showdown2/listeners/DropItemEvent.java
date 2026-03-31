package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItemEvent implements Listener {

    private static Showdown2 plugin;

    public DropItemEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDropItem(final PlayerDropItemEvent event) {

        if(plugin.ghostManager.getGhostPlayers().contains(event.getPlayer().getName())) { event.setCancelled(true); return; }

        if(plugin.getPlayers().contains(event.getPlayer())) {
            switch (plugin.currentMode) {
                case "Zoomo Go":
                case "Gub Game":
                case "Dimension Dash":
                case "Craftalot":
                case "Slime Golf":
                case "Voting":
                case "Crumble Clash":
                case "Push Point":
                    event.setCancelled(true);
                    break;
                case "Lobby":
                    event.setCancelled(!event.getItemDrop().getItemStack().getType().equals(Material.POTION) && !event.getItemDrop().getItemStack().getType().equals(Material.MILK_BUCKET) && !event.getItemDrop().getItemStack().getType().equals(Material.SPLASH_POTION));
                    break;
            }
        }
    }
}
