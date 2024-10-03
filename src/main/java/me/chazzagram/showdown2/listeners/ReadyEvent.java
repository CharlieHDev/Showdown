package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashMap;

public class ReadyEvent implements Listener {

    private static Showdown2 plugin;

    public ReadyEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerCrouch(PlayerToggleSneakEvent e){
        if(plugin.runningTimers.containsKey("readytimer")) {
            if (e.isSneaking()) {
                plugin.readyPlayers.put(e.getPlayer().getName(), plugin.readyPlayers.get(e.getPlayer().getName()) + 1);
                if(plugin.readyPlayers.get(e.getPlayer().getName()) < 15){
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, (float)((float)plugin.readyPlayers.get(e.getPlayer().getName())/5));
                }
                if (plugin.readyPlayers.get(e.getPlayer().getName()) == 10) {
                    for(Player player : plugin.getPlayers()){
                        plugin.messagePlayer(player, plugin.getPlayerDisplayName(e.getPlayer().getName()) + " §fis ready!");
                    }
                    plugin.messagePlayer(e.getPlayer(), "§aYou are now ready!");
                    e.getPlayer().sendTitle("§a§lYOU'RE READY!", "Good Job!", 0, 60, 40);
                    summonFirework(e.getPlayer().getLocation(), PlayerConfig.get().getString("players." + e.getPlayer().getName() + ".team"));
                }
            }
        }
    }

    public void summonFirework(Location location, String team){
        World world = location.getWorld();

        // Spawn a firework entity
        Firework firework = (Firework) world.spawnEntity(location, EntityType.FIREWORK_ROCKET);

        // Get the firework meta data
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // Create a firework effect (star shape with a trail and flicker)
        FireworkEffect effect = FireworkEffect.builder()
                .withColor(plugin.teamColors.get(team))
                .withFade(plugin.teamColors.get(team))
                .with(FireworkEffect.Type.BURST)
                .build();

        // Add the effect to the firework
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(0); // Set firework power (1 = short duration)
        firework.setFireworkMeta(fireworkMeta);

        // Detonate the firework immediately
        firework.detonate();
    }
}
