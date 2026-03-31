package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.PlayerInfoConfig;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ReadyEvent implements Listener {

    private static Showdown2 plugin;

    public ReadyEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    Random rand = new Random();


    @EventHandler
    public void onPlayerCrouch(PlayerToggleSneakEvent e){
        if(plugin.runningTimers.containsKey("readytimer") && plugin.readyType.equals("sneak")) {
            if(plugin.ghostManager.getGhostPlayers().contains(e.getPlayer().getName())) return;
            if(plugin.getPlayers().contains(e.getPlayer())) {
                if (e.isSneaking()) {
                    plugin.readyPlayers.put(e.getPlayer().getName(), plugin.readyPlayers.get(e.getPlayer().getName()) + 1);
                    if (plugin.readyPlayers.get(e.getPlayer().getName()) < 15) {
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, (float) ((float) plugin.readyPlayers.get(e.getPlayer().getName()) / 5));
                    }
                    if (plugin.readyPlayers.get(e.getPlayer().getName()) == 10) {
                        plugin.readyPlayerCount++;
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            plugin.messagePlayer(player, plugin.getPlayerDisplayName(e.getPlayer().getName()) + " §fis ready!");
                        }
                        plugin.messagePlayer(e.getPlayer(), "§aYou are now ready!");
                        e.getPlayer().sendTitle("§a§lYOU'RE READY!", "(§a#" + plugin.readyPlayerCount + "§f) " + congratsMessages[rand.nextInt(congratsMessages.length)], 0, 60, 40);
                        if(PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(e.getPlayer().getName())){
                            if(plugin.readyPlayerCount < PlayerInfoConfig.get().getInt("players." + e.getPlayer().getName() + ".bestreadycheck")) {
                                PlayerInfoConfig.get().set("players." + e.getPlayer().getName() + ".bestreadycheck", plugin.readyPlayerCount);
                                PlayerInfoConfig.save();
                            }
                        }
                        plugin.summonFirework(e.getPlayer().getLocation(), PlayerConfig.get().getString("players." + e.getPlayer().getName() + ".team"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeftClick(PlayerInteractEvent e) {
        Action action = e.getAction();

        if(plugin.runningTimers.containsKey("readytimer") && plugin.readyType.equals("punch")) {
            if(plugin.ghostManager.getGhostPlayers().contains(e.getPlayer().getName())) return;
            if(plugin.getPlayers().contains(e.getPlayer())) {
                if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                    plugin.readyPlayers.put(e.getPlayer().getName(), plugin.readyPlayers.get(e.getPlayer().getName()) + 1);
                    if (plugin.readyPlayers.get(e.getPlayer().getName()) < 15) {
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, (float) ((float) plugin.readyPlayers.get(e.getPlayer().getName()) / 5));
                    }
                    if (plugin.readyPlayers.get(e.getPlayer().getName()) == 10) {
                        plugin.readyPlayerCount++;
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            plugin.messagePlayer(player, plugin.getPlayerDisplayName(e.getPlayer().getName()) + " §fis ready!");
                        }
                        plugin.messagePlayer(e.getPlayer(), "§aYou are now ready!");
                        e.getPlayer().sendTitle("§a§lYOU'RE READY!", "(§a#" + plugin.readyPlayerCount + "§f) " + congratsMessages[rand.nextInt(congratsMessages.length)], 0, 60, 40);
                        if(PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(e.getPlayer().getName())){
                            if(plugin.readyPlayerCount < PlayerInfoConfig.get().getInt("players." + e.getPlayer().getName() + ".bestreadycheck")) {
                                PlayerInfoConfig.get().set("players." + e.getPlayer().getName() + ".bestreadycheck", plugin.readyPlayerCount);
                                PlayerInfoConfig.save();
                            }
                        }
                        plugin.summonFirework(e.getPlayer().getLocation(), PlayerConfig.get().getString("players." + e.getPlayer().getName() + ".team"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPunch(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player p) {
            if(plugin.runningTimers.containsKey("readytimer") && plugin.readyType.equals("punch")) {
                if(plugin.ghostManager.getGhostPlayers().contains(p.getName())) return;
                if(plugin.getPlayers().contains(p)) {
                    plugin.readyPlayers.put(p.getName(), plugin.readyPlayers.get(p.getName()) + 1);
                    if (plugin.readyPlayers.get(p.getName()) < 15) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, (float) ((float) plugin.readyPlayers.get(p.getName()) / 5));
                    }
                    if (plugin.readyPlayers.get(p.getName()) == 10) {
                        plugin.readyPlayerCount++;
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            plugin.messagePlayer(player, plugin.getPlayerDisplayName(p.getName()) + " §fis ready!");
                        }
                        plugin.messagePlayer(p, "§aYou are now ready!");
                        p.sendTitle("§a§lYOU'RE READY!", "(§a#" + plugin.readyPlayerCount + "§f) " + congratsMessages[rand.nextInt(congratsMessages.length)], 0, 60, 40);
                        if(PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(p.getName())){
                            if(plugin.readyPlayerCount < PlayerInfoConfig.get().getInt("players." + p.getName() + ".bestreadycheck")) {
                                PlayerInfoConfig.get().set("players." + p.getName() + ".bestreadycheck", plugin.readyPlayerCount);
                                PlayerInfoConfig.save();
                            }
                        }
                        plugin.summonFirework(p.getLocation(), PlayerConfig.get().getString("players." + p.getName() + ".team"));
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerJump(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        boolean isJumping = plugin.jumpStates.getOrDefault(p, false);
        if (event.getFrom().getY() < event.getTo().getY() &&
                p.getVelocity().getY() > 0.0 &&
                !p.isFlying()){
            if (!isJumping) {
                plugin.jumpStates.put(p, true);
                if (plugin.runningTimers.containsKey("readytimer") && plugin.readyType.equals("jump")) {
                    if(plugin.ghostManager.getGhostPlayers().contains(p.getName())) return;
                    if (plugin.getPlayers().contains(p)) {
                        plugin.readyPlayers.put(p.getName(), plugin.readyPlayers.get(p.getName()) + 1);
                        if (plugin.readyPlayers.get(p.getName()) < 15) {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, (float) ((float) plugin.readyPlayers.get(p.getName()) / 5));
                        }
                        if (plugin.readyPlayers.get(p.getName()) == 10) {
                            plugin.readyPlayerCount++;
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                plugin.messagePlayer(player, plugin.getPlayerDisplayName(p.getName()) + " §fis ready!");
                            }
                            plugin.messagePlayer(p, "§aYou are now ready!");
                            p.sendTitle("§a§lYOU'RE READY!", "(§a#" + plugin.readyPlayerCount + "§f) " + congratsMessages[rand.nextInt(congratsMessages.length)], 0, 60, 40);
                            if(PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(event.getPlayer().getName())){
                                if(plugin.readyPlayerCount < PlayerInfoConfig.get().getInt("players." + event.getPlayer().getName() + ".bestreadycheck")) {
                                    PlayerInfoConfig.get().set("players." + event.getPlayer().getName() + ".bestreadycheck", plugin.readyPlayerCount);
                                    PlayerInfoConfig.save();
                                }
                            }
                            plugin.summonFirework(p.getLocation(), PlayerConfig.get().getString("players." + p.getName() + ".team"));
                        }
                    }
                }
            }
        }
        if (p.getVelocity().getY() <= 0.0 &&
                p.getLocation().getBlock().getRelative(0, -1, 0).getType().isSolid()) {

            plugin.jumpStates.put(p, false);
        }
    }

    String[] congratsMessages = { "Good Job!", "Amazing!", "Class!", "Thanks!", "Smashing!", "Sneaky!", "Bravo!", "Legendary!", "Proper Job!", "Massive!", "GGs!" };
}
