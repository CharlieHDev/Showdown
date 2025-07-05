package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

public class RiptideEvent implements Listener {


    private static Showdown2 plugin;

    public RiptideEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTridentThrow(ProjectileLaunchEvent event) {
        if (plugin.currentMode.equals("Craftalot") || plugin.currentMode.equals("Colour Dash")) {
            if (!(event.getEntity() instanceof Trident)) {
                return;
            }

            Projectile projectile = event.getEntity();

            if (!(projectile.getShooter() instanceof Player)) {
                return;
            }

            event.setCancelled(true);

            Player player = (Player) projectile.getShooter();

            Material blockType = player.getLocation().getBlock().getType();
            if (blockType == Material.WATER || blockType == Material.KELP || blockType == Material.SEAGRASS) {

                Vector direction = player.getLocation().getDirection().normalize();
                Vector velocity = direction.multiply(3);
                player.setVelocity(velocity);

                player.getWorld().playSound(player.getLocation(), Sound.ITEM_TRIDENT_RIPTIDE_1, 1.0f, 1.0f);
            }
        }
    }
}
