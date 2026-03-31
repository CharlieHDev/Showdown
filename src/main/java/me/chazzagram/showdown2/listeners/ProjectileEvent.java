package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.PlayerInfoConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ProjectileEvent implements Listener {

    private static Showdown2 plugin;

    public ProjectileEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if(plugin.currentMode.equals("Crumble Clash") && plugin.copperDecay) {
            if (event.getEntity().getType() != EntityType.WIND_CHARGE) return;

            if (!(event.getEntity().getShooter() instanceof Player player)) return;

            ItemStack windcharge = new ItemStack(Material.WIND_CHARGE, 1);
            ItemMeta meta = windcharge.getItemMeta();
            meta.setDisplayName("§fWind Charge");
            windcharge.setItemMeta(meta);

            BukkitTask task = new BukkitRunnable() {
                int timeLeft = 0;
                @Override
                public void run() {
                    if(plugin.runningTimers.containsKey(player.getName() + "wc")) {
                        if (!plugin.pausedTimers.contains(player.getName() + "wc")) {
                            timeLeft++;
                            plugin.runningTimers.get(player.getName() + "wc").setValue(timeLeft);
                            if(timeLeft == 4){
                                player.getInventory().setItemInOffHand(windcharge);
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§7[§a+§7] §r" + windcharge.getItemMeta().getDisplayName()));
                                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
                                plugin.runningTimers.remove(player.getName() + "wc");
                                cancel();
                            }
                        }
                    } else {
                        plugin.runningTimers.remove(player.getName() + "wc");
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 20L, 20L);

            plugin.runningTimers.put(player.getName() + "wc", new AbstractMap.SimpleEntry<>(task, 0));

        }
        if (plugin.runningTimers.containsKey("readytimer") && plugin.readyType.equals("snowballs")) {
            if (!(event.getEntity().getShooter() instanceof Player player)) return;

            if (!(event.getEntity() instanceof Snowball)) return;

            event.setCancelled(false);

            plugin.readyPlayers.put(player.getName(), plugin.readyPlayers.get(player.getName()) + 1);

            int count = 0;
            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null) continue;

                if (item.getType() == Material.SNOWBALL) {
                    count += item.getAmount();
                }
            }
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, ((17 - count) * 0.2F));
            if (count == 7) {
                plugin.readyPlayerCount++;
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    plugin.messagePlayer(player2, plugin.getPlayerDisplayName(player.getName()) + " §fis ready!");
                }
                plugin.messagePlayer(player, "§aYou are now ready!");
                player.sendTitle("§a§lYOU'RE READY!", "(§a#" + plugin.readyPlayerCount + "§f) " + congratsMessages[rand.nextInt(congratsMessages.length)], 0, 60, 40);
                if (PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(player.getName())) {
                    if (plugin.readyPlayerCount < PlayerInfoConfig.get().getInt("players." + player.getName() + ".bestreadycheck")) {
                        PlayerInfoConfig.get().set("players." + player.getName() + ".bestreadycheck", plugin.readyPlayerCount);
                        PlayerInfoConfig.save();
                    }
                }
                plugin.summonFirework(player.getLocation(), PlayerConfig.get().getString("players." + player.getName() + ".team"));
            }
        }
        if (event.getEntity() instanceof Snowball snowball && snowball.getShooter() instanceof Player shooter) {
            if (plugin.voteFrozenManager.freezeCheck) {
                event.setCancelled(false);
                List<Snowball> playerSnowballs = plugin.thrownSnowballs.getOrDefault(shooter, new ArrayList<>());
                playerSnowballs.add(snowball);
                plugin.thrownSnowballs.put(shooter, playerSnowballs);
            }
        }
    }

    Random rand = new Random();
    String[] congratsMessages = { "Good Job!", "Amazing!", "Class!", "Thanks!", "Smashing!", "Sneaky!", "Bravo!", "Legendary!", "Proper Job!", "Massive!", "GGs!" };

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if(event.getEntity() instanceof EnderPearl pearl && pearl.getShooter() instanceof Player player) {
            Block hitBlock = event.getHitBlock();

            if (hitBlock != null) {
                if(hitBlock.getType() == Material.BARRIER) {
                    event.setCancelled(true);
                    plugin.messagePlayer(player, "Your ender pearl did not land safely, it has been returned to your inventory.");
                    ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL);
                    ItemMeta meta = enderpearl.getItemMeta();

                    meta.setDisplayName("§aEnder Pearl");
                    enderpearl.setItemMeta(meta);

                    player.getInventory().addItem(enderpearl);
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§7[§a+§7] §r" + enderpearl.getItemMeta().getDisplayName()));

                }
            }
        }

        if (event.getEntity() instanceof Snowball snowball && event.getHitEntity() instanceof Player victim && snowball.getShooter() instanceof Player shooter) {
            if(plugin.currentMode.equals("Crumble Clash")) {
                event.setCancelled(false);
                plugin.crumbleKillTracker.put(
                        victim.getName(),
                        new CrumbleKillData(shooter.getName(), System.currentTimeMillis())
                );
            }
        }

        if (event.getEntity() instanceof Snowball snowball) {
            if (plugin.voteFrozenManager.freezeCheck) {
                if (event.getHitBlock() instanceof Block block) {
                    if(!getWoolColors().contains(block.getType())){
                        event.setCancelled(true);
                        if(plugin.votingEnabled) {
                            for (Player p : plugin.thrownSnowballs.keySet()) {
                                if (plugin.thrownSnowballs.get(p).contains(snowball)) {
                                    ItemStack iceball = new ItemStack(Material.SNOWBALL, 1);
                                    ItemMeta meta = iceball.getItemMeta();
                                    meta.setDisplayName("§b§lIce Ball");
                                    meta.setLore(Arrays.asList("§fThrow this at a player or the floor", "§fto freeze the surrounding floor."));
                                    iceball.setItemMeta(meta);
                                    p.getInventory().addItem(iceball);
                                }
                            }
                        }
                    } else {
                        for (Player player : plugin.thrownSnowballs.keySet()) {
                            if (plugin.thrownSnowballs.get(player).contains(snowball)) {
                                plugin.voteFrozenManager.setFrozenBlocks(player.getName(), block);
                            }
                        }
                    }
                }
                if (event.getHitEntity() instanceof Player) {
                    for(Player player2 : plugin.thrownSnowballs.keySet()){
                        if(plugin.thrownSnowballs.get(player2).contains(snowball)){
                            Block target = player2.getLocation().getBlock().getRelative(BlockFace.DOWN);
                            plugin.voteFrozenManager.setFrozenBlocks(player2.getName(), target);
                        }
                    }
                }
            }
        }

        if (event.getEntity() instanceof Fireball fireball && event.getHitEntity() instanceof Player victim && fireball.getShooter() instanceof Player shooter) {
            event.setCancelled(false);
            plugin.crumbleKillTracker.put(
                    victim.getName(),
                    new CrumbleKillData(shooter.getName(), System.currentTimeMillis())
            );
        }

        if (event.getEntity() instanceof Snowball snowball && snowball.getShooter() instanceof Player shooter) {
            Block hitBlock = event.getHitBlock();
            if (hitBlock != null) {
                if (hitBlock.getType() == Material.SNOW_BLOCK) {
                    hitBlock.setType(Material.AIR);
                    for (Player target : Bukkit.getWorld("build").getPlayers()) {
                        if (target.equals(shooter)) continue;

                        Location feet = target.getLocation();
                        if (feet.getBlock().getRelative(BlockFace.DOWN).equals(hitBlock)) {
                            plugin.crumbleKillTracker.put(
                                    target.getName(),
                                    new CrumbleKillData(shooter.getName(), System.currentTimeMillis())
                            );
                        }
                    }
                }
            }
        }
    }

    private List<Material> getWoolColors() {
        return Arrays.asList(
                Material.RED_WOOL, Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.LIGHT_BLUE_WOOL, Material.LIME_WOOL,
                Material.YELLOW_WOOL, Material.PURPLE_WOOL, Material.BLACK_WOOL
        );
    }
}
