package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.GubTPConfig;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.AbstractMap;
import java.util.Random;

public class BellDongEvent implements Listener {

    private static Showdown2 plugin;

    public BellDongEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBellDong(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        Material material = e.getClickedBlock().getType();
        if (material == Material.BELL) {
            if (!plugin.bellRung) {
                if(!plugin.bellRingers.contains(e.getPlayer())) {
                    plugin.bellRingers.add(e.getPlayer());
                }
                if (!plugin.runningTimers.containsKey("bell")) {
                    BukkitTask task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (plugin.bellRingers.isEmpty()) {
                                plugin.runningTimers.remove("bell");
                                cancel();
                            }
                            plugin.bellRingers.clear();
                        }

                    }.runTaskTimer(plugin, 0L, 20L);

                    plugin.runningTimers.put("bell", new AbstractMap.SimpleEntry<>(task, 6));

                }
                e.setCancelled(false);
                if (plugin.bellRingers.size() >= 15) {
                    plugin.bellRung = true;
                    Bukkit.getWorld("build").spawnParticle(Particle.EXPLOSION, e.getClickedBlock().getLocation().clone().add(0.5, 1.5, 0.5), 3, 0.0, 0.0, 0.0, 0);
                    Bukkit.getWorld("build").spawnEntity(e.getClickedBlock().getLocation().clone().add(0.5, 1.5, 0.5), EntityType.PARROT);
                    plugin.playSoundAll(Sound.BLOCK_END_PORTAL_SPAWN, 1F);
                    Vector velocity = new Vector(0, 0.8, 0);
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.setVelocity(velocity);
                    }
                } else if (plugin.bellRingers.size() >= 10){
                    Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, e.getClickedBlock().getLocation().clone().add(0.5, 1.5, 0.5), 10, 0.0, 2.0, 0.0, 0);
                } else if (plugin.bellRingers.size() >= 5){
                    Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, e.getClickedBlock().getLocation().clone().add(0.5, 1.5, 0.5), 6, 0.0, 1.0, 0.0, 0);
                } else {
                    Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, e.getClickedBlock().getLocation().clone().add(0.5, 1.5, 0.5), 3, 0.0, 0.0, 0.0, 0);
                }
            }
        }
    }
}
