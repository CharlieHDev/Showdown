package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PickupItemEvent implements Listener {

    private static Showdown2 plugin;

    public PickupItemEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player && plugin.currentMode.equals("Voting") && plugin.votingEnabled) {
            event.setCancelled(true);
            Item item = event.getItem();
            if(item.getItemStack().getType().equals(Material.TNT) && !plugin.powerUpHolders.contains(player.getName())){
                plugin.powerUpHolders.add(player.getName());
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 10, 1);
                plugin.messagePlayer(player, """
                        §f
                        §f
                        §c§lPOWER-UP READY!
                        §e§oHold shift to charge up a voting explosion!
                        §f
                        """);
                plugin.runningTimers.remove("powerup");
                item.remove();
            }
        }
    }
}
