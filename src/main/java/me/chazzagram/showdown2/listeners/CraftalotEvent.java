package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;

public class CraftalotEvent implements Listener {


    private static Showdown2 plugin;

    public CraftalotEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void edguardInteractEvent(PlayerInteractEntityEvent e){

        Player p = e.getPlayer();
        EntityType entity = e.getRightClicked().getType();
        Inventory inventory = p.getInventory();
        if(plugin.currentMode.equals("Craftalot")){
            if(entity.equals(EntityType.VILLAGER)){
                if(plugin.itemToCraft.containsKey(p.getName())){
                    Material item = Material.getMaterial(plugin.itemToCraft.get(p.getName()));
                    if(inventory.contains(item)){
                        plugin.messagePlayer(p, "You have " + item.getData().getName());
                    } else {
                        plugin.messagePlayer(p, "You don't have " + item.getData().getName());
                    }
                } else {
                    plugin.itemToCraft.put(p.getName(), "STONE_SWORD");

                }
            }
        }
    }

}
