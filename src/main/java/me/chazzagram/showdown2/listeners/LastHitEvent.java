package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import me.chazzagram.showdown2.files.TeleportConfig;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class LastHitEvent implements Listener {

    private static Showdown2 plugin;

    public LastHitEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {
        if (plugin.currentMode.equals("Zoomo Go")) {
            if (e.getDamager() instanceof Player killer && e.getEntity() instanceof Player victim) {
                plugin.lastHitPlayer.put(victim.getName(), killer.getName());
            }
        } else {
            if(plugin.pvpEnabled) {
                if (e.getDamager() instanceof Player killer && e.getEntity() instanceof Player victim) {
                    handlePlayerDamage(killer, victim, e);
                } else if (e.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player killer) {
                    if (e.getEntity() instanceof Player victim) {
                        handlePlayerDamage(killer, victim, e);
                    }
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    public void handlePlayerDamage(Player killer, Player victim, EntityDamageByEntityEvent e) {
        if(plugin.currentMode.equals("Gub Game")) {
            if (victim.getHealth() - e.getFinalDamage() <= 0) {
                for (Player p : plugin.getPlayers()) {
                    plugin.messagePlayer(p, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(killer.getName(), victim.getName()));
                }
                plugin.messagePlayer(victim, "§c\uD83D\uDC80 §7| §cYou died to " + plugin.getPlayerDisplayName(killer.getName()));
                e.setCancelled(true);
                victim.setGameMode(GameMode.SPECTATOR);
                plugin.gubGameKills.put(killer.getName(), plugin.gubGameKills.get(killer.getName()) + 1);
                plugin.earnPoints(killer.getName(), 40 - plugin.gubKitKills.get(plugin.gubGameKills.get(killer.getName())), true);
                plugin.gubKitKills.put(plugin.gubGameKills.get(killer.getName()), plugin.gubKitKills.get(plugin.gubGameKills.get(killer.getName())) + 1);
                victim.setHealth(20);
                if (plugin.gubGameKills.get(killer.getName()).equals(15)) {
                    killer.setGameMode(GameMode.SPECTATOR);
                    killer.sendTitle("§eFINISH", "§e\uD83D\uDCB0" + (41 - plugin.gubKitKills.get(plugin.gubGameKills.get(killer.getName()))) + " §7| §c\uD83D\uDC80 " + plugin.getPlayerDisplayName(victim.getName()), 0, 20, 0);
                } else {
                    killer.sendTitle("", "§e\uD83D\uDCB0" + (41 - plugin.gubKitKills.get(plugin.gubGameKills.get(killer.getName()))) + " §7| §c\uD83D\uDC80 " + plugin.getPlayerDisplayName(victim.getName()), 0, 20, 0);
                    killer.getInventory().clear();
                    plugin.messagePlayer(killer, "§c\uD83D\uDC80 §7| NEXT KIT! (§e§l" + plugin.gubGameKills.get(killer.getName()) + "/15§7)");
                    for (ItemStack item : getGubKits().get(plugin.gubGameKills.get(killer.getName()))) {
                        killer.getInventory().addItem(item);
                    }
                }
                BukkitTask task = new BukkitRunnable() {
                    int timeLeft = 6;
                    @Override
                    public void run() {
                        if(plugin.runningTimers.containsKey(victim.getName()+"respawn")) {
                            if (!plugin.pausedTimers.contains(victim.getName()+"respawn")) {
                                plugin.runningTimers.get(victim.getName()+"respawn").setValue(timeLeft);
                                timeLeft--;
                                if (timeLeft == 0) {
                                    plugin.messageConsole("Timer finished.");
                                    victim.setGameMode(GameMode.ADVENTURE);
                                    victim.teleport(TeleportConfig.get().getLocation("teams." + PlayerConfig.get().getString("players." + victim.getName() + ".team") + ".gubgame"));
                                    plugin.runningTimers.remove(victim.getName()+"respawn");
                                    cancel();
                                } else {

                                    plugin.messageConsole(timeLeft + " seconds left..");
                                }
                            }
                        } else {
                            plugin.messageConsole("Timer removed by external factor.");
                            cancel();
                        }
                    }

                }.runTaskTimer(plugin, 0L, 20L);

                plugin.runningTimers.put(victim.getName()+"respawn", new AbstractMap.SimpleEntry<>(task, 6));
            }
        }
        if(plugin.currentMode.equals("Survival Games")){
            if (victim.getHealth() - e.getFinalDamage() <= 0) {
                plugin.messagePlayer(victim, "§c\uD83D\uDC80 §7| §cYou died to " + plugin.getPlayerDisplayName(killer.getName()));
                victim.sendTitle("§c§lYOU DIED.", "", 0, 40, 10);
                e.setCancelled(true);
                victim.setGameMode(GameMode.SPECTATOR);
                plugin.deadPlayers.add(victim.getName());
                plugin.earnPoints(killer.getName(), 30, true);
                killer.sendTitle("", "§e\uD83D\uDCB030" + " §7| §c\uD83D\uDC80 " + plugin.getPlayerDisplayName(victim.getName()), 0, 20, 0);
                for (Player p : plugin.getPlayers()) {
                    if(!p.getGameMode().equals(GameMode.SPECTATOR) && p.getName().equals(killer.getName())) {
                        plugin.messagePlayer(p, "§e\uD83D\uDCB05 §7| " + plugin.formatKillMessage(killer.getName(), victim.getName()));
                        plugin.earnPoints(p.getName(), 5, true);
                    } else {
                        plugin.messagePlayer(p, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(killer.getName(), victim.getName()));
                    }
                }
                switch(plugin.deadPlayers.size()){
                    case 8:
                        Bukkit.getWorld("world").getWorldBorder().setSize(100, 60);
                        for(Player p : plugin.getPlayers()){
                            p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                        }
                        break;
                    case 16:
                        Bukkit.getWorld("world").getWorldBorder().setSize(80, 60);
                        for(Player p : plugin.getPlayers()){
                            p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                        }
                        break;
                    case 24:
                        Bukkit.getWorld("world").getWorldBorder().setSize(60, 60);
                        for(Player p : plugin.getPlayers()){
                            p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                        }
                        break;
                    case 32:
                        Bukkit.getWorld("world").getWorldBorder().setSize(40, 60);
                        for(Player p : plugin.getPlayers()){
                            p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                        }
                        break;
                    default:
                        break;
                }
                boolean teamDead = true;
                for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + victim.getName() + ".team") + ".players")) {
                    if (!plugin.deadPlayers.contains(player)) {
                        teamDead = false;
                        break;
                    }
                }

                if (teamDead) {
                    for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
                        plugin.messagePlayer(player2, "\n§c§l\uD83D\uDC80 §7| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + victim.getName() + ".team")) + " §chave been eliminated.\n§f");
                    }
                    plugin.deadTeams.add(PlayerConfig.get().getString("players." + victim.getName() + ".team"));
                }

                Set<String> teamList = new HashSet<>();

                for (Player player : plugin.getPlayers()) {
                    String team = PlayerConfig.get().getString("players." + player.getName() + ".team");
                    if (team != null && !team.isEmpty()) {
                        teamList.add(team);
                    }
                }

                if (plugin.deadTeams.size() == teamList.size() - 1) {
                    plugin.deadTeams.clear();
                    plugin.runningTimers.remove("survivalgames");
                    plugin.gameEnd();
                }

            }
        }
    }

    public ArrayList<ItemStack[]> getGubKits() {
        ArrayList<ItemStack[]> kits = new ArrayList<>();

        ItemStack[] kit1 = new ItemStack[1];
        kit1[0] = new ItemStack(Material.NETHERITE_SWORD);
        kits.add(kit1);

        ItemStack[] kit2 = new ItemStack[1];
        kit2[0] = new ItemStack(Material.DIAMOND_SWORD);
        kits.add(kit2);

        ItemStack[] kit3 = new ItemStack[1];
        kit3[0] = new ItemStack(Material.NETHERITE_AXE);
        kits.add(kit3);

        ItemStack[] kit4 = new ItemStack[1];
        kit4[0] = new ItemStack(Material.TRIDENT);
        kits.add(kit4);

        ItemStack[] kit5 = new ItemStack[2];
        kit5[0] = new ItemStack(Material.CROSSBOW);
        kit5[1] = new ItemStack(Material.ARROW);
        kits.add(kit5);

        ItemStack[] kit6 = new ItemStack[1];
        kit6[0] = new ItemStack(Material.DIAMOND_AXE);
        kits.add(kit6);

        ItemStack[] kit7 = new ItemStack[2];
        kit7[0] = new ItemStack(Material.BOW);
        kit7[1] = new ItemStack(Material.ARROW);
        kits.add(kit7);

        ItemStack[] kit8 = new ItemStack[1];
        kit8[0] = new ItemStack(Material.IRON_SWORD);
        kits.add(kit8);

        ItemStack[] kit9 = new ItemStack[1];
        kit9[0] = new ItemStack(Material.STONE_SWORD);
        kits.add(kit9);

        ItemStack[] kit10 = new ItemStack[1];
        kit10[0] = new ItemStack(Material.DIAMOND_SHOVEL);
        kits.add(kit10);

        ItemStack[] kit11 = new ItemStack[1];
        kit11[0] = new ItemStack(Material.IRON_PICKAXE);
        kits.add(kit11);

        ItemStack[] kit12 = new ItemStack[1];
        kit12[0] = new ItemStack(Material.STONE_SHOVEL);
        kits.add(kit12);

        ItemStack[] kit13 = new ItemStack[1];
        kit13[0] = new ItemStack(Material.NETHERITE_HOE);
        kits.add(kit13);

        ItemStack[] kit14 = new ItemStack[1];
        kit14[0] = new ItemStack(Material.WOODEN_PICKAXE);
        kits.add(kit14);

        ItemStack[] kit15 = new ItemStack[1];
        kit15[0] = new ItemStack(Material.AIR);
        kits.add(kit15);

        for(ItemStack[] kit : kits){
            for(ItemStack item : kit){
                if(item.getType() != Material.AIR) {
                    ItemMeta meta = item.getItemMeta();
                    meta.setUnbreakable(true);
                    item.setItemMeta(meta);
                }
            }
        }

        return kits;
    }
}
