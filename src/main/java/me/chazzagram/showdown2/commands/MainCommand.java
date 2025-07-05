package me.chazzagram.showdown2.commands;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainCommand implements CommandExecutor {

    private final Showdown2 plugin;

    public MainCommand(Showdown2 plugin) {
        this.plugin = plugin;
    }

    Location safeSpace = new Location(Bukkit.getServer().getWorld("build"), -71, 159, 581);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

    if (commandSender instanceof ConsoleCommandSender) {
        if (args.length == 0) {
            plugin.messageConsole("Invalid command.");
        } else {
            switch (args[0].toLowerCase()) {
                case "cdcp":
                    if (args.length > 2) {
                        if(plugin.runningTimers.containsKey("colourdash")) {
                            if (!plugin.colourDashCheckpoints.containsKey(args[2])) {
                                plugin.colourDashCheckpoints.put(args[2], 0);
                            }
                            if (plugin.colourDashCheckpoints.get(args[2]) < Integer.parseInt(args[1])) {
                                int placement = 1;
                                for (Integer checkpoint : plugin.colourDashCheckpoints.values()) {
                                    if (checkpoint == Integer.parseInt(args[1])) {
                                        placement++;
                                    }
                                }
                                int pointsEarned = 41 - placement;
                                plugin.earnPoints(args[2], pointsEarned, true);
                                Player p = Bukkit.getServer().getPlayer(args[2]);
                                p.sendTitle("§a[✔] \uD83C\uDFC3-" + args[1], "§8[§f§l⏱§8] §e§o" + plugin.getTimer("colourdashwatch"), 0, 100, 5);
                                plugin.messagePlayer(p, "§a[\uD83C\uDFC3-" + args[1] + "] Dashpoint reached!");
                                plugin.colourDashCheckpoints.put(args[2], Integer.parseInt(args[1]));
                                for (Player player : plugin.getPlayers()) {
                                    plugin.messagePlayer(player, "§a§l⏱ §8| " + plugin.getPlayerDisplayName(p.getName()) + "§7 has reached §a\uD83C\uDFC3-" + args[1] + "§7!");
                                }

                            }
                        }
                    }

                    break;
                case "cdfinish":
                    if (args.length > 1) {
                        if(plugin.runningTimers.containsKey("colourdash")) {
                            if (plugin.colourDashCheckpoints.get(args[1]) < 10) {
                                plugin.cdCompletions++;
                                int placement = 1;
                                for (Integer checkpoint : plugin.colourDashCheckpoints.values()) {
                                    if (checkpoint == 10) {
                                        placement++;
                                    }
                                }
                                int pointsEarned = 102 - (2 * placement);
                                plugin.earnPoints(args[1], pointsEarned, true);

                                Player p = Bukkit.getServer().getPlayer(args[1]);
                                String team = PlayerConfig.get().getString("players." + p.getName() + ".team");
                                plugin.modeCompletions.put(team, (plugin.modeCompletions.get(team) + 1));
                                p.sendTitle("§aFINISH", "§8[§f§l⏱§8] §e§o" + plugin.getTimer("colourdashwatch"), 0, 100, 5);
                                plugin.messagePlayer(p, "\n§e\uD83D\uDCB0" + pointsEarned + " §8| §a§lCourse Completed!");
                                plugin.messagePlayer(p, "§f§l⏱ §8| §fTime Taken: §e" + plugin.getTimer("colourdashwatch") + "\n");
                                plugin.colourDashCheckpoints.put(args[1], 10);
                                p.setGameMode(GameMode.SPECTATOR);
                                for (Player player : plugin.getPlayers()) {
                                    plugin.messagePlayer(player, "§f\uD83D\uDC51 §8| " + plugin.getPlayerDisplayName(p.getName()) + "§e was §f§l#" + placement + " §eto finish!");
                                }

                                if(plugin.cdCompletions == PlayerConfig.get().getConfigurationSection("players").getKeys(false).size()) {
                                    plugin.runningTimers.remove("colourdash");
                                    plugin.runningTimers.remove("colourdashwatch");
                                    plugin.gameEnd();
                                }
                            }
                        }
                    }
                    break;
                case "bbtp":
                    if(args.length > 2){
                        if(plugin.runningTimers.containsKey("bridgebuilders")) {
                            String teamName = PlayerConfig.get().getString("players." + args[2] + ".team");
                            if(teamName != null){
                                int zOffset = 38 * (Integer.parseInt(args[1])-1);
                                int xOffset = 35;
                                double x = 247.5;
                                int y = -21;
                                double z = 661.5;
                                switch(teamName){
                                    case "RubyRaiders": xOffset *= 0; break;
                                    case "AmberAmbushers": break;
                                    case "TopazTroopers": xOffset *= 2; break;
                                    case "KyaniteKillers": xOffset *= 3; break;
                                    case "DiamondDestroyers": xOffset *= 4; break;
                                    case "SapphireSoldiers": xOffset *= 5; break;
                                    case "SmithsoniteSlayers": xOffset *= 6; break;
                                    case "CrystalCrashers": xOffset *= 7; break;
                                    default:
                                        break;
                                }
                                if(Bukkit.getPlayer(args[2]) != null){
                                    Player p = Bukkit.getPlayer(args[2]);
                                    Location tpLoc = new Location(Bukkit.getWorld("build"), (x + xOffset), y, (z - zOffset), 180, 0);
                                    p.teleport(tpLoc);
                                    plugin.messagePlayer(p, "§7§oYou fell! Teleporting you back..");
                                }
                            }
                        }
                    }
                    break;
                case "bbjcp":
                    if (args.length > 2) {
                        if(plugin.runningTimers.containsKey("bridgebuilders")) {
                            if (!plugin.bridgeJumpRegister.get(Integer.parseInt(args[1])).contains(args[2])) {
                                plugin.bridgeJumpRegister.get(Integer.parseInt(args[1])).add(args[2]);
                                plugin.summonFirework(Bukkit.getPlayer(args[2]).getLocation(), PlayerConfig.get().getString("players." + args[2] + ".team"));
                                int register = 0;
                                for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[2] + ".team") + ".players")) {
                                    if (plugin.bridgeJumpRegister.get(Integer.parseInt(args[1])).contains(player)) {
                                        register++;
                                        if (register == TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[2] + ".team") + ".players").size()) {
                                            plugin.bridgeTally.put(PlayerConfig.get().getString("players." + args[2] + ".team"), plugin.bridgeTally.get(PlayerConfig.get().getString("players." + args[2] + ".team")) + 1);
                                            plugin.runningTimers.remove(PlayerConfig.get().getString("players." + args[2] + ".team") + args[1]);
                                            plugin.buildTimeStamps.put(args[2], plugin.runningTimers.get("bridgebuilders").getValue());
                                            Integer placement = plugin.bridgeJumpCheckpoints.get(Integer.parseInt(args[1]));
                                            if (placement == 1) {
                                                for (Player p : plugin.getPlayers()) {
                                                    plugin.messagePlayer(p, "§e§l⏱ §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[2] + ".team")) + "§7 crossed §a\uD83C\uDF09-" + args[1] + "§7!");
                                                }
                                            } else {
                                                for (Player p : plugin.getPlayers()) {
                                                    plugin.messagePlayer(p, "§f§l⏱ §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[2] + ".team")) + "§7 crossed §a\uD83C\uDF09-" + args[1] + "§7!");
                                                }
                                            }

                                            List<Material> blocks = plugin.getBridgeBlocks(Integer.parseInt(args[1]), PlayerConfig.get().getString("players." + args[2] + ".team"));

                                            for (String player2 : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[2] + ".team") + ".players")) {
                                                if (Bukkit.getServer().getPlayer(player2) != null) {
                                                    Player p = Bukkit.getServer().getPlayer(player2);
                                                    plugin.earnPoints(player2, 5, true);
                                                    plugin.messagePlayer(p, "§e\uD83D\uDCB05 §7| §eYour entire team completed the jump! Bonus points awarded.");
                                                    p.sendTitle("§a[✔] \uD83C\uDF09-" + args[1], "§7Now build!", 0, 40, 0);
                                                    plugin.messagePlayer(p, "§a[\uD83D\uDDFB-" + args[1] + "] Jump Complete!");
                                                    plugin.messagePlayer(p, "§cBuild mode attained, get building!");
                                                    p.setGameMode(GameMode.SURVIVAL);
                                                    p.setAllowFlight(true);
                                                    Location teleportLoc = Bukkit.getServer().getPlayer(args[2]).getLocation().clone().subtract(0, 0, 2);
                                                    teleportLoc.setYaw(180);
                                                    p.teleport(teleportLoc);

                                                    for(Material block : blocks) {
                                                        p.getInventory().addItem(new ItemStack(block, 64));
                                                    }
                                                }
                                            }
                                            String team = PlayerConfig.get().getString("players." + args[2] + ".team");
                                            for(int x = plugin.teamJump.get(team)[0]-3; x <= plugin.teamJump.get(team)[0] + 3; x++){
                                                for(int y = plugin.teamJump.get(team)[1]-2; y <= plugin.teamJump.get(team)[1]+7; y++){
                                                    Bukkit.getWorld("build").getBlockAt(x, y, (plugin.teamJump.get(team)[2]-(38*(Integer.valueOf(args[1])-1)))-16).setType(Material.BARRIER);
                                                }
                                            }
                                            plugin.bridgeJumpCheckpoints.replace(Integer.parseInt(args[1]), placement + 1);
                                        }
                                    }
                                }
                                plugin.earnPoints(args[2], 20, true);
                                if (Bukkit.getServer().getPlayer(args[2]) != null) {
                                    Player p2 = Bukkit.getServer().getPlayer(args[2]);
                                    plugin.messagePlayer(p2, "§e\uD83D\uDCB020 §7| §eYou have completed this jump!");
                                }
                                StringBuilder playerCompletions = new StringBuilder();
                                playerCompletions.append("§a✔ ".repeat(register));
                                for (String player2 : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[2] + ".team") + ".players")) {
                                    if (Bukkit.getServer().getPlayer(player2) != null) {
                                        Player p = Bukkit.getServer().getPlayer(player2);
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f + (0.2f * register));
                                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§eCompletions§f: " + playerCompletions));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "bbfinish":
                    if (args.length > 1) {
                        if(plugin.runningTimers.containsKey("bridgebuilders")) {
                            if (!plugin.bridgeJumpRegister.get(plugin.bridgeJumpRegister.size()).contains(args[1])) {
                                plugin.bridgeJumpRegister.get(plugin.bridgeJumpRegister.size()).add(args[1]);
                                plugin.summonFirework(Bukkit.getPlayer(args[1]).getLocation(), PlayerConfig.get().getString("players." + args[1] + ".team"));
                                int register = 0;
                                for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players")) {
                                    if (plugin.bridgeJumpRegister.get(plugin.bridgeJumpRegister.size()).contains(player)) {
                                        register++;
                                        if (register == TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players").size()) {
                                            plugin.runningTimers.remove(PlayerConfig.get().getString("players." + args[2] + ".team") + "6");
                                            plugin.bridgeTally.put(PlayerConfig.get().getString("players." + args[2] + ".team"), plugin.bridgeTally.get(PlayerConfig.get().getString("players." + args[2] + ".team")) + 1);
                                            Integer placement = plugin.bridgeCheckpoints.get(plugin.bridgeCheckpoints.size());
                                            switch (placement) {
                                                case 1:
                                                    for (Player p : plugin.getPlayers()) {
                                                        plugin.messagePlayer(p, "§8| §e\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + "§e was §e§l1st §eto finish!");
                                                    }
                                                    break;
                                                case 2:
                                                    for (Player p : plugin.getPlayers()) {
                                                        plugin.messagePlayer(p, "§8| §7\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + "§e was §7§l2nd §eto finish!");
                                                    }
                                                    break;
                                                case 3:
                                                    for (Player p : plugin.getPlayers()) {
                                                        plugin.messagePlayer(p, "§8| §6\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + "§e was §6§l3rd §eto finish!");
                                                    }
                                                    break;
                                                default:
                                                    for (Player p : plugin.getPlayers()) {
                                                        plugin.messagePlayer(p, "§8| §f\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + "§e was §f§l" + placement + "th §eto finish!");
                                                    }
                                                    break;
                                            }


                                            for (String player2 : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players")) {
                                                if (Bukkit.getServer().getPlayer(player2) != null) {
                                                    Player p = Bukkit.getServer().getPlayer(player2);
                                                    plugin.earnPoints(player2, 5, true);
                                                    plugin.messagePlayer(p, "§e\uD83D\uDCB05 §7| §eYour entire team completed the jump! Bonus points awarded.");
                                                    p.sendTitle("§aFINISH", "", 0, 100, 5);
                                                    plugin.messagePlayer(p, "§e\uD83D\uDCB030 §8| §a§lCourse Completed!");
                                                    p.setAllowFlight(true);
                                                    p.setGameMode(GameMode.SPECTATOR);
                                                }
                                            }
                                            plugin.bridgeCheckpoints.replace(plugin.bridgeCheckpoints.size(), placement + 1);

                                        }
                                    }
                                }
                                plugin.earnPoints(args[2], 30, true);
                                if (Bukkit.getServer().getPlayer(args[2]) != null) {
                                    Player p2 = Bukkit.getServer().getPlayer(args[2]);
                                    plugin.messagePlayer(p2, "§e\uD83D\uDCB020 §7| §eYou have completed this jump!");
                                }

                                StringBuilder playerCompletions = new StringBuilder();
                                playerCompletions.append("§a✔ ".repeat(register));
                                for (String player2 : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players")) {
                                    if (Bukkit.getServer().getPlayer(player2) != null) {
                                        Player p = Bukkit.getServer().getPlayer(player2);
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f + (0.2f * register));
                                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§eCompletions§f: " + playerCompletions));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "zoomodeath":
                    if(plugin.runningTimers.containsKey("zoomogo")) {
                        if (!plugin.deadPlayers.contains(args[1])) {
                            plugin.deadPlayers.add(args[1]);
                            Player p = Bukkit.getServer().getPlayer(args[1]);
                            plugin.messagePlayer(p, "§c\uD83D\uDC80 §7| You died.");
                            p.setGameMode(GameMode.SPECTATOR);
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                p.setAllowFlight(true);
                                p.setFlying(true);
                            }, 1L);
                            if (plugin.lastHitPlayer.containsKey(args[1])) {
                                if (!plugin.lastHitPlayer.get(args[1]).isEmpty()) {
                                    plugin.earnPoints(plugin.lastHitPlayer.get(args[1]), 20, true);
                                }
                            }

                            for (Player player : plugin.getPlayers()) {
                                if (!plugin.deadPlayers.contains(player.getName())) {
                                    if (plugin.lastHitPlayer.containsKey(args[1])) {
                                        if (!plugin.lastHitPlayer.get(args[1]).isEmpty()) {
                                            plugin.messagePlayer(player, "§e\uD83D\uDCB05 §7| " + plugin.formatKillMessage(plugin.lastHitPlayer.get(args[1]), p.getName()));
                                        } else {
                                            plugin.messagePlayer(player, "§e\uD83D\uDCB05 §7| " + plugin.formatDeathMessage(p.getName()));
                                        }
                                    }
                                    plugin.earnPoints(player.getName(), 5, true);
                                } else {
                                    if (plugin.lastHitPlayer.containsKey(args[1])) {
                                        if (!plugin.lastHitPlayer.get(args[1]).isEmpty()) {
                                            plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(plugin.lastHitPlayer.get(args[1]), p.getName()));
                                        } else {
                                            plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatDeathMessage(p.getName()));
                                        }
                                    }
                                }
                            }

                            boolean teamDead = true;
                            for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players")) {
                                if (!plugin.deadPlayers.contains(player)) {
                                    teamDead = false;
                                    break;
                                }
                            }
                            if (teamDead) {
                                for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
                                    plugin.messagePlayer(player2, "\n§c§l\uD83D\uDC80 §7| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + " §chave been eliminated.\n§f");
                                }
                                plugin.deadTeams.add(PlayerConfig.get().getString("players." + args[1] + ".team"));
                            }

                            List<String> teamList = new ArrayList<>(List.of());
                            for (Player player : plugin.getPlayers()) {
                                if (!teamList.contains(PlayerConfig.get().getString("players." + player.getName() + ".team"))) {
                                    teamList.add(PlayerConfig.get().getString("players." + player.getName() + ".team"));
                                }
                            }
                            if (plugin.deadTeams.size() == teamList.size() - 1) {
                                plugin.deadTeams.clear();
                                plugin.runningTimers.remove("zoomogo");
                                plugin.gameEnd();
                            }
                        }
                    } else if (plugin.runningTimers.containsKey("zoomogostart")) {
                        Bukkit.getPlayer(args[1]).teleport(safeSpace);
                        plugin.messagePlayer(Bukkit.getPlayer(args[1]), "§7[§e!§7] §eYou cannot die yet! You've been saved! But grace period will end when the game starts.");
                    }
                default:
                    break;
            }
        }
    } else if (commandSender instanceof Player p) {
        if (args.length == 0) {
            plugin.messagePlayer(p, "Missing Args.");

        } else {
            switch (args[0].toLowerCase()) {
                case "help":
                    p.sendMessage("""
                            §6All Showdown Commands:§f
                            /mcevent createteam <teamname>
                            /mcevent delteam <teamname>
                            /mcevent jointeam <teamname> <player>
                            /mcevent leaveteam <player>
                            /mcevent starttimer <seconds> <timer>
                            /mcevent startstopwatch <seconds> <stopwatch>
                            /mcevent stoptimer <timer>
                            /mcevent test
                            /mcevent settp <players/spectators/teamname> <location-name>
                            /mcevent deltp <players/spectators/teamname> <location-name>
                            /mcevent tpp <location-name>
                            /mcevent tpt <location-name>
                            /mcevent slimefinishers
                            /mcevent startslimegolf
                            /mcevent startcolourdash
                            /mcevent startbridgebuilders
                            /mcevent startcraftalot
                            /mcevent countvotes
                            /mcevent readycheck
                            /mcevent pause
                            /mcevent unpause
                            /mcevent whitelist
                            /mcevent unwhitelist
                            """);
                    break;
                case "startevent":
                    plugin.startEvent();
                    break;
                case "togglepvp":
                    plugin.pvpEnabled = !plugin.pvpEnabled;
                    plugin.messagePlayer(p, "PVP Enabled: " + plugin.pvpEnabled);
                    break;
                case "boss":
                    if(args.length > 1){
                        plugin.bossBarBgTest();
                    }
                    break;
                case "border":
                    if(args.length > 1){
                        plugin.newBorderRadius = Integer.parseInt(args[1]);
                    }
                    break;
                case "offglow":
                    for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                        player.setGlowing(false);
                    }
                    break;
                case "whitelist":
                    for(String key : PlayerConfig.get().getConfigurationSection("players").getKeys(false)){
                        Bukkit.getOfflinePlayer(key).setWhitelisted(true);
                    }
                    plugin.messagePlayer(p, "§ePlayers in player config have been whitelisted.");
                    break;
                case "unwhitelist":
                    for(String key : PlayerConfig.get().getConfigurationSection("players").getKeys(false)){
                        Bukkit.getOfflinePlayer(key).setWhitelisted(false);
                    }
                    plugin.messagePlayer(p, "§ePlayers in player config have been unwhitelisted.");
                    break;
                case "teams":
                    plugin.updateTeamGUI();
                    ((Player) commandSender).openInventory(plugin.gui);
                    break;

//                    for(Player player : plugin.getPlayers()) {
//                        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
//                        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
//
//                        meta.setOwningPlayer(player);
//
//                        meta.setDisplayName(player.getDisplayName());
//                        playerHead.setItemMeta(meta);
//
//                        gui.addItem(playerHead);
//                    }
                case "createteam":
                    if(args.length > 1) {
                        if (TeamsConfig.get().getConfigurationSection("teams") == null) {
                            plugin.messagePlayer(p, "Team '§6" + args[1] + "§7' has been created.");
                            TeamsConfig.get().set("teams." + args[1] + ".name", args[1]);
                            TeamsConfig.get().set("teams." + args[1] + ".icon", "0");
                            TeamsConfig.save();
                        } else {
                            boolean teamExists = false;
                            for (String key : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                                if (args[1].equals(key)) {
                                    plugin.messagePlayer(p, "A team with this name already exists!");
                                    teamExists = true;
                                    break;
                                }
                            }
                            if (!teamExists) {
                                plugin.messagePlayer(p, "Team '§6" + args[1] + "§7' has been created.");
                                TeamsConfig.get().set("teams." + args[1] + ".name", args[1]);
                                TeamsConfig.save();
                            }
                        }
                    }
                    break;

                case "delteam":
                    if(args.length > 1) {
                        if (TeamsConfig.get().getConfigurationSection("teams") == null) {
                            plugin.messagePlayer(p, "Currently no teams exist.");
                        } else {
                            boolean teamExists = false;
                            for (String key : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                                if (args[1].equals(key)) {
                                    plugin.messagePlayer(p, "Team '§6" + args[1] + "§7' has been deleted.");
                                    TeamsConfig.get().set("teams." + args[1], null);
                                    TeamsConfig.save();
                                    teamExists = true;
                                    break;
                                }
                            }
                            if (!teamExists) {
                                plugin.messagePlayer(p, "A team with this name doesn't exist!");
                            }
                        }
                    }
                    break;

                case "jointeam":
                    if(args.length > 2) {
                        if (TeamsConfig.get().getConfigurationSection("teams") == null) {
                            plugin.messagePlayer(p, "Currently no teams exist.");
                        } else {
                            boolean teamExists = false;
                            for (String key : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                                if (args[1].equals(key)) {
                                    List<String> teamPlayers = TeamsConfig.get().getStringList("teams." + args[1] + ".players");
                                    if(teamPlayers.contains(args[2])) {
                                        plugin.messagePlayer(p, "Player " + args[2] + " is already on team " + args[1] + "!");
                                    } else {
                                        plugin.messagePlayer(p, "Player " + args[2] + " has been added to team " + args[1] + "!");
                                        teamPlayers.add(args[2]);
                                        TeamsConfig.get().set("teams." + args[1] + ".players", teamPlayers);
                                        PlayerConfig.get().set("players." + args[2] + ".points", 0);
                                        PlayerConfig.get().set("players." + args[2] + ".team", TeamsConfig.get().getString("teams." + args[1] + ".name"));
                                        PlayerConfig.save();
                                        TeamsConfig.save();
                                        if(!SpectatorConfig.get().getStringList("spectators").isEmpty()) {
                                            for (String player : SpectatorConfig.get().getStringList("spectators")) {
                                                if (args[2].equals(player)) {
                                                    List<String> newSpectators = SpectatorConfig.get().getStringList("spectators");
                                                    newSpectators.remove(args[2]);
                                                    SpectatorConfig.get().set("spectators", newSpectators);
                                                    SpectatorConfig.save();
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    teamExists = true;
                                }

                            }
                            if(!teamExists) {
                                plugin.messagePlayer(p, "A team with this name doesn't exist!");
                            }
                        }
                    }
                    break;
                case "leaveteam":
                    if(args.length > 1) {
                        if (TeamsConfig.get().getConfigurationSection("teams") == null) {
                            plugin.messagePlayer(p, "Currently no teams exist.");
                        } else {
                            if(PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(args[1])){
                                String team = PlayerConfig.get().getString("players." + args[1] + ".team");
                                PlayerConfig.get().set("players." + args[1], null);
                                PlayerConfig.save();
                                List<String> teamPlayers = TeamsConfig.get().getStringList("teams." + team + ".players");
                                teamPlayers.remove(args[1]);
                                TeamsConfig.get().set("teams." + team + ".players", teamPlayers);
                                TeamsConfig.save();
                                List<String> newSpectators = SpectatorConfig.get().getStringList("spectators");
                                newSpectators.add(args[1]);
                                SpectatorConfig.get().set("spectators", newSpectators);
                                SpectatorConfig.save();
                            } else {
                                plugin.messagePlayer(p, "This player is not on a team!");
                            }
                        }
                    }
                    break;

                case "starttimer":
                    if(args.length > 2) {
                        plugin.startTimer(Integer.parseInt(args[1]), args[2]);
                    }
                    break;
                case "startstopwatch":
                    if(args.length > 2) {
                        plugin.startStopwatch(Integer.parseInt(args[1]), args[2]);
                    }
                    break;
                case "stoptimer":
                    if(args.length > 1) {
                        plugin.stopTimer(args[1]);
                    }
                    break;

                case "test":
                    for(Player players : plugin.getPlayers()){
                        plugin.messagePlayer(players, "Test.");
                    }
                    break;
                case "settp":
                    if(args.length > 2) {
                        switch (args[1]) {
                            case "players":
                                TeleportConfig.get().set("players." + args[2], p.getLocation());
                                TeleportConfig.save();
                                plugin.messagePlayer(p, "Location " + args[2] + " has been set for players!");
                                break;
                            case "spectators":
                                TeleportConfig.get().set("spectators." + args[2], p.getLocation());
                                TeleportConfig.save();
                                plugin.messagePlayer(p, "Location " + args[2] + " has been set for spectators!");
                                break;
                            default:
                                boolean teamTpFound = false;
                                for (String key : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                                    if (args[1].equals(key)) {
                                        teamTpFound = true;
                                        TeleportConfig.get().set("teams." + args[1] + "." + args[2], p.getLocation());
                                        TeleportConfig.save();
                                        plugin.messagePlayer(p, "Location " + args[2] + " has been set for " + args[1] + "!");
                                        break;
                                    }
                                }
                                if(!teamTpFound) {
                                    plugin.messagePlayer(p, "Invalid argument (spectators/players/teamname).");
                                }
                                break;
                        }
                    }
                    break;
                case "deltp":
                    if(args.length > 2) {
                        boolean teleportFound = false;
                        switch(args[1]){
                            case "players":
                                for(String key : TeleportConfig.get().getConfigurationSection("players").getKeys(false)) {
                                    if(args[2].equals(key)) {
                                        TeleportConfig.get().set("players." + args[2], null);
                                        TeleportConfig.save();
                                        plugin.messagePlayer(p, "Location " + args[2] + " has been deleted from players!");
                                        teleportFound = true;
                                        break;
                                    }
                                }
                                break;
                            case "spectators":
                                for(String key : TeleportConfig.get().getConfigurationSection("spectators").getKeys(false)) {
                                    if(args[2].equals(key)) {
                                        TeleportConfig.get().set("spectators." + args[2], null);
                                        TeleportConfig.save();
                                        plugin.messagePlayer(p, "Location " + args[2] + " has been deleted from spectators!");
                                        teleportFound = true;
                                        break;
                                    }
                                }
                                break;
                            default:
                                boolean teamTpFound = false;
                                for(String key : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                                    if(args[1].equals(key)) {
                                        teamTpFound = true;
                                        for(String key2 : TeleportConfig.get().getConfigurationSection("teams." + args[1]).getKeys(false)) {
                                            if(args[2].equals(key2)) {
                                                TeleportConfig.get().set("teams." + args[1] + "." + args[2], null);
                                                TeleportConfig.save();
                                                plugin.messagePlayer(p, "Location " + args[2] + " has been deleted from " + args[1] + "!");
                                                teleportFound = true;
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                                if (!teamTpFound) {
                                    plugin.messagePlayer(p, "Invalid argument (spectators/players/teamname).");
                                }
                                break;
                        }
                        if(!teleportFound) {
                            plugin.messagePlayer(p, "Teleport location " + args[2] + " doesn't exist!");
                        }
                    }
                    break;
                case "tpp":
                    if(args.length > 1) {
                        plugin.teleportPlayers(TeleportConfig.get().getLocation("players." + args[1]), 5);
                    }
                    break;
                case "tpt":
                    if(args.length > 1) {
                        plugin.teamTeleport(args[1], 5);
                    }
                    break;
                case "addgubtp":
                    if(GubTPConfig.get().getConfigurationSection("teleports") != null) {
                        int tpCount = GubTPConfig.get().getConfigurationSection("teleports").getKeys(false).size() + 1;
                        GubTPConfig.get().set("teleports.loc" + tpCount, p.getLocation());
                    } else {
                        GubTPConfig.get().set("teleports.loc1", p.getLocation());
                    }
                    GubTPConfig.save();
                    break;
                case "slimefinishers":
                    plugin.slimeGolfTimes();
                    break;
                case "startslimegolf1":
                    plugin.currentRound = 1;
                    plugin.startSlimeGolf();
                    break;
                case "startslimegolf2":
                    plugin.currentRound = 2;
                    plugin.startSlimeGolf();
                    break;
                case "startcolourdash":
                    plugin.startColourDash();
                    break;
                case "startbridgebuilders":
                    plugin.startBridgeBuilders();
                    break;
                case "startcraftalot":
                    plugin.startCraftalot();
                    break;
                case "startzoomogo1":
                    plugin.currentRound = 1;
                    plugin.startZoomoGo();
                    break;
                case "startzoomogo2":
                    plugin.currentRound = 2;
                    plugin.startZoomoGo();
                    break;
                case "startgubgame":
                    plugin.startGubGame();
                    break;
                case "startsurvivalgames":
                    plugin.startSurvivalGames();
                    break;
                case "countvotes":
                    if(args.length > 1){
                        if(args[1].equals("least")){
                            plugin.leastVotes = true;
                            plugin.startVoting();
                        } else if (args[1].equals("audience")){
                            plugin.audienceVote = true;
                            plugin.startVoting();
                        } else {
                            plugin.messagePlayer(p, "Incorrect argument. (least or audience)");
                        }
                    } else {
                        plugin.startVoting();
                    }
                    break;
                case "readycheck":
                    plugin.jumpStates.clear();
                    if(args.length > 1) {
                        switch(args[1]){
                            case "jump":
                                plugin.readyType = "jump";
                                break;
                            case "sneak":
                                plugin.readyType = "sneak";
                                break;
                            case "punch":
                                plugin.readyType = "punch";
                                break;
                        }
                        plugin.getReadyPlayers();
                    } else {
                        plugin.messagePlayer(p, "Missing Args.");
                    }
                    break;
                case "pause":
                    plugin.pauseEvent();
                    break;
                case "unpause":
                    plugin.resumeEvent();
                    break;
                case "resetpoints":
                    for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                        TeamsConfig.get().set("teams." + team + ".points", 0);
                    }
                    for(String player : PlayerConfig.get().getConfigurationSection("players").getKeys(false)) {
                        PlayerConfig.get().set("players." + player + ".points", 0);
                    }
                    PlayerConfig.save();
                    TeamsConfig.save();
                    break;
                default:
                    plugin.messagePlayer(p, "Missing Args.");
                    break;
            }
        }
    } else if (commandSender instanceof BlockCommandSender) {
        if (args.length == 0) {
            plugin.messageConsole("Invalid command.");
        } else {
            switch (args[0].toLowerCase()) {
                case "slimecp":
                    if (args.length > 2) {
                        if(plugin.runningTimers.containsKey("slimegolf")) {
                            Integer placement = plugin.slimeCheckpoints.get(Integer.parseInt(args[1]));
                            if (placement == 1) {
                                for (Player p : plugin.getPlayers()) {
                                    plugin.messagePlayer(p, "§8| §b★ §8| " + plugin.getTeamDisplayName(args[2]) + "§e was 1st to reach §a\uD83D\uDDFB-" + args[1] + "§7!");
                                }
                            } else {
                                for (Player p : plugin.getPlayers()) {
                                    plugin.messagePlayer(p, "§8| §a§l⏱ §8| " + plugin.getTeamDisplayName(args[2]) + "§7 has reached §a\uD83D\uDDFB-" + args[1] + "§7!");
                                }
                            }

                            int pointsEarned = 80 - placement*4;
                            int dividedPointsEarned = pointsEarned / 4;
                            plugin.teamCheckpoints.put(args[2], Integer.parseInt(args[1]));

                            for (String player : TeamsConfig.get().getStringList("teams." + args[2] + ".players")) {
                                if (Bukkit.getServer().getPlayer(player) != null) {
                                    Player p = Bukkit.getServer().getPlayer(player);
                                    p.sendTitle("§a[✔] \uD83D\uDDFB-" + args[1], "§8[§f§l⏱§8] §e§o" + plugin.getTimer("slimegolf"), 0, 100, 5);
                                    plugin.messagePlayer(p, "§a[\uD83D\uDDFB-" + args[1] + "] Checkpoint reached!");
                                    plugin.earnPoints(player, dividedPointsEarned, true);
                                }
                            }
                            plugin.slimeCheckpoints.replace(Integer.parseInt(args[1]), placement + 1);
                        }
                    }
                    break;
                case "slimefinish":
                    if (args.length > 1) {
                        if(plugin.runningTimers.containsKey("slimegolf")) {
                            Integer placement = plugin.slimeCheckpoints.get(plugin.slimeCheckpoints.size());
                            switch (placement) {
                                case 1:
                                    for (Player p : plugin.getPlayers()) {
                                        plugin.messagePlayer(p, "§8| §e\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(args[1]) + "§e was §e§l1st §eto finish!");
                                    }
                                    break;
                                case 2:
                                    for (Player p : plugin.getPlayers()) {
                                        plugin.messagePlayer(p, "§8| §7\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(args[1]) + "§e was §7§l2nd §eto finish!");
                                    }
                                    break;
                                case 3:
                                    for (Player p : plugin.getPlayers()) {
                                        plugin.messagePlayer(p, "§8| §6\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(args[1]) + "§e was §6§l3rd §eto finish!");
                                    }
                                    break;
                                default:
                                    for (Player p : plugin.getPlayers()) {
                                        plugin.messagePlayer(p, "§8| §f\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(args[1]) + "§e was §f§l" + placement + "th §eto finish!");
                                    }
                                    break;
                            }

                            int pointsEarned = 148 - (4 * placement);
                            int dividedPointsEarned = pointsEarned/4;
                            plugin.teamCheckpoints.put(args[1], 6);
                            plugin.slimeFinishers.put(args[1], plugin.getTimer("slimegolf"));

                            for (String player : TeamsConfig.get().getStringList("teams." + args[1] + ".players")) {
                                if (Bukkit.getServer().getPlayer(player) != null) {
                                    Player p = Bukkit.getServer().getPlayer(player);
                                    p.sendTitle("§aFINISH", "§8[§f§l⏱§8] §e§o" + plugin.getTimer("slimegolf"), 0, 100, 5);
                                    plugin.messagePlayer(p, "§e\uD83D\uDCB0" + pointsEarned + " §8| §a§lHole Completed!");
                                    plugin.messagePlayer(p, "§f§l⏱ §8| §fTime Taken: §e" + plugin.getTimer("slimegolf"));
                                    plugin.earnPoints(player, dividedPointsEarned, true);
                                    p.setGameMode(GameMode.SPECTATOR);
                                }
                            }
                            plugin.slimeCheckpoints.replace(plugin.slimeCheckpoints.size(), placement + 1);

                            List<String> teamList = new ArrayList<>();
                            for(Player p : plugin.getPlayers()) {
                                if(!teamList.contains(PlayerConfig.get().getString("players." + p.getName() + ".team"))) {
                                    teamList.add(PlayerConfig.get().getString("players." + p.getName() + ".team"));
                                }
                            }

                            if(placement.equals(teamList.size())){
                                plugin.runningTimers.remove("slimegolf");
                                plugin.runningTimers.remove("slimegolftimer");
                                plugin.gameEnd();
                            }
                        }
                    }
                    break;
                case "bbcp":
                    if (args.length > 2) {
                        if(plugin.runningTimers.containsKey("bridgebuilders")) {
                            Integer placement = plugin.bridgeCheckpoints.get(Integer.parseInt(args[1]));
                            if(!plugin.bridgeTally.containsKey(args[2])){
                                plugin.bridgeTally.put(args[2], 0);
                            }
                            plugin.bridgeTally.put(args[2], plugin.bridgeTally.get(args[2]) + 1);
                            plugin.startBridgeJump(args[2], Integer.parseInt(args[1]));
                            if (placement == 1) {
                                for (Player p : plugin.getPlayers()) {
                                    plugin.messagePlayer(p, "§e§l⏱ §8| " + plugin.getTeamDisplayName(args[2]) + "§7 built §a\uD83C\uDF09-" + args[1] + "§7!");
                                }
                            } else {
                                for (Player p : plugin.getPlayers()) {
                                    plugin.messagePlayer(p, "§f§l⏱ §8| " + plugin.getTeamDisplayName(args[2]) + "§7 built §a\uD83C\uDF09-" + args[1] + "§7!");
                                }
                            }

//                            int pointsEarned = 51 - placement;
//                            plugin.earnTeamPoints(args[2], pointsEarned);
                            plugin.teamCheckpoints.put(args[2], Integer.parseInt(args[1]));

                            Location teleportLoc = new Location(Bukkit.getWorld("build"), plugin.teamJump.get(args[2])[0]+0.5, plugin.teamJump.get(args[2])[1], (plugin.teamJump.get(args[2])[2])-(38*(Integer.valueOf(args[1])-1))+3);

                            teleportLoc.setYaw(180);

                            for (String player : TeamsConfig.get().getStringList("teams." + args[2] + ".players")) {
                                if (Bukkit.getServer().getPlayer(player) != null) {
                                    Player p = Bukkit.getServer().getPlayer(player);
                                    p.getInventory().clear();
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_WORK_FLETCHER, 1f, 1f);
                                    plugin.summonFirework(p.getLocation(), args[2]);
                                    p.sendTitle("§a[✔] \uD83C\uDF09-" + args[1], "§7Now get ready to run!", 0, 40, 0);
                                    plugin.messagePlayer(p, "§a[\uD83D\uDDFB-" + args[1] + "] Build Complete!");
                                    plugin.messagePlayer(p, "§cBuild mode removed, move onto the next build.");
                                    p.setGameMode(GameMode.ADVENTURE);
                                    p.setAllowFlight(false);
                                    p.setFlying(false);

                                    p.teleport(teleportLoc);
                                }
                            }
                            plugin.bridgeCheckpoints.replace(Integer.parseInt(args[1]), placement + 1);

                            int buildTime = plugin.runningTimers.get("bridgebuilders").getValue() - plugin.buildTimeStamps.get(args[2]);

                            plugin.bridgeCourseTimes.computeIfAbsent(args[2], k -> new HashMap<>()).put(Integer.parseInt(args[1]), buildTime);
                        }


                    }
                    break;
                default:
                    break;
                }
            }
        }
        return true;
    }
}
