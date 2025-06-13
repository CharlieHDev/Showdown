package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ColourDashEvent implements Listener {

    private static Showdown2 plugin;

    public ColourDashEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Material item = e.getBlock().getType();
        Inventory inventory = p.getInventory();
        if(plugin.currentMode.equals("Colour Dash") || (plugin.currentMode.equals("Bridge Builders"))){
            if(!(plugin.currentMode.equals("Colour Dash") && item.equals(Material.BLUE_ICE))) {
                ItemStack sixtyfour = new ItemStack(item);
                sixtyfour.setAmount(64);
                if (!inventory.contains(sixtyfour)) {
                    if (p.getInventory().getItemInOffHand().getType().equals(item) && !p.getInventory().getItemInOffHand().equals(sixtyfour)) {
                        p.getInventory().setItemInOffHand(sixtyfour);
                    } else {
                        ItemStack itemstack = new ItemStack(item);
                        itemstack.setAmount(1);
                        inventory.addItem(itemstack);
                    }
                }
                if (plugin.currentMode.equals("Bridge Builders") && !plugin.checkWithinBuildArea(e.getBlock(), p.getName())) {
                    e.setCancelled(true);
                }
            }
        } else {
            if(plugin.getPlayers().contains(p)) {
                e.setCancelled(true);
            }
        }
    }
}
