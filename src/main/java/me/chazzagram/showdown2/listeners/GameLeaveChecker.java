package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.PlayerInfoConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameLeaveChecker {

    private static Showdown2 plugin;

    public GameLeaveChecker(Showdown2 plugin) {
        this.plugin = plugin;
    }


    public void leftGame(String leftPlayer) {
        if(plugin.currentMode.equals("Zoomo Go")) {
            if(plugin.runningTimers.containsKey("zoomogo")) {
                if(plugin.ghostManager.getGhostPlayers().contains(leftPlayer)) return;
                if (PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(leftPlayer)) {
                    if (!plugin.deadPlayers.contains(leftPlayer)) {
                        plugin.zoomoLives.replace(leftPlayer, 0);
                        plugin.deadPlayers.add(leftPlayer);
                        plugin.ghostManager.addGhostPlayer(leftPlayer);
                        if (plugin.lastHitPlayer.containsKey(leftPlayer)) {
                            if (!plugin.lastHitPlayer.get(leftPlayer).isEmpty()) {
                                plugin.killRecord.add(plugin.getPlayerDisplayName(plugin.lastHitPlayer.get(leftPlayer)) + " §c⚔ " + plugin.getPlayerDisplayName(leftPlayer));
                                plugin.playerKillCount.merge(plugin.lastHitPlayer.get(leftPlayer), 1, Integer::sum);
                                if (PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(plugin.lastHitPlayer.get(leftPlayer))) {
                                    PlayerInfoConfig.get().set("players." + plugin.lastHitPlayer.get(leftPlayer) + ".kills", PlayerInfoConfig.get().getInt("players." + plugin.lastHitPlayer.get(leftPlayer) + ".kills") + 1);
                                    PlayerInfoConfig.save();
                                }
                            }
                        }

                        String attacker = plugin.lastHitPlayer.getOrDefault(leftPlayer, "");
                        boolean hasCredit = !attacker.isEmpty();

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            boolean isKiller = hasCredit && player.getName().equals(attacker);

                            if (!plugin.deadPlayers.contains(player.getName()) && !plugin.getSpectators().contains(player)) {
                                if (hasCredit) {
                                    if (isKiller) {
                                        plugin.messagePlayer(player, "§e\uD83D\uDCB016 §7| " + plugin.formatKillMessage(attacker, leftPlayer));
                                        plugin.earnPoints(player.getName(), 16, true);
                                    } else {
                                        plugin.messagePlayer(player, "§e\uD83D\uDCB03 §7| " + plugin.formatKillMessage(attacker, leftPlayer));
                                        plugin.earnPoints(player.getName(), 3, true);
                                    }
                                } else {
                                    plugin.messagePlayer(player, "§e\uD83D\uDCB03 §7| " + plugin.formatDeathMessage(leftPlayer));
                                    plugin.earnPoints(player.getName(), 3, true);
                                }
                            } else {
                                if (hasCredit) {
                                    plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(attacker, leftPlayer));
                                } else {
                                    plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatDeathMessage(leftPlayer));
                                }
                            }
                        }

                        boolean teamDead = true;
                        for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + leftPlayer + ".team") + ".players")) {
                            if (!plugin.deadPlayers.contains(player)) {
                                teamDead = false;
                                break;
                            }
                        }
                        if (teamDead) {
                            for (Player player2 : Bukkit.getOnlinePlayers()) {
                                plugin.messagePlayer(player2, "\n§c§l\uD83D\uDC80 §7| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + leftPlayer + ".team")) + " §chave been eliminated.\n§f");
                                player2.playSound(player2.getLocation(), Sound.ENTITY_ARMADILLO_PEEK, 1F, 1F);
                            }
                            plugin.deadTeams.add(PlayerConfig.get().getString("players." + leftPlayer + ".team"));
                        }

                        List<String> teamList = new ArrayList<>(List.of());
                        for (String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                            if (!TeamsConfig.get().getStringList("teams." + team + ".players").isEmpty()) {
                                teamList.add(team);
                            }
                        }
                        if (plugin.deadTeams.size() == teamList.size() - 1) {
                            for (Player p2 : plugin.getPlayers()) {
                                if (!plugin.ghostManager.getGhostPlayers().contains(p2.getName())) {
                                    plugin.winningTeam = PlayerConfig.get().getString("players." + p2.getName() + ".team");
                                    break;
                                }
                            }
                            if (Objects.equals(plugin.winningTeam, "")) {
                                plugin.winningTeam = "NO TEAM";
                            }
                            plugin.runningTimers.remove("zoomogo");
                            plugin.gameEnd();
                        }
                    }
                }
            }
        }


        if(plugin.currentMode.equals("Crumble Clash") && plugin.ccRoundStarted) {
            if(plugin.ghostManager.getGhostPlayers().contains(leftPlayer)) return;
            if (plugin.getPlayers().contains(Bukkit.getPlayer(leftPlayer))) {
                if (!plugin.deadPlayers.contains(leftPlayer)) {
                    plugin.deadPlayers.add(leftPlayer);
                    plugin.ghostManager.addGhostPlayer(leftPlayer);
                    CrumbleKillData data = plugin.crumbleKillTracker.get(leftPlayer);

                    if (data != null && System.currentTimeMillis() - data.time < 5000) {
                        Player killer = Bukkit.getPlayer(data.attacker);
                        if (killer != null && !killer.getName().equals(leftPlayer)) {
                            // Credit kill
                            String killerTeam = PlayerConfig.get().getString("players." + killer.getName() + ".team");
                            String victimTeam = PlayerConfig.get().getString("players." + leftPlayer + ".team");

                            if(!Objects.equals(killerTeam, victimTeam)) {
                                plugin.killRecord.add(plugin.getPlayerDisplayName(data.attacker) + " §c⚔ " + plugin.getPlayerDisplayName(leftPlayer));

                                plugin.playerKillCount.merge(data.attacker, 1, Integer::sum);
                                if (PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(data.attacker)) {
                                    PlayerInfoConfig.get().set("players." + data.attacker + ".kills", PlayerInfoConfig.get().getInt("players." + data.attacker + ".kills") + 1);
                                    PlayerInfoConfig.save();
                                }
                            }
                        }
                    }

                    plugin.crumbleKillTracker.remove(leftPlayer);

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        boolean creditValid = data != null && System.currentTimeMillis() - data.time < 5000;
                        Player killer = creditValid ? Bukkit.getPlayer(data.attacker) : null;

                        boolean sameTeam = false;
                        if (creditValid && killer != null) {
                            String killerTeam = PlayerConfig.get().getString("players." + data.attacker + ".team");
                            String victimTeam = PlayerConfig.get().getString("players." + leftPlayer + ".team");
                            sameTeam = killerTeam != null && killerTeam.equals(victimTeam);
                        }

                        boolean validKillCredit = creditValid && killer != null && !killer.getName().equals(leftPlayer) && !sameTeam;
                        boolean isKiller = validKillCredit && player.getName().equals(data.attacker);

                        if (!plugin.deadPlayers.contains(player.getName()) && !plugin.getSpectators().contains(player)) {
                            if (validKillCredit) {
                                if (isKiller) {
                                    plugin.messagePlayer(player, "§e\uD83D\uDCB016 §7| " + plugin.formatKillMessage(data.attacker, leftPlayer));
                                    plugin.earnPoints(player.getName(), 16, true);
                                } else {
                                    plugin.messagePlayer(player, "§e\uD83D\uDCB03 §7| " + plugin.formatKillMessage(data.attacker, leftPlayer));
                                    plugin.earnPoints(player.getName(), 3, true);
                                }
                            } else {
                                plugin.messagePlayer(player, "§e\uD83D\uDCB03 §7| " + plugin.formatDeathMessage(leftPlayer));
                                plugin.earnPoints(player.getName(), 3, true);
                            }
                        } else {
                            if (validKillCredit) {
                                plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(data.attacker, leftPlayer));
                            } else {
                                plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatDeathMessage(leftPlayer));
                            }
                        }
                    }

                    boolean teamDead = true;
                    for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + leftPlayer + ".team") + ".players")) {
                        if (!plugin.deadPlayers.contains(player)) {
                            teamDead = false;
                            break;
                        }
                    }
                    if (teamDead) {
                        for (Player player2 : Bukkit.getOnlinePlayers()) {
                            plugin.messagePlayer(player2, "\n§c§l\uD83D\uDC80 §7| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + leftPlayer + ".team")) + " §chave been eliminated.\n§f");
                            player2.playSound(player2.getLocation(), Sound.ENTITY_ARMADILLO_PEEK, 1F, 1F);
                        }
                        plugin.deadTeams.add(PlayerConfig.get().getString("players." + leftPlayer + ".team"));
                    }

                    List<String> teamList = new ArrayList<>(List.of());
                    for (String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                        if (!TeamsConfig.get().getStringList("teams." + team + ".players").isEmpty()) {
                            teamList.add(team);
                        }
                    }
                    if (plugin.deadTeams.size() == teamList.size() - 1) {
                        for (String team : teamList) {
                            if (!plugin.deadTeams.contains(team)) {
                                plugin.winningTeam = team;
                                break;
                            }
                        }
                        if (Objects.equals(plugin.winningTeam, "")) {
                            plugin.winningTeam = "NO TEAM";
                        }
                        plugin.runningTimers.remove("crumbleclash");
                        plugin.gameEnd();
                    }
                }
            }
        }
    }

    public void joinedGame(Player joinedPlayer) {
        if(!PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(joinedPlayer.getName())) return;
        switch (plugin.currentMode) {
            case "Bridge Builders":
                String team = PlayerConfig.get().getString("players." + joinedPlayer.getName() + ".team");
                switch(plugin.bbModes.get(team)) {
                    case "Build":
                        for(String teamPlayer : TeamsConfig.get().getStringList("teams." + team + ".players")){
                            if(teamPlayer.equals(team)) continue;
                            if(Bukkit.getServer().getPlayer(teamPlayer) != null) {
                                Player p = Bukkit.getServer().getPlayer(teamPlayer);
                                p.showPlayer(joinedPlayer);
                                joinedPlayer.showPlayer(p);
                            }
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                if (!joinedPlayer.isOnline()) return;

                                joinedPlayer.setGameMode(GameMode.SURVIVAL);
                                joinedPlayer.setAllowFlight(true);
                                joinedPlayer.setFlying(true);
                            }, 1L);
                        }
                        break;
                    case "Parkour":
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if (!joinedPlayer.isOnline()) return;

                            joinedPlayer.setGameMode(GameMode.ADVENTURE);
                            joinedPlayer.setAllowFlight(false);
                            joinedPlayer.setFlying(false);
                        }, 1L);

                        for (String player2 : TeamsConfig.get().getStringList("teams." + team + ".players")) {
                            if (Bukkit.getServer().getPlayer(player2) != null) {
                                Player p2 = Bukkit.getServer().getPlayer(player2);
                                if (joinedPlayer.getName().equals(p2.getName())) continue;
                                joinedPlayer.hidePlayer(p2);
                                p2.hidePlayer(joinedPlayer);
                            }
                        }
                        break;
                    case "End":
                        List<String> ghostPlayers = plugin.ghostManager.getGhostPlayers();
                        ItemStack compass = new ItemStack(Material.COMPASS);

                        ItemMeta meta = compass.getItemMeta();

                        meta.setDisplayName("§ePlayer Finder");

                        meta.addEnchant(Enchantment.UNBREAKING, 3, true);

                        compass.setItemMeta(meta);

                        joinedPlayer.setGameMode(GameMode.ADVENTURE);
                        for (Player p : plugin.getPlayers()) {
                            if (p != null) {
                                if(!ghostPlayers.contains(p.getName())) {
                                    p.hidePlayer(plugin, joinedPlayer);
                                }
                            }
                        }
                        for(String player2 : ghostPlayers){
                            Player p = Bukkit.getPlayer(player2);
                            if(p != null) {
                                p.hidePlayer(plugin, joinedPlayer);
                            }
                        }
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if (!joinedPlayer.isOnline()) return;

                            joinedPlayer.setGameMode(GameMode.ADVENTURE);
                            joinedPlayer.setAllowFlight(false);
                            joinedPlayer.setFlying(false);
                            joinedPlayer.getInventory().clear();
                            joinedPlayer.getInventory().addItem(compass);
                        }, 1L);
                        break;
                    default:
                        plugin.messageConsole("Player " + joinedPlayer.getName() + " has joined bridge builders! Nothing bad happened.");
                        break;
                }

                break;
            case "Zoomo Go":
            case "Crumble Clash":
                if(plugin.runningTimers.containsKey("zoomogo") || plugin.ccRoundStarted) {
                    List<String> ghostPlayers = plugin.ghostManager.getGhostPlayers();
                    if (plugin.deadPlayers.contains(joinedPlayer.getName())
                            || ghostPlayers.contains(joinedPlayer.getName())) {

                        ItemStack compass = new ItemStack(Material.COMPASS);

                        ItemMeta meta = compass.getItemMeta();

                        meta.setDisplayName("§ePlayer Finder");

                        meta.addEnchant(Enchantment.UNBREAKING, 3, true);

                        compass.setItemMeta(meta);

                        joinedPlayer.setGameMode(GameMode.ADVENTURE);
                        for (Player p : plugin.getPlayers()) {
                            if (p != null) {
                                if (!ghostPlayers.contains(p.getName())) {
                                    p.hidePlayer(plugin, joinedPlayer);
                                }
                            }
                        }
                        for (String player2 : ghostPlayers) {
                            Player p = Bukkit.getPlayer(player2);
                            if (p != null) {
                                p.hidePlayer(plugin, joinedPlayer);
                            }
                        }
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if (!joinedPlayer.isOnline()) return;

                            joinedPlayer.setGameMode(GameMode.ADVENTURE);
                            joinedPlayer.setAllowFlight(false);
                            joinedPlayer.setFlying(false);
                            joinedPlayer.getInventory().clear();
                            joinedPlayer.getInventory().addItem(compass);
                        }, 1L);
                    }
                    break;
                }
            case "Lobby":
                for (String player2 : plugin.ghostManager.getGhostPlayers()) {
                    Player p = Bukkit.getPlayer(player2);
                    if (p != null) {
                        p.showPlayer(plugin, joinedPlayer);
                        joinedPlayer.showPlayer(plugin, p);
                    }
                }
                joinedPlayer.setAllowFlight(true);
                joinedPlayer.setFlying(true);
                ItemStack compass = new ItemStack(Material.COMPASS);

                ItemMeta meta = compass.getItemMeta();

                meta.setDisplayName("§ePlayer Finder");

                meta.addEnchant(Enchantment.UNBREAKING, 3, true);

                compass.setItemMeta(meta);

                joinedPlayer.getInventory().removeItem(compass);
                break;
            default:
                break;
        }
    }

    public void leftGamePrior(String leftPlayer) {
        if(!PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(leftPlayer)) return;
        if(plugin.currentMode.equals("Zoomo Go")) {
            if (!plugin.deadPlayers.contains(leftPlayer)) {
                plugin.zoomoLives.replace(leftPlayer, 0);
                plugin.deadPlayers.add(leftPlayer);
                plugin.ghostManager.addGhostPlayer(leftPlayer);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!plugin.deadPlayers.contains(player.getName()) && !plugin.getSpectators().contains(player)) {
                        plugin.messagePlayer(player, "§e\uD83D\uDCB03 §7| " + plugin.formatDeathMessage(leftPlayer));
                        plugin.earnPoints(player.getName(), 3, true);
                    } else {
                        plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatDeathMessage(leftPlayer));
                    }
                }

                boolean teamDead = true;
                for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + leftPlayer + ".team") + ".players")) {
                    if (!plugin.deadPlayers.contains(player)) {
                        teamDead = false;
                        break;
                    }
                }
                if (teamDead) {
                    for (Player player2 : Bukkit.getOnlinePlayers()) {
                        plugin.messagePlayer(player2, "\n§c§l\uD83D\uDC80 §7| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + leftPlayer + ".team")) + " §chave been eliminated.\n§f");
                        player2.playSound(player2.getLocation(), Sound.ENTITY_ARMADILLO_PEEK, 1F, 1F);
                    }
                    plugin.deadTeams.add(PlayerConfig.get().getString("players." + leftPlayer + ".team"));
                }

                List<String> teamList = new ArrayList<>(List.of());
                for (String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                    if (!TeamsConfig.get().getStringList("teams." + team + ".players").isEmpty()) {
                        teamList.add(team);
                    }
                }
                if (plugin.deadTeams.size() == teamList.size() - 1) {
                    for (Player p2 : plugin.getPlayers()) {
                        if (!plugin.ghostManager.getGhostPlayers().contains(p2.getName())) {
                            plugin.winningTeam = PlayerConfig.get().getString("players." + p2.getName() + ".team");
                            break;
                        }
                    }
                    if (Objects.equals(plugin.winningTeam, "")) {
                        plugin.winningTeam = "NO TEAM";
                    }
                    plugin.runningTimers.remove("zoomogo");
                    plugin.gameEnd();
                }
            }
        }
        
        
        if(plugin.currentMode.equals("Crumble Clash")) {
            plugin.deadPlayers.add(leftPlayer);
            plugin.ghostManager.addGhostPlayer(leftPlayer);

            plugin.crumbleKillTracker.remove(leftPlayer);

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!plugin.deadPlayers.contains(player.getName()) && !plugin.getSpectators().contains(player)) {
                    plugin.messagePlayer(player, "§e\uD83D\uDCB03 §7| " + plugin.formatDeathMessage(leftPlayer));
                    plugin.earnPoints(player.getName(), 3, true);
                } else {
                    plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatDeathMessage(leftPlayer));
                }
            }

            boolean teamDead = true;
            for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + leftPlayer + ".team") + ".players")) {
                if (!plugin.deadPlayers.contains(player)) {
                    teamDead = false;
                    break;
                }
            }
            if (teamDead) {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    plugin.messagePlayer(player2, "\n§c§l\uD83D\uDC80 §7| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + leftPlayer + ".team")) + " §chave been eliminated.\n§f");
                    player2.playSound(player2.getLocation(), Sound.ENTITY_ARMADILLO_PEEK, 1F, 1F);
                }
                plugin.deadTeams.add(PlayerConfig.get().getString("players." + leftPlayer + ".team"));
            }

            List<String> teamList = new ArrayList<>(List.of());
            for (String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                if (!TeamsConfig.get().getStringList("teams." + team + ".players").isEmpty()) {
                    teamList.add(team);
                }
            }
            if (plugin.deadTeams.size() == teamList.size() - 1) {
                for (String team : teamList) {
                    if (!plugin.deadTeams.contains(team)) {
                        plugin.winningTeam = team;
                        break;
                    }
                }
                if (Objects.equals(plugin.winningTeam, "")) {
                    plugin.winningTeam = "NO TEAM";
                }
                plugin.runningTimers.remove("crumbleclash");
                plugin.gameEnd();
            }
        }
    }

}
