package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ArrowDespawnEvent implements Listener {

    private static Showdown2 plugin;

    public ArrowDespawnEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onArrowShoot(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile instanceof Arrow arrow) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!arrow.isDead() && arrow.isValid()) {
                        arrow.remove();
                    }
                }
            }.runTaskLater(plugin, 20L * 10); // 10 seconds later (20 ticks = 1 second)
        }
    }
}
